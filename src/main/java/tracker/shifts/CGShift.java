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
	 * the parent Shift class' constructor and setting the job worked at the CG.
	 *
	 * @param calendarDate the exact calendar date worked in the format "MM/DD/YYYY" or "MMM DD, YYYY"
	 * @param clockIn      the time clocked in, in the format "hh:mm AM/PM" or "HH:MM"
	 * @param clockOut     the time clocked out, in the format "hh:mm AM/PM" or "HH:MM"
	 * @param rate         the hourly pay rate
	 * @param job          the job worked
	 */
	public CGShift (String calendarDate, String clockIn, String clockOut, int rate, String job) {
		super(calendarDate, clockIn, clockOut, rate);
		this.job = Enum.valueOf(Job.class, job);
	}

	/**
	 * @return CGShift.Job enum instance
	 */
	public Job getJob () {
		return job;
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
		CASHIER, DINING, FLEX, FRYER, GRILLE, KDS, PREP, SALSARITAS, UTILITY;
	}
}
