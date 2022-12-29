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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * The main Shift Tracker application, implemented with picocli.
 */
@CommandLine.Command(name = "RIT Dining Shift Tracker",
                     description = "A command line program for keeping track of my shifts worked in RIT Dining with JSON files.",
                     version = "2.0.0", mixinStandardHelpOptions = true, usageHelpAutoWidth = true)
public class App implements Runnable {
	/** What to print to the console to indicate to the user to enter input */
	private static final String USER_PROMPT = " > ";
	/** The number of tries to attempt to get valid user input */
	private static final int ATTEMPTS = 3;

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
		try (
			FileReader fileReader = new FileReader(filename)
		) {
			System.out.println("Reading from file...");
			PayPeriod payPeriod = objectMapper.readValue(fileReader, PayPeriod.class);

			System.out.println(payPeriod.toString());
			exit();
		} catch (FileNotFoundException e) {
			System.out.printf("File " + filename + " not found.");
		} catch (IOException e) {
			System.out.println("Error converting file " + filename + " into a PayPeriod.");
		}
	}

	/**
	 * Close the global Scanner object and terminate the program.
	 */
	private void exit() {
		scanner.close();
		System.exit(0);
	}

	/**
	 * Start the thread this application belongs to and print out the help message.
	 */
	@Override
	public void run() {
		CommandLine.usage(this, System.out);
	}
}
