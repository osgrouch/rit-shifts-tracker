package tracker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import picocli.CommandLine;
import tracker.datetime.CalendarDate;
import tracker.shifts.PayPeriod;

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
			// attempt to get correct user input ATTEMPTS times before quiting
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
