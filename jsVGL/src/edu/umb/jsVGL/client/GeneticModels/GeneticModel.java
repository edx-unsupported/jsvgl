package edu.umb.jsVGL.client.GeneticModels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

/**
 * Brian White Summer 2008
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * @author Brian White
 * @version 1.0 $Id: GeneticModel.java,v 1.26 2009-09-18 19:55:12 brian Exp $
 */

/* This is the wrapper class for the entire genetic model
 * - it chooses from the characters and traits randomly to set up 
 * each gene model.
 * - it generates random organisms to populate the field cage
 * - it generates offspring from selected parents

 * It works like this:
 * - organisms contain 4 chromosomes
 * - 2 copies of the autosome (maternal and paternal)
 * - 2 copies of the sex chromosome (maternal and paternal)
 * - each chromosome is an ArrayList of Alleles

 * - there is a ChromosomeModel for each type of chromosome (auto & sex)
 * - each ChromosomeModel consists of 0 or more GeneModels
 * - the GeneModels handle the genotype-phenotype conversion for one gene each

 * - there is a NullSexChromosome with no alleles (actually all Null alleles)
 * this is the Y or W chromosome

 * - to make a random organism
 * 1) the GeneticModel asks each GeneModel for 2 random alleles
 * chosen to give roughly equal #s of each phenotype
 * 2) the GeneticModel builds these into a chromosome pair
 * and turns this into an Organism

 * - cross 2 organisms
 * 1) the genetic model takes care of recombination in each parent
 * using the CHromosomeModel to make 2 gametes
 * 2) the gametes are combined in to an Organism and its phenotype
 * is determined

 * - phenotypes are determined :
 * 1) the GeneticModel has each ChromosomeModel distribute its Alleles
 * to the GeneModels to get a Phenotype from each
 * 2) these are pooled into a set of phenotypes for display, sorting, etc.
 * 3) all pass through PhenotypeProcessor for dealing with epistasis, etc.

 * - An Allele consists of:
 * - an intVal = used as an integer index to the genotype-phenotype table
 * (0 = the null allele - from the Y or W chromosome)
 * - a Trait which contains:
 * bodyPart (eye, antenna, etc)
 * type (shape, colo, etc)
 * traitName (green, long, etc)

 * to build a GeneticModel: 
 * 1) Choose XX/XY or ZZ/ZW sex-linkage when you build one
 * 2) add the gene models to the autosome or sex chromosome as needed
 * - the first one is added just as a model
 * - any more on the same chromo must be added with a recombination freq
 * unlinked genes have a 50% rf.
 * (therefore, autosomes are modeled as one big autosome where the
 * total rf can be bigger than 100%)
 * 3) then add in PhenotypeProcessor and set up epistasis, etc.
 */

public class GeneticModel {

	public static final boolean XX_XY = true;
	public static final boolean ZZ_ZW = false;

	private int minOffspring;
	private int maxOffspring;

	private ChromosomeModel autosomeModel;
	private ChromosomeModel sexChromosomeModel;

	private ArrayList<GeneModel> allGeneModels;
	private ArrayList<Boolean> allGeneModelLocations;

	private PhenotypeProcessor phenotypeProcessor;

	private boolean XX_XYsexLinkage; 

	private Random random;

	private boolean beginnerMode;   //allows viewing of model and genotypes

	private boolean fieldPopTrueBreeding;  // all strains in field pop are true breeding

	private ProblemTypeSpecification problemTypeSpecification;

	/**
	 * the name of the problem file used to start this problem
	 */
	private String problemFileName; 

	/**
	 * because we don't want to display the character in the CageUI in the
	 * order they appear on the chromosome, need a mapping
	 * between the trait number and the displayed trait number
	 * in this array,when i = trait number; sto[i] gives its display order
	 */
	private int[] scrambledCharacterOrder;


	protected GeneticModel(boolean XX_XYsexLinkage) {
		beginnerMode = false;
		this.XX_XYsexLinkage = XX_XYsexLinkage;
		autosomeModel = new AutosomeModel();
		sexChromosomeModel = new SexChromosomeModel();
		phenotypeProcessor = new PhenotypeProcessor(this);
		random = new Random();
	}

	public PhenotypeProcessor getPhenoTypeProcessor() {
		return phenotypeProcessor;
	}

	public void setProblemTypeSPecification(ProblemTypeSpecification specs) {
		problemTypeSpecification = specs;
	}

