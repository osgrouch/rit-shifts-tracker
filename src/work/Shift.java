package src.work;

import java.math.BigDecimal;
import java.math.RoundingMode;

import src.date.CalendarDate;
import src.date.Time;

/**
 * Class representing a shift worked for RIT Dining.
 * Keeps track of the day worked, time clocked in, time clocked out,
 * place worked and the job worked.
 */
public class Shift {
	/** Day worked */
	private CalendarDate date;

	/** Time clocked in */
	private Time in;
	/** Time clocked out */
	private Time out;
	/** Total time worked */
	private Time totalTime;

	/** Place worked */
	private Location loc;
	/** Job worked, on paper */
	private Jobs job;

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
			int locChoice, int jobChoice, int rate) {
		// create an instance of Date corresponding to this shift
		this.date = new CalendarDate(weekday, calendarDate);

		// set the clock in and clock out times
		this.in = new Time(start);
		this.out = new Time(end);

		// set the location worked based on the choice given
		this.loc = selectLocation(locChoice);
		// set the job worked based on the choice given
		this.job = selectJob(jobChoice);

		// calculate the total time worked
		this.totalTime = timeDifference();

		this.payRate = rate;
		this.totalEarned = calcEarnings();
	}

	/**
	 * Create a new shift with the default $14/hour rate
	 * 
	 * @param weekday      the first letter of the day of the week worked
	 * @param calendarDate the exact calendar date worked in the format "MM/DD/YYYY"
	 * @param start        the time clocked in, in the format "HH:MM AM/PM"
	 * @param end          the time clocked out, in the format "HH:MM AM/PM"
	 * @param locChoice    the number corresponding to the location worked
	 * @param jobChoice    the number corresponding to the job worked
	 */
	public Shift(
			char weekday, String calendarDate,
			String start, String end,
			int locChoice, int jobChoice) {
		this(weekday, calendarDate, start, end, locChoice, jobChoice, 14);
	}

	/**
	 * Returns the location worked based on the number given,
	 * must be in sync with any changes made to UI.
	 * 
	 * @param locChoice the number corresponding to the location worked
	 * @return the location worked
	 */
	private Location selectLocation(int locChoice) {
		Location temp = Location.UNDEF;

		switch (locChoice) {
			case 1:
				temp = Location.CANTINAGRILLE;
				break;
			case 2:
				temp = Location.MARKET;
				break;
		}

		return temp;
	}

	/**
	 * Returns the job worked based on the number given,
	 * must be in sync with any changes made to UI.
	 * 
	 * @param jobChoice the number corresponding to the job worked
	 * @return the job worked
	 */
	private Jobs selectJob(int jobChoice) {
		Jobs temp = Jobs.UNDEF;

		switch (jobChoice) {
			case 1:
				temp = Jobs.CASHIER;
				break;
			case 2:
				temp = Jobs.DINING;
				break;
			case 3:
				temp = Jobs.FLEX;
				break;
			case 4:
				temp = Jobs.FRYER;
				break;
			case 5:
				temp = Jobs.GRILLE;
				break;
			case 6:
				temp = Jobs.KDS;
				break;
			case 7:
				temp = Jobs.PREP;
				break;
			case 8:
				temp = Jobs.SALSARITAS;
				break;
			case 9:
				temp = Jobs.STOCKER;
				break;
			case 10:
				temp = Jobs.UTILITY;
				break;
		}

		return temp;
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
	 * and return the value floored to 2 decimal places
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
	 * @return the place of work
	 */
	public Location getLoc() {
		return loc;
	}

	/**
	 * @return the shift worked, on paper
	 */
	public Jobs getJob() {
		return job;
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