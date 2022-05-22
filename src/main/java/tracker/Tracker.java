package tracker;

import picocli.CommandLine;

import java.io.PrintStream;

/** The main Shift Tracker application using Picocli */
@CommandLine.Command (name = "Shift Tracker", version = "0.0", mixinStandardHelpOptions = true)
public class Tracker implements Runnable {
	/** Where to write output to */
	private final PrintStream out;

	/** Name of an XML file representing a PayPeriod to add a shift to */
	@CommandLine.Option (names = { "-a", "--add-shift" },
	                     description = "Add a new shift to an existing PayPeriod file.")
	String addToFilename;

	/** Boolean indicating the creation a new PayPeriod */
	@CommandLine.Option (names = { "-n", "--new" },
	                     description = "Create a new PayPeriod and write it to a new file.")
	boolean newPayPeriod;

	/** Name of an XML file representing a PayPeriod to read the contents of */
	@CommandLine.Option (names = { "-r", "--read" },
	                     description = "Read the contents of an existing PayPeriod file.")
	String readFromFilename;

	/**
	 * Create a new Tracker instance and set output stream as the console.
	 */
	public Tracker () {
		this.out = System.out;
	}

	public static void main (String[] args) {
		int exitCode = new CommandLine(new Tracker()).execute(args);
		System.exit(exitCode);
	}

	/**
	 * Start the thread this application belongs to and print out the app's help message
	 */
	@Override public void run () {
		CommandLine.usage(this, out);
	}
}
