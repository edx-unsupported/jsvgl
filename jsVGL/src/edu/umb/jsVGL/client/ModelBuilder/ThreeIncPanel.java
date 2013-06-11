package edu.umb.jsVGL.client.ModelBuilder;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class ThreeIncPanel extends ModelDetailsPanel implements ChangeHandler {

	public ThreeIncPanel(String[] phenos,
			ListBox t1Choices,
			ListBox t2Choices,
			ListBox t3Choices,
			ListBox t4Choices,
			ListBox t5Choices,
			ListBox t6Choices,
			ModelPane mp) {

		t1Choices = new ListBox();
		t2Choices = new ListBox();
		t3Choices = new ListBox();
		t4Choices = new ListBox();
		t5Choices = new ListBox();
		t6Choices = new ListBox();
		for (int i = 0; i < phenos.length; i++) {
			t1Choices.addItem(phenos[i]);
			t2Choices.addItem(phenos[i]);
			t3Choices.addItem(phenos[i]);
			t4Choices.addItem(phenos[i]);
			t5Choices.addItem(phenos[i]);
			t6Choices.addItem(phenos[i]);
		}
		
		AbsolutePanel mainPanel = new AbsolutePanel();
		mainPanel.setSize("216px", "216px");
		mainPanel.setStyleName("jsVGL_ThreeIncPanel");
		
		this.t1Choices = t1Choices;
		t1Choices.addChangeHandler(this);
		this.t2Choices = t2Choices;
		t2Choices.addChangeHandler(this);
		this.t3Choices = t3Choices;
		t3Choices.addChangeHandler(this);
		this.t4Choices = t4Choices;
		t4Choices.addChangeHandler(this);
		this.t5Choices = t5Choices;
		t5Choices.addChangeHandler(this);
		this.t6Choices = t6Choices;
		t6Choices.addChangeHandler(this);

		this.mp = mp;

		mainPanel.add(t1Choices, 75, 20); // pure breeding 1
		mainPanel.add(t4Choices, 145, 70); // 1 + 2
		mainPanel.add(t2Choices, 145, 135); // pure breeding 2
		mainPanel.add(t5Choices, 75, 190); // 2 + 3
		mainPanel.add(t3Choices, 5, 135); // pure breeding 3
		mainPanel.add(t6Choices, 5, 70); // 3 + 1
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

	public void updateT4Choices(int x) {
		t4Choices.setSelectedIndex(x);
	}

	public void updateT5Choices(int x) {
		t5Choices.setSelectedIndex(x);
	}

	public void updateT6Choices(int x) {
		t6Choices.setSelectedIndex(x);
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

		if (e.getSource().equals(t4Choices)) {
			mp.setT4Value(t4Choices.getSelectedIndex());
		}

		if (e.getSource().equals(t5Choices)) {
			mp.setT5Value(t5Choices.getSelectedIndex());
		}

		if (e.getSource().equals(t6Choices)) {
			mp.setT6Value(t6Choices.getSelectedIndex());
		}
	}

}
