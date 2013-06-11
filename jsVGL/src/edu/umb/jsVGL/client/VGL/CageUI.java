package edu.umb.jsVGL.client.VGL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.umb.jsVGL.client.GeneticModels.Cage;
import edu.umb.jsVGL.client.GeneticModels.Organism;
import edu.umb.jsVGL.client.GeneticModels.OrganismList;
import edu.umb.jsVGL.client.GeneticModels.Phenotype;
import edu.umb.jsVGL.client.VGL.UIimages.UIImageResource;

/**
 * Nikunj Koolar cs681-3 Fall 2002 - Spring 2003 Project VGL File:
 * Brian White 2008
 * CustomizedFileFilter.java - Instances of this class provide for file filters
 * to show only those file that are supported by the application.
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
 * @author Nikunj Koolar & Brian White
 * @version 1.0 $Id$
 */
public class CageUI extends CaptionPanel implements Comparable<CageUI> {

	private static String FIELD_POP_COLOR = "#2E8B57";
	private static String PARENT_COLOR = "#8E2323";
	private static String OFFSPRING_COLOR = "#0x007FFF";

	private VGLII vglII;

	/**
	 * sets an upper bound so the cages (esp the Super Cross)
	 *  don't get too big
	 */
	private static int absoluteMaxOrgsPerRow = 20;

	/**
	 * boolean to indicate membership in selected set for
	 * summary chart
	 */
	private boolean isSelected;

	/**
	 * boolean to indicate if this is a superCross
	 *  (and thus requires special layout
	 */
	private boolean isSuperCross;

	/**
	 * manager for membership in selected set for summary chart
	 */
	private SummaryChartManager summaryChartManager;

	/**
	 * The Id for the Cage. This value is always one more than the id of the
	 * cage it holds. This is because the Cage id's begin from 0, but on screen
	 * they have to be shown beginning from 1.
	 */
	private int id;

	/**
	 * number of traits in this problem
	 */
	private int numberOfTraits;

	/**
	 * maximum number of organisms in any row
	 * it assumes 2 rows of orgs
	 */
	private int maxOrgsInOneRow;

	/**
	 * The panel that contains all the subpanels.
	 */
	private DockPanel superPanel;

	/**
	 * This variable stores the count of the number of different phenotypes
	 * associated with this cage
	 */
	private int numPhenosPresent;

	/**
	 * image resource
	 */
	private UIImageResource uiImageResource;

	/**
	 * This variable stores a reference to the hashmap of children associated
	 * with this cage
	 * sorted by phenotypeString
	 */
	private TreeMap<String, OrganismList> children;

	/**
	 * This stores a more easily accessible version of children
	 * it is an array of OrganismLists, each holding all the kids with 
	 * the same phenotype. It is indexed by a number for the phenotype
	 */
	private OrganismList[] childrenSortedByPhenotype;

	private String[] phenotypeNames;
	/**
	 * This variable stores a reference to the list of parents associated with
	 * this cage
	 */
	private ArrayList<Organism> parents;

	/**
	 * holds array mapping real trait # to display order number
	 * that way, the traits aren't displayed in chromosomal order
	 */
	private int[] scrambledTraitOrder;

	/**
	 * A reference to the Cage object being displayed through this UI
	 */
	private Cage cage;

	/**
	 * A reference to the selectionvial object that keeps track of the currently
	 * selected male and female organisms for crossing.
	 */
	private SelectionVial vial;

	/**
	 * This variable stores the details about the Genetic Model currently being
	 * used.
	 */
	private String details = null;

	/**
	 * This variable is used to decide the following: a. Whether to display the
	 * Genetics Model details in cage 1 b. Whether to allow the individual
	 * organisms to display allele information in balloon help active mode.
	 */
	private boolean isBeginner = false;

