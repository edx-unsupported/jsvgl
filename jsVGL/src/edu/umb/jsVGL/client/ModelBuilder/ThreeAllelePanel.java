package edu.umb.jsVGL.client.ModelBuilder;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;

public class ThreeAllelePanel extends SimplePanel {

	private ListBox interactionTypeChoices;

	public ThreeAllelePanel(boolean circularPossible) {
		interactionTypeChoices = new ListBox();
		interactionTypeChoices.addItem("Unknown");
		interactionTypeChoices.addItem("Hierarchical Dominance");
		if (circularPossible) interactionTypeChoices.addItem("Circular Dominance");
		interactionTypeChoices.addItem("Incomplete Dominance");
		this.add(interactionTypeChoices);
	}

	public ListBox getInteractionTypeChoices() {
		return interactionTypeChoices;
	}


}
