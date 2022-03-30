package src.work;

import java.math.BigDecimal;
import java.math.RoundingMode;

import src.datetime.CalendarDate;
import src.datetime.Time;

/**
 * Abstract class representing a shift worked for RIT Dining.
 * Keeps track of the day worked, time clocked in, time clocked out,
 * place worked and the job worked.
 * Child classes must implement the selectJob function with different
 * values for jobChoice to map to.
 */
public abstract class Shift {
	/** Day worked */
	private CalendarDate date;

	/** Time clocked in */
	private Time in;
	/** Time clocked out */
	private Time out;
	/** Total time worked */
	private Time totalTime;

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
	 * @param args an array of strings with the different elements each representing
	 *             a different piece of information about the shift to create
	 */
	public Shift(String[] args) {
		// create the variables for the different elements in args
		char weekday = args[0].charAt(0);
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
		int hourDiff = out.militaryHour() - in.militaryHour();
		int minutesDiff;
		if (out.getMinutes() < in.getMinutes()) {
			// if clock out minutes is less than clock in minutes,
			// then a full hour was not completed,
			// therefore decrease the hours worked by one and calculate the minutes
			--hourDiff;
			minutesDiff = (60 + out.getMinutes()) - in.getMinutes();
		} else if (out.getMinutes() > in.getMinutes()) {
			// if clock out minutes is greater than clock in minutes,
			// then just calculate their difference
			minutesDiff = out.getMinutes() - in.getMinutes();
		} else {
			// else the minutes are the same and there is no difference
			minutesDiff = 0;
		}
		// set ampm as "UNDEF" because this new instance is the difference of two times
		this.totalTime = new Time(hourDiff, minutesDiff, "UNDEF");

		// set the pay rate of this shift
		this.payRate = rate;

		// calculate the total amount of money earned this shift
		double roughEarnings = this.payRate * (this.totalTime.getHour() + this.totalTime.fractionalHour());
		BigDecimal bd = new BigDecimal(roughEarnings).setScale(2, RoundingMode.UP);
		this.totalEarned = bd.doubleValue();
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
	 * @return Time instance of total time worked
	 */
	public Time getTotalTime() {
		return totalTime;
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
}
