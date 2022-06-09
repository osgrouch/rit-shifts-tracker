package tracker.application;

import picocli.CommandLine;
import tracker.datetime.CalendarDate;
import tracker.datetime.Time;
import tracker.shifts.CGShift;
import tracker.shifts.MarketShift;
import tracker.shifts.PayPeriod;
import tracker.shifts.Shift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The main Shift Tracker application, implemented with picocli.
 */
@CommandLine.Command (name = "RIT Dining Shift Tracker",
                      description = "A command line program for keeping track of my shifts worked in RIT Dining with JSON files.",
                      version = "1.0", mixinStandardHelpOptions = true, usageHelpAutoWidth = true)
public class App implements Runnable {
	/** The directory where JSON files created/modified are located */
	protected static final String DATA_DIR = "./data/output/";

	/** What to print to the console to indicate to the user to enter input */
	private static final String USER_PROMPT = " > ";
	/** The number of tries to attempt to get valid user input */
	private static final int ATTEMPTS = 3;

	/** Helper object for dealing with JSON */
	private final JSONHandler jsonHandler;

	/** Create a new JSONHandler instance. */
	public App () {
		this.jsonHandler = new JSONHandler();
	}

	/**
	 * Create a new {@link CommandLine} object to start this application with the given arguments.
	 *
	 * @param args command line arguments
	 */
	public static void main (String[] args) {
		new CommandLine(new App()).execute(args);
	}

	/**
	 * Read the given JSON file to create a {@link PayPeriod} object, then prompt the user for input
	 * regarding the new {@link Shift} to create (location, job, date, clock in, clock out, pay rate).
	 * Add the Shift to the PayPeriod instance then write the PayPeriod as a JSON object in a file in the
	 * {@code DATA_DIR} folder.
	 *
	 * @param filename PayPeriod JSON file
	 */
	@CommandLine.Command (name = "add",
	                      description = "Add a shift to a Pay Period JSON file.")
	public void addToPayPeriod (@CommandLine.Parameters (arity = "1", paramLabel = "<filename>",
	                                                     description = "PayPeriod JSON file in " + DATA_DIR)
		                            String filename) {
		PayPeriod payPeriod = jsonHandler.payPeriodFromFile(filename);
		if (payPeriod == null) {
			// invalid filename given, thus payPeriod could not be initialized
			exit(2);
		}

		System.out.println("Creating a new Shift...");

		int locChoice = selectLocation();
		int jobChoice = selectJob(locChoice);
		String date = setDate("Date:");
		String in = setTime("Time clocked in:");
		String out = setTime("Time clocked out:");
		int payRate = setPayRate();

		Shift newEntry = createShiftFromInputData(locChoice, jobChoice, date, in, out, payRate);
		System.out.println("Adding new Shift to PayPeriod");
		payPeriod.addShift(newEntry);

		exit(jsonHandler.payPeriodToFile(payPeriod, filename));
	}