	public ProblemTypeSpecification getProblemTypeSpecification() {
		return problemTypeSpecification;
	}

	protected void addFirstAutosomalGeneModel(GeneModel gm) throws GeneticsException {
		if (autosomeModel.getNumberOfGeneModels() != 0) {
			throw new GeneticsException("Can't add first autosomal model when" 
					+ " one is already present");
		}
		autosomeModel.addGeneModel(gm);
	}

	protected void addFirstSexLinkedGeneModel(GeneModel gm) throws GeneticsException {
		if (sexChromosomeModel.getNumberOfGeneModels() != 0) {
			throw new GeneticsException("Can't add first sex-linked model when" 
					+ " one is already present");
		}
		sexChromosomeModel.addGeneModel(gm);
	}

	protected void addNextAutosomalGeneModel(float rf, GeneModel gm) throws GeneticsException {
		if (autosomeModel.getNumberOfGeneModels() == 0) {
			throw new GeneticsException("Can't add next autosomal model to" 
					+ " empty list");
		}
		autosomeModel.addGeneModel(gm);
		autosomeModel.addRecombinationFrequency(rf);
	}

	protected void addNextSexLinkedGeneModel(float rf, GeneModel gm) throws GeneticsException {
		if (sexChromosomeModel.getNumberOfGeneModels() == 0) {
			throw new GeneticsException("Can't add next sex-linked model to" 
					+ " empty list");
		}
		sexChromosomeModel.addGeneModel(gm);
		sexChromosomeModel.addRecombinationFrequency(rf);
	}

	public boolean isBeginnerMode() {
		return beginnerMode;
	}

	public void setBeginnerMode(boolean beginnerMode) {
		this.beginnerMode = beginnerMode;
	}

	public boolean isFieldPopTrueBreeding() {
		return fieldPopTrueBreeding;
	}

	public void setFieldPopTrueBreeding(boolean fieldPopTrueBreeding) {
		this.fieldPopTrueBreeding = fieldPopTrueBreeding;
	}

	protected void setMinOffspring(int min) {
		minOffspring = min;
	}

	public int getMinOffspring() {
		return minOffspring;
	}

	protected void setMaxOffspring(int max) {
		maxOffspring = max;
	}

	public int getMaxOffspring() {
		return maxOffspring;
	}

	public String getProblemFileName() {
		return problemFileName;
	}

	public void setProblemFileName(String name) {
		problemFileName = name;
	}

	public ChromosomeModel getAutosomeModel() {
		return autosomeModel;
	}

	public ChromosomeModel getSexChromosomeModel() {
		return sexChromosomeModel;
	}

	/**
	 * randomize the order in which traits are displayed in CageUI
	 * DO NOT RUN THIS UNTIL MODEL IS COMPLETE
	 */
	protected void scrambleTraitOrder() {
		scrambledCharacterOrder = new int[getNumberOfCharacters()];

		// fill array with blanks
		for (int i = 0; i < scrambledCharacterOrder.length; i++) {
			scrambledCharacterOrder[i] = -1;
		}

		// fill array with possible values to draw from
		int[] source = new int[getNumberOfCharacters()];
		for (int i = 0; i < source.length; i++) {
			source[i] = i;
		}

		//draw them randomly
		for (int i = 0; i < scrambledCharacterOrder.length; i++) {
			scrambledCharacterOrder[i] = pickRandomUnusedInt(source);
		}

	}

	private int pickRandomUnusedInt(int[] source) {
		int i = random.nextInt(source.length);
		int val = source[i];
		while (val == -1) {
			i = random.nextInt(source.length);
			val = source[i];
		}
		source[i] = -1;
		return val;
	}

	public void setPhenotypeInteraction(int interaction) {
		phenotypeProcessor.setInteractionType(interaction);
	}

	/**
	 * this implements a list of all the gene models (both autosomsal
	 * and sex-linked) for access by the grader and phenotype interactions
	 * 
	 * it indexes autosomal models then sex-linked models
	 * 
	 * the first time through, it makes the list
	 *   subsequent calls just pull out the model
	 */
	public GeneModel getGeneModelByIndex(int index) {
		if (allGeneModels == null) {
			allGeneModels = new ArrayList<GeneModel>();
			Iterator<GeneModel> aIt = autosomeModel.getGeneModels().iterator();
			while(aIt.hasNext()) {
				allGeneModels.add(aIt.next());
			}

			Iterator<GeneModel> sIt = sexChromosomeModel.getGeneModels().iterator();
			while(sIt.hasNext()) {
				allGeneModels.add(sIt.next());
			}	
		}
		return allGeneModels.get(index);
	}

