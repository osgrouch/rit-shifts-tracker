package src.test;

import src.work.*;

/**
 * Class that creates Market and C&G shifts to
 * find any errors in the programs.
 */
public class TestShift {
	/**
	 * Tests the different methods in Shift classes to ensure they work as expected.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		Shift[] shiftsArr = createShiftsArr();

		// print out the different shifts created
		for (Shift shift : shiftsArr) {
			System.out.println(shift.toString());
		}
	}

	/**
	 * Creates different Shift classes with actual values.
	 * 
	 * @return array of shifts
	 */
	public static Shift[] createShiftsArr() {
		// create Market and C&G Shifts with different values to test classes
		String[] fri1 = new String[] { "6", "03/18/2022", "7:52 AM", "10:00 AM", "3", "14" };
		MarketShift tm_fri1 = new MarketShift(fri1);
		String[] sat1 = new String[] { "7", "03/19/2022", "12:42 PM", "3:44 PM", "8", "14" };
		CGShift tcg_sat1 = new CGShift(sat1);
		String[] sat2 = new String[] { "7", "03/19/2022", "5:15 PM", "8:35 PM", "5", "14" };
		CGShift tcg_sat2 = new CGShift(sat2);
		String[] mon1 = new String[] { "2", "03/21/2022", "07:58 AM", "10:03 AM", "3", "14" };
		MarketShift tm_mon1 = new MarketShift(mon1);
		String[] wed1 = new String[] { "4", "03/23/2022", "07:51 AM", "10:02 AM", "3", "14" };
		MarketShift tm_wed1 = new MarketShift(wed1);
		String[] thurs1 = new String[] { "5", "03/24/2022", "1:01 PM", "4:11 PM", "3", "14" };
		MarketShift tm_thurs1 = new MarketShift(thurs1);
		String[] thurs2 = new String[] { "5", "03/24/2022", "4:58 PM", "7:04 PM", "2", "14" };
		CGShift tcg_thurs2 = new CGShift(thurs2);
		String[] fri2 = new String[] { "6", "03/25/2022", "7:54 AM", "10:03 AM", "3", "14" };
		MarketShift tm_fri2 = new MarketShift(fri2);
		String[] sat3 = new String[] { "7", "03/26/2022", "12:44 PM", "4:05 PM", "8", "14" };
		CGShift tcg_sat3 = new CGShift(sat3);
		String[] sat4 = new String[] { "7", "03/26/2022", "5:12 PM", "8:30 PM", "5", "14" };
		CGShift tcg_sat4 = new CGShift(sat4);
		String[] mon2 = new String[] { "2", "03/28/2022", "8:17 AM", "10:05 AM", "3", "14" };
		MarketShift tm_mon2 = new MarketShift(mon2);
		String[] wed2 = new String[] { "4", "03/30/2022", "07:53 AM", "09:56 AM", "3", "14" };
		MarketShift tm_wed2 = new MarketShift(wed2);
		String[] thurs3 = new String[] { "5", "03/31/2022", "12:58 PM", "4:10 PM", "3", "14" };
		MarketShift tm_thurs3 = new MarketShift(thurs3);
		String[] thurs4 = new String[] { "5", "03/31/2022", "4:57 PM", "7:05 PM", "2", "14" };
		CGShift tcg_thurs4 = new CGShift(thurs4);

		return new Shift[] { tm_fri1, tm_mon1, tm_wed1, tm_fri2, tcg_sat1, tm_thurs1,
				tcg_thurs2, tcg_sat2, tm_mon2, tm_wed2, tcg_sat3, tcg_sat4, tm_thurs3, tcg_thurs4 };
	}
}
