package tracker.shifts;

import com.google.gson.JsonObject;
import tracker.datetime.CalendarDate;
import tracker.datetime.Time;

/**
 * Abstract class representing a shift worked for RIT Dining. Keeps track of the day worked,
 * time clocked in, time clocked out, place worked and the job worked.
 * Children classes are expected to implement createJSONObject method.
 */
public abstract class Shift {
	/** Day worked */
	protected final CalendarDate date;

	/** Time clocked in */
	protected final Time in;
	/** Time clocked out */
	protected final Time out;

	/** Hourly pay rate of the shift, typically $14/hour but can change due to special circumstances */
	protected final int payRate;

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
		// set the pay rate
		this.payRate = rate;
	}

	/**
	 * @return double value of total time worked
	 */
	public double calcTotalHours () {
		return Time.difference(in, out);
	}

	/**
	 * Create a {@link JsonObject GSON JSON Object} out of this Shift instance.
	 *
	 * @return JSON Object representing this instance
	 */
	public abstract JsonObject createJSONObject ();

	/**
	 * @return human-readable paragraph with all the information stored in this Shift
	 */
	@Override
	public String toString () {
		String shift = "Shift:\n";
		shift += ( "\t" + date.toString() + ", " );
		shift += ( in.toString() + " - " + out.toString() + "\n" );
		return shift;
	}
}
