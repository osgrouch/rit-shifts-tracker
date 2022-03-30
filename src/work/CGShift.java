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
	 * @param weekday      the first letter of the day of the week worked
	 * @param calendarDate the exact calendar date worked in the format "MM/DD/YYYY"
	 * @param start        the time clocked in, in the format "HH:MM AM/PM"
	 * @param end          the time clocked out, in the format "HH:MM AM/PM"
	 * @param locChoice    the number corresponding to the location worked
	 * @param jobChoice    the number corresponding to the job worked
	 * @param rate         the amount earned per hour of this shift
	 */
	public CGShift(
			char weekday, String calendarDate,
			String start, String end,
			int jobChoice, int rate) {
		super(weekday, calendarDate, start, end, rate);
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
	 * Create a new CGShift instance with the default hourly rate of
	 * $14/hour
	 */
	public CGShift(
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
