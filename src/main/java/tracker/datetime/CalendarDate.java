package tracker.datetime;

/** Class representing a calendar date */
public class CalendarDate {
	/** The month as a 3-letter code */
	private final Month month;
	/** The date of the month */
	private final int date;
	/** The year in the form YYYY */
	private final int year;

	/**
	 * Creates a new instance of Date with the information provided.
	 *
	 * @param date the exact calendar date worked in the format "MM/DD/YYYY"
	 */
	public CalendarDate (String date) {
		// create the different variables for the calendar date
		// by splitting the date given to "MM" "DD" "YYYY"
		String[] dateSplit = date.split("-:|/");
		// find the Month value of the given month number
		this.month = Month.valueOf(Integer.parseInt(dateSplit[0]));
		this.date = Integer.parseInt(dateSplit[1]);
		this.year = Integer.parseInt(dateSplit[2]);
	}

	/**
	 * Evaluate if the calendar date stored by this object is valid.
	 * {@code Month} must be in the range [0, 12] and {@code days} must be in the correct range,
	 * depending on the value of the month. {@code Year} value is not checked.
	 *
	 * @return true iff month and day values are in the valid range
	 */
	public boolean isValid () {
		boolean result = month.getCode() < 13;

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
			if (date > maxDays) {
				result = false;
			}
		}

		return result;
	}

	/**
	 * @return a human-readable String of the date stored in this instance in the format "MM/DD/YYYY"
	 */
	@Override
	public String toString () {
		int monthNum = month.getCode();
		String monthStr;
		if (monthNum < 10) {
			monthStr = "0" + monthNum;
		} else {
			monthStr = String.valueOf(monthNum);
		}
		String dateStr;
		if (date < 10) {
			dateStr = "0" + date;
		} else {
			dateStr = String.valueOf(date);
		}
		return monthStr + "/" + dateStr + "/" + year;
	}
}
