package edu.umb.jsVGL.client.ModelBuilder;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;

public class TwoAllelePanel extends SimplePanel {

	private ListBox interactionTypeChoices;

	public TwoAllelePanel(boolean incDomPossible, 
			boolean complementationPossible, 
			boolean epistasisPossible) {
		interactionTypeChoices = new ListBox();
		interactionTypeChoices.addItem("Unknown");
		interactionTypeChoices.addItem("Simple Dominance");
		if (incDomPossible || epistasisPossible) {
			interactionTypeChoices.addItem("Incomplete Dominance");
		}
		if (complementationPossible) {
			interactionTypeChoices.addItem("Complementation");
		}
		if (epistasisPossible) {
			interactionTypeChoices.addItem("Epistasis");
		}
		add(interactionTypeChoices);
	}

	public ListBox getInteractionTypeChoices() {
		return interactionTypeChoices;
	}

}
