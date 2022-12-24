package tracker.shifts;

import java.time.LocalDate;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Class representing the two weeks that count towards a paycheck.
 * Contains a SortedSet of the {@link Shift Shifts} worked during the week.
 */
public class PayPeriod {
	/** The first day of the pay period, always a Friday */
	private final LocalDate start;
	/** The last day of the pay period, always a Thursday */
	private final LocalDate end;

	/** The total number of hours worked during this pay period */
	private double hours;
	/** The total amount of money earned this pay period */
	private double pay;

	/** The SortedSet of Shifts worked during this pay week */
	private SortedSet<Shift> shifts;

	/**
	 * Create a new PayPeriod with the given starting date and calculate the end date,
	 * which will always be 13 days from the starting date.
	 *
	 * @param startDate the first day of the pay period, in the format MM/DD/YYYY or MMM DD, YYYY
	 */
	public PayPeriod(String startDate) {
		this.hours = 0;
		this.pay = 0;
		this.shifts = new TreeSet<>();
		this.start = LocalDate.parse(startDate);
		this.end = start.plusDays(13);
	}

	/**
	 * Create a new PayPeriod with the given starting and ending dates.
	 *
	 * @param startDate the first day of the pay period, in the format MM/DD/YYYY or MMM DD, YYYY
	 * @param endDate   the last day of the pay period, in the format MM/DD/YYYY or MMM DD, YYYY
	 */
	public PayPeriod(String startDate, String endDate) {
		this.hours = 0;
		this.pay = 0;
		this.shifts = new TreeSet<>();
		this.start = LocalDate.parse(startDate);
		this.end = LocalDate.parse(endDate);
	}

	/**
	 * Add a shift to the SortedSet of Shifts worked this pay period.
	 * Increment the total number of hours worked and amount earned.
	 *
	 * @param entry the Shift to add
	 */
	public void addShift(Shift entry) {
		shifts.add(entry);
		hours += entry.calcTotalHours();
		pay += entry.calcTotalHours() * entry.getPayRate();
	}

	/**
	 * Remove a shift from the SortedSet of Shifts worked this pay period.
	 * Decrement the total number of hours worked and amount earned.
	 *
	 * @param departure the Shift to remove
	 */
	public void removeShift(Shift departure) {
		shifts.remove(departure);
		hours -= departure.calcTotalHours();
		pay -= departure.calcTotalHours() * departure.getPayRate();
	}

	/**
	 * @return {@link LocalDate} instance of the first day of the pay period
	 */
	public LocalDate getStart() {
		return start;
	}

	/**
	 * @return {@link LocalDate} instance of the last day of the pay period
	 */
	public LocalDate getEnd() {
		return end;
	}

	/**
	 * @return double value of the total hours worked
	 */
	public double getHours() {
		return hours;
	}

	/**
	 * @return double value of the total amount earned
	 */
	public double getPay() {
		return pay;
	}

	/**
	 * @return SortedSet of {@link Shift Shifts} worked
	 */
	public SortedSet<Shift> getShifts() {
		return shifts;
	}

	/**
	 * @return a human-readable version of the start and end of a pay period,
	 * as well as the total number of hours worked and amount earned
	 * and all the shifts worked during the pay period
	 */
	public String toStringWithShifts() {
		String period = this.toString();
		for (Shift shift : shifts) {
			period += shift.toString();
		}
		return period;
	}

	/**
	 * @return a human-readable version of the start and end of a pay period,
	 * as well as the total number of hours worked and amount earned
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
