package src.work;

/**
 * Class representing a shift worked at the
 * Global Village Cantina and Grille (C&G).
 * Extends Shift and adds jobs specific to the C&G.
 */
public class CGShift extends Shift {
	/** Enum representing the jobs available at the C&G */
	enum Jobs {
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
		this.job = selectJob(jobChoice);
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
	 * Returns the job worked based on the number given,
	 * which in turn is based on the jobs available for the C&G.
	 * 
	 * @param jobChoice the number corresponding to the job worked
	 * @return the job worked
	 */
	public Jobs selectJob(int jobChoice) {
		Jobs temp;

		switch (jobChoice) {
			case 1:
				temp = Jobs.CASHIER;
				break;
			case 2:
				temp = Jobs.DINING;
				break;
			case 3:
				temp = Jobs.FLEX;
				break;
			case 4:
				temp = Jobs.FRYER;
				break;
			case 5:
				temp = Jobs.GRILLE;
				break;
			case 6:
				temp = Jobs.KDS;
				break;
			case 7:
				temp = Jobs.PREP;
				break;
			case 8:
				temp = Jobs.SALSARITAS;
				break;
			default:
				temp = Jobs.UTILITY;
				break;
		}

		return temp;
	}

	/**
	 * @return the job worked during this shift
	 */
	public Jobs getJob() {
		return job;
	}
}
