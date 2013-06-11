package edu.umb.jsVGL.client.GeneticModels;

import java.util.ArrayList;

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
 * @version 1.0 $Id: Chromosome.java,v 1.13 2009-09-22 19:06:36 brian Exp $
 */

public class Chromosome {

	public final static int AUTOSOME = 1;
	public final static int SEX_CHROMOSOME = 0;

	private ArrayList<Allele> alleles;

	public Chromosome(ArrayList<Allele> alleles) {
		this.alleles = alleles;
	}

	public Chromosome(Chromosome c) {
		this(c.getAllAlleles());
	}

	/**
	 * constructor for building from a work file
	 */
	public Chromosome(Element e, int chromoNum) {
		alleles = new ArrayList<Allele>();
		NodeList alleleNodes = e.getChildNodes();
		for (int i = 0; i < alleleNodes.getLength(); i++) {
			Element alleleE = (Element) alleleNodes.item(i);
			String indexAndNumber = alleleE.getAttribute("i");
			String[] parts = indexAndNumber.split(",");
			int geneIndex = 
				Integer.parseInt(parts[0]);
			int traitNum = 
				Integer.parseInt(parts[1]);
			Allele allele = new Allele(
					TraitFactory.getInstance().getTrait(
							chromoNum, geneIndex, traitNum), 
							traitNum);
			alleles.add(geneIndex, allele);
		}

	}

	public Allele getAllele(int i) {
		return alleles.get(i);
	}

	public ArrayList<Allele> getAllAlleles() {
		return alleles;
	}

	public Element save(String id) throws Exception {
		Document d = XMLParser.createDocument();
		Element e = d.createElement("C");

		String shortId = "";
		if (id.equals("MaternalAutosome")) shortId = "MA";
		if (id.equals("PaternalAutosome")) shortId = "PA";
		if (id.equals("MaternalSexChromosome")) shortId = "MS";
		if (id.equals("PaternalSexChromosome")) shortId = "PS";
		
		e.setAttribute("i", shortId);

		if (this == NullSexChromosome.getInstance()) {
			e.setAttribute("s", String.valueOf(-1));
		} else {
			e.setAttribute("s", String.valueOf(alleles.size()));
		}

		for (int i = 0; i < alleles.size(); i++) {
			e.appendChild(alleles.get(i).save(i));
		}
		return e;
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		for(Allele a: alleles) {
			b.append(a.getTrait().getTraitName().toString()
					+ "-"
					+ a.getTrait().getBodyPart().toString()
					+ ";");
		}
		if (b.length() > 0) {
			b.deleteCharAt(b.length() - 1);
		}
		return b.toString();
	}

}
