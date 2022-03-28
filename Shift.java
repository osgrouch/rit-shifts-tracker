/**
 * Class representing a shift worked for RIT Dining
 */
public class Shift {
	/** Day worked */
	private Date date;
	/** Time clocked in */
	private Time in;
	/** Time clocked out */
	private Time out;

	/**
	 * Create a new shift and set its starting and ending times.
	 * 
	 * @param weekday      the day of the week worked
	 * @param calendarDate the exact calendar date worked
	 * @param start        the time clocked in
	 * @param end          the time clocked out
	 */
	public Shift(
			String weekday, String calendarDate, String start, String end) {
		this.date = createDate(weekday, calendarDate);
		// set the clock in and clock out times
		this.in = createTime(start);
		this.out = createTime(end);

		// TODO
		// - add shift location
		// - add actual job worked
	}

	/**
	 * Creates an instance of Date record with the information provided
	 * 
	 * @param weekday      the day of the week
	 * @param calendarDate the exact calendar date worked in the format "MM/DD/YYYY"
	 * @return Date instance
	 */
	private Date createDate(String weekday, String calendarDate) {
		// create the DaysOfTheWeek variable
		DaysOfTheWeek dayOfTheWeek;
		switch (weekday.charAt(0)) {
			case 'M':
				dayOfTheWeek = DaysOfTheWeek.MONDAY;
				break;
			case 'T':
				// T can be either Tuesday or Thursday
				if (weekday.charAt(1) == 'u') {
					dayOfTheWeek = DaysOfTheWeek.TUESDAY;
				} else {
					dayOfTheWeek = DaysOfTheWeek.THURSDAY;
				}
				break;
			case 'W':
				dayOfTheWeek = DaysOfTheWeek.WEDNESDAY;
				break;
			case 'F':
				dayOfTheWeek = DaysOfTheWeek.FRIDAY;
				break;
			case 'S':
				// S can be either Saturday or Sunday
				if (weekday.charAt(1) == 'a') {
					dayOfTheWeek = DaysOfTheWeek.SATURDAY;
				} else {
					dayOfTheWeek = DaysOfTheWeek.SUNDAY;
				}
				break;
		}

		// create the different variables for the calendar date
		String[] dateSplit = calendarDate.split("/");
		int month = Integer.parseInt(dateSplit[0]);
		int day = Integer.parseInt(dateSplit[1]);
		int year = Integer.parseInt(dateSplit[2]);

		return new Date(dayOfTheWeek, month, day, year);
	}

	/**
	 * Creates a new Time instance with the given entry.
	 * 
	 * @param entry the time in the format "HH:MM AM/PM"
	 * @return Time instance with properties from entry
	 */
	private Time createTime(String entry) {
		String[] entrySplit = entry.split(":\\s+");
		int hour = Integer.parseInt(entrySplit[0]);
		int minutes = Integer.parseInt(entrySplit[1]);
		String ampm = entrySplit[2];

		return new Time(hour, minutes, ampm);
	}
}