	/**
	 * Array of Parent Organisms. This array was initially concieved to hold
	 * simply the images of the parents (which explains its naming) but later
	 * on, in order to support linking between parents and their corresponding
	 * objects in the original cages where they were present, this array was
	 * then used to store parent OrganismUI objects.
	 */
	private OrganismUI[] parentOrganismUIs;

	/**
	 * This array of arrays stores the organismUIs for all the organisms of
	 * all the phenotypes associated with this cage. 
	 * it is stored in 2 rows where the length of the rows is
	 * (maximum number of offspring for one pheno)/2 
	 * For eg. If the pheno with the most offspring has 40 in it
	 * and there is one pheno then this variable will be [2][20] in size. If the
	 * cage contains 2 phenotypes then this variable will be [4][20] in size.
	 * - there is probably some danger with really high numbers of offspring
	 */
	private OrganismUI[][] childrenOrganismUIs;

	/**
	 * The constructor
	 * 
	 * @param isbeginnersmode
	 *            true if user is allowed to view underlying genetics details,
	 *            false otherwise
	 * @param cage
	 *            reference to the Cage object
	 * @param sv
	 *            reference to the SelectionVial object
	 * @param details
	 *            string containing information about the underlying genetics
	 *            model
	 */
	public CageUI(VGLII vglII,
			boolean isbeginnersmode, 
			boolean isSuperCross,
			Cage cage,
			SelectionVial sv, 
			String details, 
			int numberOfTraits,
			int[] scrambledTraitOrder) {

		super("Cage " + (cage.getId() + 1));
		setStyleName("jsVGL_CageUI");

		uiImageResource = GWT.create(UIImageResource.class);

		this.vglII = vglII;
		this.isBeginner = isbeginnersmode;
		this.isSuperCross = isSuperCross;
		this.cage = cage;
		vial = sv;

		id = cage.getId() + 1;
		children = cage.getChildren();
		parents = cage.getParents();

		this.scrambledTraitOrder = scrambledTraitOrder;

		if (id == 1)
			if (details != null)
				this.details = details;

		this.numberOfTraits = numberOfTraits;

		setupSubComponents();

		isSelected = false;
		summaryChartManager = SummaryChartManager.getInstance();

		maxOrgsInOneRow = (cage.getMaxOrgListSize()/2) + 1;  // assumes 2 rows of orgs
		// add 1 in case rounding

		//setup the GUI of its internal components
		components();
	}

	/**
	 * This method initializes the objects and widgets that store information
	 * about the various phenotypes associated with the cage.
	 */
	private void setupSubComponents() {
		// do this all by the number of phenotypes present in the chidren
		Set<String> phenotypeStrings = children.keySet();
		Iterator<String> it1 = phenotypeStrings.iterator();
		numPhenosPresent = phenotypeStrings.size();

		phenotypeNames = new String[numPhenosPresent];
		childrenSortedByPhenotype = new OrganismList[numPhenosPresent];

		int i = 0;
		while (it1.hasNext()) {
			phenotypeNames[i] = new String(it1.next());
			childrenSortedByPhenotype[i] = children.get(phenotypeNames[i]);
			i++;
		}
	}

	/**
	 * This method sets up the GUI and other characteristics of the internals of
	 * the Cage
	 */
	private void components() {
		setupOrganismPanel();
		setupParentInfoPanel();
		setContentWidget(superPanel);
	}


