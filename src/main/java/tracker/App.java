package tracker;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import picocli.CommandLine;
import tracker.jackson.PayPeriodDeserializer;
import tracker.jackson.PayPeriodSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NotDirectoryException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The main Shift Tracker application, implemented with picocli.
 */
@CommandLine.Command(name = "RIT Dining Shift Tracker",
                     description = "A command line program for keeping track of my shifts worked in RIT Dining with JSON files.",
                     version = "2.0.0", mixinStandardHelpOptions = true, usageHelpAutoWidth = true)
public class App implements Runnable {
	/** What to print to the console to indicate to the user to enter input. */
	private static final String USER_PROMPT = " > ";
	/** The number of tries to attempt to get valid user input. */
	private static final int ATTEMPTS = 3;
	/** The current year, to append to dates given by user. */
	private static final int YEAR = 2023;

	/** Scanner object used to take user input. */
	private final Scanner scanner;
	/** Object mapper to use for de/serialization of PayPeriod objects. */
	private final ObjectMapper objectMapper;
	/** Object writer to use to indent JSON files during serialization of PayPeriod objects. */
	private final ObjectWriter objectWriter;

	public App() {
		this.scanner = new Scanner(System.in);

		// register customer de/serializers for PayPeriod objects
		SimpleModule simpleModule = new SimpleModule(
			"PayPeriod De/Serializer",
			new Version(1, 0, 0, null, null, null)
		);
		simpleModule.addDeserializer(PayPeriod.class, new PayPeriodDeserializer());
		simpleModule.addSerializer(PayPeriod.class, new PayPeriodSerializer());
		this.objectMapper = new ObjectMapper().registerModule(simpleModule);

		// set indent and eol characters for output json file
		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter("\t", "\n");
		prettyPrinter.indentArraysWith(indenter);
		prettyPrinter.indentObjectsWith(indenter);
		this.objectWriter = objectMapper.writer(prettyPrinter);
	}

	/**
	 * Create a new {@link CommandLine} object to start this application with the given arguments.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		new CommandLine(new App()).execute(args);
	}

	/**
	 * Create a new {@link PayPeriod} to write in a JSON file under the given directory.
	 * Prompts the user to enter the date on which the new {@linkplain PayPeriod} starts.
	 *
	 * @param directory Directory to create a new PayPeriod JSON file.
	 */
	@CommandLine.Command(name = "new",
	                     description = "Create a new PayPeriod JSON file.")
	public void createNewPayPeriod(@CommandLine.Parameters(arity = "1",
	                                                       paramLabel = "<directory>",
	                                                       description = "Directory to create a new PayPeriod JSON file.")
	                               String directory) {
		String jsonFilename = null;
		System.out.println("Searching for " + directory + "...");
		try {
			File directoryFile = new File(directory);
			if (!directoryFile.isDirectory()) {
				throw new NotDirectoryException(directory);
			}

			System.out.println("Creating a new PayPeriod...");
			String date = getDate("When does the pay period start?");

			jsonFilename = directoryFile.getPath() + File.separator + date + ".json";
			File jsonFile = new File(jsonFilename);
			if (jsonFile.exists()) {
				throw new FileAlreadyExistsException(jsonFilename);
			}

			objectWriter.writeValue(jsonFile, new PayPeriod(date));
			System.out.println("PayPeriod written to " + jsonFilename + ".");
		} catch (NotDirectoryException e) {
			System.out.println("Directory " + directory + " not found.");
		} catch (FileAlreadyExistsException e) {
			System.out.println("File " + jsonFilename + " already exists.");
		} catch (IOException e) {
			System.out.println("Error writing to file " + jsonFilename + ".");
			throw new RuntimeException(e);
		}
		exit();
	}

	/**
	 * Parse the given JSON file to create a {@link PayPeriod} and print its information.
	 *
	 * @param filename Path to a PayPeriod JSON file.
	 */
	@CommandLine.Command(name = "read",
	                     description = "Read a PayPeriod from a JSON file.")
	public void readFromJson(@CommandLine.Parameters(arity = "1",
	                                                 paramLabel = "<filename>",
	                                                 description = "Path to a PayPeriod JSON file.")
	                         String filename) {
		System.out.println("Searching for " + filename + "...");
		try {
			File jsonFile = new File(filename);
			if (!jsonFile.isFile()) {
				throw new FileNotFoundException();
			}
			System.out.println("Reading from file...");
			PayPeriod payPeriod = objectMapper.readValue(jsonFile, PayPeriod.class);
			System.out.println(payPeriod.toString());
		} catch (FileNotFoundException e) {
			System.out.printf("File " + filename + " not found.");
		} catch (IOException e) {
			System.out.println("Error reading from file " + filename + ".");
			throw new RuntimeException(e);
		}
		exit();
	}

	/**
	 * Close the global Scanner object and terminate the program.
	 */
	private void exit() {
		scanner.close();
		System.exit(0);
	}

	/**
	 * Prompt the user to enter a date in the format <code>MM/DD</code>.
	 * Converts the given date into the format <code>YYYY-MM-DD</code> with the year stored in this object,
	 * then verifies the entered date by using {@link LocalDate#parse(CharSequence)}.
	 *
	 * @param message Message to print to user before date prompt.
	 * @return Date entered, in the format <code>YYYY-MM-DD</code>.
	 */
	private String getDate(String message) {
		String date = null;
		boolean invalidDate = true;

		System.out.println(message);
		for (int attempt = 0; attempt < ATTEMPTS; attempt++) {
			try {
				System.out.print("(MM/DD)" + USER_PROMPT);
				String input = scanner.nextLine();
				String[] inputSplit = input.split("/");
				if (inputSplit.length != 2) {
					throw new InputMismatchException();
				}

				date = YEAR + "-" + inputSplit[0] + "-" + inputSplit[1];
				LocalDate.parse(date); // YYYY-MM-DD
				invalidDate = false;
				break;
			} catch (InputMismatchException e) {
				System.out.println("Invalid input for date entered.");
			} catch (DateTimeParseException e) {
				System.out.println("Error parsing given date.");
			}
		}

		if (invalidDate) {
			System.out.println("Invalid input for date entered " + ATTEMPTS + " times.");
			exit();
		}
		return date;
	}

	/**
	 * Start the thread this application belongs to and print out the help message.
	 */
	@Override
	public void run() {
		CommandLine.usage(this, System.out);
	}
}