	/**
	 * Read the given JSON file to create a {@link PayPeriod} object, then prompt the user for input
	 * regarding the new {@link Shift} to edit. Then prompt the user for what fields to edit (location, job, date,
	 * clock in, clock out, pay rate). Add the modified Shift to the PayPeriod instance then
	 * write the PayPeriod as a JSON object in a file in the {@code DATA_DIR} folder.
	 *
	 * @param filename PayPeriod JSON file
	 */
	@CommandLine.Command (name = "edit",
	                      description = "Edit a shift in a Pay Period JSON file.")
	public void editAPayPeriod (@CommandLine.Parameters (arity = "1", paramLabel = "<filename>",
	                                                     description = "PayPeriod JSON file in " + DATA_DIR)
		                            String filename) {
		PayPeriod payPeriod = jsonHandler.payPeriodFromFile(filename);
		if (payPeriod == null) {
			// invalid filename given, thus payPeriod could not be initialized
			exit(2);
		}
		System.out.println(payPeriod.toStringWithShifts());
		Scanner sc = new Scanner(System.in);

		String date = setDate("Enter the date of the Shift to edit:");

		System.out.println("Looking for Shift with given date in the PayPeriod object...");
		CalendarDate inputDate = new CalendarDate(date);
		ArrayList<Shift> matchingShifts = new ArrayList<>();
		for (Shift shift : payPeriod.getShifts()) {
			if (shift.getDate().equals(inputDate)) {
				matchingShifts.add(shift);
			}
		}

		int matches = matchingShifts.size();

		if (matches == 0) {
			System.out.println("No Shifts found on " + date);
			exit(0);
		}

		System.out.println("Found " + matches + " Shift(s) on " + date);
		int shiftChoice = -1;
		boolean invalidShift = true;
		for (int i = 0; i < ATTEMPTS; ++i) {
			try {
				for (int j = 0; j < matches; ++j) {
					Shift shift = matchingShifts.get(j);
					System.out.println("Shift " + ( j + 1 ) + ":");
					System.out.println(shift.toString());
				}

				System.out.print("(number above)" + USER_PROMPT);
				String choice = sc.nextLine();
				shiftChoice = Integer.parseInt(choice);
				if (shiftChoice < 1 || shiftChoice > matches) {
					throw new IndexOutOfBoundsException();
				}

				// reaching this point indicates a valid choice was made and can break out of loop
				invalidShift = false;
				break;
			} catch (NumberFormatException e) {
				// a number was not entered
				System.out.println("Invalid input entered, enter a number from the list");
			} catch (IndexOutOfBoundsException e) {
				// a number outside the valid range was entered
				System.out.println("Number entered is outside of the valid range");
			}
		}
		if (invalidShift) {
			System.out.println("Invalid input entered " + ATTEMPTS + " times");
			exit(1);
		}

		Shift modifiedShift = matchingShifts.get(shiftChoice - 1);
		Shift oldShift = modifiedShift;
		int actionChoice = -1;
		while (true) {
			// continue looping for edits until user inputs 0
			System.out.println(modifiedShift.toString());
			boolean invalidAction = true;

			String[] actionsList = new String[]{
				"Finalize edits", "Change location", "Change job",
				"Change date", "Change clock in time", "Change clock out time", "Change pay rate" };
			System.out.println("Choose an action:");
			for (int i = 0; i < ATTEMPTS; ++i) {
				try {
					for (int j = 0; j < actionsList.length; ++j) {
						System.out.println(j + " - " + actionsList[j]);
					}

					System.out.print("(number above)" + USER_PROMPT);
					String choice = sc.nextLine();
					actionChoice = Integer.parseInt(choice);
					if (actionChoice < 0 || actionChoice > actionsList.length) {
						throw new IndexOutOfBoundsException();
					}

					// reaching this point indicates a valid choice was made and can break out of loop
					invalidAction = false;
					break;
				} catch (NumberFormatException e) {
					// a number was not entered
					System.out.println("Invalid input entered, enter a number from the list");
				} catch (IndexOutOfBoundsException e) {
					// a number outside the valid range was entered
					System.out.println("Number entered is outside of the valid range");
				}
			}
			if (invalidAction) {
				System.out.println("Invalid input entered " + ATTEMPTS + " times");
				exit(1);
			}
			if (actionChoice == 0) {
				break;
			}

			int locChoice = -1;
			int jobChoice = -1;
			if (modifiedShift instanceof MarketShift) {
				locChoice = 1;
				jobChoice = ( (MarketShift) modifiedShift ).getJob().ordinal() + 1;
			}
			if (modifiedShift instanceof CGShift) {
				locChoice = 2;
				jobChoice = ( (CGShift) modifiedShift ).getJob().ordinal() + 1;
			}
			String newDate = modifiedShift.getDate().toString();
			String newIn = modifiedShift.getIn().toString();
			String newOut = modifiedShift.getOut().toString();
			int newRate = modifiedShift.getPayRate();
			switch (actionChoice) {
				case 1 -> locChoice = selectLocation();
				case 2 -> jobChoice = selectJob(locChoice);
				case 3 -> newDate = setDate("Enter new date:");
				case 4 -> newIn = setTime("Time clocked in:");
				case 5 -> newOut = setTime("Time clocked out:");
				case 6 -> newRate = setPayRate();
			}
			modifiedShift = createShiftFromInputData(locChoice, jobChoice, newDate, newIn, newOut, newRate);
		}

		int exitCode = -1;
		if (!modifiedShift.equals(oldShift)) {
			// write changes if any have been made
			System.out.println("Removing old Shift from PayPeriod");
			payPeriod.removeShift(oldShift);
			System.out.println("Adding new Shift to PayPeriod");
			payPeriod.addShift(modifiedShift);
			exitCode = jsonHandler.payPeriodToFile(payPeriod, filename);
		} else {
			System.out.println("No changes were made to the selected Shift");
			exitCode = 0;
		}

		exit(exitCode);
	}