	/**
	 * This method sets up the panels for the Cage
	 */
	private void setupOrganismPanel() {

		superPanel = new DockPanel();
		DockPanel detailsPanel = new DockPanel();
		CaptionPanel captionedDetailsPanel = null;
		if (id > 1) {
			captionedDetailsPanel = new CaptionPanel("Offspring");
			captionedDetailsPanel.add(detailsPanel);
			captionedDetailsPanel.setStyleName("jsVGL_RegularDetailsPanel");
		} else {
			captionedDetailsPanel = new CaptionPanel("Organisms Collected From the Wild");
			captionedDetailsPanel.add(detailsPanel);
			captionedDetailsPanel.setStyleName("jsVGL_FieldPopDetailsPanel");
		}
		HorizontalPanel individualPanel = new HorizontalPanel();

		CaptionPanel captionedOrganismPanel = new CaptionPanel("Organisms");
		captionedOrganismPanel.setStyleName("jsVGL_CageInfoSubPanels");
		VerticalPanel organismsPanel = new VerticalPanel();
		captionedOrganismPanel.add(organismsPanel);

		CaptionPanel captionedCountsPanel = new CaptionPanel("Counts");
		captionedCountsPanel.setStyleName("jsVGL_CageInfoSubPanels");
		VerticalPanel countsPanel = new VerticalPanel();
		captionedCountsPanel.add(countsPanel);

		// headers for the different traits
		CaptionPanel[] captionedTraitPanels = new CaptionPanel[numberOfTraits];
		VerticalPanel[] traitPanels = new VerticalPanel[numberOfTraits];

		// need to get the type of each trait
		//  get one organism's pheno (it doesn't matter which one)
		ArrayList<Phenotype> phenotypes = 
				childrenSortedByPhenotype[0].get(0).getPhenotypes();
		for (int i = 0; i < numberOfTraits; i++) {
			traitPanels[i] = new VerticalPanel();
			captionedTraitPanels[i] = new CaptionPanel(phenotypes.get(scrambledTraitOrder[i]).getTrait().getBodyPart());
			captionedTraitPanels[i].setStyleName("jsVGL_CageInfoSubPanels");
			captionedTraitPanels[i].add(traitPanels[i]);
		}

		if (isSuperCross) {
			childrenOrganismUIs = new OrganismUI[2 * numPhenosPresent][absoluteMaxOrgsPerRow];
		} else {
			childrenOrganismUIs = new OrganismUI[2 * numPhenosPresent][maxOrgsInOneRow];
		}

		//For each phenotype, setup its own panels for organismUIs,count and
		//pictures and add them to the right places in the organismpanel,
		// countspanel, phenotype panels
		//and the picturespanel
		for (int i = 0; i < numPhenosPresent; i++) {
			IndividualPanelSet panelSet = setupIndividualPanel(i);	
			organismsPanel.add(panelSet.getOrganismPanel());	
			countsPanel.add(panelSet.getCountsPanel());
			HorizontalPanel[] phenoPanels = panelSet.getPhenotypePanels();
			for (int j = 0; j < numberOfTraits; j++) {
				traitPanels[j].add(
						phenoPanels[scrambledTraitOrder[j]]);
			}
		}

		for (int i = 0; i < numberOfTraits; i++) {
			individualPanel.add(captionedTraitPanels[i]);
		}

		individualPanel.add(captionedOrganismPanel);

		individualPanel.add(captionedCountsPanel);

		detailsPanel.add(individualPanel, DockPanel.NORTH);

		if (id > 1) {
			superPanel.add(captionedDetailsPanel, DockPanel.SOUTH);
		} else {
			if (isBeginner) {
				superPanel.add(captionedDetailsPanel, DockPanel.NORTH);
			} else {
				superPanel.add(captionedDetailsPanel, DockPanel.CENTER);
			}
		}
	}

