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
 * @version 1.0 $Id$
 */

public abstract class TraitSet {
	
	ArrayList<Trait> traits;
	Random r;
	
	public TraitSet() {
		r = new Random();
	}
	
	public int getNumberOfMembers() {
		return traits.size();
	}
	
	//pick a random one and delete it from the list
	public Trait getRandomTrait() {
		if (traits.size() == 0) {
			return null;
		}
		int i = r.nextInt(traits.size());
		Trait t = traits.get(i);
		traits.remove(i);
		return t;
	}
	
	public String getBodyPart() {
		return traits.get(0).getBodyPart();
	}

}
