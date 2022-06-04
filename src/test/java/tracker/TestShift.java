package tracker;

import tracker.shifts.CGShift;
import tracker.shifts.MarketShift;
import tracker.shifts.Shift;

import java.util.ArrayList;

/**
 * Class that creates Market and C&G shifts to find any errors in the programs.
 */
public class TestShift {
	/**
	 * Tests the different methods in Shift classes to ensure they work as expected.
	 *
	 * @param args command line arguments
	 */
	public static void main (String[] args) {
		ArrayList<Shift> shiftsArr = createShiftsArr();

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
	public static ArrayList<Shift> createShiftsArr () {
		ArrayList<Shift> shifts = new ArrayList<>();

		// create Market and C&G Shifts with different values to test classes
		MarketShift tm_fri1 = new MarketShift("03/18/2022", "7:52 AM", "10:00 AM", 14, "UTILITY");
		shifts.add(tm_fri1);
		CGShift tcg_sat1 = new CGShift("03/19/2022", "12:42 PM", "3:44 PM", 14, "SALSARITAS");
		shifts.add(tcg_sat1);
		CGShift tcg_sat2 = new CGShift("03/19/2022", "5:15 PM", "8:35 PM", 14, "GRILLE");
		shifts.add(tcg_sat2);
		MarketShift tm_mon1 = new MarketShift("03/21/2022", "07:58 AM", "10:03 AM", 14, "UTILITY");
		shifts.add(tm_mon1);
		MarketShift tm_wed1 = new MarketShift("03/23/2022", "07:51 AM", "10:02 AM", 14, "UTILITY");
		shifts.add(tm_wed1);
		MarketShift tm_thurs1 = new MarketShift("03/24/2022", "1:01 PM", "4:11 PM", 14, "UTILITY");
		shifts.add(tm_thurs1);
		CGShift tcg_thurs2 = new CGShift("03/24/2022", "4:58 PM", "7:04 PM", 14, "STOCKER");
		shifts.add(tcg_thurs2);
		MarketShift tm_fri2 = new MarketShift("03/25/2022", "7:54 AM", "10:03 AM", 14, "UTILITY");
		shifts.add(tm_fri2);
		CGShift tcg_sat3 = new CGShift("03/26/2022", "12:44 PM", "4:05 PM", 14, "SALSARITAS");
		shifts.add(tcg_sat3);
		CGShift tcg_sat4 = new CGShift("03/26/2022", "5:12 PM", "8:30 PM", 14, "GRILLE");
		shifts.add(tcg_sat4);
		MarketShift tm_mon2 = new MarketShift("03/28/2022", "8:17 AM", "10:05 AM", 14, "UTILITY");
		shifts.add(tm_mon2);
		MarketShift tm_wed2 = new MarketShift("03/30/2022", "07:53 AM", "09:56 AM", 14, "UTILITY");
		shifts.add(tm_wed2);
		MarketShift tm_thurs3 = new MarketShift("03/31/2022", "12:58 PM", "4:10 PM", 14, "UTILITY");
		shifts.add(tm_thurs3);
		CGShift tcg_thurs4 = new CGShift("03/31/2022", "4:57 PM", "7:05 PM", 14, "STOCKER");
		shifts.add(tcg_thurs4);

		return shifts;
	}
}
