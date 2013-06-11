package edu.umb.jsVGL.client.Grader;

public class CageScoreForCharacter {
	
	public String character;
	public boolean showsSexLinkage;
	public boolean showsInteraction;
	public boolean capableOfShowingLinkage;

	public CageScoreForCharacter(String character) {
		this.character = character;
		showsSexLinkage = false;
		showsInteraction = false;
		capableOfShowingLinkage = false;
	}

}
