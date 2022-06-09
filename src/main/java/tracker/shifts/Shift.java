package tracker.shifts;

import tracker.datetime.CalendarDate;
import tracker.datetime.Time;

import java.util.Map;
import java.util.Objects;

/**
 * Abstract class representing a shift worked for RIT Dining. Keeps track of the day worked,
 * time clocked in, time clocked out, place worked and the job worked.
 * Children classes are expected to implement createJSONObject method.
 */
public abstract class Shift {
	/** Map of the names of the different children class of this class, has to be updated manually */
	public static final Map<Integer, String> locations = Map.of(1, "MARKET", 2, "CANTINA-GRILLE");

	/** Day worked */
	private final CalendarDate date;

	/** Time clocked in */
	private final Time in;
	/** Time clocked out */
	private final Time out;

	/** Hourly pay rate of the shift, typically $14/hour but can change due to special circumstances */
	private final int payRate;

	/**
	 * Create a new Shift and set its starting and ending times.
	 *
	 * @param calendarDate the exact calendar date worked in the format "MM/DD/YYYY" or "MMM DD, YYYY"
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
	 * @return {@link CalendarDate} instance of the date worked
	 */
	public CalendarDate getDate () {
		return date;
	}

	/**
	 * @return {@link Time} instance of the time clocked in
	 */
	public Time getIn () {
		return in;
	}

	/**
	 * @return {@link Time} instance of the time clocked out
	 */
	public Time getOut () {
		return out;
	}

	/**
	 * @return int value of the amount paid per hour
	 */
	public int getPayRate () {
		return payRate;
	}

	/**
	 * @return hash code of this Shift instance
	 */
	@Override public int hashCode () {
		return Objects.hash(date, in, out, payRate);
	}

	/**
	 * Check if this object is equal to the given object. If they are both Shift objects,
	 * compare their date, in, out and payRate values.
	 *
	 * @param o object to compare to
	 * @return true iff both are Shift objects with the same private fields, else false
	 */
	@Override public boolean equals (Object o) {
		boolean result = false;
		if (o instanceof Shift) {
			Shift other = (Shift) o;
			result = this.date.equals(other.date) && this.in.equals(other.in) && this.out.equals(other.out) &&
				( this.payRate == other.payRate );
		}
		return result;
	}

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
