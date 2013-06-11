package edu.umb.jsVGL.client.ModelBuilder;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import edu.umb.jsVGL.client.GeneticModels.ProblemTypeSpecification;

public class ModelPane extends AbsolutePanel implements ChangeHandler {


	private int index;
	private String character;
	private String[] traits;
	private ProblemTypeSpecification specs;
	private ModelBuilderUI modelBuilderUI;

	private ListBox sexLinkageChoices;
	private ListBox sexLinkageCageChoices;
	private ListBox alleleNumberChoices;
	private ListBox interactionTypeChoices;
	private ListBox interactionCageChoices;
	private ListBox t1Choices;
	private ListBox t2Choices;
	private ListBox t3Choices;
	private ListBox t4Choices;
	private ListBox t5Choices;
	private ListBox t6Choices;

	private boolean circularPossible;
	private boolean incDomPossible;
	private boolean complementationPossible;
	private boolean epistasisPossible;

	private CaptionPanel interactionTypePanel;
	private CaptionPanel interactionDetailsPanel;

	// values updated by the model details panels
	private int t1Value;
	private int t2Value;
	private int t3Value;
	private int t4Value;
	private int t5Value;
	private int t6Value;


	public ModelPane(int index, 
			String character,
			String[] traits, 
			ProblemTypeSpecification specs,
			ModelBuilderUI modelBuilderUI) {
		this.index = index;
		this.character = character;
		this.traits = traits;
		this.specs = specs;
		this.modelBuilderUI = modelBuilderUI;
		setupUI();
	}

