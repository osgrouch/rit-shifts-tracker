package tracker.shifts;

import tracker.datetime.CalendarDate;
import tracker.datetime.Time;

/**
 * Abstract class representing a shift worked for RIT Dining.
 * Keeps track of the day worked, time clocked in, time clocked out,
 * place worked and the job worked.
 * Child classes are expected to implement a Job enum to track
 * the exact job worked during the shift.
 */
public abstract class Shift {
	/** Day worked */
	private final CalendarDate date;

	/** Time clocked in */
	private final Time in;
	/** Time clocked out */
	private final Time out;

	/** Hourly pay rate of the shift, typically $14/hour but can change due to special circumstances */
	private final int payRate;

	/**
	 * Create a new shift and set its starting and ending times.
	 *
	 * @param calendarDate the exact calendar date worked in the format "MM/DD/YYYY"
	 * @param clockIn      the time clocked in, in the format "hh:mm AM/PM" or "HH:MM"
	 * @param clockOut     the time clocked out, in the format "hh:mm AM/PM" or "HH:MM"
	 * @param rate         the hourly pay rate
	 */
	public Shift (String calendarDate, String clockIn, String clockOut, int rate) {
		// create an instance of Date corresponding to this shift
		this.date = new CalendarDate(calendarDate);

		// set the clock in and clock out times
		this.in = new Time(clockIn);
		this.out = new Time(clockOut);

		// set the pay rate of this shift
		this.payRate = rate;
	}

	/**
	 * @return double value of total time worked
	 */
	public double calcTotalHours () {
		return Time.difference(in, out);
	}

	/**
	 * @return the hourly wage for this shift
	 */
	public int getPayRate () {
		return payRate;
	}

	/**
	 * @return human-readable paragraph with all the information stored in this
	 * shift
	 */
	@Override
	public String toString () {
		String shift = "Shift:\n";

		shift += ( "\t" + date.toString() + "\n" );
		shift += ( "\t" + in.toString() + " - " + out.toString() + "\n" );

		return shift;
	}
}
