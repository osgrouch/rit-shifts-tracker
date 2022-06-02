package tracker.shifts;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
	 * Create a new PayPeriod with the given starting date and calculate the end date,
	 * which will always be 13 days from the starting date.
	 *
	 * @param startDate the first day of the pay period, in the format MM-DD-YYYY
	 */
	public PayPeriod (String startDate) {
		this.hours = 0;
		this.pay = 0;
		this.shifts = new ArrayList<>();
		this.start = new CalendarDate(startDate);

		// calculate the end date, 13 days from the starting date
		int[] dateSplit = CalendarDate.splitDateIntoInt(startDate);
		int month = dateSplit[0];
		int day = dateSplit[1];
		int year = dateSplit[2];
		int maxDays = 31;
		switch (month) {
			case 2:
				maxDays = 28;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				maxDays = 30;
		}
		day += 13;
		if (day > maxDays) {
			day -= maxDays;
			++month;
			if (month > 12) {
				month = 1;
				++year;
			}
		}
		this.end = new CalendarDate(month, day, year);
	}

	/**
	 * Create a {@link JsonObject GSON JSON Object} out of this PayPeriod instance.
	 *
	 * @return JSON Object representing this instance
	 */
	public JsonObject createJSONObject () {
		JsonObject payPeriod = new JsonObject();

		payPeriod.addProperty("start", start.toString());
		payPeriod.addProperty("end", end.toString());

		// write the hours to only 2 decimal places
		String hoursStr = String.format("%.2f", hours);
		payPeriod.addProperty("hours", Double.valueOf(hoursStr));

		// write the pay to only 2 decimal places
		String payStr = String.format("%.2f", pay);
		payPeriod.addProperty("pay", Double.valueOf(payStr));

		JsonArray jsonShifts = new JsonArray();
		for (Shift shift : shifts) {
			JsonObject jsonShift = shift.createJSONObject();
			jsonShifts.add(jsonShift);
		}
		payPeriod.add("shifts", jsonShifts);

		return payPeriod;
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
		pay += entry.calcTotalHours() * entry.payRate;
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
