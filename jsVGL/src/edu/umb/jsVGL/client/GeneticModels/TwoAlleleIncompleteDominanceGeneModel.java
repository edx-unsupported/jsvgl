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
 * @version 1.0 $Id: TwoAlleleIncompleteDominanceGeneModel.java,v 1.11 2009-09-22 19:48:48 brian Exp $
 */

public class TwoAlleleIncompleteDominanceGeneModel extends GeneModel {

	/*
	 * Trait t1: one homozygote trait
	 * Trait t2: other homozygote trait
	 * Trait t3: heterozygote trait
	 * Trait t4: null
	 * Trait t5: null
	 * Trait t6: null
	 */

	public TwoAlleleIncompleteDominanceGeneModel(int index) {
		super(index);
	}
	
	//build from saved work file
	public TwoAlleleIncompleteDominanceGeneModel(
			NodeList traitList, int chromo, int gene) {
		super(gene);

		t1 = TraitFactory.getInstance().buildTrait((Element)traitList.item(0), chromo, gene, 1, true);
		t2 = TraitFactory.getInstance().buildTrait((Element)traitList.item(1), chromo, gene, 2, true);
		t3 = TraitFactory.getInstance().buildTrait((Element)traitList.item(2), chromo, gene, 3, true);
		setupGenoPhenoTable();
	}


	public Phenotype getPhenotype(Allele a1, Allele a2) {
		return genoPhenoTable[a1.getIntVal()][a2.getIntVal()];
	}

	public Allele[] getRandomAllelePair(boolean trueBreeding) {
		// want equal frequency of each PHENOTYPE unless true breeding
		Allele[] allelePair = new Allele[2];
		
		int x = rand.nextInt(3);
		
		if (trueBreeding) x = rand.nextInt(2); // homozygotes only if true breeding
		
		switch (x) {

		case 0:
			// 1,1 homozygote
			allelePair[0] = new Allele(t1, 1);
			allelePair[1] = new Allele(t1, 1);
			break;

		case 1:
			// 2,2 homozygote
			allelePair[0] = new Allele(t2, 2);
			allelePair[1] = new Allele(t2, 2);	
			break;
			
		case 2:			// 1,2 heterozygote
			// 2 possibilities: 1,2 and 2,1
			if(rand.nextInt(2) == 0) {
				allelePair[0] = new Allele(t1, 1);
				allelePair[1] = new Allele(t2, 2);								
			} else {
				allelePair[0] = new Allele(t2, 2);
				allelePair[1] = new Allele(t1, 1);								
			}	
		}
		return allelePair;
	}
	
	public void setupTraits() {
		//there are two alleles and three possible phenos
		// get the phenos first; then load table
		charSpecBank = CharacterSpecificationBank.getInstance();
		traitSet = charSpecBank.getRandomTraitSet();
		t1 = traitSet.getRandomTrait();   // homozygote 1
		t2 = traitSet.getRandomTrait();   // homozygote 2
		t3 = traitSet.getRandomTrait();   // heterozygote
	}

	public void setupGenoPhenoTable() {
		genoPhenoTable = new Phenotype[3][3];
		
		genoPhenoTable[0][0] = null;  				//impossible
		genoPhenoTable[0][1] = new Phenotype(t1);  	// 1,Y = 1
		genoPhenoTable[0][2] = new Phenotype(t2);   // 2,Y = 2
		
		genoPhenoTable[1][0] = new Phenotype(t1);  	// 1,Y = 1
		genoPhenoTable[1][1] = new Phenotype(t1);  	// 1,1 = 1
		genoPhenoTable[1][2] = new Phenotype(t3);   // 1,2 = 3 (inc dom)
		
		genoPhenoTable[2][0] = new Phenotype(t2);  	// 2,Y
		genoPhenoTable[2][1] = new Phenotype(t3);   // 1,2 = 3 (inc dom)
		genoPhenoTable[2][2] = new Phenotype(t2);   // 2,2
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append(t1.getCharacterName() + "<br>");
		b.append("Two Allele Incomplete Dominance<br>");
		b.append("<ul>");
		b.append("<li>" + t1.getTraitName() + " and " + t2.getTraitName() + " are homozygotes</li>");
		b.append("<li>" + t3.getTraitName() + " is the heterozygote</li>");
		b.append("</ul>");
		
		b.append("<table border=1>");
		b.append("<tr><th>Genotype</th><th>Phenotype</th></tr>");
		b.append("<tr><td>" + t1.getTraitName() + "/" + t1.getTraitName() + "</td>");
		b.append("<td>" + t1.getTraitName() +"</td></tr>");
		
		b.append("<tr><td>" + t1.getTraitName() + "/" + t2.getTraitName() + "</td>");
		b.append("<td>" + t3.getTraitName() +"</td></tr>");
		
		b.append("<tr><td>" + t2.getTraitName() + "/" + t2.getTraitName() + "</td>");
		b.append("<td>" + t2.getTraitName() +"</td></tr>");
		
		b.append("</table>");
		return b.toString();
	}

	public Element save(int index, float rf) throws Exception {
		Document d = XMLParser.createDocument();
		Element e = d.createElement("GeneModel");
		e.setAttribute("Index", String.valueOf(index));
		e.setAttribute("Type", "TwoAlleleIncompleteDominance");
		e.setAttribute("RfToPrevious", String.valueOf(rf));
		e.appendChild(t1.save(1));
		e.appendChild(t2.save(2));
		e.appendChild(t3.save(3));
		return e;
	}
	
	public String getCharacter() {
		return t1.getBodyPart() + " " + t1.getType();
	}
	
	public Trait[] getTraits() {
		Trait[] t = new Trait[3];
		t[0] = t1;
		t[1] = t2;
		t[2] = t3;
		return t;
	}
	
	public String[] getTraitStrings() {
		String[] t = new String[4];
		t[0] = "?";
		t[1] = t1.getTraitName();
		t[2] = t2.getTraitName();
		t[3] = t3.getTraitName();
		return t;
	}
	
	public String getDomTypeText() {
		return "Incomplete";
	}

	public String getInteractionHTML() {
		StringBuffer b = new StringBuffer();
		b.append("<ul>");
		b.append("<li>" + t1.getTraitName() + " is pure breeding.</li>");
		b.append("<li>" + t3.getTraitName() + " is in between ");
		b.append(t1.getTraitName() + " and " + t2.getTraitName() + ".</li>");
		b.append("<li>" + t2.getTraitName() + " is pure breeding.</li>");
		b.append("</ul>");
		return b.toString();
	}

	public int getNumAlleles() {
		return 2;
	}



}
