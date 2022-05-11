package src.work;

/**
 * Class representing a shift worked at the
 * Global Village Cantina and Grille (C&G).
 * Extends Shift and adds jobs specific to the C&G.
 */
public class CGShift extends Shift {
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
		 * assumes the given argument is in the valid range of [1, 9].
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
	 * Create a new CGShift and set its information by calling
	 * the parent Shift class' constructor
	 * 
	 * @param args an array of strings with the different elements
	 *             each representing a different piece of information about the
	 *             shift to create
	 */
	public CGShift(String[] args) {
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
		return "Cantina and Grille " + job.name() + " " + super.toString();
	}
}
