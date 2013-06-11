package edu.umb.jsVGL.client.GeneticModels;

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
 * @version 1.0 $Id$
 */

public abstract class Trait {
	
	//eg. yellow
	private String traitName;
	//eg. color
	private String type;
	// eg. eye
	private String bodyPart;
	
	public Trait(String traitName, String type, String bodyPart) {
		this.traitName = traitName;
		this.type = type;
		this.bodyPart = bodyPart;
	}
		
	public String getTraitName() {
		return traitName;
	}

	public String getType() {
		return type;
	}

	public String getBodyPart() {
		return bodyPart;
	}
	
	public void setTraitName(String s) {
		traitName = s;
	}
	
	public String getCharacterName() {
		return  getBodyPart().toString() + " " + getType().toString();
	}
	
	public Element save(int index) throws Exception {
		Document d = XMLParser.createDocument();
		Element e = d.createElement("Trait");
		e.setAttribute("Index", String.valueOf(index));
		e.setAttribute("TraitName", traitName);
		e.setAttribute("Type", type);
		e.setAttribute("BodyPart", bodyPart);
		return e;
	}
	
	public String toString() {
		return bodyPart + ":" + type + ":" + traitName;
	}

}