	/**
	 * Prompts the user for input regarding the start date of a new pay period JSON file to create,
	 * checking that the date entered is valid. Checks if there is an existing pay period JSON file
	 * with the start date entered before creating a new {@link PayPeriod} to write as a JSON object
	 * to a file in the {@code DATA_DIR} folder.
	 */
	@CommandLine.Command (name = "new",
	                      description = "Create a new Pay Period JSON file.")
	public void createNewPayPeriod () {
		System.out.println("Creating a new Pay Period JSON file...");
		Scanner sc = new Scanner(System.in);

		String date = setDate("When does the Pay Period start?");
		ArrayList<String> dateSplit = new ArrayList<>(Arrays.asList(date.split("/")));

		int month = Integer.parseInt(dateSplit.get(0));
		if (month < 10) {
			dateSplit.set(0, "0" + month);
		}
		int day = Integer.parseInt(dateSplit.get(1));
		if (day < 10) {
			dateSplit.set(1, "0" + day);
		}

		// filenames are in the format YYYY-MM-DD
		String filename = dateSplit.get(2) + "-" + dateSplit.get(0) + "-" + dateSplit.get(1) + ".json";
		if (jsonHandler.fileExists(filename)) {
			System.out.println("A pay period JSON file with that starting date already exists");
			exit(3);
		}
		System.out.println("Creating PayPeriod class...");
		PayPeriod payPeriod = new PayPeriod(dateSplit.get(0) + "/" + dateSplit.get(1) + "/" + dateSplit.get(2));

		exit(jsonHandler.payPeriodToFile(payPeriod, filename));
	}

	/**
	 * Read the given JSON file to create a {@link PayPeriod} object, then print the PayPeriod to the console.
	 *
	 * @param filename PayPeriod JSON file
	 */
	@CommandLine.Command (name = "read",
	                      description = "Read the contents of a Pay Period JSON file.")
	public void readFromPayPeriod (@CommandLine.Parameters (arity = "1", paramLabel = "<filename>",
	                                                        description = "PayPeriod JSON file in " + DATA_DIR)
		                               String filename) {
		PayPeriod payPeriod = jsonHandler.payPeriodFromFile(filename);
		if (payPeriod == null) {
			// invalid filename given, thus payPeriod could not be initialized
			exit(2);
		}

		System.out.println(payPeriod.toStringWithShifts());
		exit(0);
	}

	/**
	 * Exit the application with the given code.
	 *
	 * @param code the exit code, meanings:
	 *             <ul>
	 *                  <li>0 - No errors</li>
	 *                  <li>1 - Invalid user input</li>
	 *             	    <li>2 - Invalid user command line arguments</li>
	 *                  <li>3 - Attempting to recreate an existing file</li>
	 *                  <li>4 - IOException when writing to file</li>
	 *             </ul>
	 */
	private void exit (int code) {
		System.exit(code);
	}

	/**
	 * Prompt the user to select a location from the available Shift subclasses. Prompts the user for input
	 * {@code ATTEMPTS} times before quiting if given invalid input.
	 *
	 * @return an integer representing the location choice
	 */
	private int selectLocation () {
		Scanner sc = new Scanner(System.in);
		int locChoice = -1;
		boolean invalidLocation = true;

		System.out.println("Location of the shift:");
		for (int i = 0; i < ATTEMPTS; ++i) {
			try {
				for (int j = 1; j <= Shift.locations.keySet().size(); ++j) {
					// print each possible Shift location in order, starting from 1
					System.out.println("\t" + j + " - " + Shift.locations.get(j));
				}

				System.out.print("(number above)" + USER_PROMPT);
				String choice = sc.nextLine();
				locChoice = Integer.parseInt(choice);
				if (locChoice < 1 || locChoice > Shift.locations.keySet().size()) {
					throw new IndexOutOfBoundsException();
				}

				// reaching this point indicates a valid choice was made and can break out of loop
				invalidLocation = false;
				break;
			} catch (NumberFormatException e) {
				// a number was not entered
				System.out.println("Invalid input entered, enter a number from the list");
			} catch (IndexOutOfBoundsException e) {
				// a number outside the valid range was entered
				System.out.println("Number entered is outside of the valid range");
			}
		}
		if (invalidLocation) {
			System.out.println("Invalid input entered " + ATTEMPTS + " times");
			exit(1);
		}

		return locChoice;
	}

