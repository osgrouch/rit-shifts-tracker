package tracker.shifts;

/**
 * Class representing a shift worked at The Market at Global Village (Market).
 * Extends {@link Shift} and adds jobs specific to the Market through an enum.
 */
public class MarketShift extends Shift {
	/** The job worked during the shift */
	private final Job job;

	/**
	 * Create a new MarketShift and set its information by calling
	 * the parent Shift class' constructor and setting the job worked at the Market.
	 *
	 * @param calendarDate the exact calendar date worked in the format "MM/DD/YYYY" or "MMM DD, YYYY"
	 * @param clockIn      the time clocked in, in the format "hh:mm AM/PM" or "HH:MM"
	 * @param clockOut     the time clocked out, in the format "hh:mm AM/PM" or "HH:MM"
	 * @param rate         the hourly pay rate
	 * @param job          the job worked
	 */
	public MarketShift (String calendarDate, String clockIn, String clockOut, int rate, String job) {
		super(calendarDate, clockIn, clockOut, rate);
		this.job = Enum.valueOf(Job.class, job);
	}

	/**
	 * @return MarketShift.Job enum instance
	 */
	public Job getJob () {
		return job;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public String toString () {
		return "Market " + job.name() + " " + super.toString();
	}

	/** Enum representing the jobs available at the Market */
	public static enum Job {
		CASHIER, STOCKER, UTILITY;
	}
}
