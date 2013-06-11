package edu.umb.jsVGL.client.GeneticModels;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
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
 * @version 1.0 $Id: ThreeAlleleIncompleteDominanceGeneModel.java,v 1.11 2009-09-22 19:48:48 brian Exp $
 */

public class ThreeAlleleIncompleteDominanceGeneModel extends GeneModel {

	/*
	 * Trait t1: homozygote 1
	 * Trait t2: homozygote 2
	 * Trait t3: homozygote 3
	 * Trait t4: 1,2 heterozygote
	 * Trait t5: 2,3 heterozygote
	 * Trait t6: 3,1 heterozygote
	 */

	public ThreeAlleleIncompleteDominanceGeneModel(int index) {
		super(index);
	}

	//build from saved work file
	public ThreeAlleleIncompleteDominanceGeneModel(
			NodeList traitList, int chromo, int gene) {
		super(gene);

		t1 = TraitFactory.getInstance().buildTrait((Element)traitList.item(0), chromo, gene, 1, true);
		t2 = TraitFactory.getInstance().buildTrait((Element)traitList.item(1), chromo, gene, 2, true);
		t3 = TraitFactory.getInstance().buildTrait((Element)traitList.item(2), chromo, gene, 3, true);
		t4 = TraitFactory.getInstance().buildTrait((Element)traitList.item(3), chromo, gene, 4, true);
		t5 = TraitFactory.getInstance().buildTrait((Element)traitList.item(4), chromo, gene, 5, true);
		t6 = TraitFactory.getInstance().buildTrait((Element)traitList.item(5), chromo, gene, 6, true);
		setupGenoPhenoTable();
	}

	public Phenotype getPhenotype(Allele a1, Allele a2) {
		return genoPhenoTable[a1.getIntVal()][a2.getIntVal()];
	}

	public Allele[] getRandomAllelePair(boolean trueBreeding) {
		// want equal frequency of each PHENOTYPE unless true breeding
		Allele[] allelePair = new Allele[2];

		int x = rand.nextInt(6);

		if (trueBreeding) x = rand.nextInt(3);  // homozygotes only

		switch (x) {

		case 0:
			// phenotype 1
			// 1,1 homozygote
			allelePair[0] = new Allele(t1, 1);
			allelePair[1] = new Allele(t1, 1);
			break;

		case 1:
			// phenotype 2
			// 2,2 homozygote
			allelePair[0] = new Allele(t2, 2);
			allelePair[1] = new Allele(t2, 2);
			break;

		case 2:
			// phenotype 3
			// 3,3 homozygote
			allelePair[0] = new Allele(t3, 3);
			allelePair[1] = new Allele(t3, 3);
			break;

		case 3:
			// 1,2 heterozygote
			// 2 possibilities: 1,2 and 2,1
			if(rand.nextInt(2) == 0) {
				allelePair[0] = new Allele(t1, 1);
				allelePair[1] = new Allele(t2, 2);								
			} else {
				allelePair[0] = new Allele(t2, 2);
				allelePair[1] = new Allele(t1, 1);								
			}

		case 4:
			// 2,3 heterozygote
			// 2 possibilities: 3,2 and 2,3
			if(rand.nextInt(2) == 0) {
				allelePair[0] = new Allele(t3, 3);
				allelePair[1] = new Allele(t2, 2);								
			} else {
				allelePair[0] = new Allele(t2, 2);
				allelePair[1] = new Allele(t3, 3);								
			}	

		case 5:
			// 1,3 heterozygote
			// 2 possibilities: 1,3 and 3,1
			if(rand.nextInt(2) == 0) {
				allelePair[0] = new Allele(t1, 1);
				allelePair[1] = new Allele(t3, 3);								
			} else {
				allelePair[0] = new Allele(t3, 3);
				allelePair[1] = new Allele(t1, 1);								
			}

		}
		return allelePair;
	}

	public void setupTraits() {
		//there are three alleles and six possible phenos
		// get the phenos first; then load table
		charSpecBank = CharacterSpecificationBank.getInstance();
		traitSet = charSpecBank.getRandomTraitSet();
		t1 = traitSet.getRandomTrait();   // homo 1
		t2 = traitSet.getRandomTrait();   // homo 2
		t3 = traitSet.getRandomTrait();   // homo 3
		t4 = traitSet.getRandomTrait();   // 1,2 het
		t5 = traitSet.getRandomTrait();   // 2,3 het
		t6 = traitSet.getRandomTrait();   // 1,3 het
	}

