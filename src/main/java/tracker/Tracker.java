package tracker;

import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/** The main Shift Tracker application using Picocli */
@CommandLine.Command (name = "Shift Tracker", version = "0.0", mixinStandardHelpOptions = true)
public class Tracker implements Runnable {
	/** Boolean indicating if to call toString method on the PayPeriod file being modified before exiting */
	@CommandLine.Option (names = { "-v", "--view" },
	                     description = "View the contents of the PayPeriod file before exiting.")
	boolean view;

	public static void main (String[] args) {
		new CommandLine(new Tracker()).execute(args);
	}

	/**
	 * Start the thread this application belongs to and handle arguments.
	 */
	@Override public void run () {
		CommandLine.usage(this, System.out);
	}

	/**
	 * Create a new Shift and add it to the given PayPeriod XML file.
	 *
	 * @param filename the PayPeriod XML file to add to
	 * @throws IOException if input/output exception in BufferReader used to take input from user from console to
	 *                     create a new shift
	 */
	@CommandLine.Command (name = "add",
	                      description = "Add a new shift to an existing PayPeriod file.")
	public void add (String filename) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	}

	/**
	 * Create a new PayPeriod XML file.
	 */
	@CommandLine.Command (name = "new",
	                      description = "Create a new PayPeriod and write it to a new file.")
	public void newPP () {

	}

	/**
	 * Read a given PayPeriod XML file and print its contents to the console.
	 *
	 * @param filename the PayPeriod XML file to read
	 */
	@CommandLine.Command (name = "read",
	                      description = "Read the contents of an existing PayPeriod file.")
	public void read (String filename) {

	}
}