	private void setupUI() {

		/*
		 * sex linkage info
		 */
		CaptionPanel sexLinkagePanel = new CaptionPanel("Sex Linkage");
		sexLinkagePanel.setStyleName("jsVGL_SexLinkagePanel");
		sexLinkageChoices = new ListBox();
		if (specs.getGene1_chSexLinked() > 0.0) {
			sexLinkageChoices.addItem("Unknown");
			sexLinkageChoices.addItem("NotSexLinked");
			sexLinkageChoices.addItem("XX Female/XY Male");
			if (specs.getChZZ_ZW() > 0.0) {
				sexLinkageChoices.addItem("ZZ Male/ZW Female");
			}
		} else {
			sexLinkageChoices.addItem("Not Sex Linked");
		}
		sexLinkageChoices.addChangeHandler(this);
		sexLinkagePanel.add(sexLinkageChoices);

		add(sexLinkagePanel);


		// number of alleles
		CaptionPanel alleleNumberChoicePanel = new CaptionPanel("Number of Alleles");
		alleleNumberChoicePanel.setStyleName("jsVGL_NumAllelesPanel");
		boolean isOnly2Alleles;
		alleleNumberChoices = new ListBox();
		if ((specs.getGene1_ch3Alleles() > 0.0) ||
				(specs.getGene2_ch3Alleles() > 0.0) ||
				(specs.getGene3_ch3Alleles() > 0.0)) {
			isOnly2Alleles = false;
			alleleNumberChoices.addItem("Unknown");
			alleleNumberChoices.addItem("2-Allele");
			alleleNumberChoices.addItem("3-Allele");
		} else {
			isOnly2Alleles = true;
			alleleNumberChoices.addItem("2-Allele");
		}
		alleleNumberChoicePanel.add(alleleNumberChoices);
		add(alleleNumberChoicePanel);

		// allele interaction type
		circularPossible = false;
		if ((specs.getGene1_chCircDom() != 0.0)
				|| (specs.getGene2_chCircDom() != 0.0)
				|| (specs.getGene3_chCircDom() != 0.0)) circularPossible = true;

		incDomPossible = false;
		if ((specs.getGene1_chIncDom() != 0.0)
				|| (specs.getGene2_chIncDom() != 0.0)
				|| (specs.getGene3_chIncDom() != 0.0)) incDomPossible = true;

		complementationPossible = false;
		if ((specs.getPhenotypeInteraction() > 0.0)
				&& (specs.getEpistasis() != 1.0)) complementationPossible = true;

		epistasisPossible = false;
		if ((specs.getPhenotypeInteraction() > 0.0)
				&& (specs.getEpistasis() != 0.0)) epistasisPossible = true;

		interactionTypePanel = new CaptionPanel("Interactions among phenotypes");
		interactionTypePanel.setStyleName("jsVGL_InteractionTypePanel");
		if (isOnly2Alleles) {
			TwoAllelePanel twap = new TwoAllelePanel(
					incDomPossible, complementationPossible, epistasisPossible);
			interactionTypeChoices = twap.getInteractionTypeChoices();
			interactionTypeChoices.addChangeHandler(this);
			interactionTypePanel.add(twap);
		} else {
			interactionTypePanel.add(new UnknownInteractionPanel());
		}

		add(interactionTypePanel);

		// allele interaction details
		interactionDetailsPanel = new CaptionPanel("Specific Interactions Between Phenotypes:");
		interactionDetailsPanel.setStyleName("jsVGL_InteractionDetailsPanel");
		interactionDetailsPanel.add(new UnknownSpecificsPanel());
		add(interactionDetailsPanel);

		// relevant crosses
		CaptionPanel relevantCrossPanelWrapper = new CaptionPanel("Relevant Cages:");
		relevantCrossPanelWrapper.setStyleName("jsVGL_RelevantCrossPanel");
		VerticalPanel relevantCrossPanel = new VerticalPanel();
		// if sex-linkage, need a relevant cage selector
		if (specs.getGene1_chSexLinked() > 0.0) {
			HorizontalPanel upperPanel = new HorizontalPanel();
			String[] cageList = modelBuilderUI.getVGLII().getCageList();
			sexLinkageCageChoices = new ListBox();
			for (int i = 0; i < cageList.length; i++) {
				sexLinkageCageChoices.addItem(cageList[i]);
			}
			sexLinkageCageChoices.addChangeHandler(this);
			upperPanel.add(new Label("For/against Sex-linkage:"));
			upperPanel.add(sexLinkageCageChoices);
			relevantCrossPanel.add(upperPanel);
		}
		HorizontalPanel lowerPanel = new HorizontalPanel();
		String[] cageList = modelBuilderUI.getVGLII().getCageList();
		interactionCageChoices = new ListBox();
		for (int i = 0; i < cageList.length; i++) {
			interactionCageChoices.addItem(cageList[i]);
		}
		interactionCageChoices.addChangeHandler(this);
		lowerPanel.add(new Label("For mode of inheritance:"));
		lowerPanel.add(interactionCageChoices);
		relevantCrossPanel.add(lowerPanel);
		relevantCrossPanelWrapper.add(relevantCrossPanel);
		add(relevantCrossPanelWrapper);
	}

	public void setT1Value(int t1Value) {
		this.t1Value = t1Value;
		modelBuilderUI.getVGLII().setChangeSinceLastSave();
	}

	public void setT2Value(int t2Value) {
		this.t2Value = t2Value;
		modelBuilderUI.getVGLII().setChangeSinceLastSave();
	}

	public void setT3Value(int t3Value) {
		this.t3Value = t3Value;
		modelBuilderUI.getVGLII().setChangeSinceLastSave();
	}

	public void setT4Value(int t4Value) {
		this.t4Value = t4Value;
		modelBuilderUI.getVGLII().setChangeSinceLastSave();
	}

	public void setT5Value(int t5Value) {
		this.t5Value = t5Value;
		modelBuilderUI.getVGLII().setChangeSinceLastSave();
	}

	public void setT6Value(int t6Value) {
		this.t6Value = t6Value;
		modelBuilderUI.getVGLII().setChangeSinceLastSave();
	}

	public void clearValues() {
		modelBuilderUI.getVGLII().setChangeSinceLastSave();
		t1Value = 0;
		t2Value = 0;
		t3Value = 0;
		t4Value = 0;
		t5Value = 0;
		t6Value = 0;
	}