	/**
	 * lists gene models by sex linked or not
	 *  first time thru, makes list
	 */
	public boolean isGeneModelSexLinkedByIndex(int index) {
		if (allGeneModelLocations == null) {
			allGeneModelLocations = new ArrayList<Boolean>();
			Iterator<GeneModel> aIt = autosomeModel.getGeneModels().iterator();
			while(aIt.hasNext()) {
				aIt.next();
				allGeneModelLocations.add(new Boolean(false));
			}

			Iterator<GeneModel> sIt = sexChromosomeModel.getGeneModels().iterator();
			while(sIt.hasNext()) {
				sIt.next();
				allGeneModelLocations.add(new Boolean(true));
			}	
		}
		return allGeneModelLocations.get(index);

	}

	public Cage generateFieldPopulation() {
		Cage cage = new Cage(0);
		int numOffspring = 
				random.nextInt(maxOffspring - minOffspring) + minOffspring;
		for (int i = 0; i < numOffspring; i++) {
			cage.addNew(getRandomOrganism(fieldPopTrueBreeding));
		}
		return cage;
	}

	private Organism getRandomOrganism(boolean trueBreeding) {
		return phenotypeProcessor.getProcessedRandomOrganism(
				autosomeModel, 
				sexChromosomeModel, 
				trueBreeding);
	}

	public Cage crossTwo(int newCageID, 
			Organism mom, Organism dad, 
			int numOffspring, 
			boolean isSuperCross) {
		Cage cage = new Cage(newCageID, mom, dad, isSuperCross);
		for (int i = 0; i < numOffspring; i++) {
			cage.addNew(getOffspringOrganism(newCageID, mom, dad));
		}
		return cage;
	}

	private Organism getOffspringOrganism(int cageID, Organism mom, Organism dad) {
		Chromosome maternalAutosomeContribution = null;
		Chromosome maternalSexChromosomeContribution = null;
		Chromosome paternalAutosomeContribution = null;
		Chromosome paternalSexChromosomeContribution = null;
		maternalAutosomeContribution = 
				autosomeModel.getGamete(
						mom.getMaternalAutosome(), mom.getPaternalAutosome());
		maternalSexChromosomeContribution = 
				sexChromosomeModel.getGamete(
						mom.getMaternalSexChromosome(), mom.getPaternalSexChromosome());
		paternalAutosomeContribution = 
				autosomeModel.getGamete(
						dad.getMaternalAutosome(), dad.getPaternalAutosome());
		paternalSexChromosomeContribution = 
				sexChromosomeModel.getGamete(
						dad.getMaternalSexChromosome(), dad.getPaternalSexChromosome());

		ArrayList<Phenotype> phenotypes = new ArrayList<Phenotype>();
		phenotypes.addAll(
				autosomeModel.getPhenotypes(
						maternalAutosomeContribution, 
						paternalAutosomeContribution));
		phenotypes.addAll(  
				sexChromosomeModel.getPhenotypes(
						maternalSexChromosomeContribution, 
						paternalSexChromosomeContribution));
		return new Organism(cageID,
				maternalAutosomeContribution,
				paternalAutosomeContribution,
				maternalSexChromosomeContribution,
				paternalSexChromosomeContribution,
				phenotypeProcessor.processPhenotypes(phenotypes),
				isMale(maternalSexChromosomeContribution, 
						paternalSexChromosomeContribution),
						this);
	}

	protected ArrayList<Phenotype> getPhenotypes(
			Chromosome mA,
			Chromosome pA,
			Chromosome mS,
			Chromosome pS) {
		ArrayList<Phenotype> phenotypes = new ArrayList<Phenotype>();
		phenotypes.addAll(
				autosomeModel.getPhenotypes(mA, pA));
		phenotypes.addAll(  
				sexChromosomeModel.getPhenotypes(mS, pS));
		return phenotypeProcessor.processPhenotypes(phenotypes);
	}

