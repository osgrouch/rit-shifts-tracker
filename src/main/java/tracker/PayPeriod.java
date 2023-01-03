package tracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Class representing the two weeks that count towards a paycheck.
 * Contains a SortedSet of the {@link Shift shifts} worked during the week.
 */
public class PayPeriod {
	/** First day of the pay period, always a Friday. */
	private final LocalDate start;
	/** Last day of the pay period, always a Thursday. */
	private final LocalDate end;

	/** Shifts worked during this pay period, sorted by their date and time. */
	private final SortedSet<Shift> shifts;

	/** Total number of hours worked. */
	private double hours;
	/** Total amount of money earned. */
	private double pay;

	/**
	 * Create a new PayPeriod with the given starting date and calculate the end date,
	 * which will always be 13 days from the starting date.
	 *
	 * @param startDate First day of the pay period.
	 */
	public PayPeriod(String startDate) {
		this(startDate, null);
	}

	/**
	 * Create a new PayPeriod with the given starting date and calculate the end date,
	 * which will always be 13 days from the starting date.
	 *
	 * @param startDate  First day of the pay period.
	 * @param dateFormat Format to use to parse the given date using {@link DateTimeFormatterBuilder#appendPattern(String)}.<br>
	 *                   If <code>null</code>, will parse date with default {@link LocalDate} format.
	 */
	public PayPeriod(String startDate, String dateFormat) {
		this.hours = 0;
		this.pay = 0;
		this.shifts = new TreeSet<>();

		LocalDate parsedStart;
		if (dateFormat != null) {
			parsedStart = LocalDate.parse(
				startDate,
				new DateTimeFormatterBuilder()
					.parseCaseInsensitive()
					.appendPattern(dateFormat)
					.toFormatter(Locale.US)
			);
		} else {
			parsedStart = LocalDate.parse(startDate);
		}
		this.start = parsedStart;
		this.end = start.plusDays(13);
	}

	/**
	 * Create a new PayPeriod with the given starting date and calculate the end date,
	 * which will always be 13 days from the starting date.
	 * Add the given List of Shifts to this PayPeriod, recalculating the total hours and total pay.
	 *
	 * @param startDate  First day of the pay period.
	 * @param dateFormat Format to use to parse the given date using {@link DateTimeFormatterBuilder#appendPattern(String)}.<br>
	 *                   If <code>null</code>, will parse date with default {@link LocalDate} format.
	 * @param shifts     List of Shifts worked this pay period.
	 */
	public PayPeriod(String startDate, String dateFormat, List<Shift> shifts) {
		this(startDate, dateFormat);
		for (Shift shift : shifts) {
			addShift(shift);
		}
	}

	/**
	 * Add a shift to the SortedSet of Shifts worked this pay period.
	 * Increment the total number of hours worked and amount earned.
	 *
	 * @param entry Shift to add.
	 */
	public void addShift(Shift entry) {
		if (shifts.add(entry)) {
			hours += entry.calcTotalHours();
			pay += entry.calcPay();
		}
	}

	/**
	 * Remove a shift from the SortedSet of Shifts worked this pay period.
	 * Decrement the total number of hours worked and amount earned.
	 *
	 * @param departure Shift to remove.
	 */
	public void removeShift(Shift departure) {
		if (shifts.remove(departure)) {
			hours -= departure.calcTotalHours();
			pay -= departure.calcPay();
		}
	}

	/**
	 * @return First day of the pay period in the format <code>YYYY-MM-DD</code>.
	 */
	public String getStart() {
		return start.toString();
	}

	/**
	 * @return Last day of the pay period in the format <code>YYYY-MM-DD</code>.
	 */
	public String getEnd() {
		return end.toString();
	}

	/**
	 * @return Total number of hours worked.
	 */
	public double getHours() {
		return hours;
	}

	/**
	 * @return Total amount of money earned.
	 */
	public double getPay() {
		return pay;
	}

	/**
	 * @return List of {@link Shift shifts} worked this pay period.
	 */
	public List<Shift> getShifts() {
		return List.copyOf(shifts);
	}

	/**
	 * @return Human-readable String with information about this pay period.
	 * Includes every shift worked during this pay period.
	 */
	@Override
	public String toString() {
		DateTimeFormatter dateFormat = new DateTimeFormatterBuilder()
			.appendPattern("MMM dd, u")
			.toFormatter();

		String period = start.format(dateFormat) + " - " + end.format(dateFormat) + "\n";
		period += "\tShifts: " + shifts.size() + "\n";
		period += "\tHours: " + String.format("%.2f", hours) + "\n";
		period += "\tEarned: " + String.format("%.2f", pay) + "\n";
		for (Shift shift : shifts) {
			period += ("\t" + shift.toString() + "\n");
		}
		return period;
	}
}
