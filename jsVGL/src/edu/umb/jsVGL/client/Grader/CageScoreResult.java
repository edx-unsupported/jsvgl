package edu.umb.jsVGL.client.Grader;

public class CageScoreResult {
	
	private String html;
	private CageScoreForCharacter[] individualCharacterResults;
	
	public CageScoreResult(int numCharacters) {
		html = "";
		individualCharacterResults = new CageScoreForCharacter[numCharacters];
	}
	
	public String getHTML() {
		return html;
	}
	
	public void setHTML(String html) {
		this.html = html;
	}
	
	public void addCageScoreForCharacter(int index, CageScoreForCharacter csfc) {
		if (index < individualCharacterResults.length) {
			individualCharacterResults[index] = csfc;
		}
	}
	
	public CageScoreForCharacter getCageScoreForCharacter(int index) {
		return individualCharacterResults[index];
	}

}
