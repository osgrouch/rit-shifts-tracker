package tracker.datetime;

/** Class representing Time, used to track clock ins and clock outs. */
public class Time {
	/** The hour in 24h format */
	private final int hour;
	/** The minute */
	private final int minutes;

	/**
	 * Create a new Time instance with the given information.
	 *
	 * @param instant the time in the format "hh:mm AM/PM" or "HH:MM"
	 */
	public Time (String instant) {
		// splits entry into: "HH" "MM" "AM/PM"
		String[] split = instant.split(":|\\s+");
		int instantHour = Integer.parseInt(split[0]);
		// set hour in 24h format
		if (split.length == 3) {
			// time in the format "hh:mm AM/PM"
			boolean morning = ( split[2].equals("am") || split[2].equals("AM") );
			if (instantHour == 12) {
				// handle 12AM and 12PM
				if (morning) {
					this.hour = 0;
				} else {
					this.hour = 12;
				}
			} else if (morning) {
				this.hour = instantHour;
			} else {
				this.hour = 12 + instantHour;
			}
		} else {
			// time in the format "HH:MM"
			this.hour = instantHour;
		}
		this.minutes = Integer.parseInt(split[1]);
	}

	/**
	 * Calculate the hour difference between two Time instances.
	 *
	 * @param in  the lesser Time instance
	 * @param out the greater Time instance
	 * @return double value of hours between Time instances
	 */
	public static double difference (Time in, Time out) {
		int hourDiff = out.getHour() - in.getHour();
		int minutesDiff;

		if (out.getMinutes() < in.getMinutes()) {
			// if clock out minutes is less than clock in minutes,
			// then a full hour was not completed,
			// therefore decrease the hours worked by one and calculate the minutes
			--hourDiff;
			minutesDiff = ( 60 + out.getMinutes() ) - in.getMinutes();
		} else if (out.getMinutes() > in.getMinutes()) {
			// if clock out minutes is greater than clock in minutes,
			// then just calculate their difference
			minutesDiff = out.getMinutes() - in.getMinutes();
		} else {
			// else the minutes are the same and there is no difference
			minutesDiff = 0;
		}

		return hourDiff + ( minutesDiff / 60.0 );
	}

	/**
	 * Evaluate if the hour and minutes stored by this Time object are valid.
	 * {@code Hour} must be in the range [0, 24] and {@code minutes} be in the range [0, 59].
	 *
	 * @return true iff hours and minutes are in the valid range
	 */
	public boolean isValid () {
		return ( hour > -1 && hour < 25 ) && ( minutes > -1 && minutes < 60 );
	}

	/**
	 * @return the hour in 24h format
	 */
	public int getHour () {
		return hour;
	}

	/**
	 * @return the minutes
	 */
	public int getMinutes () {
		return minutes;
	}

	/**
	 * @return a human-readable String of the time stored in this instance in the format HH:MM
	 */
	@Override
	public String toString () {
		String readableTime = "";

		// adds the zero in front of the hour if needed
		if (hour < 10) {
			readableTime += ( "0" + hour );
		} else {
			readableTime += hour;
		}
		readableTime += ":";
		// adds the zero in front of the minute to display the time correctly
		if (minutes < 10) {
			readableTime += ( "0" + minutes );
		} else {
			readableTime += minutes;
		}

		return readableTime;
	}
}