	/**
	 * This method returns a JPanel containing the OrganismUIs for each phenotype
	 * associated with this cage.
	 * 
	 * @param number
	 *            index of the phenotype in the list of phenotypes for which the
	 *            panels are being set up.
	 */
	private IndividualPanelSet setupIndividualPanel(int number) {

		HorizontalPanel topRowOfOrganismsPanel = new HorizontalPanel();
		HorizontalPanel bottomRowOfOrganismsPanel = new HorizontalPanel();

		OrganismUI[] topRowOfOrganismUIs = childrenOrganismUIs[2 * number];
		OrganismUI[] bottomRowOFOrganismUIs = childrenOrganismUIs[2 * number + 1];
		Iterator<Organism> it = childrenSortedByPhenotype[number].iterator();

		//lay out two neat rows of OrganismUIs
		if (isSuperCross) {

			if (cage.isAlreadyBeenTrimmed()) {
				/*
				 * here, the cage has been trimmed, so display it like a normal
				 * cage EXCEPT that use absoluteMaxOrgsPerRow not maxOrgsPerRow
				 */
				int count = 0;
				int i = 0;
				int j = 0;
				while (it.hasNext()) {
					Organism o1 = (Organism) it.next();
					count++;
					if (count <= absoluteMaxOrgsPerRow) {
						topRowOfOrganismUIs[i] = new OrganismUI(o1, false, isBeginner,
								vial);
						topRowOfOrganismsPanel.add(topRowOfOrganismUIs[i]);
						i++;
					} else {
						bottomRowOFOrganismUIs[j] = new OrganismUI(o1, false, isBeginner,
								vial);
						bottomRowOfOrganismsPanel.add(bottomRowOFOrganismUIs[j]);
						j++;
					}
					o1.setVisibleInCage(true);
				}
				SimplePanel filler = new SimplePanel();
				filler.setWidth("15px");
				filler.setHeight("15px");
				if (i < absoluteMaxOrgsPerRow) {
					while (i < absoluteMaxOrgsPerRow) {
						topRowOfOrganismsPanel.add(filler);
						i++;
					}
				}
				if (j < absoluteMaxOrgsPerRow) {
					while (j < absoluteMaxOrgsPerRow) {
						bottomRowOfOrganismsPanel.add(filler);
						j++;
					}
				}

			} else {
				/*
				 * this is the first time the super cross has been seen,
				 * 	so need to "trim" to save only the visible organisms
				 *  (that way, the saved XML is MUCH smaller)
				 *  
				 * there's a boolean for each organism: isVisibleInCage to mark this
				 *  
				 */

				// first, mark all as hidden; then mark only the visible ones
				while (it.hasNext()) {
					Organism o = (Organism) it.next();
					o.setVisibleInCage(false);
				}
				it = childrenSortedByPhenotype[number].iterator();

				// if super cross, need a row of males and a row of females
				int i = 0;
				while (it.hasNext() && (i < (2 * absoluteMaxOrgsPerRow))) {
					Organism o = (Organism) it.next();
					// first, a row of males (or females, if there are no males)
					if (i < absoluteMaxOrgsPerRow) {
						topRowOfOrganismUIs[i] = new OrganismUI(o, false, isBeginner, vial);
						topRowOfOrganismsPanel.add(topRowOfOrganismUIs[i]);
						o.setVisibleInCage(true);
						i++;
					} else {
						// second row:
						// then see if there are any females
						//   if so, then make a row of females
						//   if not, make another row of males
						if ((childrenSortedByPhenotype[number].getNumberOfFemales() > 0) 
								&& (it.hasNext())) {
							// run thru the remaining males
							while (((Organism)it.next()).getSexString().equals("Male")) {}
						} 
						// get next org
						if (it.hasNext()) {
							o = (Organism) it.next();
							bottomRowOFOrganismUIs[i - absoluteMaxOrgsPerRow] = 
									new OrganismUI(o, false, isBeginner, vial);
							bottomRowOfOrganismsPanel.add(
									bottomRowOFOrganismUIs[i - absoluteMaxOrgsPerRow]);
							o.setVisibleInCage(true);
							i++;
						}
					}
				}
			}
		} else {
			int count = 0;
			int i = 0;
			int j = 0;
			while (it.hasNext()) {
				Organism o1 = (Organism) it.next();
				count++;
				if (count <= maxOrgsInOneRow) {
					topRowOfOrganismUIs[i] = new OrganismUI(o1, false, isBeginner,
							vial);
					topRowOfOrganismsPanel.add(topRowOfOrganismUIs[i]);
					i++;
				} else {
					bottomRowOFOrganismUIs[j] = new OrganismUI(o1, false, isBeginner,
							vial);
					bottomRowOfOrganismsPanel.add(bottomRowOFOrganismUIs[j]);
					j++;
				}
				o1.setVisibleInCage(true);
			}
			SimplePanel filler = new SimplePanel();
			filler.setWidth("15px");
			filler.setHeight("15px");
			if (i < maxOrgsInOneRow) {
				while (i < maxOrgsInOneRow) {
					topRowOfOrganismsPanel.add(filler);
					i++;
				}
			}
			if (j < maxOrgsInOneRow) {
				while (j < maxOrgsInOneRow) {
					bottomRowOfOrganismsPanel.add(filler);
					j++;
				}
			}
		}
		VerticalPanel organismPanel = new VerticalPanel();
		organismPanel.setStyleName("jsVGL_OrganismRowPanel");
		organismPanel.add(topRowOfOrganismsPanel);
		organismPanel.add(bottomRowOfOrganismsPanel);
		organismPanel.setHeight("39px");

		VerticalPanel countPanel = new VerticalPanel();
		countPanel.setStyleName("jsVGL_CountRowPanel");

		SimplePanel maleLabel = new SimplePanel();
		maleLabel.add(new HTML("<img src=\"" + (new Image(uiImageResource.maleBlack())).getUrl() + "\">"));
		maleLabel.setStyleName("jsVGL_CountLabel");
		SimplePanel femaleLabel = new SimplePanel();
		femaleLabel.add(new HTML("<img src=\"" + (new Image(uiImageResource.femaleBlack())).getUrl() + "\">"));
		femaleLabel.setStyleName("jsVGL_CountLabel");

		/*
		 * if it's a regular cross - get the counts from the cage's list of organsims
		 * if it's a supercross - get the counts that were saved with it
		 * 	UNLESS it's an new (therefore untrimmed) supercross, in that case the saved counts aren't there
		 * 		so use cages' organism lists
		 */
		int numberOfMales = 0;
		if (isSuperCross && (cage.isAlreadyBeenTrimmed())) {
			numberOfMales = cage.getPhenotypeCounts(phenotypeNames[number]).getMales();
		} else {
			numberOfMales = childrenSortedByPhenotype[number].getNumberOfMales();
		}
		String mCount = (new Integer(numberOfMales)).toString();
		if (numberOfMales < 10)
			mCount = "0" + mCount;
		Label maleCountLabel = new Label(mCount);

		int numberOfFemales = 0;
		if (isSuperCross && (cage.isAlreadyBeenTrimmed())) {
			numberOfFemales = cage.getPhenotypeCounts(phenotypeNames[number]).getFemales();
		} else {
			numberOfFemales = childrenSortedByPhenotype[number].getNumberOfFemales();
		}
		String fCount = (new Integer(numberOfFemales)).toString();
		if (numberOfFemales < 10)
			fCount = "0" + fCount;
		Label femaleCountLabel = new Label(fCount);

		if (isSuperCross) {
			maleCountLabel.setWidth("35px");
			maleCountLabel.setHeight("15px");
			femaleCountLabel.setWidth("35px");
			femaleCountLabel.setHeight("15px");
		} else {
			maleCountLabel.setWidth("25px");
			maleCountLabel.setHeight("15px");
			femaleCountLabel.setWidth("25px");
			femaleCountLabel.setHeight("15px");
		}

		HorizontalPanel malePanel = new HorizontalPanel();
		HorizontalPanel femalePanel = new HorizontalPanel();
		malePanel.add(maleCountLabel);
		malePanel.add(maleLabel);
		femalePanel.add(femaleCountLabel);
		femalePanel.add(femaleLabel);
		countPanel.add(malePanel);
		countPanel.add(femalePanel);

		HorizontalPanel[] phenotypePanels = new HorizontalPanel[numberOfTraits];
		ArrayList<Phenotype> phenoList = 
				childrenSortedByPhenotype[number].get(0).getPhenotypes();
		for (int k = 0; k < numberOfTraits; k++) {
			phenotypePanels[k] = new HorizontalPanel();
			phenotypePanels[k].setStyleName("jsVGL_PhenotypeRowPanel");
			phenotypePanels[k].add(new Label(phenoList.get(k).getTrait().getTraitName()));
			phenotypePanels[k].setHeight("34px");
		}

		return new IndividualPanelSet(organismPanel,
				countPanel,
				phenotypePanels);
	}

