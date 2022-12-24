package tracker.shifts;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.Objects;

/**
 * Class representing a shift worked for RIT Dining.
 * Keeps track of the day worked, time clocked in, time clocked out, rate paid per hour, and the place worked.
 */
public class Shift implements Comparable<Shift> {
	/** Location worked at. */
	private final String location;

	/** Day worked. */
	private final LocalDate date;

	/** Time clocked in. */
	private final LocalTime in;
	/** Time clocked out. */
	private final LocalTime out;

	/** Hourly pay rate. */
	private final int payRate;

	/**
	 * Create a new Shift.
	 *
	 * @param location Location worked at.
	 * @param date     Date worked in the format <code>MM/DD/YYYY</code>.
	 * @param clockIn  Time clocked in, in the format <code>HH:MM AM/PM</code>.
	 * @param clockOut Time clocked out, in the format <code>HH:MM AM/PM</code>.
	 * @param payRate  Hourly pay rate.
	 */
	public Shift(String location, String date, String clockIn, String clockOut, int payRate) {
		this.location = location;
		this.date = LocalDate.parse(
			date,
			new DateTimeFormatterBuilder()
				.parseCaseInsensitive()
				.appendPattern("M/d/u")
				.toFormatter(Locale.US)
		);

		DateTimeFormatter parseTimeFormat = new DateTimeFormatterBuilder()
			.parseCaseInsensitive()
			.appendPattern("h:mm a")
			.toFormatter(Locale.US);
		this.in = LocalTime.parse(clockIn, parseTimeFormat);
		this.out = LocalTime.parse(clockOut, parseTimeFormat);

		this.payRate = payRate;
	}

	/**
	 * @return Total time worked.
	 */
	public double calcTotalHours() {
		double hours = Duration.between(in, out).toHours();
		System.out.println(hours);
		return hours;
	}

	/**
	 * @return Total amount earned.
	 */
	public double calcPay() {
		return calcTotalHours() * payRate;
	}

	/**
	 * @return Location worked at.
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @return Date worked in the format <code>MMM DD, YYYY</code>.
	 */
	public String getDate() {
		return date.format(
			new DateTimeFormatterBuilder()
				.parseCaseInsensitive()
				.appendPattern("MMM dd, u")
				.toFormatter(Locale.US)
		);
	}

	/**
	 * @return Time clocked in, in the format <code>HH:MM AM/PM</code>.
	 */
	public String getIn() {
		return in.format(
			new DateTimeFormatterBuilder()
				.parseCaseInsensitive()
				.appendPattern("hh:mm a")
				.toFormatter(Locale.US)
		);
	}

	/**
	 * @return Time clocked out, in the format <code>HH:MM AM/PM</code>.
	 */
	public String getOut() {
		return out.format(
			new DateTimeFormatterBuilder()
				.parseCaseInsensitive()
				.appendPattern("hh:mm a")
				.toFormatter(Locale.US)
		);
	}

	/**
	 * @return Hourly pay rate.
	 */
	public int getPayRate() {
		return payRate;
	}

	/**
	 * Compare this Shift with the given Shift by comparing their dates and times clocked in.
	 *
	 * @param o Shift to compare to, not null.
	 * @return -1 if this Shift is before the given Shift,<br>
	 * 0 if both Shifts are at the same date and time,<br>
	 * 1 if this Shift is after the given Shift.
	 */
	@Override
	public int compareTo(Shift o) {
		int dateComparison = date.compareTo(o.date);
		if (dateComparison == 0) {
			return in.compareTo(o.in);
		}
		return dateComparison;
	}

	/**
	 * @return Hash code of this Shift instance.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(location, date, in, out, payRate);
	}

	/**
	 * Check if this object is equal to the given object.
	 * If they are both Shift objects, compare their locations, dates, times clocked in and out, and pay rate values.
	 *
	 * @param o Object to compare to.
	 * @return True iff given object is a Shift object with the same private field values as this object.
	 */
	@Override
	public boolean equals(Object o) {
		boolean result = false;
		if (o instanceof Shift other) {
			result = this.location.equalsIgnoreCase(other.location)
				&& this.date.equals(other.date)
				&& this.in.equals(other.in)
				&& this.out.equals(other.out)
				&& (this.payRate == other.payRate);
		}
		return result;
	}
}
