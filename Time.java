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
	 * @param hour    the hour
	 * @param minutes the minute
	 * @param ampm    AM, PM or UNDEF when calculating time worked
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
	 * @return AM or PM
	 */
	public String getAmpm() {
		return ampm;
	}
}