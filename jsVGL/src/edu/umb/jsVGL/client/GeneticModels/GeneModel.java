package edu.umb.jsVGL.client.GeneticModels;

import java.util.Random;

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
 * @version 1.0 $Id$
 */

public abstract class GeneModel {
	
	// not all will be used in all 
	public Trait t1;
	public Trait t2;
	public Trait t3;
	public Trait t4;
	public Trait t5;
	public Trait t6;
	
	int index; 
	
	CharacterSpecificationBank charSpecBank;
	TraitSet traitSet;
	
	Random rand;
	
	Phenotype[][] genoPhenoTable;
	
	public GeneModel(int index) {
		this.index = index;
		rand = new Random();
		setupTraits();
		setupGenoPhenoTable();
	}
	
	public int getIndex() {
		return index;
	}
	
	public abstract void setupTraits();
		
	public abstract void setupGenoPhenoTable();
	
	public abstract Phenotype getPhenotype(Allele a1, Allele a2);
	
	public abstract Allele[] getRandomAllelePair(boolean trueBreeding);
	
	public abstract String getCharacter();
	
	public abstract Trait[] getTraits();
	
	public abstract String[] getTraitStrings();
	
	public abstract String toString();
	
	public abstract int getNumAlleles();
	
	public String getNumAlleleText() {
		return String.valueOf(getNumAlleles());
	}
	
	public abstract String getDomTypeText();
	
	public abstract String getInteractionHTML();
	
	public abstract Element save(int index, float rf) throws Exception;
	
}
