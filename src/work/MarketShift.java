package src.work;

/**
 * Class representing a shift worked at
 * The Market at Global Village (Market).
 * Extends Shift and adds jobs specific to the Market.
 */
public class MarketShift extends Shift {
	/** Enum representing the jobs available at the Market */
	public static enum Job {
		CASHIER(1),
		STOCKER(2),
		UTILITY(3);

		/** Number representing the job worked */
		private int code;

		/**
		 * Create a Job enum element with the given code.
		 * 
		 * @param code the code representing the job
		 */
		private Job(int code) {
			this.code = code;
		}

		/**
		 * @return the job's code
		 */
		public int getCode() {
			return code;
		}

		/**
		 * Find the job value of the given number by iterating through the
		 * elements in the enum until a matching code is found,
		 * assumes the given argument is in the valid range of [1, 3].
		 * 
		 * @param num the number of the job to find
		 * @return the job
		 */
		public static Job valueOfCode(int num) {
			Job value = null;

			for (Job current : values()) {
				if (num == current.getCode()) {
					value = current;
					break;
				}
			}

			return value;
		}
	}

	/** The job worked during the shift */
	private Job job;

	/**
	 * Create a new MarketShift and set its information by calling
	 * the parent Shift class' constructor.
	 * 
	 * @param args an array of strings with the different elements
	 *             each representing a different piece of information about the
	 *             shift to create
	 */
	public MarketShift(String[] args) {
		super(args);
		this.job = Job.valueOfCode(Integer.parseInt(args[4]));
	}

	/**
	 * @return the job worked during this shift
	 */
	public Job getJob() {
		return job;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Market " + job.name() + " " + super.toString();
	}
}
