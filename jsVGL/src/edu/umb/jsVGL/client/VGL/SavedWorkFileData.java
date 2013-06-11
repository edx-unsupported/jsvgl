package edu.umb.jsVGL.client.VGL;

import java.util.ArrayList;

import com.google.gwt.xml.client.Element;

import edu.umb.jsVGL.client.GeneticModels.Cage;
import edu.umb.jsVGL.client.GeneticModels.GeneticModel;
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
 * @version 1.0 $Id: GeneticModelAndCageSet.java,v 1.2 2008-06-24 14:13:46 brian Exp $
 */

/**
 * data class for passing result of parsed work file
 * from the WorkFileProcessor to VGL
 * @author brian
 *
 */
public class SavedWorkFileData {
	
	private GeneticModel geneticModel;
	private ArrayList<Cage> cages;
	private Element modelBuilderState;
	
	public SavedWorkFileData(GeneticModel geneticModel, 
			ArrayList<Cage> cages,
			Element modelBuilderState) {
		this.geneticModel = geneticModel;
		this.cages = cages;
		this.modelBuilderState = modelBuilderState;
	}

	public GeneticModel getGeneticModel() {
		return geneticModel;
	}

	public ArrayList<Cage> getCages() {
		return cages;
	}
	
	public Element getModelBuilderState() {
		return modelBuilderState;
	}
	

}
