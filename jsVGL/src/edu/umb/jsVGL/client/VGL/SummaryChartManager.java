package edu.umb.jsVGL.client.VGL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import edu.umb.jsVGL.client.GeneticModels.Organism;
import edu.umb.jsVGL.client.GeneticModels.OrganismList;
import edu.umb.jsVGL.client.GeneticModels.Phenotype;
import edu.umb.jsVGL.client.GeneticModels.Trait;
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
 * @version 1.0 $Id: SummaryChartManager.java,v 1.9 2009-09-18 15:24:18 brian Exp $
 */

public class SummaryChartManager {

	private static SummaryChartManager instance;

	private TreeSet<CageUI> selectedSet;

	private SummaryChartUI summaryChartUI;

	private SummaryChartManager() {
		selectedSet = new TreeSet<CageUI>();
	}

	public static SummaryChartManager getInstance() {
		if (instance == null) {
			instance = new SummaryChartManager();
		}
		return instance;
	}

	public void addToSelected(CageUI cageUI) {
		selectedSet.add(cageUI);
	}

	public void removeFromSelected(CageUI cageUI) {
		selectedSet.remove(cageUI);
	}

	public void clearSelectedSet() {
		Iterator<CageUI> it = selectedSet.iterator();
		while (it.hasNext()) {
			it.next().setIsSelected(false);
		}
		selectedSet = new TreeSet<CageUI>();
	}

	public int[] getScrambledCharacterOrder() {
		return getOneOrganism().getGeneticModel().getScrambledCharacterOrder();
	}

	public void hideSummaryChart() {
		if (summaryChartUI != null) {
//			summaryChartUI.setVisible(false);
		}
	}


	/**
	 * totals up the contents of the selectedSet of cageUIs
	 * split by the phenotypes you're interested in
	 * - these are specified in traitsToCount:
	 *   if you want to sort based on all 3 traits,
	 *   traitsToCount = {0,1,2}
	 *   if you want only traits 1 and 3
	 *   traitsToCount = {1,3}
	 * @param traitsToCount
	 * @return
	 */
	public PhenotypeCount[] calculateTotals(ArrayList<Integer> selectedTraits, boolean sortBySex) {
		TreeMap<String, MFTotCounts> totals = new TreeMap<String, MFTotCounts>();

		Iterator<CageUI> cageUIIterator = selectedSet.iterator();
		while (cageUIIterator.hasNext()) {
			TreeMap<String, OrganismList> children = 
				cageUIIterator.next().getCage().getChildren();
			Iterator<String> oListIterator = children.keySet().iterator();
			while (oListIterator.hasNext()) {
				OrganismList oList = children.get(oListIterator.next());
				String customPhenotypeString = 
					oList.getCustomPhenotypeString(selectedTraits);
				if (!totals.containsKey(customPhenotypeString)) {
					totals.put(customPhenotypeString, new MFTotCounts(0,0));
				}
				MFTotCounts oldTotal = totals.get(customPhenotypeString);
				totals.put(customPhenotypeString, oldTotal.add(oList.getMFTotCounts()));
			}
		}
		
		PhenotypeCount[] result;
		if (sortBySex) {
			result = new PhenotypeCount[totals.keySet().size() * 2];
		} else {
			result = new PhenotypeCount[totals.keySet().size()];
		}

		Iterator<String> customPhenoIterator = totals.keySet().iterator();
		int i = 0;
		while (customPhenoIterator.hasNext()) {
			String pheno = customPhenoIterator.next();
			if (sortBySex) {
				result[i] = new PhenotypeCount("Male/" + pheno, totals.get(pheno).getMales());
				i++;
				result[i] = new PhenotypeCount("Female/" + pheno, totals.get(pheno).getFemales());
				i++;
			} else {
				result[i] = new PhenotypeCount(pheno, totals.get(pheno).getTotal());
				i++;
			}
		}

		return result;
	}

	public CageUI[] getSelectedSet() {
		CageUI[] result = new CageUI[selectedSet.size()];
		Iterator<CageUI> it = selectedSet.iterator();
		int i = 0;
		while (it.hasNext()) {
			result[i] = it.next();
		}
		return result;
	}

	public Trait[] getTraitSet() {
		if (selectedSet == null) {
			return null;
		}

		ArrayList<Phenotype> phenoList = getOneOrganism().getPhenotypes();
		Trait[] result = new Trait[phenoList.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = phenoList.get(i).getTrait();
		}
		return result;
	}

	//just get one random organism so you can find full
	//  set of phenos
	private Organism getOneOrganism() {
		TreeMap<String, OrganismList> children = 
			selectedSet.first().getCage().getChildren();
		String pheno = children.keySet().iterator().next();
		return children.get(pheno).get(0);	
	}

	public void showSummaryChart(VGLII master) {
		if(selectedSet.size() == 0) {
//			JOptionPane.showMessageDialog(master,
//					"<html>" + 
//					Messages.getInstance().getString("VGLII.SummaryChartWarningLine1") + //$NON-NLS-1$
//					"<br>" + 
//					Messages.getInstance().getString("VGLII.SummaryChartWarningLine2") + //$NON-NLS-1$
//					"<br>" +
//					Messages.getInstance().getString("VGLII.SummaryChartWarningLine3") + //$NON-NLS-1$
//					"<br>" + 
//					Messages.getInstance().getString("VGLII.SummaryChartWarningLine4"), //$NON-NLS-1$
//					Messages.getInstance().getString("VGLII.SummaryChartWarningHeadline"), //$NON-NLS-1$
//					JOptionPane.WARNING_MESSAGE);
			return;
		}
//		summaryChartUI = new SummaryChartUI(master);
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		Iterator<CageUI> it = selectedSet.iterator();
		while(it.hasNext()) {
			b.append(it.next().getId() + ","); //$NON-NLS-1$
		}
		if(b.length() != 0) {
			b.deleteCharAt(b.length() -1);
		}
		return b.toString();
	}
	
}
