package tracker.colors;

import static tracker.colors.ConsoleColors.*;

/** Class to add color to console output, depending on the type of output that is to be displayed. */
public class Colorize {
	/** Basic constructor */
	public Colorize () {}

	/**
	 * @param message the information message to colorize
	 * @return String with color YELLOW
	 */
	public String information (String message) {
		return YELLOW + message + RESET;
	}

	/**
	 * @param message the prompt message to colorize
	 * @return String with color BLUE
	 */
	public String prompt (String message) {
		return BLUE + message + RESET;
	}

	/**
	 * @param message the error message to colorize
	 * @return String with color RED_BOLD
	 */
	public String error (String message) {
		return RED_BOLD + message + RESET;
	}

	/**
	 * @param message the success message to colorize
	 * @return String with color GREEN_BOLD
	 */
	public String success (String message) {
		return GREEN_BOLD + message + RESET;
	}
}
