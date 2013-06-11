package edu.umb.jsVGL.client.Grader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.umb.jsVGL.client.GeneticModels.Cage;
import edu.umb.jsVGL.client.GeneticModels.GeneModel;
import edu.umb.jsVGL.client.GeneticModels.Organism;
import edu.umb.jsVGL.client.GeneticModels.OrganismList;
import edu.umb.jsVGL.client.GeneticModels.Phenotype;
import edu.umb.jsVGL.client.GeneticModels.PhenotypeProcessor;
import edu.umb.jsVGL.client.GeneticModels.Trait;
import edu.umb.jsVGL.client.GeneticModels.TwoAlleleSimpleDominanceGeneModel;
import edu.umb.jsVGL.client.ModelBuilder.ModelBuilderUI;

/**
 * takes a set of Cages and determines if each shows:
 * 	-sex linkage or not
 * 		= if sex imbalance in offspring (eg red females but no red males)
 * 	-interaction evidence for one or more traits
 * 		= if offspring don't look like parents
 * 			A x B -> all A or all B
 * 			A x A -> any pheno other than A
 * 			A x B -> any pheno other than A or B
 * - linkage
 * @author brian
 *
 */
public class CageScorer {

	private ArrayList<Cage> cages;
	private ModelBuilderUI mbui;

	public CageScorer(ArrayList<Cage> cages, ModelBuilderUI mbui) {
		this.cages = cages;
		this.mbui = mbui;
	}

	public String getCageScores() {
		StringBuffer b = new StringBuffer();
		TreeSet<Integer> selectedCages = 
				mbui.getChosenRelevantCages();
		b.append("<hr>");
		b.append("<b>Selected Cages:</b><br>");

		if(selectedCages.size() == 0) {
			b.append("<b>No cages were selected.</b>");
		} else {
			Iterator<Integer> cageNumIt = selectedCages.iterator();
			while (cageNumIt.hasNext()) {
				int cageNum = cageNumIt.next();
				b.append(scoreCage(cages.get(cageNum - 1)).getHTML());
			}

		}

		b.append("</ul>");
		return b.toString();
	}

	/*
	 * used by Auto Grader
	 * 
	 * note that the cage number (the number the users see) starts with 1
	 * the cage index starts with 0, so need to subtract 1 to get index
	 * 
	 * note that the methods that give a cage number give -1 as a flag for null result
	 * also you can't get any info from the field pop (cage number 1)
	 */
	public CageScoreResult scoreCage(int cageNum) {
		if (cageNum <= 1) return null;
		return scoreCage(cages.get(cageNum - 1));
	}

