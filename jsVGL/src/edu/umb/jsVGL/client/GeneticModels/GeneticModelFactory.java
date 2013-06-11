package edu.umb.jsVGL.client.GeneticModels;

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

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import edu.umb.jsVGL.client.VGL.Base64Coder;
import edu.umb.jsVGL.client.VGL.SavedWorkFileData;

public class GeneticModelFactory {

	private Random r;

	private static GeneticModelFactory instance;

	private GeneticModelFactory() {
		r = new Random();
	}

	public static GeneticModelFactory getInstance() {
		if (instance == null) {
			instance = new GeneticModelFactory();
		}
		return instance;
	}


	// use params from web page
	public GeneticModel createRandomModel(Dictionary params) {
		GeneticModel model = null;
		ProblemTypeSpecification specs = 
				processModelSpecParams(params);
		model = createRandomModel(specs);
		model.scrambleTraitOrder();
		return model;
	}
	
	public SavedWorkFileData setupModelAndStateFromBase64Zip(String input) {
		String xmlString = Base64Coder.readBase64Zip(input);
		return readModelFromXML(xmlString);
	}

	public SavedWorkFileData readModelFromXML(String xmlString) {
		SavedWorkFileData result = null;
		Document doc = XMLParser.parse(xmlString);
		WorkFileProcessor processor = 
				new WorkFileProcessor(doc.getDocumentElement().getChildNodes());
		result = 
				new SavedWorkFileData(
						processor.getGeneticModel(), 
						processor.getCages(),
						processor.getModelBuilderState());
		return result;
	}

	public GeneticModel createTestModel() {
		GeneticModel model = new GeneticModel(true);
		try {
			model.addFirstAutosomalGeneModel(new TwoAlleleSimpleDominanceGeneModel(0));
			model.addNextAutosomalGeneModel(0.2f, new TwoAlleleSimpleDominanceGeneModel(1));
		} catch (GeneticsException e) {
			e.printStackTrace();
		}
		return model;
	}

	public ProblemTypeSpecification processModelSpecElements(NodeList elements) {
		ProblemTypeSpecification problemSpec = new ProblemTypeSpecification();

		for (int i = 0; i < elements.getLength(); i++) {
			Element current = (Element)elements.item(i);
			problemSpec = updateProblemSpec(problemSpec, current.getTagName(), current.getFirstChild().getNodeValue());
		}
		return problemSpec;
	}

	public ProblemTypeSpecification processModelSpecParams(Dictionary params) {
		ProblemTypeSpecification problemSpec = new ProblemTypeSpecification();
		Set<String> paramNames = params.keySet();
		Iterator<String> paramNameIt = paramNames.iterator();
		while (paramNameIt.hasNext()) {
			String name = paramNameIt.next();
			String value = params.get(name);
			problemSpec = updateProblemSpec(problemSpec, name, value);
		}
		return problemSpec;
	}

