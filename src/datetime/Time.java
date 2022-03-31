package src.datetime;

/**
 * Class representing Time, used to track clock ins, clock outs
 * and total time worked during a shift.
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
	 * @param ampm    AM, PM or UNDEF if being used to hold difference in time
	 */
	public Time(int hour, int minutes, String ampm) {
		this.hour = hour;
		this.minutes = minutes;
		this.ampm = ampm;
	}

	/**
	 * Converts the 12h hour into 24h military time,
	 * if valid ampm was set.
	 * 
	 * @return the hour in 24h format
	 */
	public int militaryHour() {
		int temp;

		if (hour == 12 && ampm.equals("AM")) {
			temp = 0;
		} else if (ampm.equals("AM") || (ampm.equals("PM") && hour == 12)) {
			temp = hour;
		} else if (ampm.equals("PM")) {
			temp = hour + 12;
		} else {
			temp = -1;
		}

		return temp;

	}

	/**
	 * Convert the minutes worked into a fraction of an hour,
	 * useful for calculating pay for a shift.
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
	 * @return AM, PM or UNDEF
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

		// only add AM/PM to the time, not UNDEF
		if (!ampm.equals("UNDEF")) {
			readableTime += (" " + ampm);
		}

		return readableTime;
	}
}
