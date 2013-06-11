package edu.umb.jsVGL.client.GeneticModels;

import com.google.gwt.xml.client.Element;

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
 * @version 1.0 $Id: TraitFactory.java,v 1.6 2008-07-03 01:53:43 brian Exp $
 */

public class TraitFactory {
	
	/**
	 * place to put all Traits for access by Organisms
	 * indexed by 	[chromosome: 0 = auto, 1 = sex]
	 * 				[gene# on chromosome]
	 * 				[trait# for that gene on that chromo]
	 * ** must be initialized based on the current model
	 */
	Trait[][][] traitBank;  
	
	private static TraitFactory instance;
	
	private TraitFactory() {
		
	}
	
	public static TraitFactory getInstance() {
		if (instance == null) {
			instance = new TraitFactory();
		}
		return instance;
	}
	
	public void initializeTraitBank(
			int numChromos, 
			int numGenes, 
			int maxNumTraitsPerGene) {

		traitBank = new Trait[numChromos][numGenes][maxNumTraitsPerGene + 1];
		
		for (int i = 0; i < numChromos; i++) {
			for (int j = 0; j < numGenes; j++) {
				for (int k = 0; k < maxNumTraitsPerGene + 1; k++) {
					traitBank[i][j][k] = null;
				}
			}
		}
	}
	
	public Trait buildTrait(Element e, int chromo, int gene, int traitNum, boolean addToTraitBank) {
		String traitName = e.getAttribute("TraitName");
		String type = e.getAttribute("Type");
		String bodyPart = e.getAttribute("BodyPart");

		Trait t = null;
		if (type.equals("Color")) {
			t = new ColorTrait(traitName, bodyPart);
		} else if (type.equals("Number")) {
			t = new NumberTrait(traitName, bodyPart);
		} else if (type.equals("Shape")) {
			t = new ShapeTrait(traitName, bodyPart);
		} else if (type.equals("Simple")) {
			t = new SimpleTrait(traitName);
		}
		if (addToTraitBank) traitBank[chromo][gene][traitNum] = t;

		return t;
	}
	
	public Trait getTrait(int chromoNum, int geneNum, int traitNum) {
		return traitBank[chromoNum][geneNum][traitNum];
	}

}