	public void updateCageChoices(int nextCageId) {
		if (sexLinkageCageChoices != null) {
			sexLinkageCageChoices.addItem("Cage " + nextCageId);
		}
		interactionCageChoices.addItem("Cage " + nextCageId);
	}

	public void onChange(ChangeEvent e) {

		modelBuilderUI.getVGLII().setChangeSinceLastSave();
		

		if (e.getSource().equals(alleleNumberChoices)) {
			
			String selectedAlleleNumber = alleleNumberChoices.getItemText(alleleNumberChoices.getSelectedIndex());
			
			if (selectedAlleleNumber.equals("Unknown")) {
				interactionTypePanel.remove(interactionTypePanel.getContentWidget());
				interactionTypePanel.setContentWidget(new UnknownInteractionPanel());

				interactionDetailsPanel.remove(interactionDetailsPanel.getContentWidget());
				interactionDetailsPanel.setContentWidget(new UnknownSpecificsPanel());
				clearValues();
			}
			if (selectedAlleleNumber.equals("2-Allele")) {
				interactionTypePanel.remove(interactionTypePanel.getContentWidget());
				TwoAllelePanel twap = new TwoAllelePanel(incDomPossible,
						complementationPossible, epistasisPossible);
				interactionTypeChoices = twap.getInteractionTypeChoices();
				interactionTypeChoices.addChangeHandler(this);
				interactionTypePanel.setContentWidget(twap);

				interactionDetailsPanel.remove(interactionDetailsPanel.getContentWidget());
				interactionDetailsPanel.setContentWidget(new UnknownSpecificsPanel());
				clearValues();
			}
			if (selectedAlleleNumber.equals("3-Allele")) {
				interactionTypePanel.remove(interactionTypePanel.getContentWidget());
				ThreeAllelePanel thap = new ThreeAllelePanel(circularPossible);
				interactionTypeChoices = thap.getInteractionTypeChoices();
				interactionTypeChoices.addChangeHandler(this);
				interactionTypePanel.setContentWidget(thap);

				interactionDetailsPanel.remove(interactionDetailsPanel.getContentWidget());
				interactionDetailsPanel.setContentWidget(new UnknownSpecificsPanel());
				clearValues();
			}
		}

		if (e.getSource().equals(interactionTypeChoices)) {

			String selectedAlleleNumber = alleleNumberChoices.getItemText(alleleNumberChoices.getSelectedIndex());
			String selectedInteractionType = interactionTypeChoices.getItemText(interactionTypeChoices.getSelectedIndex());

			if (selectedAlleleNumber.equals("2-Allele")) {
				if (selectedInteractionType.equals("Unknown")) {
					interactionDetailsPanel.setCaptionText("Specific Interactions Between Phenotypes:");
					if (interactionDetailsPanel.getContentWidget() != null) interactionDetailsPanel.remove(interactionDetailsPanel.getContentWidget());
					interactionDetailsPanel.setContentWidget(new UnknownSpecificsPanel());
					clearValues();
				}
				if (selectedInteractionType.equals("Simple Dominance")) {
					interactionDetailsPanel.setCaptionText("Specific Interactions Between Phenotypes:");
					if (interactionDetailsPanel.getContentWidget() != null) interactionDetailsPanel.remove(interactionDetailsPanel.getContentWidget());
					interactionDetailsPanel.setContentWidget(
							new TwoSimplePanel(traits, t1Choices, t2Choices, this));
					clearValues();
				}
				if (selectedInteractionType.equals("Incomplete Dominance")) {
					interactionDetailsPanel.setCaptionText("Specific Interactions Between Phenotypes:");
					if (interactionDetailsPanel.getContentWidget() != null) interactionDetailsPanel.remove(interactionDetailsPanel.getContentWidget());
					interactionDetailsPanel.setContentWidget(
							new TwoIncPanel(traits, t1Choices, t2Choices, t3Choices, this));
					clearValues();
				}
				
				if (selectedInteractionType.equals("Complementation")) {
					interactionDetailsPanel.setCaptionText("Pathway");
					if (interactionDetailsPanel.getContentWidget() != null) interactionDetailsPanel.remove(interactionDetailsPanel.getContentWidget());
					interactionDetailsPanel.setContentWidget(
							new ComplementationPanel(traits, t1Choices, t2Choices, this));
					clearValues();
				}
				
				if (selectedInteractionType.equals("Epistasis")) {
					interactionDetailsPanel.setCaptionText("Pathway");
					if (interactionDetailsPanel.getContentWidget() != null) interactionDetailsPanel.remove(interactionDetailsPanel.getContentWidget());
					interactionDetailsPanel.setContentWidget(
							new EpistasisPanel(traits, t1Choices, t2Choices, t3Choices, this));
					clearValues();
				}
			}
			
			if (selectedAlleleNumber.equals("3-Allele")) {
				if (selectedInteractionType.equals("Unknown")) {
					interactionDetailsPanel.setCaptionText("Specific Interactions Between Phenotypes:");
					if (interactionDetailsPanel.getContentWidget() != null) interactionDetailsPanel.remove(interactionDetailsPanel.getContentWidget());
					interactionDetailsPanel.setContentWidget(new UnknownSpecificsPanel());
					clearValues();
				}
				
				if (selectedInteractionType.equals("Incomplete Dominance")) {
					interactionDetailsPanel.setCaptionText("Specific Interactions Between Phenotypes:");
					if (interactionDetailsPanel.getContentWidget() != null) interactionDetailsPanel.remove(interactionDetailsPanel.getContentWidget());
					interactionDetailsPanel.setContentWidget(
							new ThreeIncPanel(
									traits, 
									t1Choices, 
									t2Choices, 
									t3Choices,
									t4Choices,
									t5Choices,
									t6Choices, 
									this));
					clearValues();
				}
				if (selectedInteractionType.equals("Hierarchical Dominance")) {
					interactionDetailsPanel.setCaptionText("Specific Interactions Between Phenotypes:");
					if (interactionDetailsPanel.getContentWidget() != null) interactionDetailsPanel.remove(interactionDetailsPanel.getContentWidget());
					interactionDetailsPanel.setContentWidget(
							new ThreeHierPanel(traits, 
									t1Choices, t2Choices, t3Choices, this));
					clearValues();
				}
				if (selectedInteractionType.equals("Circular Dominance")) {
					interactionDetailsPanel.setCaptionText("Specific Interactions Between Phenotypes:");
					if (interactionDetailsPanel.getContentWidget() != null) interactionDetailsPanel.remove(interactionDetailsPanel.getContentWidget());
					interactionDetailsPanel.setContentWidget(
							new ThreeCircPanel(traits, 
									t1Choices, t2Choices, t3Choices, this));
					clearValues();
				}
			}
		}
	}

