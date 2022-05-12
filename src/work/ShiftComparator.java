package src.work;

import java.util.Comparator;

import src.datetime.CalendarDate;
import src.datetime.Time;

/**
 * Class that overrides Comparator's compare method on Shift class
 * elements, so that Shifts can be used in a SortedSet.
 * Compares Shifts by their dates and clock in times.
 */
public class ShiftComparator implements Comparator<Shift> {
	/**
	 * Compare the given Shifts by comparing their dates and clock in times.
	 * 
	 * @param one the first Shift to compare
	 * @param two the second Shift to compare
	 * @return -1 if one < two,
	 *         0 if equal,
	 *         1 if one > two
	 */
	@Override
	public int compare(Shift one, Shift two) {
		int result = CalendarDate.compare(one.getDate(), two.getDate());

		if (result == 0)
			result = Time.compare(one.getIn(), two.getIn());

		return result;
	}
}
