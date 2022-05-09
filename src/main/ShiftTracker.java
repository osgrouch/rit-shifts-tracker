package src.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class representing the main program. Contains the different methods used in
 * the RIT Dining Shift Tracker application.
 */
public class ShiftTracker {
	/** The prompt used to indicate the user must enter a value */
	private static final String INPUT_PROMPT = "> ";

	public void init() {
		printHeader("RIT Dining Shifts Tracker");
		System.out.println("\t0. Quit");
		System.out.println("\t1. Create");
		System.out.println("\t2. View");
		System.out.print(INPUT_PROMPT);

		int choice = -1;
		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean invalidChoice = false;
		while (choice < 0 || choice > 2) {
			try {
				line = br.readLine();
				choice = Integer.parseInt(line);
				if (choice < 0 || choice > 2) {
					invalidChoice = true;
				} else {
					invalidChoice = false;
				}
			} catch (NumberFormatException e) {
				invalidChoice = true;
			} catch (IOException e) {
				System.out.println(e);
				exit();
			}

			if (invalidChoice) {
				System.out.println("Invalid choice, try again");
				System.out.println("\t0. Quit");
				System.out.println("\t1. Create");
				System.out.println("\t2. View");
				System.out.print(INPUT_PROMPT);
			}
		}
	}

	/**
	 * Prints the given message to the console with underscoring.
	 * 
	 * @param msg the message to print
	 */
	public void printHeader(String msg) {
		System.out.println(msg);

		for (int i = 0; i < msg.length(); ++i) {
			System.out.print("-");
		}
		System.out.println();
	}

	/**
	 * Exit the program without errors.
	 */
	public void exit() {
		System.exit(0);
	}
}
