package src.datetime;

/**
 * Class representing Time, used to track clock ins and clock outs.
 */
public class Time {
	/** The hour in 12h format */
	private int hour;
	/** The minute */
	private int minutes;
	/** AM or PM */
	private String ampm;

	/**
	 * Create a new Time instance with the given information.
	 * 
	 * @param entry the time in the format "HH:MM AM/PM"
	 */
	public Time(String entry) {
		// splits entry into: "HH" "MM" "AM/PM"
		String[] entrySplit = entry.split(":|\\s+");
		this.hour = Integer.parseInt(entrySplit[0]);
		this.minutes = Integer.parseInt(entrySplit[1]);
		this.ampm = entrySplit[2];
	}

	/**
	 * Create a new time instance with pre-formatted parameters.
	 * 
	 * @param hour    the hour
	 * @param minutes the minutes
	 * @param ampm    AM or PM
	 */
	public Time(int hour, int minutes, String ampm) {
		this.hour = hour;
		this.minutes = minutes;
		this.ampm = ampm;
	}

	/**
	 * Converts the 12h hour into 24h military time.
	 * 
	 * @return the hour in 24h format
	 */
	public int militaryHour() {
		int temp;

		if (hour == 12 && ampm.equals("AM")) {
			temp = 0;
		} else if (ampm.equals("AM") || (ampm.equals("PM") && hour == 12)) {
			temp = hour;
		} else {
			temp = hour + 12;
		}

		return temp;

	}

	/**
	 * Convert the minutes worked into a fraction of an hour.
	 * 
	 * @return double value of minutes/60
	 */
	public double fractionalHour() {
		return minutes / 60.0;
	}

	/**
	 * @return the hour in 12h format
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * @return the minutes
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 * @return AM or PM
	 */
	public String getAmpm() {
		return ampm;
	}

	/**
	 * @return a human readable String of the time stored in this instance
	 */
	@Override
	public String toString() {
		String readableTime = hour + ":";

		// adds the zero in front of the minute to display the minutes correctly
		if (minutes < 10) {
			readableTime += ("0" + minutes);
		} else {
			readableTime += minutes;
		}

		readableTime += (" " + ampm);

		return readableTime;
	}

	/**
	 * Compare two given Times by hour and minute.
	 * 
	 * @param one the first Time to compare
	 * @param two the second Time to compare
	 * @return -1 if one < two,
	 *         0 if equal,
	 *         1 if one > two
	 */
	public static int compare(Time one, Time two) {
		int result = Integer.compare(one.militaryHour(), two.militaryHour());

		if (result == 0)
			result = Integer.compare(one.getMinutes(), two.getMinutes());

		return result;
	}

	/**
	 * Calculate the hour difference between two Time instances.
	 * 
	 * @param in  the lesser Time instance
	 * @param out the greater Time instance
	 * @return double value of hours between Time instances
	 */
	public static double difference(Time in, Time out) {
		int hourDiff = out.militaryHour() - in.militaryHour();
		int minutesDiff;

		if (out.getMinutes() < in.getMinutes()) {
			// if clock out minutes is less than clock in minutes,
			// then a full hour was not completed,
			// therefore decrease the hours worked by one and calculate the minutes
			--hourDiff;
			minutesDiff = (60 + out.getMinutes()) - in.getMinutes();
		} else if (out.getMinutes() > in.getMinutes()) {
			// if clock out minutes is greater than clock in minutes,
			// then just calculate their difference
			minutesDiff = out.getMinutes() - in.getMinutes();
		} else {
			// else the minutes are the same and there is no difference
			minutesDiff = 0;
		}

		return hourDiff + (minutesDiff / 60.0);
	}
}
