package tracker.datetime;

/** Class representing a calendar date */
public class CalendarDate {
	/** The month as a 3-letter code */
	private final Month month;
	/** The date of the month */
	private final int day;
	/** The year in the form YYYY */
	private final int year;

	/**
	 * Creates a new CalendarDate instance with the month, day and year provided.
	 *
	 * @param month the month
	 * @param day   the day
	 * @param year  the year
	 */
	public CalendarDate (int month, int day, int year) {
		// find the Month value of the given month number
		this.month = Month.valueOf(month);
		this.day = day;
		this.year = year;
	}

	/**
	 * Create a new CalendarDate instance with the given date.
	 * Determines in what of the two valid formats the date is and then sets this instance's fields.
	 * Will not error check the date given.
	 *
	 * @param date a string in the format MM/DD/YYYY or MMM DD, YYYY
	 */
	public CalendarDate (String date) {
		Month monthVal = null;
		int dayVal = -1;
		int yearVal = -1;
		try {
			// date in the format MM/DD/YYYY
			int[] dateSplit = CalendarDate.splitDateIntoInt(date);
			monthVal = Month.valueOf(dateSplit[0]);
			dayVal = dateSplit[1];
			yearVal = dateSplit[2];
		} catch (IllegalArgumentException e) {
			// thrown by calling splitDateIntoInt method with argument in incorrect format,
			// indicating the date is in the format MMM DD, YYYY
			String[] dateSplit = date.split("\\s+");
			// find the month enum value of the first element
			monthVal = Enum.valueOf(Month.class, dateSplit[0]);
			// remove the comma from the second element
			dayVal = Integer.parseInt(dateSplit[1].substring(0, 2));
			yearVal = Integer.parseInt(dateSplit[2]);
		} finally {
			this.month = monthVal;
			this.day = dayVal;
			this.year = yearVal;
		}
	}

	/**
	 * Compare two given CalendarDates by year, month and date.
	 *
	 * @param one the first CalendarDate to compare
	 * @param two the second CalendarDate to compare
	 * @return -1 if one < two,
	 * 0 if equal,
	 * 1 if one > two
	 */
	public static int compare (CalendarDate one, CalendarDate two) {
		int result = Integer.compare(one.getYear(), two.getYear());

		if (result == 0) {
			result = Month.compare(one.getMonth(), two.getMonth());

			if (result == 0) {
				result = Integer.compare(one.getDay(), two.getDay());
			}
		}

		return result;
	}

	/**
	 * Split the given date String into an array of ints.
	 *
	 * @param date the date in the format MM/DD/YYYY
	 * @return int array of {MM, DD, YYYY}
	 * @throws IllegalArgumentException when the date given is not in the format MM/DD/YYYY
	 */
	public static int[] splitDateIntoInt (String date) throws IllegalArgumentException {
		try {
			String[] dateArr = date.split("/");
			int month = Integer.parseInt(dateArr[0]);
			int day = Integer.parseInt(dateArr[1]);
			int year = Integer.parseInt(dateArr[2]);
			return new int[]{ month, day, year };
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Evaluate if the calendar date stored by this object is valid.
	 * {@code Days} must be in the correct range based on the {@code month} value.
	 * {@code Year} value is only checked to be positive.
	 *
	 * @return true iff month, day and year values are in the valid range
	 */
	public boolean isValid () {
		if (month == null) {
			// if month was not set, invalid month value was passed
			// when creating this instance, therefore this instance is not valid
			return false;
		}
		// check month is in the positive valid range
		boolean result = ( month.getCode() > 0 ) && ( month.getCode() < 13 );

		if (result) {
			// only check date if month is valid
			int maxDays = 31;
			switch (month.getCode()) {
				case 2:
					maxDays = 29;
					break;
				case 4:
				case 6:
				case 9:
				case 11:
					maxDays = 30;
			}
			if (( day < 0 ) || ( day > maxDays )) {
				// check day is in the valid positive range
				result = false;
			}
			if (result && ( year < 0 )) {
				// check year is positive
				result = false;
			}
		}

		return result;
	}

	public Month getMonth () {
		return month;
	}

	public int getDay () {
		return day;
	}

	public int getYear () {
		return year;
	}

	/**
	 * @return a human-readable String of the date stored in this instance in the format "MMM DD, YYYY"
	 */
	@Override
	public String toString () {
		// add the 0 in front of the date for pretty printing
		String dateStr;
		if (day < 10) {
			dateStr = "0" + day;
		} else {
			dateStr = String.valueOf(day);
		}
		return month.name() + " " + dateStr + ", " + year;
	}
}
