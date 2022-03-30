package src.work;

/**
 * Class representing a shift worked at The Market at Global Village (Market).
 * Extends Shift and adds jobs specific to the Market.
 */
public class MarketShift extends Shift {
	/** Enum representing the jobs available at the Market */
	public static enum Jobs {
		CASHIER,
		STOCKER,
		UTILITY
	}

	/** The job worked during the shift */
	private Jobs job;

	/**
	 * Create a new MarketShift and set its information by calling the parent Shift
	 * class' constructor.
	 * 
	 * @param weekday      the first letter of the day of the week worked
	 * @param calendarDate the exact calendar date worked in the format "MM/DD/YYYY"
	 * @param start        the time clocked in, in the format "HH:MM AM/PM"
	 * @param end          the time clocked out, in the format "HH:MM AM/PM"
	 * @param locChoice    the number corresponding to the location worked
	 * @param jobChoice    the number corresponding to the job worked
	 * @param rate         the amount earned per hour of this shift
	 */
	public MarketShift(
			char weekday, String calendarDate,
			String start, String end,
			int jobChoice, int rate) {
		super(weekday, calendarDate, start, end, rate);

		switch (jobChoice) {
			case 1:
				this.job = Jobs.CASHIER;
				break;
			case 2:
				this.job = Jobs.STOCKER;
				break;
			default:
				this.job = Jobs.UTILITY;
				break;
		}
	}

	/**
	 * Create a new MarketShift instance with the default hourly rate of $14/hour.
	 */
	public MarketShift(
			char weekday, String calendarDate,
			String start, String end,
			int jobChoice) {
		this(weekday, calendarDate, start, end, jobChoice, 14);
	}

	/**
	 * @return the job worked during this shift
	 */
	public Jobs getJob() {
		return job;
	}
}