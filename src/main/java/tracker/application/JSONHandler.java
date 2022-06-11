package tracker.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonWriter;
import tracker.shifts.CGShift;
import tracker.shifts.MarketShift;
import tracker.shifts.PayPeriod;
import tracker.shifts.Shift;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import static tracker.application.App.DATA_DIR;

/**
 * Class with methods to handle reading/writing to and from JSON files.
 * Also has methods to convert a Shift and PayPeriod to and from JSON.
 */
public class JSONHandler {
	/** Basic constructor. */
	public JSONHandler () {}

	/**
	 * Check if the given file exists.
	 *
	 * @param filename PayPeriod JSON file
	 * @return true if the file exists, else false
	 */
	public boolean fileExists (String filename) {
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
			payPeriod = jsonToPayPeriod(payPeriodMap);
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
	 */
	public void payPeriodToFile (PayPeriod payPeriod, String filename) {
		System.out.println("Preparing to write to file...");
		try {
			FileWriter file = new FileWriter(DATA_DIR + filename);
			JsonWriter jsonWriter = new JsonWriter(file);
			jsonWriter.setIndent("\t");

			Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
			System.out.println("Writing PayPeriod class as JSON object...");
			gsonBuilder.toJson(payPeriodToJSON(payPeriod), jsonWriter);

			file.close();
			System.out.println("Pay Period written to " + DATA_DIR + filename);
		} catch (IOException e) {
			System.out.println("IO Exception encountered when attempting to write to file");
		}
	}

	/**
	 * Create a JSON object out of the given Shift.
	 *
	 * @param shift a {@link Shift} object
	 * @return {@link JsonObject GSON JSON object} representing a Shift
	 */
	public JsonObject shiftToJSON (Shift shift) {
		JsonObject jsonShift = new JsonObject();

		if (shift instanceof MarketShift) {
			jsonShift.addProperty("location", "MARKET");
			jsonShift.addProperty("job", ( (MarketShift) shift ).getJob().name());
		} else {
			jsonShift.addProperty("location", "CANTINA-GRILLE");
			jsonShift.addProperty("job", ( (CGShift) shift ).getJob().name());
		}

		jsonShift.addProperty("date", shift.getDate().toString());
		jsonShift.addProperty("in", shift.getIn().toString());
		jsonShift.addProperty("out", shift.getOut().toString());
		jsonShift.addProperty("hourly", shift.getPayRate());

		return jsonShift;
	}

	/**
	 * Create a JSON object out of the given PayPeriod.
	 *
	 * @param payPeriod a {@link PayPeriod} object
	 * @return {@link JsonObject GSON JSON object} representing a PayPeriod
	 */
	public JsonObject payPeriodToJSON (PayPeriod payPeriod) {
		JsonObject jsonPayPeriod = new JsonObject();

		jsonPayPeriod.addProperty("start", payPeriod.getStart().toString());
		jsonPayPeriod.addProperty("end", payPeriod.getEnd().toString());

		// write the hours to only 2 decimal places
		String hoursStr = String.format("%.2f", payPeriod.getHours());
		jsonPayPeriod.addProperty("hours", Double.valueOf(hoursStr));

		// write the pay to only 2 decimal places
		String payStr = String.format("%.2f", payPeriod.getPay());
		jsonPayPeriod.addProperty("pay", Double.valueOf(payStr));

		JsonArray jsonShifts = new JsonArray();
		for (Shift shift : payPeriod.getShifts()) {
			JsonObject jsonShift = shiftToJSON(shift);
			jsonShifts.add(jsonShift);
		}
		jsonPayPeriod.add("shifts", jsonShifts);

		return jsonPayPeriod;
	}

	/**
	 * Convert the Map of JSON keys and values into a Shift object.
	 *
	 * @param jsonMap the Map of JSON keys and values
	 */
	public Shift jsonToShift (LinkedTreeMap<?, ?> jsonMap) {
		Shift shift;

		String loc = (String) jsonMap.get("location");
		String date = (String) jsonMap.get("date");
		String in = (String) jsonMap.get("in");
		String out = (String) jsonMap.get("out");
		Double prDouble = (Double) jsonMap.get("hourly");
		int payRate = prDouble.intValue();
		String job = (String) jsonMap.get("job");

		if (loc.equals("MARKET")) {
			shift = new MarketShift(date, in, out, payRate, job);
		} else {
			shift = new CGShift(date, in, out, payRate, job);
		}

		return shift;
	}

	/**
	 * Convert the Map of JSON keys and values into a PayPeriod object.
	 * Recalculates the hours and pay fields when adding each shift to the ArrayList of Shifts in the PayPeriod.
	 *
	 * @param jsonMap the Map of JSON keys and values
	 */
	public PayPeriod jsonToPayPeriod (Map<?, ?> jsonMap) {
		String start = (String) jsonMap.get("start");
		String end = (String) jsonMap.get("end");
		PayPeriod payPeriod = new PayPeriod(start, end);

		ArrayList<LinkedTreeMap<?, ?>> jsonShifts = (ArrayList<LinkedTreeMap<?, ?>>) jsonMap.get("shifts");
		for (LinkedTreeMap<?, ?> jsonShift : jsonShifts) {
			payPeriod.addShift(jsonToShift(jsonShift));
		}

		return payPeriod;
	}
}
