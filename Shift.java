public class Shift {
	private Time in;
	private Time out;

	/**
	 * Create a new shift and set its starting and ending times.
	 * The start and end strings passed will be in the format "HH:MM AM/PM"
	 * 
	 * @param start the time clocked in
	 * @param end   the time clocked out
	 */
	public Shift(String start, String end, ) {
		// create the start shift with the given start string
		// which will always be in the format [HH:MM AM/PM]
		String[] startSplit = start.split(":\\s+");
		int startHour = Integer.parseInt(startSplit[0]);
		int startMinutes = Integer.parseInt(startSplit[1]);
		String startAmpm = startSplit[2];

		String[] endSplit = end.split(":\\s+");
		int endHour = Integer.parseInt(endSplit[0]);
		int endMinutes = Integer.parseInt(endSplit[1]);
		String endAmpm = endSplit[2];

		Time shiftStart = new Time(startHour, startMinutes, startAmpm);
		Time endShift = new Time(endHour, endMinutes, endAmpm);

		this.in = shiftStart;
		this.out = endShift;
	}
}