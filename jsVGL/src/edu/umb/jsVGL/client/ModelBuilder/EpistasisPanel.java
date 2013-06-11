package edu.umb.jsVGL.client.ModelBuilder;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class EpistasisPanel extends ModelDetailsPanel implements ChangeHandler {
	Label gALabel;
	Label gBLabel;


	public EpistasisPanel(String[] phenos,
			ListBox t1Choices,
			ListBox t2Choices,
			ListBox t3Choices,
			ModelPane mp) {
		t1Choices = new ListBox();
		t2Choices = new ListBox();
		t3Choices = new ListBox();
		for (int i = 0; i < phenos.length; i++) {
			t1Choices.addItem(phenos[i]);
			t2Choices.addItem(phenos[i]);
			t3Choices.addItem(phenos[i]);
		}
		this.t1Choices = t1Choices;
		t1Choices.addChangeHandler(this);
		this.t2Choices = t2Choices;
		t2Choices.addChangeHandler(this);
		this.t3Choices = t3Choices;
		t3Choices.addChangeHandler(this);
		this.mp = mp;

		AbsolutePanel mainPanel = new AbsolutePanel();
		mainPanel.setStyleName("jsVGL_ComplementationPanel");
		mainPanel.setSize("216px", "216px");

		mainPanel.add(t1Choices, 25, 5);
		mainPanel.add(t2Choices, 25, 90);
		mainPanel.add(t3Choices, 25, 170);
		setWidget(mainPanel);
	}

	public String[] getChoices() {
		String[] r = new String[3];
		r[0] = t1Choices.getItemText(t1Choices.getSelectedIndex());
		r[1] = t2Choices.getItemText(t2Choices.getSelectedIndex());
		r[2] = t3Choices.getItemText(t3Choices.getSelectedIndex());
		return r;
	}

	public void updateT1Choices(int x) {
		t1Choices.setSelectedIndex(x);
	}
	
	public void updateT2Choices(int x) {
		t2Choices.setSelectedIndex(x);
	}
	
	public void updateT3Choices(int x) {
		t3Choices.setSelectedIndex(x);
	}


//	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		Graphics2D g2d = (Graphics2D)g;
//		g2d.setColor(Color.GRAY);
//		g2d.setStroke(new BasicStroke(5F));
//		g2d.drawLine(gALabel.getLocation().x, gALabel.getLocation().y + 18, 
//				gALabel.getLocation().x + 40, gALabel.getLocation().y + 18);
//		g2d.drawLine(gALabel.getLocation().x + 35, gALabel.getLocation().y + 13, 
//				gALabel.getLocation().x + 40, gALabel.getLocation().y + 18);
//		g2d.drawLine(gALabel.getLocation().x + 35, gALabel.getLocation().y + 23, 
//				gALabel.getLocation().x + 40, gALabel.getLocation().y + 18);
//		g2d.drawLine(gBLabel.getLocation().x, gBLabel.getLocation().y + 18, 
//				gBLabel.getLocation().x + 40, gBLabel.getLocation().y + 18);
//		g2d.drawLine(gBLabel.getLocation().x + 35, gBLabel.getLocation().y + 13, 
//				gBLabel.getLocation().x + 40, gBLabel.getLocation().y + 18);
//		g2d.drawLine(gBLabel.getLocation().x + 35, gBLabel.getLocation().y + 23, 
//				gBLabel.getLocation().x + 40, gBLabel.getLocation().y + 18);
//	}

	public void onChange(ChangeEvent e) {
		if (e.getSource().equals(t1Choices)) {
			mp.setT1Value(t1Choices.getSelectedIndex());
		}
	
		if (e.getSource().equals(t2Choices)) {
			mp.setT2Value(t2Choices.getSelectedIndex());
		}
		
		if (e.getSource().equals(t3Choices)) {
			mp.setT3Value(t3Choices.getSelectedIndex());
		}
	}
}

