package tracker.shifts;

/**
 * Class representing a shift worked at the Global Village Cantina and Grille (C&G).
 * Extends {@link Shift} and adds jobs specific to the C&G through an enum.
 */
public class CGShift extends Shift {
	/** The job worked during the shift */
	private final Job job;

	/**
	 * Create a new CGShift and set its information by calling
	 * the parent Shift class' constructor.
	 *
	 * @param calendarDate the exact calendar date worked in the format "MM/DD/YYYY"
	 * @param clockIn      the time clocked in, in the format "hh:mm AM/PM" or "HH:MM"
	 * @param clockOut     the time clocked out, in the format "hh:mm AM/PM" or "HH:MM"
	 * @param rate         the hourly pay rate
	 * @param jobChoice    a number representing the job worked
	 */
	public CGShift (String calendarDate, String clockIn, String clockOut, int rate, int jobChoice) {
		super(calendarDate, clockIn, clockOut, rate);
		this.job = Job.valueOf(jobChoice);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString () {
		return "Cantina and Grille " + job.name() + " " + super.toString();
	}

	/** Enum representing the jobs available at the C&G */
	public static enum Job {
		CASHIER(1),
		DINING(2),
		FLEX(3),
		FRYER(4),
		GRILLE(5),
		KDS(6),
		PREP(7),
		SALSARITAS(8),
		UTILITY(9);

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
		 * assumes the given argument is in the valid range of [1, 9].
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