package tracker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import picocli.CommandLine;
import tracker.datetime.CalendarDate;
import tracker.datetime.Time;
import tracker.shifts.CGShift;
import tracker.shifts.MarketShift;
import tracker.shifts.PayPeriod;
import tracker.shifts.Shift;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

/**
 * The main Shift Tracker application, implemented with picocli.
 */
@CommandLine.Command (name = "RIT Dining Shift Tracker",
                      description = "A command line program for keeping track of my shifts worked in RIT Dining with JSON files.",
                      version = "1.0", mixinStandardHelpOptions = true, usageHelpAutoWidth = true)
public class App implements Runnable {
	/** What to print to the console to indicate to the user to enter input */
	private static final String USER_PROMPT = "> ";
	/** The number of tries to attempt to get valid user input */
	private static final int ATTEMPTS = 3;
	/** The directory where JSON files created/modified are located */
	private static final String DATA_DIR = "./data/output/";

	/** Basic constructor */
	public App () {
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
	 * Start the thread this application belongs to and print out the help message.
	 */
	@Override
	public void run () {
		CommandLine.usage(this, System.out);
	}

	@CommandLine.Command (name = "add",
	                      description = "Add a shift to a Pay Period JSON file.")
	public void addToPayPeriod (@CommandLine.Parameters (arity = "1", paramLabel = "<filename>",
	                                                     description = "PayPeriod JSON file in " + DATA_DIR)
		                            String filename) {
		try {
			System.out.println("Looking for file...");
			Gson gson = new Gson();
			FileReader fileReader = new FileReader(DATA_DIR + filename);

			System.out.println("File found, parsing file contents...");
			Map<?, ?> payPeriodMap = gson.fromJson(fileReader, Map.class);
			PayPeriod payPeriod = new PayPeriod(payPeriodMap);

			System.out.println("Creating a new Shift...");
			Scanner sc = new Scanner(System.in);

			System.out.println("Location of the shift:");
			int locChoice = -1;
			boolean invalidLocation = true;
			for (int i = 0; i < ATTEMPTS; ++i) {
				try {
					for (int j = 1; j <= Shift.locations.keySet().size(); ++j) {
						// print each possible Shift location in order, starting from 1
						System.out.println("\t" + j + " - " + Shift.locations.get(j));
					}

					System.out.print("(number above) " + USER_PROMPT);
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
				System.out.println("Invalid input entered " + ATTEMPTS + " times, exiting program...");
				exit(1);
			}

			System.out.println("Job worked:");
			int jobChoice = -1;
			boolean invalidJob = true;
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

					System.out.print("(number above) " + USER_PROMPT);
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
				System.out.println("Invalid input entered " + ATTEMPTS + " times, exiting program...");
				exit(1);
			}

			System.out.println("Date:");
			String date = "";
			boolean invalidDate = true;
			for (int i = 0; i < ATTEMPTS; ++i) {
				try {
					System.out.print("(MM/DD/YYYY) " + USER_PROMPT);
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
				System.out.println("Invalid input entered " + ATTEMPTS + " times, exiting program...");
				exit(1);
			}

			System.out.println("Time clocked in:");
			String in = "";
			Time timeIn = null;
			boolean invalidIn = true;
			for (int i = 0; i < ATTEMPTS; ++i) {
				try {
					System.out.print("(HH:MM AM/PM) " + USER_PROMPT);
					in = sc.nextLine();
					int length = in.split(":|\\s+").length;
					if (length != 2 && length != 3) {
						throw new Exception();
					}

					timeIn = new Time(in);
					if (!timeIn.isValid()) {
						throw new IndexOutOfBoundsException();
					}

					// reaching this point indicates a correct time was entered and can break out of loop
					invalidIn = false;
					break;
				} catch (IndexOutOfBoundsException e) {
					// time entered was invalid, invalid hour or minute
					System.out.println("Invalid time entered");
				} catch (Exception e) {
					// input for time was incorrectly formatted
					System.out.println("Invalid input for time entered");
				}
			}
			if (invalidIn) {
				System.out.println("Invalid input entered " + ATTEMPTS + " times, exiting program...");
				exit(1);
			}

			System.out.println("Time clocked out:");
			String out = "";
			Time timeOut = null;
			boolean invalidOut = true;
			for (int i = 0; i < ATTEMPTS; ++i) {
				try {
					System.out.print("(HH:MM AM/PM) " + USER_PROMPT);
					out = sc.nextLine();
					int length = out.split(":|\\s+").length;
					if (length != 2 && length != 3) {
						throw new Exception();
					}

					timeOut = new Time(out);
					if (!timeOut.isValid()) {
						throw new IndexOutOfBoundsException();
					}

					// reaching this point indicates a correct time was entered and can break out of loop
					invalidOut = false;
					break;
				} catch (IndexOutOfBoundsException e) {
					// time entered was invalid, invalid hour or minute
					System.out.println("Invalid time entered");
				} catch (Exception e) {
					// input for time was incorrectly formatted
					System.out.println("Invalid input for time entered");
				}
			}
			if (invalidOut) {
				System.out.println("Invalid input entered " + ATTEMPTS + " times, exiting program...");
				exit(1);
			}

			if (Time.difference(timeIn, timeOut) < 0) {
				// checks time clocked in is less than time clocked out
				System.out.println("Incorrect times entered, enter the lesser time first, then greater time second");
				exit(1);
			}

			System.out.println("Is the following pay rate correct?");
			int payRate = 14;   // the typical pay rate
			boolean invalidRate = true;
			for (int i = 0; i < ATTEMPTS; ++i) {
				System.out.println("\tPay Rate = " + payRate);
				System.out.print("(Y/N) " + USER_PROMPT);
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
							System.out.print("(number) " + USER_PROMPT);
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
				System.out.println("Invalid input entered " + ATTEMPTS + " times, exiting program...");
				exit(1);
			}

			System.out.println("Creating Shift class...");
			Shift newEntry = null;
			if (locChoice == 1) {
				// MARKET
				String job = MarketShift.Job.values()[jobChoice - 1].name();
				newEntry = new MarketShift(date, in, out, payRate, job);
			} else {
				// CANTINA-GRILLE
				String job = CGShift.Job.values()[jobChoice - 1].name();
				newEntry = new CGShift(date, in, out, payRate, job);
			}

			System.out.println("Adding new Shift to PayPeriod");
			payPeriod.addShift(newEntry);

			System.out.println("Preparing to write to file...");
			try {
				FileWriter file = new FileWriter(DATA_DIR + filename);
				JsonWriter jsonWriter = new JsonWriter(file);
				jsonWriter.setIndent("\t");

				Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
				System.out.println("Writing PayPeriod class as JSON object...");
				gsonBuilder.toJson(payPeriod.createJSONObject(), jsonWriter);

				file.close();
				System.out.println("Edited Pay Period written to " + DATA_DIR + filename);
				exit(0);
			} catch (IOException e) {
				System.out.println("IO Exception encountered when attempting to write to file");
				e.printStackTrace();
				exit(4);
			}
		} catch (FileNotFoundException e) {
			System.out.println("The file " + DATA_DIR + filename + " was not found, exiting program...");
			exit(2);
		}
	}

	/**
	 * Prompts the user for input regarding the start date of a new pay period JSON file to create,
	 * checking that the date entered is valid. Checks if there is an existing pay period JSON file
	 * with the start date entered before creating a new {@link PayPeriod} to create the JSON object written
	 * to a file in the {@code DATA_DIR} folder.
	 */
	@CommandLine.Command (name = "new",
	                      description = "Create a new Pay Period JSON file.")
	public void createNewPayPeriod () {
		System.out.println("Creating a new Pay Period JSON file...");
		Scanner sc = new Scanner(System.in);

		System.out.println("When does the Pay Period start?");
		ArrayList<String> dateSplit = new ArrayList<>();
		boolean invalidDate = true;
		for (int i = 0; i < ATTEMPTS; ++i) {
			try {
				System.out.print("(MM/DD/YYYY) " + USER_PROMPT);
				String date = sc.nextLine();

				dateSplit = new ArrayList<>(Arrays.asList(date.split("/")));
				if (dateSplit.size() != 3) {
					throw new Exception();
				}

				int month = Integer.parseInt(dateSplit.get(0));
				int day = Integer.parseInt(dateSplit.get(1));
				int year = Integer.parseInt(dateSplit.get(2));
				CalendarDate dateEntered = new CalendarDate(month, day, year);
				if (!dateEntered.isValid()) {
					throw new NumberFormatException();
				}

				if (month < 10) {
					dateSplit.set(0, "0" + month);
				}
				if (day < 10) {
					dateSplit.set(1, "0" + day);
				}

				// reaching this point indicates a correct date was entered and can break out of the loop
				invalidDate = false;
				break;
			} catch (NumberFormatException e) {
				// called when string was entered instead of a number
				// or when a number entered is out of bounds
				System.out.println("Invalid date entered");
			} catch (Exception e) {
				// called when input is not in the correct format MM/DD/YYYY
				System.out.println("Invalid input for date entered");
			}
		}
		if (invalidDate) {
			System.out.println("Invalid date entered " + ATTEMPTS + " times, exiting program...");
			exit(1);
		}

		// filenames are in the format YYYY-MM-DD
		File newFile = new File(
			DATA_DIR + dateSplit.get(2) + "-" + dateSplit.get(0) + "-" + dateSplit.get(1) + ".json");
		if (newFile.exists()) {
			System.out.println("A pay period JSON file with that starting date already exists, exiting program...");
			exit(3);
		}
		System.out.println("Creating PayPeriod class...");
		PayPeriod payPeriod = new PayPeriod(dateSplit.get(0) + "/" + dateSplit.get(1) + "/" + dateSplit.get(2));

		System.out.println("Creating JSON file to write to...");
		try {
			FileWriter file = new FileWriter(newFile);
			JsonWriter jsonWriter = new JsonWriter(file);
			jsonWriter.setIndent("\t");

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			System.out.println("Writing PayPeriod class as JSON object...");
			gson.toJson(payPeriod.createJSONObject(), jsonWriter);

			file.close();
			System.out.println("New pay period JSON file created at " + newFile);
			exit(0);
		} catch (IOException e) {
			System.out.println("IO Exception encountered when attempting to write to file");
			e.printStackTrace();
			exit(4);
		}
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
		try {
			System.out.println("Looking for file...");
			Gson gson = new Gson();
			FileReader fileReader = new FileReader(DATA_DIR + filename);

			System.out.println("File found, parsing file contents...");
			Map<?, ?> payPeriodMap = gson.fromJson(fileReader, Map.class);
			PayPeriod payPeriod = new PayPeriod(payPeriodMap);

			System.out.println("Reading from " + DATA_DIR + filename + ":");
			System.out.println();
			System.out.println(payPeriod.shiftsToString());
			exit(0);
		} catch (FileNotFoundException e) {
			System.out.println("The file " + DATA_DIR + filename + " was not found, exiting program...");
			exit(2);
		}
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
}
