package edu.umb.jsVGL.client.GeneticModels;

import java.util.ArrayList;


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
 * @version 1.0 $Id: ShapeTraitSet.java,v 1.7 2009-09-19 20:28:19 brian Exp $
 */

public class ShapeTraitSet extends TraitSet {
	
	public ShapeTraitSet(String bodyPart) {
		traits = new ArrayList<Trait>();
		traits.add(new ShapeTrait("Forked", bodyPart));
		traits.add(new ShapeTrait("Long", bodyPart));
		traits.add(new ShapeTrait("Short", bodyPart));
		traits.add(new ShapeTrait("Bent", bodyPart));
		traits.add(new ShapeTrait("Pointy", bodyPart));
		traits.add(new ShapeTrait("Knobbed", bodyPart));
		traits.add(new ShapeTrait("Zigzag", bodyPart));
	}

}
