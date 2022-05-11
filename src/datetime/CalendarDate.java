package src.datetime;

/** Class representing a calendar date, also tracking the day of the week. */
public class CalendarDate {
	/** The day of the week */
	private DayOfTheWeek day;
	/** The month as a 3 letter code */
	private Month month;
	/** The date of the month */
	private int date;
	/** The year in the form YYYY */
	private int year;

	/**
	 * Creates a new instance of Date with the information provided.
	 * 
	 * @param weekday      the first number of the day of the week worked
	 * @param calendarDate the exact calendar date worked in the format "MM/DD/YYYY"
	 */
	public CalendarDate(int weekday, String calendarDate) {
		// find the DayOfTheWeek value of the given day number
		this.day = DayOfTheWeek.valueOfCode(weekday);

		// create the different variables for the calendar date
		// by splitting the date given to "MM" "DD" "YYYY"
		String[] dateSplit = calendarDate.split("/");
		// find the Month value of the given month number
		this.month = Month.valueOfCode(Integer.parseInt(dateSplit[0]));
		this.date = Integer.parseInt(dateSplit[1]);
		this.year = Integer.parseInt(dateSplit[2]);
	}

	/**
	 * @return the day of the week
	 */
	public DayOfTheWeek getDayOfTheWeek() {
		return day;
	}

	/**
	 * @return the month enum value
	 */
	public Month getMonth() {
		return month;
	}

	/**
	 * @return the date
	 */
	public int getDate() {
		return date;
	}

	/**
	 * @return the year in 4 digits
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return a human readable String of the date stored in this instance
	 *         in the format "MM/DD/YYYY"
	 */
	public String compactDate() {
		return month.getCode() + "/" + date + "/" + year;
	}

	/**
	 * @return a human readable String of the date stored in this instance
	 *         in the format "DAY MMM DD, YYYY"
	 */
	@Override
	public String toString() {
		return day.name() + " " + month.name() + " " + date + ", " + year;
	}

	/**
	 * Compare two given CalendarDates by year, month and date.
	 * 
	 * @param one the first CalendarDate to compare
	 * @param two the second CalendarDate to compare
	 * @return -1 if one < two,
	 *         0 if equal,
	 *         1 if one > two
	 */
	public static int compare(CalendarDate one, CalendarDate two) {
		int result = Integer.compare(one.getYear(), two.getYear());

		if (result == 0) {
			result = Month.compare(one.getMonth(), two.getMonth());

			if (result == 0)
				result = Integer.compare(one.getDate(), two.getDate());
		}

		return result;
	}
}
