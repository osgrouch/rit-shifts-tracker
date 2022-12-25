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
	 * @param location   Location worked at.
	 * @param date       Date worked.
	 * @param clockIn    Time clocked in.
	 * @param clockOut   Time clocked out.
	 * @param payRate    Hourly pay rate.
	 * @param dateFormat Format to use to parse the given date using {@link DateTimeFormatterBuilder#appendPattern(String)}.<br>
	 *                   If <code>null</code>, will parse date with default {@link LocalDate} format.
	 * @param timeFormat Format to use to parse the given times using {@link DateTimeFormatterBuilder#appendPattern(String)}.<br>
	 *                   If <code>null</code>, will parse times with default {@link LocalTime} format.
	 */
	public Shift(String location, String date, String clockIn, String clockOut, int payRate,
	             String dateFormat, String timeFormat) {
		this.location = location;
		this.payRate = payRate;

		LocalDate parsedDate;
		if (dateFormat != null) {
			parsedDate = LocalDate.parse(
				date,
				new DateTimeFormatterBuilder()
					.parseCaseInsensitive()
					.appendPattern(dateFormat)
					.toFormatter(Locale.US)
			);
		} else {
			parsedDate = LocalDate.parse(date);
		}
		this.date = parsedDate;

		LocalTime parsedIn;
		LocalTime parsedOut;
		if (timeFormat != null) {
			parsedIn = LocalTime.parse(
				clockIn,
				new DateTimeFormatterBuilder()
					.parseCaseInsensitive()
					.appendPattern(timeFormat)
					.toFormatter(Locale.US)
			);
			parsedOut = LocalTime.parse(
				clockOut,
				new DateTimeFormatterBuilder()
					.parseCaseInsensitive()
					.appendPattern(timeFormat)
					.toFormatter(Locale.US)
			);
		} else {
			parsedIn = LocalTime.parse(clockIn);
			parsedOut = LocalTime.parse(clockOut);
		}
		this.in = parsedIn;
		this.out = parsedOut;
	}

	/**
	 * @return Total number of hours worked.
	 */
	public double calcTotalHours() {
		return (double) Duration.between(in, out).toHours();
	}

	/**
	 * @return Total amount of money earned.
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
	 * @return Date worked in the format <code>YYYY-MM-DD</code>.
	 */
	public String getDate() {
		return date.toString();
	}

	/**
	 * @return Time clocked in, in the format <code>HH:MM</code>.
	 */
	public String getIn() {
		return in.toString();
	}

	/**
	 * @return Time clocked out, in the format <code>HH:MM</code>.
	 */
	public String getOut() {
		return out.toString();
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

	/**
	 * @return Human-readable String with information about this shift.
	 */
	@Override
	public String toString() {
		DateTimeFormatter dateFormat = new DateTimeFormatterBuilder()
			.appendPattern("MMM dd, u")
			.toFormatter();
		DateTimeFormatter timeFormat = new DateTimeFormatterBuilder()
			.appendPattern("hh:mm a")
			.toFormatter();

		String shift = (location + " Shift:\n");
		shift += ("\t" + date.format(dateFormat) + "\n");
		shift += ("\t" + in.format(timeFormat) + " - " + out.format(timeFormat) + "\n");
		return shift;
	}
}
