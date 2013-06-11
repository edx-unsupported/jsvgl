package edu.umb.jsVGL.client.GeneticModels;

import java.util.ArrayList;
import java.util.Random;

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
 * @version 1.0 $Id: CharacterSpecificationBank.java,v 1.11 2009-09-19 20:28:19 brian Exp $
 */

public class CharacterSpecificationBank {

	private ArrayList<CharacterSpecification> allCharSpecs;
	private Random r;

	private static CharacterSpecificationBank instance;

	private CharacterSpecificationBank() {	
		refreshAll();		
		r = new Random();
	}

	public static CharacterSpecificationBank getInstance() {
		if (instance == null) {
			instance = new CharacterSpecificationBank();
		}
		return instance;
	}

	public TraitSet getRandomTraitSet() {
		TraitSet ts = null;
		int i = 0;
		// keep looking until you get a real one
		while (ts == null) {
			i = r.nextInt(allCharSpecs.size());
			CharacterSpecification cs = allCharSpecs.get(i);
			ts = cs.getRandomTraitSet();
		}
		// then remove all similar traits from the set
		//  that way, you'll never have color twice in the same problem
		String className = ts.getClass().getName().substring(ts.getClass().getName().lastIndexOf(".") + 1);
		for (int j = 0; j < allCharSpecs.size(); j++) {
			CharacterSpecification cs = allCharSpecs.get(j);
			cs.purgeTraitSetsMatching(className);
		}
		return ts;
	}

	public void refreshAll() {
		//build the bank of possible characters
		allCharSpecs = new ArrayList<CharacterSpecification>();

		//add in the body-related characters
		// only color and shape allowed (number doesn't make sense)
		CharacterSpecification bodyCharSpecs = new CharacterSpecification();
		bodyCharSpecs.add(new ColorTraitSet("Body"));
		bodyCharSpecs.add(new ShapeTraitSet("Body"));
		allCharSpecs.add(bodyCharSpecs);

		//add in the eye-related characters
		//  only color allowed
		CharacterSpecification eyeCharSpecs = new CharacterSpecification();
		eyeCharSpecs.add(new ColorTraitSet("Eye"));
		allCharSpecs.add(eyeCharSpecs);

		//add in the antenna-related characters
		// all are possible
		CharacterSpecification antennaCharSpecs = new CharacterSpecification();
		antennaCharSpecs.add(new ColorTraitSet("Antenna"));
		antennaCharSpecs.add(new ShapeTraitSet("Antenna"));
		antennaCharSpecs.add(new NumberTraitSet("Antenna"));
		allCharSpecs.add(antennaCharSpecs);
		
		// add in the wing-related characters
		CharacterSpecification wingCharSpecs = new CharacterSpecification();
		wingCharSpecs.add(new ColorTraitSet("Wing"));
		wingCharSpecs.add(new ShapeTraitSet("Wing"));
		wingCharSpecs.add(new NumberTraitSet("Wing"));
		allCharSpecs.add(wingCharSpecs);
		
		//add in the leg-related characters
		// all are possible
		CharacterSpecification legCharSpecs = new CharacterSpecification();
		legCharSpecs.add(new ColorTraitSet("Leg"));
		legCharSpecs.add(new ShapeTraitSet("Leg"));
		legCharSpecs.add(new NumberTraitSet("Leg"));
		allCharSpecs.add(legCharSpecs);
	}
	
}
