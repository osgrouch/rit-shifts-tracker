package src.main;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import src.datetime.CalendarDate;

import src.work.CGShift;
import src.work.MarketShift;
import src.work.PayPeriod;
import src.work.Shift;
import src.work.ShiftComparator;

/**
 * The Model in MVC implementation of the Shift Tracker application.
 */
public class ShiftTracker {
	/**
	 * Map of all Shifts in all PayPeriods in the model,
	 * where the key is a string of the Shift date,
	 * and the value is a TreeSet of Shifts worked on that date.
	 */
	private Map<String, SortedSet<Shift>> shifts;

	/** The SortedSet of PayPeriods in the model */
	private SortedSet<PayPeriod> payPeriods;

	/**
	 * Create a new ShiftTracker instance.
	 */
	public ShiftTracker() {
		this.shifts = new TreeMap<String, SortedSet<Shift>>();
		this.payPeriods = new TreeSet<PayPeriod>(new Comparator<PayPeriod>() {
			/**
			 * Compare the given PayPeriods by comparing their start dates.
			 * 
			 * @param one the first PayPeriod to compare
			 * @param two the second PayPeriod to compare
			 * @return -1 if one < two,
			 *         0 if equal,
			 *         1 if one > two
			 */
			@Override
			public int compare(PayPeriod one, PayPeriod two) {
				return CalendarDate.compare(one.getStart(), two.getStart());
			}
		});
	}

	/**
	 * Create a new CGShift with the given arguments
	 * and add it to the Map of created Shifts.
	 * 
	 * @param args the arguments used to create a new CGShift
	 * @return the newly created CGShift
	 */
	public Shift createCGShift(String[] args) {
		Shift newShift = new CGShift(args);
		addShift(newShift);
		return newShift;
	}

	/**
	 * Create a new MarketShift with the given arguments
	 * and add it to the Map of created Shifts.
	 * 
	 * @param args the arguments used to create a new MarketShift
	 * @return the newly created MarketShift
	 */
	public Shift createMarketShift(String[] args) {
		Shift newShift = new MarketShift(args);
		addShift(newShift);
		return newShift;
	}

	/**
	 * Add the given shift to the Map of Shifts in the model.
	 * 
	 * @param entry the Shift to add
	 */
	public void addShift(Shift entry) {
		// the key is the date worked
		String key = entry.getDate().compactDate();

		if (shifts.containsKey(key)) {
			// there is an existing entry for this date
			// so add this shift to the SortedSet of shifts worked on that date
			shifts.get(key).add(entry);
		} else {
			// there is no entry for this date so create a new entry
			SortedSet<Shift> dateSet = new TreeSet<Shift>(new ShiftComparator());
			dateSet.add(entry);
			shifts.put(key, dateSet);
		}
	}

	/**
	 * Create a new PayPeriod and add it to the SortedSet.
	 * 
	 * @param start the PayPeriod starting date
	 * @param end   the PayPeriod ending date
	 */
	public void createPayPeriod(String start, String end) {
		PayPeriod newPayPeriod = new PayPeriod(start, end);
		addPayPeriod(newPayPeriod);
	}

	/**
	 * Add the given Shift to the given PayPeriod.
	 * 
	 * @param entry  the Shift to add
	 * @param period the PayPeriod to add to
	 */
	public void addToPayPeriod(Shift entry, PayPeriod period) {
		period.addShift(entry);
	}

	/**
	 * Add a new PayPeriod to the SortedSet of PayPeriods.
	 * 
	 * @param entry the PayPeriod to add
	 */
	public void addPayPeriod(PayPeriod entry) {
		payPeriods.add(entry);
	}

	/**
	 * @return the Map of Shifts
	 */
	public Map<String, SortedSet<Shift>> getShifts() {
		return shifts;
	}

	/**
	 * Count all the days worked.
	 * 
	 * @return the total number of key-value pairs in the Shifts Map
	 */
	public int getTotalDays() {
		return shifts.size();
	}

	/**
	 * Count all the Shifts worked across all dates worked.
	 * 
	 * @return the total number of Shifts in the Shifts Map
	 */
	public int getTotalShifts() {
		int count = 0;

		for (SortedSet<Shift> date : shifts.values()) {
			count += date.size();
		}

		return count;
	}

	/**
	 * @return the SortedSet of PayPeriods
	 */
	public SortedSet<PayPeriod> getPayPeriods() {
		return payPeriods;
	}

	/**
	 * @return the total number of PayPeriods in the SortedSet
	 */
	public int getTotalPayPeriods() {
		return payPeriods.size();
	}
}
