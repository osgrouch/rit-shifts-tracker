package tracker.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import tracker.shifts.PayPeriod;

import java.io.*;
import java.util.Map;

import static tracker.application.App.DATA_DIR;

public class JSONHandler {
	/**
	 * Check if the given file exists.
	 *
	 * @param filename PayPeriod JSON file
	 * @return true if the file exists, else false
	 */
	public boolean jsonFileExists (String filename) {
		return new File(DATA_DIR + filename).exists();
	}

	/**
	 * Read the given file to create a PayPeriod object.
	 *
	 * @param filename PayPeriod JSON file
	 * @return {@link PayPeriod} instance
	 */
	public PayPeriod payPeriodFromFile (String filename) {
		PayPeriod payPeriod;
		try {
			System.out.println("Looking for file...");
			Gson gson = new Gson();
			FileReader fileReader = new FileReader(DATA_DIR + filename);

			System.out.println("File found, parsing file contents...");
			Map<?, ?> payPeriodMap = gson.fromJson(fileReader, Map.class);
			payPeriod = new PayPeriod(payPeriodMap);
		} catch (FileNotFoundException e) {
			System.out.println("The file " + DATA_DIR + filename + " was not found");
			payPeriod = null;
		}
		return payPeriod;
	}

	/**
	 * Write the given PayPeriod object to the given file.
	 *
	 * @param payPeriod a {@link PayPeriod} object
	 * @param filename  the name of the file to write to
	 * @return exit code, 0 if no errors encountered, 4 if IOException
	 */
	public int payPeriodToFile (PayPeriod payPeriod, String filename) {
		int exit = -1;
		System.out.println("Preparing to write to file...");
		try {
			FileWriter file = new FileWriter(DATA_DIR + filename);
			JsonWriter jsonWriter = new JsonWriter(file);
			jsonWriter.setIndent("\t");

			Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
			System.out.println("Writing PayPeriod class as JSON object...");
			gsonBuilder.toJson(payPeriod.createJSONObject(), jsonWriter);

			file.close();
			System.out.println("Pay Period written to " + DATA_DIR + filename);
			exit = 0;
		} catch (IOException e) {
			System.out.println("IO Exception encountered when attempting to write to file");
			e.printStackTrace();
			exit = 4;
		}
		return exit;
	}
}
