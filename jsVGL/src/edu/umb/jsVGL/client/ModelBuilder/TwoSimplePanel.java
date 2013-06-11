package edu.umb.jsVGL.client.ModelBuilder;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TwoSimplePanel extends ModelDetailsPanel implements ChangeHandler {

	public TwoSimplePanel(String[] phenos, 
			ListBox t1Choices, 
			ListBox t2Choices,
			ModelPane mp) {
		
		VerticalPanel mainPanel = new VerticalPanel();
		
		t2Choices = new ListBox();
		t1Choices = new ListBox();
		for (int i = 0; i < phenos.length; i++) {
			t1Choices.addItem(phenos[i]);
			t2Choices.addItem(phenos[i]);
		}
		mainPanel.add(t2Choices);
		Label l = new Label("Is Dominant To");
		l.setStyleName("jsVGL_InteractionText");
		mainPanel.add(l);
		mainPanel.add(t1Choices);

		this.t1Choices = t1Choices;
		t1Choices.addChangeHandler(this);
		this.t2Choices = t2Choices;
		t2Choices.addChangeHandler(this);

		this.mp = mp;
		setWidget(mainPanel);
	}

	public void updateT1Choices(int x) {
		t1Choices.setSelectedIndex(x);
	}

	public void updateT2Choices(int x) {
		t2Choices.setSelectedIndex(x);
	}


	public void onChange(ChangeEvent event) {
		if (event.getSource().equals(t1Choices)) {
			mp.setT1Value(t1Choices.getSelectedIndex());
		}

		if (event.getSource().equals(t2Choices)) {
			mp.setT2Value(t2Choices.getSelectedIndex());
		}
	}

}
