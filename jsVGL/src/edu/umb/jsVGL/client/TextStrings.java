package edu.umb.jsVGL.client;

public class TextStrings {

	private final static String VERSION = "0.9";

	public static final String WELCOME_TEXT = "<html><body>"
			+ "<h3>Welcome to js VGL version " + VERSION + "</h3>"
			+ "You can either:"
			+ "<ul><li>Start a new problem by clicking either of the &quot;New Problem&quot; buttons above.</li>"
			+ "<ul><li><b>New Practice Problem</b> starts a problem in Practice mode. In this mode, the <b>Genetic Model</b> tab shows how"
			+ " the trait(s) are inherited. Placing the cursor over any organism will also pop up its genotype. Note that problems worked in Practice Mode "
			+ "cannot be saved for grading.</li>"
			+ "<li><b>New Graded Problem</b> starts a problem where you must determine how the trait(s) in the problem are inherited.</li></ul>"
			+ "<li>Continue working on a problem you have previously saved; if you have saved a problem, it will appear in thepanel to the right.</li></ul>"
			+ "<h3>Working a problem</h3>"
			+ "You solve jsVGL problems by crossing organisms - select a male and a female symbol and then click the <b>Cross Two</b> button."
			+ "jsVGL will then generate the resulting offspring. By choosing your crosses and observing the results carefully, you can determine "
			+ "how the trait(s) in your problem are inherited. You then enter your findings into the <b>Genetic Model</b> tab. Once you are sure"
			+ " of your answer, you can then submit it for grading."
			+ "</body></html>";

	public static final String ABOUT_jsVGL = 
			"<h3>About jsVGL</h3>"
					+ "jsVGL is a javascript version of the "
					+ "<a href=\"http://vgl.umb.edu\"target=\"_blank\">Virtual Genetics Lab</a>. "
					+ "Both are developed by <a href=\"mailto:brian.white@umb.edu\">Brian White</a> "
					+ "at the University of Massachusetts, Boston.";

	public static final String SUPER_CROSS_TEXT = 
			"<h3>Super Cross</h3>"
					+ "This carries out a cross with a large number of offpspring."
					+ "It is useful for getting recombination frequency data.<br><br>"
					+"Choose the desired number of offspring from the list below:<br>";
}