	public boolean isMale(Chromosome sexChr1, Chromosome sexChr2) {		
		//see if one of the chromos is a NullSexChromosome (Y or W)
		boolean heterogametic = false;
		if((sexChr1 == NullSexChromosome.getInstance()) || 
				(sexChr2 == NullSexChromosome.getInstance())) {
			heterogametic = true;
		}	

		if (XX_XYsexLinkage) {
			if (heterogametic) {
				return true;
			} else {
				return false;
			}
		} else {
			if (heterogametic) {
				return false;
			} else {
				return true;
			}
		}
	}

	public boolean getSexLinkageType() {
		return XX_XYsexLinkage;
	}

	public int getNumberOfGeneModels() {
		return autosomeModel.getNumberOfGeneModels() + sexChromosomeModel.getNumberOfGeneModels();
	}

	/*
	 * this is the same as # of gene models
	 * EXCEPT when there's epistasis or complementation
	 */
	public int getNumberOfCharacters() {
		return phenotypeProcessor.getProcessedNumberOfCharacters(
				autosomeModel, sexChromosomeModel);
	}

	public int[] getScrambledCharacterOrder() {
		return scrambledCharacterOrder;
	}

	protected void setScrambledCharacterOrder(int[] scrambler) {
		scrambledCharacterOrder = scrambler;
	}

	public boolean anyTraitsOnSexChromosome() {
		if (sexChromosomeModel.getNumberOfGeneModels() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean anyTraitsOnAutosome() {
		if (autosomeModel.getNumberOfGeneModels() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Element save() throws Exception {
		Document d = XMLParser.createDocument();
		Element e = d.createElement("GeneticModel");
		e.setAttribute("XX_XYSexDetermination", String.valueOf(XX_XYsexLinkage));
		e.setAttribute("BeginnerMode", String.valueOf(beginnerMode));
		e.setAttribute("NumberOfGeneModels", String.valueOf(getNumberOfGeneModels()));
		e.setAttribute("NumberOfCharacters", String.valueOf(getNumberOfCharacters()));
		e.setAttribute("MinOffspring", String.valueOf(minOffspring));
		e.setAttribute("MaxOffspring", String.valueOf(maxOffspring));

		Document ds = XMLParser.createDocument();
		Element scrambler = ds.createElement("CharacterOrderScrambler");
		for (int i = 0; i < getNumberOfCharacters(); i++) {
			Document dt = XMLParser.createDocument();
			Element temp = dt.createElement("Character");
			temp.setAttribute("Index", String.valueOf(i));
			temp.setAttribute("Character", String.valueOf(scrambledCharacterOrder[i]));
			scrambler.appendChild(temp);
		}
		e.appendChild(scrambler);

		e.appendChild(problemTypeSpecification.save());
		e.appendChild(phenotypeProcessor.save());
		e.appendChild(autosomeModel.save());
		e.appendChild(sexChromosomeModel.save());
		return e;
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("<html><body>");
		if (XX_XYsexLinkage) {
			b.append("XX/XY sex determination");
		} else {
			b.append("ZZ/ZW sex determination");
		}
		b.append("<br>");
		if (phenotypeProcessor.getInteractionType() == PhenotypeProcessor.COMPLEMENTATION) {
			b.append("Complementation: ");
			b.append(phenotypeProcessor.getT1().getCharacterName());
			b.append("<br>" + (new Allele(phenotypeProcessor.getT1(), 0).getName())
					+ " ---(Gene A)--->"
					+ (new Allele(phenotypeProcessor.getT1(), 0).getName())
					+ "<br> ---(Gene B)--->"
					+ (new Allele(phenotypeProcessor.getT2(), 0).getName())
					+ "<br>");
			b.append("<br>");
		}
		if (phenotypeProcessor.getInteractionType() == PhenotypeProcessor.EPISTASIS) {
			b.append("Epistasis: ");
			b.append(phenotypeProcessor.getT1().getCharacterName());
			b.append("<br>" + (new Allele(phenotypeProcessor.getT1(), 0).getName())
					+ " ---(Gene A)--->"
					+ (new Allele(phenotypeProcessor.getT2(), 0).getName())
					+ "<br> ---(Gene B)--->"
					+ (new Allele(phenotypeProcessor.getT3(), 0).getName())
					+ "<br>");
			b.append("<br>");
		}

		b.append(autosomeModel.toString());
		b.append(sexChromosomeModel.toString());
		b.append("</body></html>");
		return b.toString();
	}
}