	/**
	 * Prompt the user to select a job from the appropriate Shift subclass. Prompts the user for input
	 * {@code ATTEMPTS} times before quiting if given invalid input.
	 *
	 * @return an integer representing the job choice
	 */
	private int selectJob (int locChoice) {
		Scanner sc = new Scanner(System.in);
		int jobChoice = -1;
		boolean invalidJob = true;

		System.out.println("Job worked:");
		for (int i = 0; i < ATTEMPTS; ++i) {
			try {
				int maxNumber = -1;
				if (locChoice == 1) {
					// MARKET
					maxNumber = MarketShift.Job.values().length;
					for (int j = 1; j <= maxNumber; ++j) {
						System.out.println("\t" + j + " - " + MarketShift.Job.values()[j - 1]);
					}
				} else {
					// CANTINA-GRILLE
					maxNumber = CGShift.Job.values().length;
					for (int j = 1; j <= maxNumber; ++j) {
						System.out.println("\t" + j + " - " + CGShift.Job.values()[j - 1]);
					}
				}

				System.out.print("(number above)" + USER_PROMPT);
				String choice = sc.nextLine();
				jobChoice = Integer.parseInt(choice);
				if (jobChoice < 1 || jobChoice > maxNumber) {
					throw new IndexOutOfBoundsException();
				}

				// reaching this point indicates a valid choice was made and can break out of loop
				invalidJob = false;
				break;
			} catch (NumberFormatException e) {
				// a number was not entered
				System.out.println("Invalid input entered, enter a number from the list");
			} catch (IndexOutOfBoundsException e) {
				// a number outside the valid range was entered
				System.out.println("Number entered is outside of the valid range");
			}
		}
		if (invalidJob) {
			System.out.println("Invalid input entered " + ATTEMPTS + " times");
			exit(1);
		}

		return jobChoice;
	}

	/**
	 * Prompt the user to input a date in the format MM/DD/YYYY. Prompts the user for input
	 * {@code ATTEMPTS} times before quiting if given invalid input.
	 *
	 * @param message the message to print out before prompting the user for input
	 * @return a String in the format MM/DD/YYYY
	 */
	private String setDate (String message) {
		Scanner sc = new Scanner(System.in);
		String date = "";
		boolean invalidDate = true;

		System.out.println(message);
		for (int i = 0; i < ATTEMPTS; ++i) {
			try {
				System.out.print("(MM/DD/YYYY)" + USER_PROMPT);
				date = sc.nextLine();
				if (date.split("/").length != 3) {
					throw new Exception();
				}

				CalendarDate dateEntered = new CalendarDate(date);
				if (!dateEntered.isValid()) {
					throw new NumberFormatException();
				}

				// reaching this point indicates a correct date was entered and can break out of loop
				invalidDate = false;
				break;
			} catch (NumberFormatException e) {
				// string was entered instead of a number
				// or when a number entered is out of bounds
				System.out.println("Invalid date entered");
			} catch (Exception e) {
				// input is not in the correct format MM/DD/YYYY
				System.out.println("Invalid input for date entered");
			}
		}
		if (invalidDate) {
			System.out.println("Invalid input entered " + ATTEMPTS + " times");
			exit(1);
		}

		return date;
	}

