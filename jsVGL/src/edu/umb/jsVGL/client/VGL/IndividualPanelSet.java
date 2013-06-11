package edu.umb.jsVGL.client.VGL;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
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
 * @version 1.0 $Id: IndividualPanelSet.java,v 1.2 2008-06-24 14:13:46 brian Exp $
 */

public class IndividualPanelSet {
	
	private VerticalPanel organismPanel;
	private VerticalPanel countsPanel;
	private HorizontalPanel[] phenotypePanels;
	
	public IndividualPanelSet(VerticalPanel organismPanel,
			VerticalPanel countsPanel,
			HorizontalPanel[] phenotypePanels) {
		this.organismPanel = organismPanel;
		this.countsPanel = countsPanel;
		this.phenotypePanels = phenotypePanels;
	}

	public VerticalPanel getOrganismPanel() {
		return organismPanel;
	}

	public VerticalPanel getCountsPanel() {
		return countsPanel;
	}

	public HorizontalPanel[] getPhenotypePanels() {
		return phenotypePanels;
	}

}
