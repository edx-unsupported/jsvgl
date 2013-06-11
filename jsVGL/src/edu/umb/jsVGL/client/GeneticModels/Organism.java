package edu.umb.jsVGL.client.GeneticModels;

import java.util.ArrayList;

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
 * @version 1.0 $Id: Organism.java,v 1.15 2009-09-22 19:06:36 brian Exp $
 */

public class Organism {

	private int id;  //organism's id#
	private int cageId; // cage's id#
	
	/*
	 * because super crosses have so many Organisms, they take a long time
	 * to save - but you only need crossable organisms for the visible ones
	 * - these are flagged by this boolean
	 * 
	 * this will be true for all orgs in regular crosses
	 * 	and for visible orgs in super cross
	 * 	but most orgs in supercross won't be visible (false)
	 */
	private boolean visibleInCage;

	private Chromosome maternalAutosome;
	private Chromosome paternalAutosome;
	private Chromosome maternalSexChromosome;
	private Chromosome paternalSexChromosome;

	private ArrayList<Phenotype> phenotypes;

	private boolean male;

	private GeneticModel geneticModel;

	//full constructor
	public Organism(int cageId,
			Chromosome maternalAutosome,
			Chromosome paternalAutosome,
			Chromosome maternalSexChromosome,
			Chromosome paternalSexChromosome,
			ArrayList<Phenotype> phenotypes,
			boolean male,
			GeneticModel geneticModel) {
		visibleInCage = true;
		this.cageId = cageId;
		this.maternalAutosome = maternalAutosome;
		this.paternalAutosome = paternalAutosome;
		this.maternalSexChromosome = maternalSexChromosome;
		this.paternalSexChromosome = paternalSexChromosome;
		this.phenotypes = phenotypes;
		this.male = male;
		this.geneticModel = geneticModel;
	}

	//constructor for field population
	//  where cageId = 0
	public Organism(Chromosome maternalAutosome,
			Chromosome paternalAutosome,
			Chromosome maternalSexChromosome,
			Chromosome paternalSexChromosome,
			ArrayList<Phenotype> phenotypes,
			boolean male,
			GeneticModel geneticModel) {

		this(0, 
				maternalAutosome, 
				paternalAutosome,
				maternalSexChromosome,
				paternalSexChromosome,
				phenotypes,
				male,
				geneticModel);
	}
	
	public boolean isVisibleInCage() {
		return visibleInCage;
	}
	
