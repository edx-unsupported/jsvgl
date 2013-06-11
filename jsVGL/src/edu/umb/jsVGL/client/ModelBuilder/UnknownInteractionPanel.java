package edu.umb.jsVGL.client.ModelBuilder;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class UnknownInteractionPanel extends SimplePanel{
	
	public UnknownInteractionPanel() {
		setSize("300px", "100px");
		this.add(new Label("Please choose the number of alleles."));
	}

}
