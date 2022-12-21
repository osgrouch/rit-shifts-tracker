package tracker.shifts;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Abstract class representing a shift worked for RIT Dining. Keeps track of the day worked,
 * time clocked in, time clocked out, place worked and the job worked.
 * Children classes are expected to implement createJSONObject method.
 */
public abstract class Shift implements Comparable<Shift> {
	/** Day worked */
	private final LocalDate date;

	/** Time clocked in */
	private final LocalTime in;
	/** Time clocked out */
	private final LocalTime out;

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
	public Shift(String calendarDate, String clockIn, String clockOut, int rate) {
		// create an instance of Date corresponding to this shift
		this.date = LocalDate.parse(calendarDate);
		// set the clock in and clock out times
		this.in = LocalTime.parse(clockIn);
		this.out = LocalTime.parse(clockOut);
		// set the pay rate
		this.payRate = rate;
	}

	/**
	 * @return double value of total time worked
	 */
	public double calcTotalHours() {
		return Duration.between(in, out).toHours();
	}

	/**
	 * @return {@link LocalDate} instance of the date worked
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * @return {@link LocalTime} instance of the time clocked in
	 */
	public LocalTime getIn() {
		return in;
	}

	/**
	 * @return {@link LocalTime} instance of the time clocked out
	 */
	public LocalTime getOut() {
		return out;
	}

	/**
	 * @return int value of the amount paid per hour
	 */
	public int getPayRate() {
		return payRate;
	}

	/**
	 * Compare this Shift with the given Shift by comparing their dates and times clocked in.
	 *
	 * @param o Shift to compare to, not null
	 * @return -1 if this Shift is before the given Shift,<br>
	 * 0 if both Shifts are at the same date and time,<br>
	 * 1 if this Shift is after the given Shift
	 */
	@Override
	public int compareTo(Shift o) {
		int dateComparison = date.compareTo(o.getDate());
		if (dateComparison == 0) {
			return in.compareTo(o.getIn());
		}
		return dateComparison;
	}

	/**
	 * @return hash code of this Shift instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(date, in, out, payRate);
	}

	/**
	 * Check if this object is equal to the given object. If they are both Shift objects,
	 * compare their date, in, out and payRate values.
	 *
	 * @param o object to compare to
	 * @return true iff both are Shift objects with the same private fields, else false
	 */
	@Override
	public boolean equals(Object o) {
		boolean result = false;
		if (o instanceof Shift) {
			Shift other = (Shift) o;
			result = this.date.equals(other.date) && this.in.equals(other.in) && this.out.equals(other.out) &&
				(this.payRate == other.payRate);
		}
		return result;
	}

	/**
	 * @return human-readable paragraph with all the information stored in this Shift
	 */
	@Override
	public String toString() {
		String shift = "Shift:\n";
		shift += ("\t" + date.toString() + "\n");
		shift += ("\t" + in.toString() + " - " + out.toString() + "\n");
		return shift;
	}
}
