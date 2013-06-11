package edu.umb.jsVGL.client.GeneticModels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;

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
 * @version 1.0 $Id: WorkFileProcessor.java,v 1.9 2008-10-25 18:55:20 brian Exp $
 */

/**
 * class to read in work files and build a GeneticModel
 * and a set of populated cages
 * @author brian
 *
 */
public class WorkFileProcessor {

	private String problemFileName;
	private GeneticModel geneticModel;
	private ArrayList<Cage> cages;
	private Element modelBuilderState;

	public WorkFileProcessor(NodeList elements) {
		problemFileName = "";
		for (int i = 0; i < elements.getLength(); i++) {
			Element current = (Element)elements.item(i);
			String name = current.getTagName();
			try {
				if (name.equals("ProbFileName")) problemFileName = current.getFirstChild().getNodeValue();
				if (name.equals("GeneticModel")) geneticModel = processSavedModelInfo(current);
				if (name.equals("Organisms")) cages = processSavedCages(current);
				if (name.equals("ModelBuilderState")) modelBuilderState = current;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Element getModelBuilderState() {
		return modelBuilderState;
	}

	public GeneticModel getGeneticModel() {
		return geneticModel;
	}

	public ArrayList<Cage> getCages() {
		return cages;
	}

	private GeneticModel processSavedModelInfo(Element e) throws Exception {

		GeneticModel model = 
				new GeneticModel(Boolean.parseBoolean(e.getAttribute("XX_XYSexDetermination")));

		model.setProblemFileName(problemFileName);

		model.setMinOffspring(Integer.parseInt(e.getAttribute("MinOffspring")));
		model.setMaxOffspring(Integer.parseInt(e.getAttribute("MaxOffspring")));

		//get the tags inside the "Model" tag
		if (e.getAttribute("BeginnerMode") != null) {
			model.setBeginnerMode(Boolean.parseBoolean(e.getAttribute("BeginnerMode")));
		} 
		int numberOfCharacters = Integer.parseInt(e.getAttribute("NumberOfCharacters"));

		int numberOfGeneModels = Integer.parseInt(e.getAttribute("NumberOfGeneModels"));


		TraitFactory.getInstance().initializeTraitBank(2, numberOfGeneModels, 6);
		CharacterSpecificationBank.getInstance().refreshAll();

		// now the rest
		NodeList parts = e.getChildNodes();
		for (int i = 0; i < parts.getLength(); i++) {
			Element current = (Element)parts.item(i);
			String name = current.getTagName();

			if (name.equals("ProblemTypeSpecification")) {
				model.setProblemTypeSPecification(
						GeneticModelFactory.getInstance().processModelSpecElements(
								current.getChildNodes()));
			}

			if (name.equals("CharacterOrderScrambler")) {
				int[] scrambler = new int[numberOfCharacters];
				NodeList csItems = current.getChildNodes();
				for (int j = 0; j < csItems.getLength(); j++) {
					Element te = (Element)csItems.item(j);
					scrambler[Integer.parseInt(te.getAttribute("Index"))] = 
							Integer.parseInt(te.getAttribute("Character"));
				}
				model.setScrambledCharacterOrder(scrambler);

			} else if (name.equals("ChromosomeModel")) {
				boolean sexChromosome = Boolean.parseBoolean(current.getAttribute("SexChromosome"));
				processChromosomeModelInfo(model, sexChromosome, current);
			} else if (name.equals("PhenotypeProcessor")) 
				model.getPhenoTypeProcessor().load(current);
		}
		return model;
	}

	private void processChromosomeModelInfo(
			GeneticModel model, 
			Boolean sexChromosome, 
			Element e) throws Exception {

		int chromoNumber = 1;
		if (sexChromosome) chromoNumber = 0;

		// set up the geneModels first
		NodeList gmParts = e.getChildNodes();
		int geneModelIndex = 0;
		float rf = -1.0f;
		for (int i = 0; i < gmParts.getLength(); i++) {
			Element gmEl = (Element)gmParts.item(i);
			rf = Float.parseFloat(gmEl.getAttribute("RfToPrevious"));
			GeneModel geneModel = buildGeneModel(gmEl, chromoNumber, geneModelIndex);

			if (sexChromosome) {
				if (geneModelIndex == 0) {
					model.addFirstSexLinkedGeneModel(geneModel);
				} else {
					model.addNextSexLinkedGeneModel(rf, geneModel);
				}
			} else {
				if (geneModelIndex == 0) {
					model.addFirstAutosomalGeneModel(geneModel);
				} else {
					model.addNextAutosomalGeneModel(rf, geneModel);
				}
			}

			geneModelIndex++;
		}
	}

	private GeneModel buildGeneModel(Element e, int chromo, int gene) {
		String type = e.getAttribute("Type");

		NodeList traitList = e.getChildNodes();
		if (type.equals("TwoAlleleSimpleDominance")) {
			return new TwoAlleleSimpleDominanceGeneModel(
					traitList, chromo, gene);
		} else if(type.equals("TwoAlleleIncompleteDominance")) {
			return new TwoAlleleIncompleteDominanceGeneModel(
					traitList, chromo, gene);
		} else if(type.equals("ThreeAlleleHierarchicalDominance")) {
			return new ThreeAlleleHierarchicalDominanceGeneModel(
					traitList, chromo, gene);
		} else if(type.equals("ThreeAlleleCircularDominance")) {
			return new ThreeAlleleCircularDominanceGeneModel(
					traitList, chromo, gene);			
		} else if(type.equals("ThreeAlleleIncompleteDominance")) {
			return new ThreeAlleleIncompleteDominanceGeneModel(
					traitList, chromo, gene);						
		} else if (type.equals("Interacting")){
			return new InteractingGeneModel(traitList, chromo, gene);
		} else {
			return null;
		}
	}

	private ArrayList<Cage> processSavedCages(Element e) {
		ArrayList<Cage> cages = new ArrayList<Cage>();
		NodeList cageNodes = e.getChildNodes();
		for (int i = 0; i < cageNodes.getLength(); i++) {
			Element cageE = (Element)cageNodes.item(i);
			if(cageE.getTagName().equals("Cage")) {
				Cage c = buildCage(cageE);
				cages.add(c);
			}
		}
		return cages;
	}

	private Cage buildCage(Element e) {
		int cageId;
		cageId = Integer.parseInt(e.getAttribute("Id"));
		int numChildren = Integer.parseInt(e.getAttribute("NumChildren"));

		Cage cage = new Cage(cageId);

		cage.setSuper(Boolean.parseBoolean(e.getAttribute("SuperCross")));
		
		/*
		 * if we're loading a supercross, it has already been "trimmed"
		 * 	(only save the visible organisms)
		 *  need to mark this for display properly
		 */
		cage.setAlreadyBeenTrimmed(cage.isSuperCross());

		NodeList contentNodes = e.getChildNodes();
		for (int i = 0; i < contentNodes.getLength(); i++) {
			Element item = (Element)contentNodes.item(i);
			if (item.getTagName().equals("Parents")) {
				NodeList parentNodes = item.getChildNodes();
				Element p1E = (Element)parentNodes.item(0);
				Organism p1 = 
						OrganismFactory.buildOrganism(
								p1E, 
								Integer.parseInt(p1E.getAttribute("i").split(",")[1]),
								geneticModel);
				Element p2E = (Element)parentNodes.item(1);
				Organism p2 = 
						OrganismFactory.buildOrganism(
								p2E,
								Integer.parseInt(p2E.getAttribute("i").split(",")[1]),
								geneticModel);
				cage.setParents(p1, p2);
			} else if (item.getTagName().equals("Children")) {
				TreeMap<Integer, Organism> childrenInOrder = new TreeMap<Integer, Organism>();
				NodeList childNodes = item.getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					Element childE = (Element)childNodes.item(j);
					if (childE.getTagName().equals("O")) {
						int index = Integer.parseInt(childE.getAttribute("i").split(",")[0]);
						childrenInOrder.put(index,
								OrganismFactory.buildOrganism(
										childE, cageId, geneticModel));
					}
				}

				Iterator<Integer> sortedChildIt = childrenInOrder.keySet().iterator();
				while (sortedChildIt.hasNext()) {
					/*
					 * need to check if present because saved supercross only saves visible
					 * organisms, and they don't have sequential id numbers
					 */
					Organism o = childrenInOrder.get(sortedChildIt.next());
					if (o != null) {
						cage.addSaved(o);
					}
				}
				// this is for super cross only
			} else if (item.getTagName().equals("PhenoCountData")) {
				cage.addToPhenotypeCounts(
						item.getAttribute("Pheno"), 
						new MFTotCounts(
								Integer.parseInt(item.getAttribute("Ms")), 
										Integer.parseInt(item.getAttribute("Fs"))));
			}
		}
		return cage;
	}

}