	/**
	 * This method sets up the Panel that display the information about the
	 * parents or if the Cage id is 1 and beginner's mode is true then it
	 * displays the details about the underlying genetics model
	 */
	private void setupParentInfoPanel() {
		if (id > 1) {
			CaptionPanel captionedParentInfoPanel = new CaptionPanel("Parents");
			captionedParentInfoPanel.setStyleName("jsVGL_ParentInfoPanel");
			HorizontalPanel parentInfoPanel = new HorizontalPanel();
			parentOrganismUIs = new OrganismUI[2];
			Organism o1 = parents.get(0);
			Organism o2 = parents.get(1);
			int cageId = o1.getCageId() + 1;
			String phenoName1 = o1.getPhenotypeString();
			parentOrganismUIs[0] = new OrganismUI(o1, true, isBeginner, vial);
			parentInfoPanel.add(parentOrganismUIs[0]);
			parentInfoPanel.add(new Label("(Cage " + cageId + ") " + phenoName1));
			Label crossLabel = new Label("X");
			parentInfoPanel.add(crossLabel);
			crossLabel.setStyleName("jsVGL_CrossLabel");
			cageId = o2.getCageId() + 1;
			String phenoName2 = o2.getPhenotypeString();
			parentOrganismUIs[1] = new OrganismUI(o2, true, isBeginner, vial);
			parentInfoPanel.add(parentOrganismUIs[1]);
			parentInfoPanel.add(new Label("(Cage " + cageId + ") " + phenoName2));
			captionedParentInfoPanel.add(parentInfoPanel);
			superPanel.add(captionedParentInfoPanel, DockPanel.NORTH);
		} 
	}

