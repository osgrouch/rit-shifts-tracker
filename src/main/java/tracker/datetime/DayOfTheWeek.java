package tracker.datetime;

/** Enum representing the seven days of the week */
public enum DayOfTheWeek {
	SUNDAY(1), MONDAY(2), TUESDAY(3), WEDNESDAY(4), THURSDAY(5), FRIDAY(6), SATURDAY(7);

	/** Number representing the day of the week */
	private final int code;

	/**
	 * Create a DayOfTheWeek enum element with the given code.
	 *
	 * @param code the code representing the day of the week
	 */
	private DayOfTheWeek (int code) {
		this.code = code;
	}

	/**
	 * Find the DayOfTheWeek value of the given number by iterating through the
	 * elements in the enum until a matching code is found,
	 * assumes the given argument is in the valid range of [1, 7].
	 *
	 * @param num the number of the DayOfTheWeek to find
	 * @return the DayOfTheWeek
	 */
	public static DayOfTheWeek valueOfCode (int num) {
		DayOfTheWeek value = null;

		for (DayOfTheWeek current : values()) {
			if (num == current.getCode()) {
				value = current;
				break;
			}
		}

		return value;
	}

	/**
	 * @return the day of the week's code
	 */
	public int getCode () {
		return code;
	}
}
