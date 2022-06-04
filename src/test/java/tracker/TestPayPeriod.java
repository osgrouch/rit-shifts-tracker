package tracker;

import tracker.shifts.PayPeriod;
import tracker.shifts.Shift;

import java.util.ArrayList;

/**
 * Class for testing the PayPeriod class.
 */
public class TestPayPeriod {
	/**
	 * Test the different fields in PayPeriod to ensure they behave as expected.
	 *
	 * @param args command line arguments
	 */
	public static void main (String[] args) {
		ArrayList<Shift> shifts = TestShift.createShiftsArr();
		// hard coding in the value because CalendarDate::toString no longer formats for PayPeriod constructor
		PayPeriod period = new PayPeriod("3/18/2022");

		// add the shifts to the tree map in PayPeriod class
		for (Shift shift : shifts) {
			period.addShift(shift);
		}
		System.out.println(period.toStringWithShifts());
	}
}
