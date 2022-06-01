package tracker.datetime;

/** Enum representing the twelve months in a year */
public enum Month {
	JAN(1), FEB(2), MAR(3), APR(4), MAY(5), JUN(6), JUL(7), AUG(8),
	SEP(9), OCT(10), NOV(11), DEC(12);

	/** Number representing the month */
	private final int code;

	/**
	 * Create a Month enum element with the given code.
	 *
	 * @param code the code representing the month
	 */
	private Month (int code) {
		this.code = code;
	}

	/**
	 * Compare the given Months by comparing their codes.
	 *
	 * @param one the first Month to compare
	 * @param two the second Month to compare
	 * @return -1 if one < two, 0 if equal, 1 if one > two
	 */
	public static int compare (Month one, Month two) {
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
	public static Month valueOf (int num) {
		Month value = null;
		for (Month current : values()) {
			if (num == current.getCode()) {
				value = current;
				break;
			}
		}
		return value;
	}

	/**
	 * @return the month's code
	 */
	public int getCode () {
		return code;
	}
}
