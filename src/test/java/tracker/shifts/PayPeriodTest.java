package tracker.shifts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/** Class to test PayPeriod and Shift class with JUnit5. */
class PayPeriodTest {
	/**
	 * Test the ShiftComparator class works by adding Shifts to the PayPeriod in a random order, then visually
	 * inspecting the PayPeriod output to see if the Shifts were ordered correctly.
	 */
	@Test
	@DisplayName ("Visually test SortedSet correctly sorts Shifts")
	public void testAddingShifts () {
		Set<Shift> shifts = new HashSet<>();
		Shift firstShift = new MarketShift("03/18/2022", "7:52 AM", "10:00 AM", 14, "UTILITY");
		PayPeriod payPeriod = new PayPeriod(firstShift.getDate().toString());

		shifts.add(firstShift);
		shifts.add(new CGShift("03/19/2022", "12:42 PM", "3:44 PM", 14, "SALSARITAS"));
		shifts.add(new CGShift("03/19/2022", "5:15 PM", "8:35 PM", 14, "GRILLE"));
		shifts.add(new MarketShift("03/21/2022", "07:58 AM", "10:03 AM", 14, "UTILITY"));
		shifts.add(new MarketShift("03/23/2022", "07:51 AM", "10:02 AM", 14, "UTILITY"));
		shifts.add(new MarketShift("03/24/2022", "1:01 PM", "4:11 PM", 14, "UTILITY"));
		shifts.add(new CGShift("03/24/2022", "4:58 PM", "7:04 PM", 14, "DINING"));
		shifts.add(new MarketShift("03/25/2022", "7:54 AM", "10:03 AM", 14, "UTILITY"));
		shifts.add(new CGShift("03/26/2022", "12:44 PM", "4:05 PM", 14, "SALSARITAS"));
		shifts.add(new CGShift("03/26/2022", "5:12 PM", "8:30 PM", 14, "GRILLE"));
		shifts.add(new MarketShift("03/28/2022", "8:17 AM", "10:05 AM", 14, "UTILITY"));
		shifts.add(new MarketShift("03/30/2022", "07:53 AM", "09:56 AM", 14, "UTILITY"));
		shifts.add(new MarketShift("03/31/2022", "12:58 PM", "4:10 PM", 14, "UTILITY"));
		shifts.add(new CGShift("03/31/2022", "4:57 PM", "7:05 PM", 14, "DINING"));

		System.out.println("Printing each Shift in the unordered HashSet:");
		System.out.println();
		for (Shift shift : shifts) {
			System.out.println(shift);
			payPeriod.addShift(shift);
		}

		System.out.println("Printing order Shifts in PayPeriod:");
		System.out.println();
		System.out.println(payPeriod.toStringWithShifts());
	}
}
