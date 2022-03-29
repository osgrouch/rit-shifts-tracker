/**
 * Class representing a shift worked for RIT Dining.
 * Keeps track of the day worked, time clocked in, time clocked out,
 * place worked and the job worked.
 */
public class Shift {
	/** Day worked */
	private Date date;

	/** Time clocked in */
	private Time in;
	/** Time clocked out */
	private Time out;
	/** Total time worked */
	private Time total;

	/** Place worked */
	private Location loc;
	/** Job worked, on paper */
	private Jobs job;

	/**
	 * Create a new shift and set its starting and ending times.
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
		// create an instance of Date corresponding to this shift
		this.date = createDate(weekday, calendarDate);

		// set the clock in and clock out times
		this.in = createTime(start);
		this.out = createTime(end);

		// set the location worked based on the choice given
		this.loc = selectLocation(locChoice);
		// set the job worked based on the choice given
		this.job = selectJob(jobChoice);

		this.total = timeDifference();
	}

	/**
	 * Creates an instance of Date record with the information provided.
	 * 
	 * @param weekday      the first letter of the day of the week worked
	 * @param calendarDate the exact calendar date worked in the format "MM/DD/YYYY"
	 * @return Date instance with properties from weekday and calendarDate
	 */
	private Date createDate(char weekday, String calendarDate) {
		// create the DaysOfTheWeek variable
		DaysOfTheWeek dayOfTheWeek = DaysOfTheWeek.UNDEF;
		switch (weekday) {
			// catches repeating first letters by assigning
			// Thursday as R and Sunday as U
			case 'M':
				dayOfTheWeek = DaysOfTheWeek.MONDAY;
				break;
			case 'T':
				dayOfTheWeek = DaysOfTheWeek.TUESDAY;
				break;
			case 'W':
				dayOfTheWeek = DaysOfTheWeek.WEDNESDAY;
				break;
			case 'R':
				dayOfTheWeek = DaysOfTheWeek.THURSDAY;
				break;
			case 'F':
				dayOfTheWeek = DaysOfTheWeek.FRIDAY;
				break;
			case 'S':
				dayOfTheWeek = DaysOfTheWeek.SATURDAY;
				break;
			case 'U':
				dayOfTheWeek = DaysOfTheWeek.SUNDAY;
				break;
		}

		// create the different variables for the calendar date
		// by splitting the date given to "MM" "DD" "YYYY"
		String[] dateSplit = calendarDate.split("/");
		int month = Integer.parseInt(dateSplit[0]);
		int day = Integer.parseInt(dateSplit[1]);
		int year = Integer.parseInt(dateSplit[2]);

		return new Date(dayOfTheWeek, month, day, year);
	}

	/**
	 * Creates a new Time instance with the given entry.
	 * 
	 * @param entry the time in the format "HH:MM AM/PM"
	 * @return Time instance with properties from entry
	 */
	private Time createTime(String entry) {
		// splits entry into: "HH" "MM" "AM/PM"
		String[] entrySplit = entry.split(":\\s+");
		int hour = Integer.parseInt(entrySplit[0]);
		int minutes = Integer.parseInt(entrySplit[1]);
		String ampm = entrySplit[2];

		return new Time(hour, minutes, ampm);
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
		int hourDiff = out.getMilitaryHour() - in.getMilitaryHour();

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
	 * @return Date of the shift
	 */
	public Date getDate() {
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
	public Time getTotal() {
		return total;
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
}