	public int getId() {
		return id;
	}

	public boolean isSuperCross() {
		return isSuperCross;
	}

	public void setIsSelected(boolean selected) {
		isSelected = selected;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	/**
	 * Getter method to access the Cage object associated with this UI
	 * 
	 * @return the Cage object
	 */
	public Cage getCage() {
		return cage;
	}

	/**
	 * Getter method to access the OrganismUIs for the parents for the cage of
	 * this UI
	 * 
	 * @return the array containing the OrganismUIs of the parents
	 */
	public OrganismUI[] getParentUIs() {
		if (parentOrganismUIs != null) {
			if (parentOrganismUIs[0] != null && parentOrganismUIs[1] != null)
				return parentOrganismUIs;
			else
				return null;
		} else
			return null;
	}

	/**
	 * This method returns the OrganismUI of the Organism with the sent id.
	 * 
	 * @param id
	 *            The index of the organism
	 * @return the OrganismUI of the organism
	 */
	public OrganismUI getOrganismUIFor(int id) {

		int orgsPerRow = maxOrgsInOneRow;
		if (isSuperCross) orgsPerRow = absoluteMaxOrgsPerRow;

		for (int i = 0; i < 2 * numPhenosPresent; i++) {
			for (int j = 0; j < orgsPerRow; j++) {
				OrganismUI organismUI = ((OrganismUI) (childrenOrganismUIs[i][j]));
				if (organismUI != null) {
					if (organismUI.getOrganism().getId() == id)
						return organismUI;
				} else
					break;
			}
		}
		return null;
	}

	public int compareTo(CageUI o) {
		return id - o.getId();
	}

}