	public void setupGenoPhenoTable() {
		genoPhenoTable = new Phenotype[4][4];

		genoPhenoTable[0][0] = null;  				//impossible
		genoPhenoTable[0][1] = new Phenotype(t1);  	// 1,Y = 1
		genoPhenoTable[0][2] = new Phenotype(t2);   // 2,Y = 2
		genoPhenoTable[0][3] = new Phenotype(t3);   // 3,Y = 3	

		genoPhenoTable[1][0] = new Phenotype(t1);  	// 1,Y = 1
		genoPhenoTable[1][1] = new Phenotype(t1);  	// 1,1 = 1 
		genoPhenoTable[1][2] = new Phenotype(t4);   // 1,2 = 4 
		genoPhenoTable[1][3] = new Phenotype(t6);   // 1,3 = 6 

		genoPhenoTable[2][0] = new Phenotype(t2);  	// 2,Y = 2
		genoPhenoTable[2][1] = new Phenotype(t4);   // 2,1 = 4 
		genoPhenoTable[2][2] = new Phenotype(t2);   // 2,2 = 2
		genoPhenoTable[2][3] = new Phenotype(t5);   // 2,3 = 5 

		genoPhenoTable[3][0] = new Phenotype(t3);   // 3,Y = 3
		genoPhenoTable[3][1] = new Phenotype(t6);   // 3,1 = 6 
		genoPhenoTable[3][2] = new Phenotype(t5);   // 3,2 = 5 
		genoPhenoTable[3][3] = new Phenotype(t3);   // 3,3 = 3 
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append(t1.getCharacterName() + "<br>");
		b.append("Three Allele Incomplete Dominance<br>");
		b.append("<table border=1>");
		b.append("<tr><th>Genotype</th><th>Phenotype</th></tr>");
		b.append("<tr><td>" + t1.getTraitName() + "/" + t1.getTraitName() + "</td>");
		b.append("<td>" + t1.getTraitName() +"</td></tr>");

		b.append("<tr><td>" + t1.getTraitName() + "/" + t2.getTraitName() + "</td>");
		b.append("<td>" + t4.getTraitName() +"</td></tr>");

		b.append("<tr><td>" + t2.getTraitName() + "/" + t2.getTraitName() + "</td>");
		b.append("<td>" + t2.getTraitName() +"</td></tr>");

		b.append("<tr><td>" + t2.getTraitName() + "/" + t3.getTraitName() + "</td>");
		b.append("<td>" + t5.getTraitName() +"</td></tr>");

		b.append("<tr><td>" + t3.getTraitName() + "/" + t3.getTraitName() + "</td>");
		b.append("<td>" + t3.getTraitName() +"</td></tr>");

		b.append("<tr><td>" + t3.getTraitName() + "/" + t1.getTraitName() + "</td>");
		b.append("<td>" + t6.getTraitName() +"</td></tr>");

		b.append("</table>");
		return b.toString();
	}

	public Element save(int index, float rf) throws Exception {
		Document d = XMLParser.createDocument();
		Element e = d.createElement("GeneModel");
		e.setAttribute("Index", String.valueOf(index));
		e.setAttribute("Type", "ThreeAlleleIncompleteDominance");
		e.setAttribute("RfToPrevious", String.valueOf(rf));
		e.appendChild(t1.save(1));
		e.appendChild(t2.save(2));
		e.appendChild(t3.save(3));
		e.appendChild(t4.save(4));
		e.appendChild(t5.save(5));
		e.appendChild(t6.save(6));
		return e;
	}

	public String getCharacter() {
		return t1.getBodyPart() + " " + t1.getType();
	}

	public Trait[] getTraits() {
		Trait[] t = new Trait[6];
		t[0] = t1;
		t[1] = t2;
		t[2] = t3;
		t[3] = t4;
		t[4] = t5;
		t[5] = t6;
		return t;
	}

	public String[] getTraitStrings() {
		String[] t = new String[7];
		t[0] = "?";
		t[1] = t1.getTraitName();
		t[2] = t2.getTraitName();
		t[3] = t3.getTraitName();
		t[4] = t4.getTraitName();
		t[5] = t5.getTraitName();
		t[6] = t6.getTraitName();
		return t;
	}

	public String getDomTypeText() {
		return "Incomplete";
	}

	public String getInteractionHTML() {
		StringBuffer b = new StringBuffer();
		b.append("<ul>");
		b.append("<li>" + t1.getTraitName() + " is pure breeding.</li>");
		b.append("<li>" + t4.getTraitName() + " is in between ");
		b.append(t1.getTraitName() + " and " + t2.getTraitName() + ".</li>");
		b.append("<li>" + t2.getTraitName() + " is pure breeding.</li>");
		b.append("<li>" + t5.getTraitName() + " is in between ");
		b.append(t2.getTraitName() + " and " + t3.getTraitName() + ".</li>");
		b.append("<li>" + t3.getTraitName() + " is pure breeding.</li>");
		b.append("<li>" + t6.getTraitName() + " is in between ");
		b.append(t1.getTraitName() + " and " + t3.getTraitName() + ".</li>");
		b.append("</ul>");
		return b.toString();
	}


	public int getNumAlleles() {
		return 3;
	}


}
