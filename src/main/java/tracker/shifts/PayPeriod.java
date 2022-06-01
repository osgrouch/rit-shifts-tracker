package tracker.shifts;

import tracker.datetime.CalendarDate;

import java.util.ArrayList;

/**
 * Class representing the two weeks that count towards a paycheck.
 * Contains a ArrayList of the {@link Shift Shifts} worked during the week.
 */
public class PayPeriod {
	/** The first day of the pay period, always a Friday */
	private final CalendarDate start;
	/** The last day of the pay period, always a Thursday */
	private final CalendarDate end;

	/** The total number of hours worked during this pay period */
	private double hours;
	/** The total amount of money earned this pay period */
	private double pay;

	/** The ArrayList of Shifts worked during this pay week */
	private ArrayList<Shift> shifts;

	/**
	 * Create a new PayPeriod with the given starting and ending dates.
	 *
	 * @param startDate the first day of the pay period
	 * @param endDate   the last day of the pay period
	 */
	public PayPeriod (String startDate, String endDate) {
		this.start = new CalendarDate(startDate);
		this.end = new CalendarDate(endDate);
		this.hours = 0;
		this.pay = 0;
		this.shifts = new ArrayList<>();
	}

	/**
	 * Add a shift to the ArrayList of Shifts worked this pay period.
	 * And increment the total number of hours worked and amount earned.
	 *
	 * @param entry the shift to add
	 */
	public void addShift (Shift entry) {
		shifts.add(entry);
		hours += entry.calcTotalHours();
		pay += entry.calcTotalHours() * entry.getPayRate();
	}

	/**
	 * @return a human-readable version of the start and end of a pay period,
	 * as well as the total number of hours worked and amount earned
	 * and all the shifts worked during the pay period
	 */
	public String shiftsToString () {
		String period = this.toString();
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
		period += "\tTotal hours worked: " + String.format("%.2f", hours) + "\n";
		period += "\tTotal amount earned: " + String.format("%.2f", pay) + "\n";
		return period;
	}
}
