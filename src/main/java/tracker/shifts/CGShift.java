package tracker.shifts;

import java.util.Objects;

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
	@Override public int hashCode () {
		return Objects.hash(super.hashCode(), job);
	}

	/**
	 * Check if this object is equal to the given object. If they are both Shift objects,
	 * compare their date, in, out and payRate values. Then, if they are both CGShift objects,
	 * compare their job values.
	 *
	 * @param o object to compare to
	 * @return true iff both are CGShift objects with the same private fields, else false
	 */
	@Override public boolean equals (Object o) {
		boolean result = super.equals(o);
		if (result && o instanceof CGShift) {
			CGShift other = (CGShift) o;
			result = this.job.name().equals(other.job.name());
		} else if (result) {
			// Shifts have the same information but different locations
			result = false;
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public String toString () {
		return "Cantina and Grille " + job.name() + " " + super.toString();
	}

	/** Enum representing the jobs available at the C&G */
	public static enum Job {
		CASHIER, DINING, FLEX, FRYER, GRILLE, KDS, PREP, SALSARITAS, UTILITY;
	}
}