	public void setVisibleInCage(boolean b) {
		visibleInCage = b;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCageId() {
		return cageId;
	}

	public void setCageId(int cageId) {
		this.cageId = cageId;
	}

	public Chromosome getMaternalAutosome() {
		return maternalAutosome;
	}

	public Chromosome getPaternalAutosome() {
		return paternalAutosome;
	}

	public Chromosome getMaternalSexChromosome() {
		return maternalSexChromosome;
	}

	public Chromosome getPaternalSexChromosome() {
		return paternalSexChromosome;
	}

	public ArrayList<Phenotype> getPhenotypes() {
		return phenotypes;
	}

	public boolean isMale() {
		return male;
	}

	public GeneticModel getGeneticModel() {
		return geneticModel;
	}

	/*
	 * used for AutoGrader
	 * gets genotype at a particular locus (gene index)
	 * returns null if you try to get an allele off of a null sex-chromo (Y or W)
	 */
	public Allele[] getGenotypeForGene(int index) {

		// Find out what chromosome the gene is on
		Chromosome matChromo = null;
		Chromosome patChromo = null;
		if (geneticModel.isGeneModelSexLinkedByIndex(index)) {
			matChromo = maternalSexChromosome;
			patChromo = paternalSexChromosome;
		} else {
			matChromo = maternalAutosome;
			patChromo = paternalAutosome;
		}

		// find the name of the character so you can find it
		Phenotype p = phenotypes.get(index);
		String characterName = p.getTrait().getTraitName() + "-" + p.getTrait().getBodyPart();

		// find it in the alleles on the chromosome
		ArrayList<Allele> matAlleles = matChromo.getAllAlleles();
		ArrayList<Allele> patAlleles = patChromo.getAllAlleles();
		Allele matAllele = null;
		Allele patAllele = null;
		/*
		 * if one chromo is a null sex chromo, it has no alleles
		 * so you should search the other one for the allele names, etc.
		 */
		if (matAlleles.size() == 0) {
			for (int i = 0; i < patAlleles.size(); i++) {
				patAllele = patAlleles.get(i);
				String paString = patAllele.getTrait().getTraitName() + "-" + patAllele.getTrait().getBodyPart();
				if (characterName.equals(paString)) {
					break;
				}
			}
		} else if (patAlleles.size() == 0) {
			for (int i = 0; i < matAlleles.size(); i++) {
				matAllele = matAlleles.get(i);
				String maString = matAllele.getTrait().getTraitName() + "-" + matAllele.getTrait().getBodyPart();
				if (characterName.equals(maString)) {
					break;
				}
			}
		} else {
			for (int i = 0; i < matAlleles.size(); i++) {
				matAllele = matAlleles.get(i);
				String maString = matAllele.getTrait().getTraitName() + "-" + matAllele.getTrait().getBodyPart();
				patAllele = patAlleles.get(i);
				if (characterName.equals(maString)) {
					break;
				}
			}
		}

		Allele[] result = new Allele[2];
		result[0] = matAllele;
		result[1] = patAllele;
		return result;

	}


	//returns untranslated phenotype for internal purposes
	//  eg counting etc
	public String getPhenotypeString() {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < phenotypes.size(); i++) {
			Phenotype p = phenotypes.get(geneticModel.getScrambledCharacterOrder()[i]);
			b.append(p.getTrait().getTraitName());
			b.append("-");
			b.append(p.getTrait().getBodyPart());
			b.append("/");
		}
		b.deleteCharAt(b.length() - 1);
		return b.toString();
	}


	//shows genotype
	public String getToolTipTextString() {
		return geneticModel.getPhenoTypeProcessor().getProcessedToolTipTextString(
				maternalAutosome, 
				paternalAutosome, 
				maternalSexChromosome, 
				paternalSexChromosome);
	}

	public String getSexString() {
		if (male) {
			return "Male";
		} else {
			return "Female";
		}
	}

	/**
	 * Save this organism in the JDom Element format.
	 * 
	 * @return this organism in JDom Element format
	 */
	public Element save() throws Exception {
		
		Document d = XMLParser.createDocument();
		String sex = "F";
		if (male) sex = "M";
		
		Element orga = d.createElement("O");
		orga.setAttribute("i", String.valueOf(id) + "," + String.valueOf(cageId) + "," + sex);
		orga.appendChild(maternalAutosome.save("MaternalAutosome"));
		orga.appendChild(paternalAutosome.save("PaternalAutosome"));
		orga.appendChild(maternalSexChromosome.save("MaternalSexChromosome"));
		orga.appendChild(paternalSexChromosome.save("PaternalSexChromosome"));

		return orga;
	}


	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Organism: [");
		if (!male) {
			b.append("fe");
		}
		b.append("male]\n");
		b.append("Genotype:\n");
		b.append("Maternal auto:\n");
		b.append(maternalAutosome.toString() + "\n");
		b.append("Paternal auto:\n");
		b.append(paternalAutosome.toString() + "\n");
		b.append("Maternal sex chr:\n");
		b.append(maternalSexChromosome.toString() + "\n");
		b.append("Paternal sex chr:\n");
		b.append(paternalSexChromosome.toString() + "\n");
		b.append("Phenotypes:\n");
		for (Phenotype p: phenotypes) {
			b.append(p.toString() + "\n");
		}
		b.append("pheno string=" + getPhenotypeString() + "\n");
		b.append("**organism**\n");
		return b.toString();
	}

}
