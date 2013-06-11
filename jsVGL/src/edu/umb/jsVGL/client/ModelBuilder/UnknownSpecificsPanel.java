package edu.umb.jsVGL.client.ModelBuilder;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;

public class UnknownSpecificsPanel extends ModelDetailsPanel {

	public UnknownSpecificsPanel() {
		setSize("300px", "100px");
		add(new Label("Please choose the interaction type."));
	}

	public void onChange(ChangeEvent event) {}

}
