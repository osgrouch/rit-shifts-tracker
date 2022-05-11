package src.work;

import src.datetime.CalendarDate;
import src.datetime.Time;

/**
 * Abstract class representing a shift worked for RIT Dining.
 * Keeps track of the day worked, time clocked in, time clocked out,
 * place worked and the job worked.
 * Child classes are expected to implement a Job enum to track
 * the exact job worked during the shift.
 */
public abstract class Shift {
	/** Day worked */
	private CalendarDate date;

	/** Time clocked in */
	private Time in;
	/** Time clocked out */
	private Time out;
	/** Total time worked */
	private double totalHours;

	/**
	 * Hourly pay rate during the shift, typically $14/hour but
	 * can change due to special circumstances
	 */
	private int payRate;
	/** Total amount earned during this shift */
	private double totalEarned;

	/**
	 * Create a new shift and set its starting and ending times.
	 * 
	 * @param args an array of strings with the different elements each
	 *             representing a different piece of information about
	 *             the shift to create
	 */
	public Shift(String[] args) {
		// create the variables for the different elements in args
		int weekday = Integer.parseInt(args[0]);
		String calendarDate = args[1];
		String start = args[2];
		String end = args[3];
		// args[4] is skipped because that is the job worked,
		// which is set within the child class
		int rate = Integer.parseInt(args[5]);

		// create an instance of Date corresponding to this shift
		this.date = new CalendarDate(weekday, calendarDate);

		// set the clock in and clock out times
		this.in = new Time(start);
		this.out = new Time(end);

		// calculate the total time worked
		this.totalHours = Time.difference(in, out);

		// set the pay rate of this shift
		this.payRate = rate;

		// calculate the total amount of money earned this shift
		this.totalEarned = this.payRate * this.totalHours;
	}

	/**
	 * @return Date of the shift
	 */
	public CalendarDate getDate() {
		return date;
	}

	/**
	 * @return Time instance of time clocked in
	 */
	public Time getIn() {
		return in;
	}

	/**
	 * @return Time instance of time clocked out
	 */
	public Time getOut() {
		return out;
	}

	/**
	 * @return double value of total time worked
	 */
	public double getTotalHours() {
		return totalHours;
	}

	/**
	 * @return the hourly wage for this shift
	 */
	public int getPayRate() {
		return payRate;
	}

	/**
	 * @return the total amount made for this shift
	 */
	public double getTotalEarned() {
		return totalEarned;
	}

	/**
	 * @return human readable paragraph with all the information stored in this
	 *         shift
	 */
	@Override
	public String toString() {
		String shift = "Shift:\n";

		shift += ("\t" + date.toString() + "\n");
		shift += ("\t" + in.toString() + " - " + out.toString() + "\n");
		shift += ("\t$" + String.format("%.2f", totalEarned) + " = "
				+ payRate + " * " + String.format("%.2f", totalHours) + "\n");

		return shift;
	}
}
