package src.test;

import src.work.*;

/**
 * Class that creates Market and C&G shifts to
 * find any errors in the programs.
 */
public class TestShift {
	public static void main(String[] args) {
		// create Market and C&G Shifts with different values to test classes
		String[] fri = new String[] { "F", "03/25/2022", "7:54 AM", "10:03 AM", "3", "14" };
		MarketShift tm_fri = new MarketShift(fri);
		String[] sat1 = new String[] { "S", "03/26/2022", "12:44 PM", "4:05 PM", "8", "14" };
		CGShift tcg_sat1 = new CGShift(sat1);
		String[] sat2 = new String[] { "S", "03/26/2022", "5:12 PM", "8:30 PM", "5", "14" };
		CGShift tcg_sat2 = new CGShift(sat2);
		String[] mon = new String[] { "M", "03/28/2022", "8:17 AM", "10:05 AM", "3", "14" };
		MarketShift tm_mon = new MarketShift(mon);
		String[] wed = new String[] { "W", "03/30/2022", "07:53 AM", "09:56 AM", "3", "14" };
		MarketShift tm_wed = new MarketShift(wed);

		Shift[] shiftsArr = new Shift[] { tm_fri, tcg_sat1, tcg_sat2, tm_mon, tm_wed };
		for (Shift shift : shiftsArr) {
			System.out.println(shift.toString());
		}
	}
}
