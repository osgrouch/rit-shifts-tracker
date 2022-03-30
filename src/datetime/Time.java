package src.datetime;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
		String[] entrySplit = entry.split(":\\s+");
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

		if (ampm.equals("AM")) {
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
	 * rounding down the float to 4 decimal places;
	 * useful for calculating pay for a shift.
	 * 
	 * @return float value of minutes/60
	 */
	public float fractionalHour() {
		BigDecimal bd = new BigDecimal(minutes / 60).setScale(4, RoundingMode.DOWN);
		return bd.floatValue();
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
}
