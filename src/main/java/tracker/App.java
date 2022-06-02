package tracker;

import picocli.CommandLine;

/**
 * The main Shift Tracker application, implemented with picocli.
 */
@CommandLine.Command (name = "RIT Dining Shift Tracker",
                      description = "A simple command line program for keeping track of my shifts worked in RIT Dining.",
                      version = "1.0", mixinStandardHelpOptions = true, usageHelpAutoWidth = true)
public class App implements Runnable {
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
}
