package edu.umb.jsVGL.client.ModelBuilder;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

import edu.umb.jsVGL.client.VGL.VGLII;

public class LinkagePanel extends SimplePanel {

	private VGLII vglII;

	private ListBox g1g2Linked;
	private ListBox g2g3Linked;
	private ListBox g3g1Linked;

	private ListBox g1g2LinkageRelevantCage;
	private ListBox g2g3LinkageRelevantCage;
	private ListBox g3g1LinkageRelevantCage;	

	private String[] chars;

	public LinkagePanel(String[] characters, VGLII vglII) {
		this.vglII = vglII;

		g1g2LinkageRelevantCage = new ListBox();
		g2g3LinkageRelevantCage = new ListBox();
		g3g1LinkageRelevantCage = new ListBox();
		String[] cageList = vglII.getCageList();
		for (int i = 0; i < cageList.length; i++) {
			g1g2LinkageRelevantCage.addItem(cageList[i]);
			g2g3LinkageRelevantCage.addItem(cageList[i]);
			g3g1LinkageRelevantCage.addItem(cageList[i]);
		}

		this.chars = characters;

	
		g1g2Linked = new ListBox();
		g2g3Linked = new ListBox();
		g3g1Linked = new ListBox();
		g1g2Linked.addItem("Unknown");
		g2g3Linked.addItem("Unknown");
		g3g1Linked.addItem("Unknown");
		g1g2Linked.addItem("Unlinked");
		g2g3Linked.addItem("Unlinked");
		g3g1Linked.addItem("Unlinked");

		for (int i = 2; i < 51; i++) {
			String s = "Linked & RF= " + (i - 1) + "%";
			g1g2Linked.addItem(s);
			g2g3Linked.addItem(s);
			g3g1Linked.addItem(s);
		}

		VerticalPanel mainPanel = new VerticalPanel();
		
		VerticalPanel panelA = new VerticalPanel();
		panelA.setStyleName("jsVGL_SubdividerPanel");
		HorizontalPanel row1 = new HorizontalPanel();
		Label l1 = new Label(chars[0] + " and " + chars[1] + " are:");
		l1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		l1.setStyleName("jsVGL_InteractionText");
		row1.add(l1);
		row1.add(g1g2Linked);
		panelA.add(row1);
		SimplePanel spacerPanelA = new SimplePanel();
		spacerPanelA.setHeight("15px");
		panelA.add(spacerPanelA);
		HorizontalPanel row2 = new HorizontalPanel();
		Label l2 = new Label("Relevant Cage:");
		l2.setStyleName("jsVGL_InteractionText");
		row2.add(l2);
		row2.add(g1g2LinkageRelevantCage);
		panelA.add(row2);
		mainPanel.add(panelA);
		
		if (chars.length == 3) {
			VerticalPanel panelB = new VerticalPanel();
			panelB.setStyleName("jsVGL_SubdividerPanel");
			HorizontalPanel row3 = new HorizontalPanel();
			Label l3 = new Label(chars[1] + " and " + chars[2] + " are ");
			l3.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			l3.setStyleName("jsVGL_InteractionText");
			row3.add(l3);
			row3.add(g2g3Linked);
			panelB.add(row3);
			SimplePanel spacerPanelB = new SimplePanel();
			spacerPanelB.setHeight("15px");
			panelA.add(spacerPanelB);
			HorizontalPanel row4 = new HorizontalPanel();
			Label l4 = new Label("Relevant Cages:");
			l4.setStyleName("jsVGL_InteractionText");
			row4.add(l4);
			row4.add(g2g3LinkageRelevantCage);
			panelB.add(row4);
			mainPanel.add(panelB);

			VerticalPanel panelC = new VerticalPanel();
			panelC.setStyleName("jsVGL_SubdividerPanel");
			HorizontalPanel row5 = new HorizontalPanel();
			Label l5 = new Label(chars[0] + " and " + chars[2] + " are ");
			l5.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			l5.setStyleName("jsVGL_InteractionText");
			row5.add(l5);
			row5.add(g3g1Linked);
			panelC.add(row5);
			SimplePanel spacerPanelC = new SimplePanel();
			spacerPanelC.setHeight("15px");
			panelA.add(spacerPanelC);
			HorizontalPanel row6 = new HorizontalPanel();
			Label l6 = new Label("Relevant Cages:");
			l6.setStyleName("jsVGL_InteractionText");
			row6.add(l6);
			row6.add(g3g1LinkageRelevantCage);
			panelC.add(row6);
			mainPanel.add(panelC);
		}
		setWidget(mainPanel);
	}
	
	/*
	 * for these, the encoding is:
	 * 	unknown = -1.0f
	 *  unlinked = 0.5f
	 *  linkage is between 0 and 0.5
	 */
	public double getG1G2LinkageChoice() {
		return getSelectedRf(g1g2Linked);
	}
	public double getG2G3LinkageChoice() {
		return getSelectedRf(g2g3Linked);
	}
	public double getG1G3LinkageChoice() {
		return getSelectedRf(g3g1Linked);
	}
	