	/**
	 * Prompt the user to input a time in the format HH:MM (AM/PM optional). Prompts the user for input
	 * {@code ATTEMPTS} times before quiting if given invalid input.
	 *
	 * @param message the message to print out before prompting the user for input
	 * @return a String in the format HH:MM (AM/PM optional)
	 */
	private String setTime (String message) {
		Scanner sc = new Scanner(System.in);
		String time = "";
		boolean invalidTime = true;

		System.out.println(message);
		for (int i = 0; i < ATTEMPTS; ++i) {
			try {
				System.out.print("(HH:MM AM/PM)" + USER_PROMPT);
				time = sc.nextLine();
				int length = time.split(":|\\s+").length;
				if (length != 2 && length != 3) {
					throw new Exception();
				}

				Time timeObj = new Time(time);
				if (!timeObj.isValid()) {
					throw new IndexOutOfBoundsException();
				}

				// reaching this point indicates a correct time was entered and can break out of loop
				invalidTime = false;
				break;
			} catch (IndexOutOfBoundsException e) {
				// time entered was invalid, invalid hour or minute
				System.out.println("Invalid time entered");
			} catch (Exception e) {
				// input for time was incorrectly formatted
				System.out.println("Invalid input for time entered");
			}
		}
		if (invalidTime) {
			System.out.println("Invalid input entered " + ATTEMPTS + " times");
			exit(1);
		}

		return time;
	}

	/**
	 * Prompt the user to set the pay rate for the shift. Since the minimum wage is 14, begin by prompting
	 * if 14 is the correct number. If not, then prompt the user to input the rate. Prompts the user for input
	 * {@code ATTEMPTS} times before quiting if given invalid input.
	 *
	 * @return an integer representing the pay rate
	 */
	private int setPayRate () {
		Scanner sc = new Scanner(System.in);
		int payRate = 14;   // the typical pay rate
		boolean invalidRate = true;

		System.out.println("Is the following pay rate correct?");
		for (int i = 0; i < ATTEMPTS; ++i) {
			System.out.println("\tPay Rate = " + payRate);
			System.out.print("(Y/N)" + USER_PROMPT);
			String answerStr = sc.nextLine();
			char answer = answerStr.charAt(0);
			if (answer == 'y' || answer == 'Y') {
				// make no changes to the default pay rate
				invalidRate = false;
				break;
			} else if (answer == 'n' || answer == 'N') {
				// prompt for change to pay rate
				for (int j = 0; j < ATTEMPTS; ++j) {
					try {
						System.out.println("Enter the new pay rate:");
						System.out.print("(number)" + USER_PROMPT);
						String numStr = sc.nextLine();
						payRate = Integer.parseInt(numStr);
						if (payRate < 14) {
							// pay rate will never be below 14 minimum
							throw new IndexOutOfBoundsException();
						}
						invalidRate = false;
						break;
					} catch (NumberFormatException e) {
						// an integer was not entered
						System.out.println("Invalid input for pay rate entered");
					} catch (IndexOutOfBoundsException e) {
						// a number less than 14 was entered
						System.out.println("Invalid pay rate entered");
					}
				}
				break;
			} else {
				// invalid input
				System.out.println("Invalid answer, answer Y or N");
			}
		}
		if (invalidRate) {
			System.out.println("Invalid input entered " + ATTEMPTS + " times");
			exit(1);
		}

		return payRate;
	}

	/**
	 * Create a new Shift object of the appropriate subclass with the given arguments.
	 * Uses {@code locChoice} to create the appropriate Shift subclass and finds the job represented by jobChoice.
	 *
	 * @param locChoice the location worked
	 * @param jobChoice the job worked
	 * @param date      the date
	 * @param in        the time clocked in
	 * @param out       the time clocked out
	 * @param payRate   the pay rate
	 * @return new Shift object
	 */
	private Shift createShiftFromInputData (int locChoice, int jobChoice, String date, String in, String out,
	                                        int payRate) {
		System.out.println("Creating Shift class...");
		Shift newEntry = null;
		if (locChoice == 1) {
			// MARKET
			String job = MarketShift.Job.values()[jobChoice - 1].name();
			newEntry = new MarketShift(date, in, out, payRate, job);
		} else if (locChoice == 2) {
			// CANTINA-GRILLE
			String job = CGShift.Job.values()[jobChoice - 1].name();
			newEntry = new CGShift(date, in, out, payRate, job);
		}
		return newEntry;
	}

	/**
	 * Start the thread this application belongs to and print out the help message.
	 */
	@Override
	public void run () {
		CommandLine.usage(this, System.out);
	}
}