	private CageScoreResult scoreCage(Cage cage) {

		StringBuffer b = new StringBuffer();
		b.append("<b>Cage ");
		b.append(cage.getId() + 1);
		b.append(" </b>");
		b.append("<ul>");

		CageScoreResult result = null;

		// can't get data from the field pop
		if (cage.getId() == 0) {
			b.append("<li>You cannot get any information from the field Cage,</li>");
			result = new CageScoreResult(0);
		} else {

			// get a token organism for reference purposes
			TreeMap<String, OrganismList> children = cage.getChildren();
			Iterator<String> phenoIt = children.keySet().iterator();
			Organism org = children.get(phenoIt.next()).get(0);
			ArrayList<Phenotype> phenotypes = org.getPhenotypes();

			result = new CageScoreResult(phenotypes.size()); 

			/* 
			 * iterate over the phenotypes
			 * 		actually, over the body part:type (eg Wing:Color) - the character
			 * 		not the individual traits (Wing:Color:Red etc)
			 * 	
			 */
			for (int i = 0; i < phenotypes.size(); i++) {
				Phenotype currentPheno = org.getPhenotypes().get(i);
				String characterName = currentPheno.getTrait().getBodyPart() + " " + currentPheno.getTrait().getType();
				CageScoreForCharacter csfc = new CageScoreForCharacter(characterName);
				b.append("<li><b>");
				b.append(characterName);
				b.append("</b></li>");
				b.append("<ul>");

				/**
				 * look for sex linkage first:
				 * - for each child phenotype (character) of this trait, if you find
				 * 		males but no females
				 * 			or
				 * 		females but no males
				 * -> it shows evidence of sex linkage
				 * -> if not, it does not show evidence of sex linkage
				 * 
				 * note: you have to check all the olists with red eyes (eg)
				 * 	so, red eyes & six legs but also red eyes & 4 legs
				 */
				String character = 
						currentPheno.getTrait().getBodyPart() 
						+ ":" 
						+ currentPheno.getTrait().getType();
				boolean showsSexLinkage = false;
				phenoIt = children.keySet().iterator();
				while (phenoIt.hasNext()) {
					String pheno = phenoIt.next();
					OrganismList oList = children.get(pheno);
					Organism o = oList.get(0);
					String testCharacter = 
							o.getPhenotypes().get(i).getTrait().getBodyPart() 
							+ ":" 
							+ o.getPhenotypes().get(i).getTrait().getType();
					if (testCharacter.equals(character)) {
						int males = oList.getNumberOfMales();
						int females = oList.getNumberOfFemales();
						if (((males == 0) && (females > 0)) || 
								((males > 0) && (females == 0))) showsSexLinkage = true;
					}
				}
				b.append("<li>");

				if (showsSexLinkage) {
					b.append("<font color=green>Shows ");
				} else {
					b.append("<font color=black>Does not show ");
				}
				b.append("evidence of <u>sex linkage</u></font></li> ");
				csfc.showsSexLinkage = showsSexLinkage;

				Phenotype p1Pheno = cage.getParents().get(0).getPhenotypes().get(i);
				Phenotype p2Pheno = cage.getParents().get(1).getPhenotypes().get(i);

				/**
				 * Then look for evidence of dominance, etc
				 * three ways it can be interesting
				 * 	Case 1) if p1Pheno not found in any kids: 
				 * 			A x B -> B only or -> C only
				 * 	Case 2) if p2Pheno not found in any kids: 
				 * 			A X B -> A only or -> C only
				 * 	Case 3) if kids have pheno that isnt p1 or p2: 
				 * 			A X B -> some C or A X A -> some B or some C
				 */
				boolean case1 = true;
				boolean case2 = true;
				boolean case3 = false;
				phenoIt = children.keySet().iterator();
				while (phenoIt.hasNext()) {
					Phenotype kidPheno = 
							children.get(phenoIt.next()).get(0).getPhenotypes().get(i);

					// if you ever find p1 in any type of kid, it can't be Case1
					if (kidPheno.toString().equals(p1Pheno.toString())) case1 = false;

					// if you ever find p2 in any type of kid, it can't be Case2
					if (kidPheno.toString().equals(p2Pheno.toString())) case2 = false;

					// if these kids have a neither p1 nor p2 pheno, its Case3
					if ((!kidPheno.toString().equals(p1Pheno.toString()))
							&& (!kidPheno.toString().equals(p2Pheno.toString()))) 
						case3 = true;
				}
				b.append("<li>");
				if (case1 || case2 || case3 || showsSexLinkage) {
					b.append("<font color = green>");
					b.append("Shows ");
					csfc.showsInteraction = true;
				} else {
					b.append("<font color = black>");
					b.append("Does not show ");
				}

				if (org.getGeneticModel().getPhenoTypeProcessor().getInteractionType() 
						== PhenotypeProcessor.NO_INTERACTION) {
					b.append("evidence of <i>dominance</i></font></li>");
				} else {
					// must be EPISTASIS or COMPLEMENTATION
					b.append("evidence of <i>dominance</i> or <i>interaction</i></font></li>");
				}
				b.append("</ul>");

				/*
				 * do linkage possibility
				 * do character by character
				 * 	capableOfShowingLinkage if:
				 * 		- one parent is heterozygous for the gene
				 * 		- the other has at least one recessive allele
				 * 
				 * can only do this for simple dominance 
				 * 	otherwise, it's very hard to tell the recessive allele
				 * 
				 */
				boolean capableOfShowingLinkage = false;
				GeneModel geneModel = org.getGeneticModel().getGeneModelByIndex(i);
				if (geneModel instanceof TwoAlleleSimpleDominanceGeneModel) {

					Organism p1 = cage.getParents().get(0);
					Organism p2 = cage.getParents().get(1);

					/*
					 * start by figuring out what the recessive allele is
					 *   in 2 allele simple dominance, it's t1
					 *   
					 *   if sex-linked, then the null sex chromosome (which gives null allele here)
					 *    is equivalent to the recessive allele
					 *  
					 *  these tests are complex to avoid null pointer exceptions
					 *    when testing all possibilities
					 */
					Trait recessiveTrait = geneModel.t1;
					boolean p1HasRecAllele = (
							(p1.getGenotypeForGene(i)[0] == null) ||
							(p1.getGenotypeForGene(i)[1] == null) ||
							(p1.getGenotypeForGene(i)[0].getTrait().equals(recessiveTrait)) || 
							(p1.getGenotypeForGene(i)[1].getTrait().equals(recessiveTrait))
							);
					
					boolean p2HasRecAllele = (
							(p2.getGenotypeForGene(i)[0] == null) ||
							(p2.getGenotypeForGene(i)[1] == null) ||
							(p2.getGenotypeForGene(i)[0].getTrait().equals(recessiveTrait)) || 
							(p2.getGenotypeForGene(i)[1].getTrait().equals(recessiveTrait))
							);

					boolean p1Homozygous;
					if (p1.getGenotypeForGene(i)[0] == null) {
						// first allele is null, homozygous if 2nd is recessive
						p1Homozygous = p1.getGenotypeForGene(i)[1].getTrait().equals(recessiveTrait);
					} else if (p1.getGenotypeForGene(i)[1] == null) {
						// second allele is null, homozygous if 1st is recessive
						p1Homozygous = p1.getGenotypeForGene(i)[0].getTrait().equals(recessiveTrait);
					} else {
						// neither is null, only homozygous if both alleles the same
						p1Homozygous = p1.getGenotypeForGene(i)[0].getTrait().equals(p1.getGenotypeForGene(i)[1].getTrait());
					}
					
					boolean p2Homozygous;
					if (p2.getGenotypeForGene(i)[0] == null) {
						// first allele is null, homozygous if 2nd is recessive
						p2Homozygous = p2.getGenotypeForGene(i)[1].getTrait().equals(recessiveTrait);
					} else if (p2.getGenotypeForGene(i)[1] == null) {
						// second allele is null, homozygous if 1st is recessive
						p2Homozygous = p2.getGenotypeForGene(i)[0].getTrait().equals(recessiveTrait);
					} else {
						// neither is null, only homozygous if both alleles the same
						p2Homozygous = p2.getGenotypeForGene(i)[0].getTrait().equals(p2.getGenotypeForGene(i)[1].getTrait());
					}
					
					if ((!p1Homozygous && p2HasRecAllele) || (!p2Homozygous && p1HasRecAllele)) capableOfShowingLinkage = true;

				} 
				csfc.capableOfShowingLinkage = capableOfShowingLinkage;

				result.addCageScoreForCharacter(i, csfc);
			}


			/*
			 * look at linkage for html human grading
			 *  for now, just give the parent's genotypes
			 * 		and leave it up to the instructor
			 */
			if (mbui.hasLinkagePanel()) {
				b.append("<li><b>Linkage:</b></li>");
				b.append("<ul><li>Parent 1 Genotype:</li><ul>");
				Organism p0 = cage.getParents().get(0);
				b.append(getHTMLforGenotype(p0, p0.getMaternalAutosome().toString()));
				b.append(getHTMLforGenotype(p0, p0.getPaternalAutosome().toString()));
				b.append(getHTMLforGenotype(p0, p0.getMaternalSexChromosome().toString()));
				b.append(getHTMLforGenotype(p0, p0.getPaternalSexChromosome().toString()));
				b.append("</ul></ul>");

				b.append("<ul><li>Parent 2 Genotype:</li><ul>");				
				Organism p1 = cage.getParents().get(1);
				b.append(getHTMLforGenotype(p1, p1.getMaternalAutosome().toString()));
				b.append(getHTMLforGenotype(p1, p1.getPaternalAutosome().toString()));
				b.append(getHTMLforGenotype(p1, p1.getMaternalSexChromosome().toString()));
				b.append(getHTMLforGenotype(p1, p1.getPaternalSexChromosome().toString()));
				b.append("</ul></ul>");
			}
		}
		b.append("</ul>");
		result.setHTML(b.toString());

		return result;
	}

	private String getHTMLforGenotype(Organism org, String chromosome) {
		if (chromosome.indexOf(";") == -1) {
			return "";
		} else {
			return "<li>" + chromosome + "</li>";
		}
	}

}


