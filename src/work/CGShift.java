package src.work;

/**
 * Class representing a shift worked at the
 * Global Village Cantina and Grille (C&G).
 * Extends Shift and adds jobs specific to the C&G.
 */
public class CGShift extends Shift {
	/** Enum representing the jobs available at the C&G */
	public static enum Jobs {
		CASHIER,
		DINING,
		FLEX,
		FRYER,
		GRILLE,
		KDS,
		PREP,
		SALSARITAS,
		UTILITY
	}

	/** The job worked during the shift */
	private Jobs job;

	/**
	 * Create a new CGShift and set its information by calling the parent
	 * Shift class' constructor
	 * 
	 * @param args an array of strings with the different elements each representing
	 *             a different piece of information about the shift to create
	 */
	public CGShift(String[] args) {
		super(args);

		int jobChoice = Integer.parseInt(args[4]);
		switch (jobChoice) {
			case 1:
				this.job = Jobs.CASHIER;
				break;
			case 2:
				this.job = Jobs.DINING;
				break;
			case 3:
				this.job = Jobs.FLEX;
				break;
			case 4:
				this.job = Jobs.FRYER;
				break;
			case 5:
				this.job = Jobs.GRILLE;
				break;
			case 6:
				this.job = Jobs.KDS;
				break;
			case 7:
				this.job = Jobs.PREP;
				break;
			case 8:
				this.job = Jobs.SALSARITAS;
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