	private ProblemTypeSpecification updateProblemSpec(
			ProblemTypeSpecification origPS, 
			String paramName, 
			String paramValue) {
		
		if (paramName.equals("FieldPopTrueBreeding"))
			origPS.setFieldPopTrueBreeding(
					Boolean.parseBoolean(paramValue));

		if (paramName.equals("BeginnerMode"))
			origPS.setBeginnerMode(
					Boolean.parseBoolean(paramValue));

		if (paramName.equals("MinOffspring"))
			origPS.setMinOffspring(
					Integer.parseInt(paramValue));
		if (paramName.equals("MaxOffspring"))
			origPS.setMaxOffspring(
					Integer.parseInt(paramValue));

		if (paramName.equals("ZZ_ZW")) 
			origPS.setChZZ_ZW(
					Float.parseFloat(paramValue));

		if (paramName.equals("Gene1_SexLinked"))
			origPS.setGene1_chSexLinked(
					Float.parseFloat(paramValue));
		if (paramName.equals("Gene1_3Alleles"))
			origPS.setGene1_ch3Alleles(
					Float.parseFloat(paramValue));
		if (paramName.equals("Gene1_IncDom"))
			origPS.setGene1_chIncDom(
					Float.parseFloat(paramValue));
		if (paramName.equals("Gene1_CircDom"))
			origPS.setGene1_chCircDom(
					Float.parseFloat(paramValue));

		if (paramName.equals("Gene2_Present"))
			origPS.setGene2_chPresent(
					Float.parseFloat(paramValue));
		if (paramName.equals("Gene2_SameChrAsGene1"))
			origPS.setGene2_chSameChrAsGene1(
					Float.parseFloat(paramValue));
		if (paramName.equals("Gene2_MinRfToGene1"))
			origPS.setGene2_minRfToGene1(
					Float.parseFloat(paramValue));
		if (paramName.equals("Gene2_MaxRfToGene1"))
			origPS.setGene2_maxRfToGene1(
					Float.parseFloat(paramValue));
		if (paramName.equals("Gene2_3Alleles"))
			origPS.setGene2_ch3Alleles(
					Float.parseFloat(paramValue));
		if (paramName.equals("Gene2_IncDom"))
			origPS.setGene2_chIncDom(
					Float.parseFloat(paramValue));
		if (paramName.equals("Gene2_CircDom"))
			origPS.setGene2_chCircDom(
					Float.parseFloat(paramValue));

		if (paramName.equals("Gene3_Present"))
			origPS.setGene3_chPresent(
					Float.parseFloat(paramValue));
		if (paramName.equals("Gene3_SameChrAsGene1"))
			origPS.setGene3_chSameChrAsGene1(
					Float.parseFloat(paramValue));
		if (paramName.equals("Gene3_MinRfToPrevGene"))
			origPS.setGene3_minRfToPrevGene(
					Float.parseFloat(paramValue));
		if (paramName.equals("Gene3_MaxRfToPrevGene"))
			origPS.setGene3_maxRfToPrevGene(
					Float.parseFloat(paramValue));
		if (paramName.equals("Gene3_3Alleles"))
			origPS.setGene3_ch3Alleles(
					Float.parseFloat(paramValue));
		if (paramName.equals("Gene3_IncDom"))
			origPS.setGene3_chIncDom(
					Float.parseFloat(paramValue));
		if (paramName.equals("Gene3_CircDom"))
			origPS.setGene3_chCircDom(
					Float.parseFloat(paramValue));

		// params for epistasis & complementation
		if (paramName.equals("PhenotypeInteraction"))
			origPS.setPhenotypeInteraction(
					Float.parseFloat(paramValue));
		if (paramName.equals("Epistasis"))
			origPS.setEpistasis(
					Float.parseFloat(paramValue));
		
		return origPS;
	}


	private GeneticModel createRandomModel(ProblemTypeSpecification specs) {
		GeneticModel model = null;

		boolean gene1SexLinked = false;

		// first, the global sex-determination mechanism
		//  even if no sex-linked genes
		if (r.nextFloat() < specs.getChZZ_ZW()) {
			model = new GeneticModel(false);
		} else {
			model = new GeneticModel(true);
		}

		model.setProblemTypeSPecification(specs);

		//beginner mode
		if (specs.isBeginnerMode()) model.setBeginnerMode(true);

		// field pop is only true-breeding
		if (specs.isFieldPopTrueBreeding()) model.setFieldPopTrueBreeding(true);

		//# of offspring generated
		model.setMinOffspring(specs.getMinOffspring());
		model.setMaxOffspring(specs.getMaxOffspring());

		try {
			/*
			 * first, see if there's a phenotype interaction
			 * if so, this requires a special setup
			 *  just use the sex-linkage status of genes 1 and 2
			 *  they must be 2-allele simple dom for these to work
			 */
			if (specs.getPhenotypeInteraction() != 0.0f) {
				if (r.nextFloat() < specs.getPhenotypeInteraction()) {
					/*
					 * set up the two gene models
					 *   they must be 2-allele simple dom
					 *   but they CAN be sex-linked
					 */
					if (r.nextFloat() < specs.getGene1_chSexLinked()) {
						model.addFirstSexLinkedGeneModel(new InteractingGeneModel(0));
						gene1SexLinked = true;
					} else {
						model.addFirstAutosomalGeneModel(new InteractingGeneModel(0));
					}

					GeneModel gene2Model = new InteractingGeneModel(1);
					addGeneModelRandomly(
							model, 
							gene1SexLinked, 
							specs.getGene2_chSameChrAsGene1(), 
							specs.getGene2_minRfToGene1(), 
							specs.getGene2_maxRfToGene1(), 
							gene2Model);

					if (r.nextFloat() < specs.getEpistasis()) {
						model.setPhenotypeInteraction(PhenotypeProcessor.EPISTASIS);
					} else {
						model.setPhenotypeInteraction(PhenotypeProcessor.COMPLEMENTATION);
					}

				} else {
					/* 
					 * the default is one simply dominant 2-allele model (for Complementation)
					 * or a three allele hierarchical model for Epistasis
					 */
					GeneModel gm;
					if (r.nextFloat() < specs.getEpistasis()) {
						// epistasis - 2 allele inc dom
						gm = getRandomGeneModel(0, 0.0f, 1.0f, 0.0f);
					} else {
						// compl - 2 allele simple dom
						gm = getRandomGeneModel(0, 0.0f, 0.0f, 0.0f);
					}
					if (r.nextFloat() < specs.getGene1_chSexLinked()) {
						model.addFirstSexLinkedGeneModel(gm);
					} else {
						model.addFirstAutosomalGeneModel(gm);
					}
					model.setPhenotypeInteraction(PhenotypeProcessor.NO_INTERACTION);
				}

			} else {
				/*
				 * otherwise, process normally
				 */
				model.setPhenotypeInteraction(PhenotypeProcessor.NO_INTERACTION);

				//first gene (always must be one)
				if (r.nextFloat() < specs.getGene1_chSexLinked()) {
					model.addFirstSexLinkedGeneModel(getRandomGeneModel(
							0,
							specs.getGene1_ch3Alleles(),
							specs.getGene1_chIncDom(),
							specs.getGene1_chCircDom()));
					gene1SexLinked = true;
				} else {
					model.addFirstAutosomalGeneModel(getRandomGeneModel(
							0,
							specs.getGene1_ch3Alleles(),
							specs.getGene1_chIncDom(),
							specs.getGene1_chCircDom()));
				}

				// second gene (may be one)
				if (r.nextFloat() < specs.getGene2_chPresent()) {
					GeneModel gene2Model = getRandomGeneModel(
							1,
							specs.getGene2_ch3Alleles(), 
							specs.getGene2_chIncDom(),
							specs.getGene2_chCircDom());
					addGeneModelRandomly(
							model, 
							gene1SexLinked, 
							specs.getGene2_chSameChrAsGene1(), 
							specs.getGene2_minRfToGene1(), 
							specs.getGene2_maxRfToGene1(), 
							gene2Model);
				} else {
					// no second gene (therefore no third)
					return model;
				}
			}
			//third gene (may be one)

			if (r.nextFloat() < specs.getGene3_chPresent()) {
				GeneModel gene3Model = getRandomGeneModel(
						2,
						specs.getGene3_ch3Alleles(), 
						specs.getGene3_chIncDom(),
						specs.getGene3_chCircDom());

				addGeneModelRandomly(
						model, 
						gene1SexLinked, 
						specs.getGene3_chSameChrAsGene1(), 
						specs.getGene3_minRfToPrevGene(), 
						specs.getGene3_maxRfToPrevGene(), 
						gene3Model);

			} else {
				// no third gene (therefore no third)
				return model;
			}

		} catch (GeneticsException e) {
			e.printStackTrace();
		}

		return model;
	}