	/*
	 * Note for cage numbers vs IDs
	 * 	cages have Ids that start with 0 (field pop)
	 *     but these are hidden from the user
	 *  cages have NUMBERS that the users see that start with 1
	 *  
	 *  so, to get the index, you have to subtract 1 from the number
	 */
	public int getG1G2LinkageRelevantCage() {
		if (g1g2LinkageRelevantCage == null) return -1;
		String[] parts = ((String)g1g2LinkageRelevantCage.getItemText(g1g2LinkageRelevantCage.getSelectedIndex())).split("Cage");
		if (parts.length != 2) return -1;
		return Integer.parseInt(parts[1].trim());
	}
	public int getG2G3LinkageRelevantCage() {
		if (g2g3LinkageRelevantCage == null) return -1;
		String[] parts = ((String)g2g3LinkageRelevantCage.getItemText(g2g3LinkageRelevantCage.getSelectedIndex())).split("Cage");
		if (parts.length != 2) return -1;
		return Integer.parseInt(parts[1].trim());
	}
	public int getG1G3LinkageRelevantCage() {
		if (g3g1LinkageRelevantCage == null) return -1;
		String[] parts = ((String)g3g1LinkageRelevantCage.getItemText(g3g1LinkageRelevantCage.getSelectedIndex())).split("Cage");
		if (parts.length != 2) return -1;
		return Integer.parseInt(parts[1].trim());
	}
	
	private double getSelectedRf(ListBox listBox) {
		String choice = (String)listBox.getItemText(listBox.getSelectedIndex());
		if (choice.equals("Unknown")) return -1.0f;
		if (choice.equals("Unlinked")) return 0.5f;
		String[] parts = choice.split("=");
		double percent = Double.parseDouble(parts[1].replaceAll("%", ""));
		return percent/100.0f;
	}


	public void setStateFromFile(Element element) {
//		List<Element> elements = element.getChildren();
//		Iterator<Element> it = elements.iterator();
//		while(it.hasNext()) {
//			Element e = it.next();
//			if (e.getName().equals("G1G2")) {
//				g1g2Linked.setSelectedItem((String)e.getText());
//			}
//			if (e.getName().equals("G1G2Evidence")) {
//				g1g2LinkageRelevantCage.setSelectedItem((String)e.getText());
//			}
//			
//			if (e.getName().equals("G2G3")) {
//				g2g3Linked.setSelectedItem((String)e.getText());
//			}
//			if (e.getName().equals("G2G3Evidence")) {
//				g2g3LinkageRelevantCage.setSelectedItem((String)e.getText());
//			}
//
//			if (e.getName().equals("G3G1")) {
//				g3g1Linked.setSelectedItem((String)e.getText());
//			}
//			if (e.getName().equals("G3G1Evidence")) {
//				g3g1LinkageRelevantCage.setSelectedItem((String)e.getText());
//			}
//
//		}
	}

	public Element save() {
		Document d = XMLParser.createDocument();

		Element lpe = d.createElement("LinkagePanel");

		Element e = d.createElement("G1G2");
		e.appendChild(d.createTextNode(((String)g1g2Linked.getItemText(g1g2Linked.getSelectedIndex()))));
		lpe.appendChild(e);	
		e = d.createElement("G1G2Evidence");
		e.appendChild(d.createTextNode(((String)g1g2LinkageRelevantCage.getItemText(g1g2LinkageRelevantCage.getSelectedIndex()))));
		lpe.appendChild(e);

		e = d.createElement("G2G3");
		e.appendChild(d.createTextNode(((String)g2g3Linked.getItemText(g2g3Linked.getSelectedIndex()))));
		lpe.appendChild(e);
		e = d.createElement("G2G3Evidence");
		e.appendChild(d.createTextNode(((String)g2g3Linked.getItemText(g2g3LinkageRelevantCage.getSelectedIndex()))));
		lpe.appendChild(e);

		e = d.createElement("G3G1");
		e.appendChild(d.createTextNode(((String)g3g1Linked.getItemText(g3g1Linked.getSelectedIndex()))));
		lpe.appendChild(e);
		e = d.createElement("G3G1Evidence");
		e.appendChild(d.createTextNode(((String)g3g1LinkageRelevantCage.getItemText(g3g1LinkageRelevantCage.getSelectedIndex()))));
		lpe.appendChild(e);

		return lpe;
	}

	
	public void updateCageChoices(int nextCageId) {
		g1g2LinkageRelevantCage.addItem("Cage " + nextCageId);
		g2g3LinkageRelevantCage.addItem("Cage " + nextCageId);
		g3g1LinkageRelevantCage.addItem("Cage " + nextCageId);
	}
	
	public ArrayList<Integer> getRelevantCages() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		result.add(g1g2LinkageRelevantCage.getSelectedIndex());
		result.add(g2g3LinkageRelevantCage.getSelectedIndex());
		result.add(g3g1LinkageRelevantCage.getSelectedIndex());		
		return result;
	}

}
