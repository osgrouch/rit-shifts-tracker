package tracker;

import tracker.shifts.PayPeriod;
import tracker.shifts.Shift;

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
		Shift[] shiftsArr = TestShift.createShiftsArr();
		PayPeriod period = new PayPeriod(shiftsArr[0].getDate(),
		                                 shiftsArr[shiftsArr.length - 1].getDate());

		// add the shifts to the tree map in PayPeriod class
		for (Shift shift : shiftsArr) {
			period.addShift(shift);
		}
		System.out.println(period.toString());
	}
}