	private GeneModel getRandomGeneModel(int index, float ch3Alleles, float chIncDom, float chCircDom) {

		GeneModel geneModel = null;

		boolean threeAlleles = false;
		if (r.nextFloat() < ch3Alleles) threeAlleles = true;

		if (r.nextFloat() < chIncDom) {
			// inc dom
			if (threeAlleles) {
				geneModel = new ThreeAlleleIncompleteDominanceGeneModel(index);
			} else {
				geneModel = new TwoAlleleIncompleteDominanceGeneModel(index);
			}

		} else {
			// simple dom
			if (threeAlleles) {
				// choice for circ or hierarch dom
				if (r.nextFloat() < chCircDom) {
					geneModel = new ThreeAlleleCircularDominanceGeneModel(index);
				} else {
					geneModel = new ThreeAlleleHierarchicalDominanceGeneModel(index);
				}				
			} else {
				geneModel = new TwoAlleleSimpleDominanceGeneModel(index);
			}
		}
		return geneModel;
	}

	// adds to GeneticModel - modifies it
	private void addGeneModelRandomly(GeneticModel model,
			boolean prevGeneSexLinked,
			float chSameChromo, 
			float minRf, 
			float maxRf,
			GeneModel geneModel) throws GeneticsException{

		boolean sameChr = false;
		float rf = (r.nextFloat() * (maxRf - minRf)) + minRf;

		if (r.nextFloat() < chSameChromo) sameChr = true;

		if (prevGeneSexLinked) {
			if (sameChr) {
				// add to sex-chromosome
				// we know that there's already at least one
				//  gene on the sex-chromo so addNext
				model.addNextSexLinkedGeneModel(rf, geneModel);
			} else {
				// add to autosome
				//there may be an autosomal model or not
				// so check
				if (model.anyTraitsOnAutosome()) {
					model.addNextAutosomalGeneModel(rf, geneModel);
				} else {
					model.addFirstAutosomalGeneModel(geneModel);
				}
			}
		} else {
			//prev was autosomal
			if (sameChr) {
				//add to autosome
				// since sameChr, we know there's already
				//  at least one gene there already
				model.addNextAutosomalGeneModel(rf, geneModel);
			} else {
				// add to sex-chromosome
				//  there may or may not be genes there already
				//  so check
				if (model.anyTraitsOnSexChromosome()) {
					model.addNextSexLinkedGeneModel(rf, geneModel);
				} else {
					model.addFirstSexLinkedGeneModel(geneModel);
				}
			}
		}
	}
}
