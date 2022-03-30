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
	 * @param args an array of strings with the different elements each representing
	 *             a different piece of information about the shift to create
	 */
	public MarketShift(String[] args) {
		super(args);

		int jobChoice = Integer.parseInt(args[4]);
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
	 * @return the job worked during this shift
	 */
	public Jobs getJob() {
		return job;
	}
}
