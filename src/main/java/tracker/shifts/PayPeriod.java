package tracker.shifts;

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
	 * @param startDate First day of the pay period in the format <code>MM/DD/YYYY</code>.
	 */
	public PayPeriod(String startDate) {
		this.hours = 0;
		this.pay = 0;
		this.shifts = new TreeSet<>();

		DateTimeFormatter parseDateFormat = new DateTimeFormatterBuilder()
			.parseCaseInsensitive()
			.appendPattern("M/d/u")
			.toFormatter(Locale.US);
		this.start = LocalDate.parse(startDate, parseDateFormat);
		this.end = start.plusDays(13);
	}

	/**
	 * Add a shift to the SortedSet of Shifts worked this pay period.
	 * Increment the total number of hours worked and amount earned.
	 *
	 * @param entry Shift to add.
	 */
	public void addShift(Shift entry) {
		shifts.add(entry);
		hours += entry.calcTotalHours();
		pay += entry.calcPay();
	}

	/**
	 * Remove a shift from the SortedSet of Shifts worked this pay period.
	 * Decrement the total number of hours worked and amount earned.
	 *
	 * @param departure Shift to remove.
	 */
	public void removeShift(Shift departure) {
		shifts.remove(departure);
		hours -= departure.calcTotalHours();
		pay -= departure.calcPay();
	}

	/**
	 * @return First day of the pay period in the format <code>MMM DD, YYYY</code>.
	 */
	public String getStart() {
		return start.format(
			new DateTimeFormatterBuilder()
				.parseCaseInsensitive()
				.appendPattern("MMM dd, u")
				.toFormatter(Locale.US)
		);
	}

	/**
	 * @return Last day of the pay period in the format <code>MMM DD, YYYY</code>.
	 */
	public String getEnd() {
		return end.format(
			new DateTimeFormatterBuilder()
				.parseCaseInsensitive()
				.appendPattern("MMM dd, u")
				.toFormatter(Locale.US)
		);
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
	 * @return Human-readable version of the start and end of a pay period,
	 * as well as the total number of hours worked and amount earned
	 * and all the shifts worked during the pay period.
	 */
	public String toStringWithShifts() {
		String period = this.toString();
		for (Shift shift : shifts) {
			period += shift.toString();
		}
		return period;
	}

	/**
	 * @return Human-readable version of the start and end of a pay period,
	 * as well as the total number of hours worked and amount earned.
	 */
	@Override
	public String toString() {
		String period = start.toString() + " - " + end.toString() + "\n";
		period += "\tNumber of shifts worked: " + shifts.size() + "\n";
		period += "\tTotal hours worked: " + String.format("%.2f", hours) + "\n";
		period += "\tTotal amount earned: " + String.format("%.2f", pay) + "\n";
		return period;
	}
}
