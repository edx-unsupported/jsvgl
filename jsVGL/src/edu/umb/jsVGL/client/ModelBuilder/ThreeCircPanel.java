package edu.umb.jsVGL.client.ModelBuilder;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class ThreeCircPanel extends ModelDetailsPanel implements ChangeHandler {


	public ThreeCircPanel(String[] phenos,
			ListBox t1Choices,
			ListBox t2Choices,
			ListBox t3Choices,
			ModelPane mp) {
		
		AbsolutePanel mainPanel = new AbsolutePanel();
		mainPanel.setSize("216px", "216px");
		mainPanel.setStyleName("jsVGL_ThreeCircPanel");
		
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


		mainPanel.add(t3Choices, 55, 10);
		mainPanel.add(t2Choices, 140, 160);
		mainPanel.add(t1Choices, 0, 160);

		setWidget(mainPanel);
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
