package src.work;

import java.util.Comparator;
import java.util.TreeMap;

import src.datetime.CalendarDate;

/**
 * Class representing the two weeks that count towards a paycheck.
 * Contains a HashMap of the shifts worked during the week.
 */
public class PayPeriod {
	/** The start of the pay period */
	private CalendarDate start;
	/** The end of the pay period */
	private CalendarDate end;

	/** The total number of hours worked during this pay period */
	private double totalHours;
	/** The total amount of money earned this pay period */
	private double totalEarned;

	/**
	 * The linked hashmap containing all the shifts worked during the pay week,
	 * where the value is the shift worked and the key is the date and time clocked
	 * in
	 */
	private TreeMap<String, Shift> shiftsMap;

	/**
	 * Create a new pay period with CalendarDate arguments.
	 * 
	 * @param start the first day of the pay period
	 * @param end   the last day of the pay period
	 */
	public PayPeriod(CalendarDate start, CalendarDate end) {
		this.start = start;
		this.end = end;
		this.totalHours = 0;
		this.totalEarned = 0;
		this.shiftsMap = new TreeMap<>(new Comparator<String>() {
			/**
			 * Compare the given String keys to determine which Shift comes first.
			 * 
			 * @param one the String key of the first shift
			 * @param two the String key of the second shift
			 * @return (< 0) if one < two,
			 *         0 if equal,
			 *         (> 0) if one > two
			 */
			public int compare(String one, String two) {
				// get the information from the one argument
				String[] oneSplit = one.split("/|:|\\s+");
				int oneMonth = Integer.parseInt(oneSplit[0]);
				int oneDay = Integer.parseInt(oneSplit[1]);
				int oneYear = Integer.parseInt(oneSplit[2]);
				int oneHour = Integer.parseInt(oneSplit[3]);
				String oneAMPM = oneSplit[5];

				// get the information from the two argument
				String[] twoSplit = two.split("/|:|\\s+");
				int twoMonth = Integer.parseInt(twoSplit[0]);
				int twoDay = Integer.parseInt(twoSplit[1]);
				int twoYear = Integer.parseInt(twoSplit[2]);
				int twoHour = Integer.parseInt(twoSplit[3]);
				String twoAMPM = twoSplit[5];

				int result;

				// compare the years
				if (oneYear < twoYear) {
					result = -1;
				} else if (oneYear > twoYear) {
					result = 1;
				} else {
					// compare the months
					result = oneMonth - twoMonth;

					// compare the days
					if (result == 0) {
						result = oneDay - twoDay;

						// compare the hours
						if (result == 0) {
							if (oneAMPM.equals("PM") && oneHour != 12) {
								oneHour += 12;
							}
							if (twoAMPM.equals("PM") && twoHour != 12) {
								twoHour += 12;
							}
							result = oneHour - twoHour;
						}
					}
				}

				return result;
			}
		});
	}

	/**
	 * Format the string dates into CalendarDates then create a new pay period with
	 * the given starting and ending dates. The pay period will always start on a
	 * Friday and end on a Thursday.
	 * 
	 * @param startDate the first day of the pay period
	 * @param endDate   the last day of the pay period
	 */
	public PayPeriod(String startDate, String endDate) {
		this(new CalendarDate('F', startDate), new CalendarDate('R', endDate));
	}

	/**
	 * Add a shift to the linked hashmap of shifts worked this pay period.
	 * And increment the total number of hours worked and amount earned.
	 * 
	 * @param entry the shift to add
	 */
	public void addShift(Shift entry) {
		shiftsMap.put(entry.getDate().compactDate() + " " + entry.getIn().toString(), entry);
		totalHours += entry.getTotalHours();
		totalEarned += entry.getTotalEarned();
	}

	/**
	 * Fetches an entry from the shifts hashmap,
	 * assumes the key is formatted correctly.
	 * 
	 * @param key the key to access the entry in the hashmap
	 * @return the shift entry
	 */
	public Shift getShift(String key) {
		return shiftsMap.get(key);
	}

	/**
	 * @return the linked hashmap of shifts
	 */
	public TreeMap<String, Shift> getShiftsMap() {
		return shiftsMap;
	}

	/**
	 * @return the first day of the pay period
	 */
	public CalendarDate getStart() {
		return start;
	}

	/**
	 * @return the last day of the pay period
	 */
	public CalendarDate getEnd() {
		return end;
	}

	/**
	 * @return double value of the total number of hours worked this pay period
	 */
	public double getTotalHours() {
		return totalHours;
	}

	/**
	 * @return double value of the total earned this pay period
	 */
	public double getTotalEarned() {
		return totalEarned;
	}

	/**
	 * @return a human readable version of the start and end of a pay period,
	 *         as well as the total number of hours worked and amount earned,
	 *         and all the shifts worked during the pay period
	 */
	@Override
	public String toString() {
		String period = start.toString() + " - " + end.toString() + "\n";
		period += "\tNumber of shifts worked: " + shiftsMap.size() + "\n";
		period += "\tTotal hours worked: " + String.format("%.2f", totalHours) + "\n";
		period += "\tTotal amount earned: " + String.format("%.2f", totalEarned) + "\n";
		period += "-------------------\n";
		for (Shift shift : shiftsMap.values()) {
			period += shift.toString();
		}
		period += "-------------------\n";
		return period;
	}

	/**
	 * @return period toString without the shifts worked
	 */
	public String periodInfoString() {
		String period = start.toString() + " - " + end.toString() + "\n";
		period += "\tNumber of shifts worked: " + shiftsMap.size() + "\n";
		period += "\tTotal hours worked: " + totalHours + "\n";
		period += "\tTotal amount earned: " + totalEarned + "\n";
		return period;
	}
}