	/**
	 * get the cages selected as relevant
	 * since the 0th element in the choice list is "?"
	 * the index = the displayed cage number
	 * so need to subtract 1 to get internal cage index
	 * @return
	 */
	public ArrayList<Integer> getRelevantCages() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		if (sexLinkageCageChoices != null) 
			result.add(sexLinkageCageChoices.getSelectedIndex());
		if (interactionCageChoices != null)
			result.add(interactionCageChoices.getSelectedIndex());
		return result;
	}

	public void setupActionListeners() {
		if (alleleNumberChoices != null) {
			alleleNumberChoices.addChangeHandler(this);	
		}
	}
	
	/*
	 * 	used for AutoGrader
	 * returns null if the student didn't have a choice
	 * that is, if it's not relevant to grade
	 * - this is the case if the JComboBox has only one entry
	 */
	public String getSexLinkageChoice() {
		if (sexLinkageChoices.getItemCount() == 1) return null;
		return (String)sexLinkageChoices.getItemText(sexLinkageChoices.getSelectedIndex());
	}
	
	/*
	 * used for AutoGrader
	 * returns 0 if the student didn't have a choice
	 * that is, if it's not relevant to grade
	 * - this is the case if the JComboBox has only one entry
	 * 
	 * returns -1 if they selected "Unknown"
	 */
	public int getAlleleNumberChoice() {
		if (alleleNumberChoices.getItemCount() == 1) return 0;
		String choice = (String) alleleNumberChoices.getItemText(alleleNumberChoices.getSelectedIndex());
		if (choice.equals("Unknown")) return -1;
		String[] pieces = choice.split("-"); 
		return Integer.parseInt(pieces[0]);
	}
	
	/*
	 * used for AutoGrader
	 * this returns the string that was selected
	 * in the local language 
	 * 
	 * returns null if the student didn't have a choice
	 * that is, if it's not relevant to grade
	 * - this is the case if the JComboBox has only one entry
	 *
	 * also returns null if this choice doesn't exist yet
	 * 	if they haven't chosen the number of alleles, for example
	 */
	public String getInteractionTypeChoice() {
		if (interactionTypeChoices == null)	 return null;
		if (interactionTypeChoices.getItemCount() == 1) return null;
		return interactionTypeChoices.getItemText(interactionTypeChoices.getSelectedIndex());
	}
	
	public ModelDetailsPanel getModelDetailsPanel() {
		return (ModelDetailsPanel)interactionDetailsPanel.getContentWidget();
	}
	
	/*
	 * Note for cage numbers vs IDs
	 * 	cages have Ids that start with 0 (field pop)
	 *     but these are hidden from the user
	 *  cages have NUMBERS that the users see that start with 1
	 *  
	 *  so, to get the index, you have to subtract 1 from the number
	 */
	public int getSexLinkageCageChoice() {
		if (sexLinkageCageChoices == null) return -1;
		String[] parts = ((String)sexLinkageCageChoices.getItemText(sexLinkageCageChoices.getSelectedIndex())).split("Cage");
		if (parts.length != 2) return -1;
		return Integer.parseInt(parts[1].trim());
	}
	public int getInteractionCageChoice() {
		if (interactionCageChoices == null) return -1;
		String[] parts = ((String)interactionCageChoices.getItemText(interactionCageChoices.getSelectedIndex())).split("Cage");
		if (parts.length != 2) return -1;
		return Integer.parseInt(parts[1].trim());
	}

	public void setStateFromXML(Element element) {

		NodeList elements = element.getChildNodes();
		for (int i = 0; i < elements.getLength(); i++) {
			Element e = (Element)elements.item(i);
			if (e.getTagName().equals("SexLinkage")) {
				sexLinkageChoices.setSelectedIndex(Integer.parseInt(e.getFirstChild().getNodeValue()));
				/*
				 * need to manually fire event to get box to update
				 * 	note, need different Document
				 */
				DomEvent.fireNativeEvent(com.google.gwt.dom.client.Document.get().createChangeEvent(), sexLinkageChoices);
			}

			if (e.getTagName().equals("AlleleNumber")) {
				alleleNumberChoices.setSelectedIndex(Integer.parseInt(e.getFirstChild().getNodeValue()));
				/*
				 * need to manually fire event to get box to update
				 * 	note, need different Document
				 */
				DomEvent.fireNativeEvent(com.google.gwt.dom.client.Document.get().createChangeEvent(), alleleNumberChoices);
			}

			if (e.getTagName().equals("InteractionType")) {
				interactionTypeChoices.setSelectedIndex(Integer.parseInt(e.getFirstChild().getNodeValue()));
				/*
				 * need to manually fire event to get box to update
				 * 	note, need different Document
				 */
				DomEvent.fireNativeEvent(com.google.gwt.dom.client.Document.get().createChangeEvent(), interactionTypeChoices);
			}

			// get a reference to the details panel
			ModelDetailsPanel mdp = (ModelDetailsPanel)interactionDetailsPanel.getContentWidget();

			if (e.getTagName().equals("T1")) {
				setT1Value(Integer.parseInt(e.getFirstChild().getNodeValue()));
				mdp.updateT1Choices(Integer.parseInt(e.getFirstChild().getNodeValue()));
			}

			if (e.getTagName().equals("T2")) {
				setT2Value(Integer.parseInt(e.getFirstChild().getNodeValue()));
				mdp.updateT2Choices(Integer.parseInt(e.getFirstChild().getNodeValue()));
			}

			if (e.getTagName().equals("T3")) {
				setT3Value(Integer.parseInt(e.getFirstChild().getNodeValue()));
				mdp.updateT3Choices(Integer.parseInt(e.getFirstChild().getNodeValue()));
			}

			if (e.getTagName().equals("T4")) {
				setT4Value(Integer.parseInt(e.getFirstChild().getNodeValue()));
				mdp.updateT4Choices(Integer.parseInt(e.getFirstChild().getNodeValue()));
			}

			if (e.getTagName().equals("T5")) {
				setT5Value(Integer.parseInt(e.getFirstChild().getNodeValue()));
				mdp.updateT5Choices(Integer.parseInt(e.getFirstChild().getNodeValue()));
			}

			if (e.getTagName().equals("T6")) {
				setT6Value(Integer.parseInt(e.getFirstChild().getNodeValue()));
				mdp.updateT6Choices(Integer.parseInt(e.getFirstChild().getNodeValue()));
			}

			if (e.getTagName().equals("SexLinkageCage")) {
				sexLinkageCageChoices.setSelectedIndex(Integer.parseInt(e.getFirstChild().getNodeValue()));
			}

			if (e.getTagName().equals("DetailsCage")) {
				interactionCageChoices.setSelectedIndex(Integer.parseInt(e.getFirstChild().getNodeValue()));
			}

		}
	}

	public Element save() {
		Document d = XMLParser.createDocument();

		Element mpe = d.createElement("Character");
		mpe.setAttribute("Name", character);
		mpe.setAttribute("Index", String.valueOf(index));

		Element e = d.createElement("SexLinkage");
		e.appendChild(d.createTextNode(String.valueOf(sexLinkageChoices.getSelectedIndex())));
		mpe.appendChild(e);

		e = d.createElement("AlleleNumber");
		e.appendChild(d.createTextNode(String.valueOf(alleleNumberChoices.getSelectedIndex())));
		mpe.appendChild(e);

		if (interactionTypeChoices != null) {
			e = d.createElement("InteractionType");
			e.appendChild(d.createTextNode(String.valueOf(interactionTypeChoices.getSelectedIndex())));
			mpe.appendChild(e);
		}

		e = d.createElement("T1");
		e.appendChild(d.createTextNode(String.valueOf(t1Value)));
		mpe.appendChild(e);

		e = d.createElement("T2");
		e.appendChild(d.createTextNode(String.valueOf(t2Value)));
		mpe.appendChild(e);

		e = d.createElement("T3");
		e.appendChild(d.createTextNode(String.valueOf(t3Value)));
		mpe.appendChild(e);

		e = d.createElement("T4");
		e.appendChild(d.createTextNode(String.valueOf(t4Value)));
		mpe.appendChild(e);

		e = d.createElement("T5");
		e.appendChild(d.createTextNode(String.valueOf(t5Value)));
		mpe.appendChild(e);

		e = d.createElement("T6");
		e.appendChild(d.createTextNode(String.valueOf(t6Value)));
		mpe.appendChild(e);

		if (sexLinkageCageChoices != null) {
			e = d.createElement("SexLinkageCage");
			e.appendChild(d.createTextNode(String.valueOf(sexLinkageCageChoices.getSelectedIndex())));
			mpe.appendChild(e);
		}

		e = d.createElement("DetailsCage");
		e.appendChild(d.createTextNode(String.valueOf(interactionCageChoices.getSelectedIndex())));
		mpe.appendChild(e);

		return mpe;
	}

}
