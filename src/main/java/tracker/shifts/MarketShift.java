package tracker.shifts;

/**
 * Class representing a shift worked at The Market at Global Village (Market).
 * Extends Shift and adds jobs specific to the Market.
 */
public class MarketShift extends Shift {
	/** The job worked during the shift */
	private final Job job;

	/**
	 * Create a new MarketShift and set its information by calling
	 * the parent Shift class' constructor.
	 *
	 * @param calendarDate the exact calendar date worked in the format "MM/DD/YYYY"
	 * @param clockIn      the time clocked in, in the format "hh:mm AM/PM" or "HH:MM"
	 * @param clockOut     the time clocked out, in the format "hh:mm AM/PM" or "HH:MM"
	 * @param rate         the hourly pay rate
	 * @param jobChoice    a number representing the job worked
	 */
	public MarketShift (String calendarDate, String clockIn, String clockOut, int rate, int jobChoice) {
		super(calendarDate, clockIn, clockOut, rate);
		this.job = Job.valueOf(jobChoice);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString () {
		return "Market " + job.name() + " " + super.toString();
	}

	/** Enum representing the jobs available at the Market */
	public static enum Job {
		CASHIER(1),
		STOCKER(2),
		UTILITY(3);

		/** Number representing the job worked */
		private final int code;

		/**
		 * Create a Job enum element with the given code.
		 *
		 * @param code the code representing the job
		 */
		private Job (int code) {
			this.code = code;
		}

		/**
		 * Find the job value of the given number by iterating through the
		 * elements in the enum until a matching code is found,
		 * assumes the given argument is in the valid range of [1, 3].
		 *
		 * @param num the number of the job to find
		 * @return the job
		 */
		public static Job valueOf (int num) {
			Job value = null;

			for (Job current : values()) {
				if (num == current.getCode()) {
					value = current;
					break;
				}
			}

			return value;
		}

		/**
		 * @return the job's code
		 */
		public int getCode () {
			return code;
		}
	}
}

