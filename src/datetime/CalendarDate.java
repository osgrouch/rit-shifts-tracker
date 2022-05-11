package src.datetime;

/**
 * Class representing a calendar date,
 * also tracking what day of the week the date was..
 */
public class CalendarDate {
	/** Enum representing the seven days of the week */
	public static enum DayOfTheWeek {
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
		private DayOfTheWeek(int code) {
			this.code = code;
		}

		/**
		 * @return the day of the week's code
		 */
		public int getCode() {
			return code;
		}

		/**
		 * Find the DayOfTheWeek value of the given number by iterating through the
		 * elements in the enum until a matching code is found,
		 * assumes the given argument is in the valid range of [1, 7].
		 * 
		 * @param num the number of the DayOfTheWeek to find
		 * @return the DayOfTheWeek
		 */
		public static DayOfTheWeek valueOfCode(int num) {
			DayOfTheWeek value = null;

			for (DayOfTheWeek current : values()) {
				if (num == current.getCode()) {
					value = current;
					break;
				}
			}

			return value;
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

		/**
		 * Find the month value of the given number by iterating through the
		 * elements in the enum until a matching code is found,
		 * assumes the given argument is in the valid range of [1, 12].
		 * 
		 * @param num the number of the month to find
		 * @return the month
		 */
		public static Month valueOfCode(int num) {
			Month value = null;

			for (Month current : values()) {
				if (num == current.getCode()) {
					value = current;
					break;
				}
			}

			return value;
		}
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
}
