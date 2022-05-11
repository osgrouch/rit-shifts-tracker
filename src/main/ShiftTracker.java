package src.main;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import src.datetime.CalendarDate;
import src.work.PayPeriod;

/**
 * The Model in MVC implementation of the Shift Tracker application.
 */
public class ShiftTracker {
	/** The SortedSet of PayPeriods in the model */
	private SortedSet<PayPeriod> payPeriods;

	/** The total number of PayPeriods in the set */
	private int totalPayPeriods;

	/**
	 * Create a new ShiftTracker instance.
	 */
	public ShiftTracker() {
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
		this.totalPayPeriods = 0;
	}

	/**
	 * Add a new PayPeriod to the SortedSet of PayPeriods.
	 * 
	 * @param entry the PayPeriod to add
	 */
	public void addPayPeriod(PayPeriod entry) {
		payPeriods.add(entry);
		++totalPayPeriods;
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
		return totalPayPeriods;
	}
}
