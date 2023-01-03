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
import java.time.LocalTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.*;

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
			System.out.println("File found, creating a new PayPeriod...");

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
			System.out.println("File found, creating PayPeriod...");
			PayPeriod payPeriod = objectMapper.readValue(jsonFile, PayPeriod.class);
			System.out.println(payPeriod.toString());
		} catch (FileNotFoundException e) {
			System.out.println("File " + filename + " not found.");
		} catch (IOException e) {
			System.out.println("Error reading from file " + filename + ".");
			throw new RuntimeException(e);
		}
		exit();
	}

	/**
	 * Add a new {@link Shift} to a {@link PayPeriod} from the given JSON file.
	 *
	 * @param filename Path to a PayPeriod JSON file.
	 */
	@CommandLine.Command(name = "add",
	                     description = "Add a Shift to a PayPeriod JSON file.")
	public void addNewShift(@CommandLine.Parameters(arity = "1",
	                                                paramLabel = "<filename>",
	                                                description = "Path to a PayPeriod JSON file.")
	                        String filename) {
		System.out.println("Searching for " + filename + "...");
		try {
			File jsonFile = new File(filename);
			if (!jsonFile.isFile()) {
				throw new FileNotFoundException();
			}
			System.out.println("File found, creating PayPeriod...");
			PayPeriod payPeriod = objectMapper.readValue(jsonFile, PayPeriod.class);

			System.out.println("Creating a new Shift...");
			String location = getLoc();
			String date = getDate("Date worked:");
			String clockIn = getTime("Time clocked in:");
			String clockOut = getTime("Time clocked out");
			Shift newShift = new Shift(location, date, clockIn, clockOut);
			System.out.println("Adding new Shift to PayPeriod...");
			payPeriod.addShift(newShift);

			objectWriter.writeValue(jsonFile, payPeriod);
			System.out.println("PayPeriod updated in " + filename + ".");
		} catch (FileNotFoundException e) {
			System.out.println("File " + filename + " not found.");
		} catch (IOException e) {
			System.out.println("Error reading from file " + filename + ".");
			throw new RuntimeException(e);
		}
		exit();
	}

	/**
	 * Edit a {@link Shift} in the {@link PayPeriod} from the given JSON file.
	 *
	 * @param filename Path to a PayPeriod JSON file.
	 */
	@CommandLine.Command(name = "edit",
	                     description = "Edit a Shift in a PayPeriod JSON file.")
	public void editShift(@CommandLine.Parameters(arity = "1",
	                                              paramLabel = "<filename>",
	                                              description = "Path to a PayPeriod JSON file.")
	                      String filename) {
		System.out.println("Searching for " + filename + "...");
		try {
			File jsonFile = new File(filename);
			if (!jsonFile.isFile()) {
				throw new FileNotFoundException();
			}
			System.out.println("File found, creating PayPeriod...");
			PayPeriod payPeriod = objectMapper.readValue(jsonFile, PayPeriod.class);

			Shift oldShift = getShift("Select shift to edit:", payPeriod);
			if (oldShift == null) {
				throw new MissingResourceException(null, null, null);
			}

			Shift newShift = oldShift;
			int action = -1;
			while (true) {
				String[] actions = {
					"Write Changes", "Edit Location", "Edit Date",
					"Edit Clock In", "Edit Clock Out", "Edit Pay Rate"
				};
				boolean invalidAction = true;

				System.out.println(newShift.toString());
				System.out.println("Select an action:");
				for (int attempt = 0; attempt < ATTEMPTS; ++attempt) {
					try {
						for (int i = 0; i < actions.length; ++i) {
							System.out.println("\t" + i + " - " + actions[i]);
						}

						System.out.print("(NUMBER)" + USER_PROMPT);
						String input = scanner.nextLine();
						action = Integer.parseInt(input);

						invalidAction = false;
						break;
					} catch (NumberFormatException e) {
						System.out.println("Invalid input entered, enter a number from the list.");
					} catch (IndexOutOfBoundsException e) {
						System.out.println("Selection out of bounds, enter a number from the list.");
					}
				}

				if (invalidAction) {
					System.out.println("Invalid action selection entered " + ATTEMPTS + " times.");
					exit();
				}
				if (action == 0) {
					break;
				}
				String location = newShift.getLocation();
				String date = newShift.getDate();
				String in = newShift.getIn();
				String out = newShift.getOut();
				double payRate = newShift.getPayRate();
				switch (action) {
					case 1 -> location = getLoc();
					case 2 -> date = getDate("Enter new date:");
					case 3 -> in = getTime("Enter new time clocked in:");
					case 4 -> out = getTime("Enter new time clocked out:");
					case 5 -> payRate = getPayRate("Enter new pay rate:");
				}
				newShift = new Shift(location, date, in, out, payRate);
			}

			if (!newShift.equals(oldShift)) {
				System.out.println("Removing old Shift from PayPeriod...");
				payPeriod.removeShift(oldShift);
				System.out.println("Adding new Shift to PayPeriod...");
				payPeriod.addShift(newShift);
				objectWriter.writeValue(jsonFile, payPeriod);
				System.out.println("PayPeriod updated in " + filename + ".");
			} else {
				System.out.println("No changes were made to the selected Shift.");
			}
		} catch (FileNotFoundException e) {
			System.out.println("File " + filename + " not found.");
		} catch (IOException e) {
			System.out.println("Error reading from file " + filename + ".");
			throw new RuntimeException(e);
		} catch (MissingResourceException e) {
			System.out.println("No shifts to remove in file " + filename + ".");
		}
		exit();
	}

	/**
	 * Remove a {@link Shift} in the {@link PayPeriod} from the given JSON file.
	 *
	 * @param filename Path to a PayPeriod JSON file.
	 */
	@CommandLine.Command(name = "remove",
	                     description = "Remove a Shift from a PayPeriod JSON file.")
	public void removeShift(@CommandLine.Parameters(arity = "1",
	                                                paramLabel = "<filename>",
	                                                description = "Path to a PayPeriod JSON file.")
	                        String filename) {
		System.out.println("Searching for " + filename + "...");
		try {
			File jsonFile = new File(filename);
			if (!jsonFile.isFile()) {
				throw new FileNotFoundException();
			}
			System.out.println("File found, creating PayPeriod...");
			PayPeriod payPeriod = objectMapper.readValue(jsonFile, PayPeriod.class);

			Shift selectedShift = getShift("Select shift to remove:", payPeriod);
			if (selectedShift == null) {
				throw new MissingResourceException(null, null, null);
			}
			payPeriod.removeShift(selectedShift);

			objectWriter.writeValue(jsonFile, payPeriod);
			System.out.println("PayPeriod updated in " + filename + ".");
		} catch (FileNotFoundException e) {
			System.out.println("File " + filename + " not found.");
		} catch (IOException e) {
			System.out.println("Error reading from file " + filename + ".");
			throw new RuntimeException(e);
		} catch (MissingResourceException e) {
			System.out.println("No shifts to remove in file " + filename + ".");
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
	 * Prompt the user to enter a time in the format <code>HH:MM AM/PM</code>, <code>AM/PM</code> optional.
	 * Converts the given time into the format <code>HH:MM</code>,
	 * then verifies the entered time by using {@link LocalTime#parse(CharSequence)}.
	 *
	 * @param message Message to print to user before time prompt.
	 * @return Time entered, in the format <code>HH:MM</code>.
	 */
	private String getTime(String message) {
		String time = null;
		boolean invalidTime = true;

		System.out.println(message);
		for (int attempt = 0; attempt < ATTEMPTS; attempt++) {
			try {
				System.out.print("(HH:MM AM/PM)" + USER_PROMPT);
				String input = scanner.nextLine();
				String[] inputSplit = input.split(":|\\s+");
				if (inputSplit.length != 2 && inputSplit.length != 3) {
					// time need not have am/pm
					throw new InputMismatchException();
				}

				String format; // time format to parse input, is am/pm present?
				if (inputSplit.length == 3) {
					format = "h:m a";
				} else {
					format = "H:m";
				}
				time = LocalTime.parse(
					input,
					new DateTimeFormatterBuilder()
						.parseCaseInsensitive()
						.appendPattern(format)
						.toFormatter(Locale.US)
				).toString();

				invalidTime = false;
				break;
			} catch (InputMismatchException | NumberFormatException e) {
				System.out.println("Invalid input for time entered.");
			} catch (DateTimeParseException e) {
				System.out.println("Error parsing given time.");
			}
		}

		if (invalidTime) {
			System.out.println("Invalid input for time entered " + ATTEMPTS + " times.");
			exit();
		}
		return time;
	}

	/**
	 * Prompt the user to enter a pay rate.
	 * Converts the given number into a double to two decimal places.
	 *
	 * @param message Message to print to user before pay rate prompt.
	 * @return Number entered, to two decimal places.
	 */
	private double getPayRate(String message) {
		double payRate = -1;
		boolean invalidPayRate = true;

		System.out.println(message);
		for (int attempt = 0; attempt < ATTEMPTS; attempt++) {
			try {
				System.out.print("(NUMBER)" + USER_PROMPT);
				String input = scanner.nextLine();
				String[] inputSplit = input.split("\\.");
				if (inputSplit.length == 2) {
					input = inputSplit[0] + "." + inputSplit[1].substring(0, 2); // keep payRate to two decimal places
				}
				payRate = Double.parseDouble(input);

				invalidPayRate = false;
				break;
			} catch (NumberFormatException e) {
				System.out.println("Invalid input entered, enter a number.");
			}
		}

		if (invalidPayRate) {
			System.out.println("Invalid input for time entered " + ATTEMPTS + " times.");
			exit();
		}
		return payRate;
	}

	/**
	 * Prompt the user for the location worked for the Shift being created or edited.
	 * Uses the locations declared in {@link Shift#LOCATIONS}.
	 * If there is only one value in the {@linkplain Shift#LOCATIONS Array}, skips user prompting and returns that value.
	 * Assumes there is at least one value in the {@linkplain Shift#LOCATIONS Array}.
	 *
	 * @return Name of the location.
	 */
	private String getLoc() {
		String[] locations = Shift.LOCATIONS;
		String location = null;
		boolean invalidLocation = true;

		if (locations.length == 1) {
			System.out.println("One location found: " + locations[0]);
			location = locations[0];
			invalidLocation = false;
		} else {
			System.out.println("Select shift location:");
			for (int attempt = 0; attempt < ATTEMPTS; attempt++) {
				try {
					for (int i = 1; i < locations.length + 1; i++) {
						// list locations starting from 1 instead of 0
						System.out.println("\t" + i + " - " + locations[i - 1]);
					}

					System.out.print("(NUMBER)" + USER_PROMPT);
					String input = scanner.nextLine();
					int selection = Integer.parseInt(input) - 1; // locations listed starting from 1 instead of 0

					location = locations[selection];
					invalidLocation = false;
					break;
				} catch (NumberFormatException e) {
					System.out.println("Invalid input entered, enter a number from the list.");
				} catch (IndexOutOfBoundsException e) {
					System.out.println("Selection out of bounds, enter a number from the list.");
				}
			}
		}

		if (invalidLocation) {
			System.out.println("Invalid location selection entered " + ATTEMPTS + " times.");
			exit();
		}
		return location;
	}

	/**
	 * Prompt the user for the {@link Shift} to from the given {@link PayPeriod}.
	 * If there is only one {@linkplain Shift} in the {@linkplain PayPeriod},
	 * skips user prompting and returns that {@linkplain Shift}.
	 * If there is no {@linkplain Shift} in the given {@linkplain PayPeriod}, returns null.
	 *
	 * @param message Message to print to user before {@linkplain Shift} prompt.
	 * @return Shift selected, may be null.
	 */
	private Shift getShift(String message, PayPeriod payPeriod) {
		Shift shift = null;
		boolean invalidShift = true;

		if (payPeriod.getShifts().size() == 0) {
			invalidShift = false;
		} else if (payPeriod.getShifts().size() == 1) {
			shift = payPeriod.getShifts().get(0);
			invalidShift = false;
		} else {
			System.out.println(message);
			for (int attempt = 0; attempt < ATTEMPTS; ++attempt) {
				try {
					List<Shift> shifts = payPeriod.getShifts();
					for (int i = 1; i < shifts.size() + 1; ++i) {
						Shift tempShift = shifts.get(i - 1);
						System.out.println("\t" + i + ": " + tempShift.getDate() + " @ " + tempShift.getIn() + " - " + tempShift.getOut());
					}

					System.out.print("(NUMBER)" + USER_PROMPT);
					String input = scanner.nextLine();
					shift = shifts.get(Integer.parseInt(input) - 1); // shifts listed starting from 1 instead of 0

					invalidShift = false;
					break;
				} catch (NumberFormatException e) {
					System.out.println("Invalid input entered, enter a number from the list.");
				} catch (IndexOutOfBoundsException e) {
					System.out.println("Selection out of bounds, enter a number from the list.");
				}
			}
		}

		if (invalidShift) {
			System.out.println("Invalid Shift selection entered " + ATTEMPTS + " times.");
			exit();
		}
		return shift;
	}

	/**
	 * Start the thread this application belongs to and print out the help message.
	 */
	@Override
	public void run() {
		CommandLine.usage(this, System.out);
	}
}
