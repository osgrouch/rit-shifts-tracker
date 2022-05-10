package src.datetime;

/**
 * Class representing a calendar date,
 * also tracking what day of the week the date was..
 */
public class CalendarDate {
	/** Enum representing the seven days of the week */
	public static enum DaysOfTheWeek {
		SUNDAY(1),
		MONDAY(2),
		TUESDAY(3),
		WEDNESDAY(4),
		THURSDAY(5),
		FRIDAY(6),
		SATURDAY(7);

		/** Number representing the day of the week */
		private final int code;

		/**
		 * Create a DayOfTheWeek enum element with the given code.
		 * 
		 * @param code the code representing the day of the week
		 */
		private DaysOfTheWeek(int code) {
			this.code = code;
		}

		/**
		 * @return the day of the week's code
		 */
		public int getCode() {
			return code;
		}

		/**
		 * Compare the given days of the week by comparing their codes.
		 * 
		 * @param one the first DayOfTheWeek to compare
		 * @param two the second DayOfTheWeek to compare
		 * @return -1 if one < two,
		 *         0 if equal,
		 *         1 if one > two
		 */
		public static int compare(DaysOfTheWeek one, DaysOfTheWeek two) {
			return Integer.compare(one.getCode(), two.getCode());
		}
	}

	/** Enum representing the twelve months in a year */
	public static enum Month {
		JAN(1),
		FEB(2),
		MAR(3),
		APR(4),
		MAY(5),
		JUN(6),
		JUL(7),
		AUG(8),
		SEP(9),
		OCT(10),
		NOV(11),
		DEC(12);

		/** Number representing the month */
		private final int code;

		/**
		 * Create a Month enum element with the given code.
		 * 
		 * @param code the code representing the month
		 */
		private Month(int code) {
			this.code = code;
		}

		/**
		 * @return the month's code
		 */
		public int getCode() {
			return code;
		}

		/**
		 * Compare the given months by comparing their codes.
		 * 
		 * @param one the first Month to compare
		 * @param two the second Month to compare
		 * @return -1 if one < two,
		 *         0 if equal,
		 *         1 if one > two
		 */
		public static int compare(Month one, Month two) {
			return Integer.compare(one.getCode(), two.getCode());
		}
	}

	/**
	 * Compare two given CalendarDates by year, month and day.
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
				result = DaysOfTheWeek.compare(one.getDayOfTheWeek(), two.getDayOfTheWeek());
		}

		return result;
	}

	/** The day of the week */
	private DaysOfTheWeek dayOfTheWeek;
	/** The month as a 3 letter code */
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
		this.day = Integer.parseInt(dateSplit[1]);
		this.year = Integer.parseInt(dateSplit[2]);
	}

	/**
	 * @return the day of the week
	 */
	public DaysOfTheWeek getDayOfTheWeek() {
		return dayOfTheWeek;
	}

	/**
	 * @return the month enum value
	 */
	public Month getMonth() {
		return month;
	}

	/**
	 * @return the month as 3 letter code
	 */
	public String getMonthName() {
		return month.name();
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
	 *         in the format "MM/DD/YYYY"
	 */
	public String compactDate() {
		return month.getCode() + "/" + day + "/" + year;
	}

	/**
	 * @return a human readable String of the date stored in this instance
	 *         in the format "DAY MMM DD, YYYY"
	 */
	@Override
	public String toString() {
		return dayOfTheWeek.name() + " " + month.name() + " " + day + ", " + year;
	}
}
