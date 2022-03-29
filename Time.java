/**
 * Class representing Time, used to track clock ins, clock outs
 * and total time worked during a shift.
 */
public class Time {
	/** The hour in 12h format */
	private int hour;
	/** The hour in 24h format, useful for time math */
	private int militaryHour;
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
		this.militaryHour = calculateMilitaryHour();
	}

	/**
	 * Converts the 12h hour into 24h military time,
	 * if valid ampm was set.
	 * 
	 * @return the hour in 24h format
	 */
	private int calculateMilitaryHour() {
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
	 * @return the hour in 12h format
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * @return the hour in 24h format
	 */
	public int getMilitaryHour() {
		return militaryHour;
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