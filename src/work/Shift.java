package src.work;

import java.math.BigDecimal;
import java.math.RoundingMode;

import src.timestamp.CalendarDate;
import src.timestamp.Time;

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
	private float totalEarned;

	/**
	 * Create a new shift and set its starting and ending times.
	 * 
	 * @param weekday      the first letter of the day of the week worked
	 * @param calendarDate the exact calendar date worked in the format "MM/DD/YYYY"
	 * @param start        the time clocked in, in the format "HH:MM AM/PM"
	 * @param end          the time clocked out, in the format "HH:MM AM/PM"
	 * @param locChoice    the number corresponding to the location worked
	 * @param jobChoice    the number corresponding to the job worked
	 * @param rate         the amount earned per hour of this shift
	 */
	public Shift(
			char weekday, String calendarDate,
			String start, String end,
			int rate) {
		// create an instance of Date corresponding to this shift
		this.date = new CalendarDate(weekday, calendarDate);

		// set the clock in and clock out times
		this.in = new Time(start);
		this.out = new Time(end);

		// calculate the total time worked
		this.totalTime = timeDifference();

		this.payRate = rate;
		this.totalEarned = calcEarnings();
	}

	/**
	 * Finds the amount of time worked by taking the difference between
	 * the clock in and clock out times and returning the difference as
	 * a Time instance.
	 * 
	 * @return Time instance of the total time worked
	 */
	private Time timeDifference() {
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
		return new Time(hourDiff, minutesDiff, "UNDEF");
	}

	/**
	 * Calculate the total amount earned in this shift,
	 * and return the value floored to 2 decimal places.
	 * 
	 * @return float value of total earned
	 */
	private float calcEarnings() {
		float roughEarnings = this.payRate * (this.totalTime.getHour() + this.totalTime.fractionalHour());
		BigDecimal bd = new BigDecimal(roughEarnings).setScale(2, RoundingMode.DOWN);
		return bd.floatValue();
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
	public float getTotalEarned() {
		return totalEarned;
	}
}
