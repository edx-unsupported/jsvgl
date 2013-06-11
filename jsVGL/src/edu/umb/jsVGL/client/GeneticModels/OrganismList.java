package edu.umb.jsVGL.client.GeneticModels;

import java.util.ArrayList;
import java.util.Iterator;

import edu.umb.jsVGL.client.VGL.MFTotCounts;

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
 * @version 1.0 $Id$
 */

public class OrganismList {

	private ArrayList<Organism> organisms;
	private int numberOfMales;
	private int numberOfFemales;

	public OrganismList() {
		organisms = new ArrayList<Organism>();
		numberOfFemales = 0;
		numberOfMales = 0;
	}

	public void add(Organism o) {
		if (o.isMale()) {
			organisms.add(0, o); // if male, add to start
			numberOfMales++;
		} else {
			organisms.add(o);    //if female, add to end
			numberOfFemales++;
		}
	}

	public void add(Organism o, int orgId) {
		organisms.add(orgId, o);
		if (o.isMale()) {
			numberOfMales++;
		} else {
			numberOfFemales++;
		}

	}

	/**
	 * Return the organism which is at the given index.
	 * 
	 * @param index
	 *            the index
	 * @return the organism at the given index
	 */
	public Organism get(int index) {
		return organisms.get(index);
	}

	/**
	 * Return the organism which has the given id.
	 * 
	 * @param id
	 *            the organism's id
	 * @return the organism which has the given id
	 */
	public Organism find(int id) throws Exception {
		for (int i = 0; i < organisms.size(); i++) {
			Organism o = organisms.get(i);
			if (o.getId() == id)
				return o;
		}
		throw new GeneticsException("Cannot find Organism");
	}


	public int getNumberOfMales() {
		return numberOfMales;
	}

	public int getNumberOfFemales() {
		return numberOfFemales;
	}

	public int getTotalNumber() {
		return numberOfMales + numberOfFemales;
	}

	public MFTotCounts getMFTotCounts() {
		return new MFTotCounts(numberOfMales, numberOfFemales);
	}

	public ArrayList<Organism> getAllOrganisms() {
		return organisms;
	}

	public Iterator<Organism> iterator() {
		return organisms.iterator();
	}

	public ArrayList<Phenotype> getPhenotypes() {
		return organisms.get(0).getPhenotypes();
	}

	/** 
	 * Used by SummaryChart for counting purposes
	 * returns a string including only some or all of the
	 * parts of this phenotypeString
	 * for example, if full pheno string is
	 * 		red-eye, green-body; bent-leg
	 * and traitsToCount = {0,2}
	 * this will return "red-eye, bent-leg"
	 * @param traitsToCount
	 * @return
	 */
	public String getCustomPhenotypeString(ArrayList<Integer> selectedTraits) {
		StringBuffer b = new StringBuffer();
		Organism o = organisms.get(0);
		int[] scrambledCharacterOrder = o.getGeneticModel().getScrambledCharacterOrder();
		ArrayList<Phenotype> phenos = o.getPhenotypes();
		for (int i = 0; i < scrambledCharacterOrder.length; i++) {
			if (selectedTraits.contains(scrambledCharacterOrder[i])) {
				b.append(
						phenos.get(scrambledCharacterOrder[i])
						.getTrait().getTraitName());
				b.append("-");
				b.append(
						phenos.get(scrambledCharacterOrder[i])
						.getTrait().getBodyPart());
				b.append("/");
			}
		}
		if (b.length() > 0) {
			b.deleteCharAt(b.length() - 1);
		}
		return b.toString();
	}

}
