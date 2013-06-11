package edu.umb.jsVGL.client.ModelBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import edu.umb.jsVGL.client.GeneticModels.GeneModel;
import edu.umb.jsVGL.client.GeneticModels.GeneticModel;
import edu.umb.jsVGL.client.GeneticModels.PhenotypeProcessor;
import edu.umb.jsVGL.client.GeneticModels.ProblemTypeSpecification;
import edu.umb.jsVGL.client.VGL.VGLII;


public class ModelBuilderUI extends TabPanel {

	private VGLII vglII;

	private GeneticModel geneticModel;
	private LinkagePanel linkagePanel;
	private ModelPane[] modelPanes;


	// from new problem or saved work file
	public ModelBuilderUI (VGLII vglII, GeneticModel geneticModel) {
		this.vglII = vglII;
		this.geneticModel = geneticModel;
		setupUI();
		selectTab(0);
	}


	private void setupUI() {

		GeneModel[] geneModels = new GeneModel[geneticModel.getNumberOfGeneModels()];
		modelPanes = new ModelPane[geneticModel.getNumberOfCharacters()];

		ProblemTypeSpecification specs = geneticModel.getProblemTypeSpecification();
		if (specs.getPhenotypeInteraction() == 0.0) {
			// simple if no complementation or epistasis
			for (int i = 0; i < geneticModel.getNumberOfGeneModels(); i++) {
				geneModels[i] = geneticModel.getGeneModelByIndex(i);
				modelPanes[i] = new ModelPane(i,
						geneModels[i].getCharacter(),
						geneModels[i].getTraitStrings(), 
						specs, 
						this);
				add(modelPanes[i], geneModels[i].getTraits()[0].getCharacterName());
				modelPanes[i].setupActionListeners();
			}
		} else {
			// more of a pain if epistasis or complementation
			//	first pane is the possibly interacting pheno
			geneModels[0] = geneticModel.getGeneModelByIndex(0);
			if (geneticModel.getPhenoTypeProcessor().getInteractionType() == PhenotypeProcessor.NO_INTERACTION) {
				// if no interaction, just get the first gene model
				modelPanes[0] = new ModelPane(0, 
						geneModels[0].getCharacter(),
						geneModels[0].getTraitStrings(), 
						specs, 
						this);
				add(modelPanes[0], geneModels[0].getTraits()[0].getCharacterName());
			} else {
				// if there is an interaction, need to get the phenotypes from the PhenoProcessor
				modelPanes[0] = new ModelPane(0, 
						geneticModel.getPhenoTypeProcessor().getCharacter(),
						geneticModel.getPhenoTypeProcessor().getTraits(), 
						specs, 
						this);
				add(modelPanes[0], geneticModel.getPhenoTypeProcessor().getT1().getCharacterName());
			}
			modelPanes[0].setupActionListeners();

			// if a last character, add it (it's index = 2 since 1st 2 are interacting)
			if (geneticModel.getNumberOfCharacters() == 2) {
				geneModels[1] = geneticModel.getGeneModelByIndex(2);
				modelPanes[1] = new ModelPane(1, 
						geneModels[1].getCharacter(),
						geneModels[1].getTraitStrings(), 
						specs, 
						this);
				add(modelPanes[1], geneModels[1].getTraits()[0].getCharacterName());
			}

		}

		// set up for linkage if needed
		if (geneticModel.getNumberOfGeneModels() > 1) {
			if ((specs.getGene2_chSameChrAsGene1() != 0.0) &&
					(specs.getGene2_minRfToGene1() < 0.5)) {
				setupLinkagePanel(geneModels, this);
			}
			if ((geneticModel.getNumberOfGeneModels() > 2) &&
					(specs.getGene3_chSameChrAsGene1() != 0.0) &&
					(specs.getGene3_chSameChrAsGene1() < 0.5)) {
				setupLinkagePanel(geneModels, this);
			}
		}
	}

	private void setupLinkagePanel(GeneModel[] geneModels, TabPanel tabs) {
		ArrayList<String> characters = new ArrayList<String>();
		characters.add(geneModels[0].getCharacter());
		characters.add(geneModels[1].getCharacter());
		if (geneticModel.getNumberOfGeneModels() == 3) {
			characters.add(geneModels[2].getCharacter());
		}
		String[] chars = new String[characters.size()];
		for (int i = 0; i < characters.size(); i++) {
			chars[i] = characters.get(i);
		}
		linkagePanel = new LinkagePanel(chars, vglII);
		linkagePanel.setStyleName("jsVGL_LinkagePanel");
		add(linkagePanel, "Linkage");
	}

	public void configureFromXML(Element root) {

		NodeList elements = root.getChildNodes();
		for (int i = 0; i < elements.getLength(); i++) {
			Element e = (Element)elements.item(i);
			if (e.getTagName().equals("Character")) {
				int index = Integer.parseInt(e.getAttribute("Index"));
				modelPanes[index].setStateFromXML(e);
			}
			if (e.getTagName().equals("LinkagePanel")) {
				linkagePanel.setStateFromFile(e);
			}
		}
	}

	public VGLII getVGLII() {
		return vglII;
	}
	
	public boolean hasLinkagePanel() {
		return linkagePanel != null;
	}
	
	public ModelPane[] getModelPanes() {
		return modelPanes;
	}
	
	public LinkagePanel getLinkagePanel() {
		return linkagePanel;
	}

	public void updateCageChoices(int nextCageId) {
		
		for (int i = 0; i < modelPanes.length; i++) {
			modelPanes[i].updateCageChoices(nextCageId);
		}
		
		if (linkagePanel != null) {
			linkagePanel.updateCageChoices(nextCageId);
		}
	}

	/**
	 * get all the selected cage numbers 
	 * 	note - these are the DISPLAY numbers
	 * 		so need to subtract 1 to get internal index number
	 * 	also, don't include 0's - they're "?"
	 * these will be in numerical order
	 */
	public TreeSet<Integer> getChosenRelevantCages() {
		TreeSet<Integer> relevantCages = new TreeSet<Integer>();
		for (int i = 0; i < modelPanes.length; i++) {
			ArrayList<Integer> indices = modelPanes[i].getRelevantCages();
			if (linkagePanel != null) {
				indices.addAll(linkagePanel.getRelevantCages());
			}
			for (int j = 0; j < indices.size(); j++) {
				int num = indices.get(j);
				if (num != 0) relevantCages.add(num);
			}
		}	
		return relevantCages;
	}

	public Element save() {
		Document d = XMLParser.createDocument();
		Element e = d.createElement("ModelBuilderState");
		for (int i = 0; i < modelPanes.length; i++) {
			e.appendChild(modelPanes[i].save());
		}
		if (linkagePanel != null) e.appendChild(linkagePanel.save());
		return e;
	}


}
