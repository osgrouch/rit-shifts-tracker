package tracker.shifts;

import tracker.datetime.CalendarDate;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Class representing the two weeks that count towards a paycheck.
 * Contains a SortedSet of the shifts worked during the week.
 */
public class PayPeriod {
	/** The start of the pay period */
	private final CalendarDate start;
	/** The end of the pay period */
	private final CalendarDate end;

	/** The total number of hours worked during this pay period */
	private double totalHours;
	/** The total amount of money earned this pay period */
	private double totalEarned;

	/** The SortedSet of shifts worked during this pay week */
	private SortedSet<Shift> shifts;

	/**
	 * Create a new pay period with CalendarDate arguments.
	 *
	 * @param start the first day of the pay period
	 * @param end   the last day of the pay period
	 */
	public PayPeriod (CalendarDate start, CalendarDate end) {
		this.start = start;
		this.end = end;
		this.totalHours = 0;
		this.totalEarned = 0;
		this.shifts = new TreeSet<Shift>(new ShiftComparator());
	}

	/**
	 * Format the string dates into CalendarDates then
	 * create a new pay period with the given starting and ending dates.
	 * The pay period will always start on a Friday and end on a Thursday.
	 *
	 * @param startDate the first day of the pay period
	 * @param endDate   the last day of the pay period
	 */
	public PayPeriod (String startDate, String endDate) {
		this(new CalendarDate(6, startDate), new CalendarDate(5, endDate));
	}

	/**
	 * Add a shift to the SortedSet of shifts worked this pay period.
	 * And increment the total number of hours worked and amount earned.
	 *
	 * @param entry the shift to add
	 */
	public void addShift (Shift entry) {
		shifts.add(entry);
		totalHours += entry.getTotalHours();
		totalEarned += entry.getTotalEarned();
	}

	/**
	 * @return the SortedSet of shifts
	 */
	public SortedSet<Shift> getShifts () {
		return shifts;
	}

	/**
	 * @return the first day of the pay period
	 */
	public CalendarDate getStart () {
		return start;
	}

	/**
	 * @return the last day of the pay period
	 */
	public CalendarDate getEnd () {
		return end;
	}

	/**
	 * @return double value of the total number of hours worked this pay period
	 */
	public double getTotalHours () {
		return totalHours;
	}

	/**
	 * @return double value of the total earned this pay period
	 */
	public double getTotalEarned () {
		return totalEarned;
	}

	/**
	 * @return a human-readable version of the start and end of a pay period,
	 * as well as the total number of hours worked and amount earned and all the shifts worked during the pay period
	 */
	public String shiftsToString () {
		String period = start.toString() + " - " + end.toString() + "\n";
		period += "\tNumber of shifts worked: " + shifts.size() + "\n";
		period += "\tTotal hours worked: " + String.format("%.2f", totalHours) + "\n";
		period += "\tTotal amount earned: " + String.format("%.2f", totalEarned) + "\n";
		period += "-------------------\n";
		for (Shift shift : shifts) {
			period += shift.toString();
		}
		period += "-------------------\n";
		return period;
	}

	/**
	 * @return a human-readable version of the start and end of a pay period,
	 * as well as the total number of hours worked and amount earned
	 */
	@Override
	public String toString () {
		String period = start.toString() + " - " + end.toString() + "\n";
		period += "\tNumber of shifts worked: " + shifts.size() + "\n";
		period += "\tTotal hours worked: " + String.format("%.2f", totalHours) + "\n";
		period += "\tTotal amount earned: " + String.format("%.2f", totalEarned) + "\n";
		return period;
	}
}
