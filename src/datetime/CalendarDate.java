package src.datetime;

/**
 * Class representing a calendar date,
 * also tracking what day of the week the date was..
 */
public class CalendarDate {
	/** Enum representing the seven days of the week */
	public static enum DaysOfTheWeek {
		SUNDAY,
		MONDAY,
		TUESDAY,
		WEDNESDAY,
		THURSDAY,
		FRIDAY,
		SATURDAY
	}

	/** Enum representing the twelve months in a year */
	public static enum Month {
		JAN,
		FEB,
		MAR,
		APR,
		MAY,
		JUN,
		JUL,
		AUG,
		SEP,
		OCT,
		NOV,
		DEC
	}

	/** The day of the week */
	private DaysOfTheWeek dayOfTheWeek;
	/** The month */
	private Month month;
	/** The day */
	private int day;
	/** The year in the form YYYY */
	private int year;

	/**
	 * Creates a new instance of Date with the information provided.
	 * 
	 * @param weekday      the first letter of the day of the week worked
	 * @param calendarDate the exact calendar date worked in the format "MM/DD/YYYY"
	 */
	public CalendarDate(char weekday, String calendarDate) {
		switch (weekday) {
			// catches repeating first letters by assigning
			// Thursday as R and Sunday as U
			case 'M':
				this.dayOfTheWeek = DaysOfTheWeek.MONDAY;
				break;
			case 'T':
				this.dayOfTheWeek = DaysOfTheWeek.TUESDAY;
				break;
			case 'W':
				this.dayOfTheWeek = DaysOfTheWeek.WEDNESDAY;
				break;
			case 'R':
				this.dayOfTheWeek = DaysOfTheWeek.THURSDAY;
				break;
			case 'F':
				this.dayOfTheWeek = DaysOfTheWeek.FRIDAY;
				break;
			case 'S':
				this.dayOfTheWeek = DaysOfTheWeek.SATURDAY;
				break;
			case 'U':
				this.dayOfTheWeek = DaysOfTheWeek.SUNDAY;
				break;
		}

		// create the different variables for the calendar date
		// by splitting the date given to "MM" "DD" "YYYY"
		String[] dateSplit = calendarDate.split("/");
		int monthNum = Integer.parseInt(dateSplit[0]);
		this.day = Integer.parseInt(dateSplit[1]);
		this.year = Integer.parseInt(dateSplit[2]);

		// set the month by matching its corresponding monthNum
		switch (monthNum) {
			case 1:
				this.month = Month.JAN;
				break;
			case 2:
				this.month = Month.FEB;
				break;
			case 3:
				this.month = Month.MAR;
				break;
			case 4:
				this.month = Month.APR;
				break;
			case 5:
				this.month = Month.MAY;
				break;
			case 6:
				this.month = Month.JUN;
				break;
			case 7:
				this.month = Month.JUL;
				break;
			case 8:
				this.month = Month.AUG;
				break;
			case 9:
				this.month = Month.SEP;
				break;
			case 10:
				this.month = Month.OCT;
				break;
			case 11:
				this.month = Month.NOV;
				break;
			case 12:
				this.month = Month.DEC;
				break;
		}
	}

	/**
	 * @return the day of the week
	 */
	public DaysOfTheWeek getDayOfTheWeek() {
		return dayOfTheWeek;
	}

	/**
	 * @return the month in 3 letter code
	 */
	public Month getMonth() {
		return month;
	}

	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @return the year in 4 digits
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return a human readable String of the date stored in this instance
	 */
	@Override
	public String toString() {
		return dayOfTheWeek.name() + " " + month.name() + " " + day + ", " + year;
	}
}
