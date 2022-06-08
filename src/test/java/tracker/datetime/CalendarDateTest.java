package tracker.datetime;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Class to test CalendarDate class with JUnit5. */
class CalendarDateTest {
	/** Test the splitDateIntoInt method of CalendarDate class. */
	@Nested
	@DisplayName ("Test splitDateIntoInto method")
	class TestSplitDateIntoInt {
		/** Test different valid dates are split correctly. */
		@Nested
		@DisplayName ("Valid dates")
		class TestValidDates {
			@Test
			@DisplayName ("2/22/2022 is valid")
			public void twosDay () {
				Assertions.assertArrayEquals(new int[]{ 2, 22, 2022 }, CalendarDate.splitDateIntoInt("2/22/2022"));
			}

			@Test
			@DisplayName ("8/1/1900 is valid")
			public void past () {
				Assertions.assertArrayEquals(new int[]{ 8, 1, 1900 }, CalendarDate.splitDateIntoInt("8/1/1900"));

			}

			@Test
			@DisplayName ("1/1/2050 is valid")
			public void future () {
				Assertions.assertArrayEquals(new int[]{ 1, 1, 2050 }, CalendarDate.splitDateIntoInt("1/1/2050"));
			}
		}

		/**
		 * Test different correctly formatted inputs. The method does not check the given date is correct, it only
		 * splits the given string by parenthesis.
		 */
		@Nested
		@DisplayName ("Correctly formatted input")
		class TestWeirdButValidInputs {
			@Test
			@DisplayName ("0/0/0 is acceptable")
			public void zeros () {
				Assertions.assertArrayEquals(new int[]{ 0, 0, 0 }, CalendarDate.splitDateIntoInt("0/0/0"));
			}

			@Test
			@DisplayName ("-1/-1/-1 is acceptable")
			public void negatives () {
				Assertions.assertArrayEquals(new int[]{ -1, -1, -1 }, CalendarDate.splitDateIntoInt("-1/-1/-1"));
			}

			@Test
			@DisplayName ("2022/31/3 is acceptable")
			public void backwards () {
				Assertions.assertArrayEquals(new int[]{ 2022, 31, 3 }, CalendarDate.splitDateIntoInt("2022/31/3"));
			}

			@Test
			@DisplayName ("MM/DD/YYYY is not acceptable")
			public void letters () {
				Assertions.assertThrows(IllegalArgumentException.class,
				                        () -> CalendarDate.splitDateIntoInt("MM/DD/YYYY"));
			}
		}

		/** Test different incorrectly formatted inputs. */
		@Nested
		@DisplayName ("Incorrectly formatted input")
		class TestIncorrectlyFormattedInputs {
			@Test
			@DisplayName ("1-1-2022 throws IllegalArgumentException")
			public void dashes () {
				Assertions.assertThrows(IllegalArgumentException.class,
				                        () -> CalendarDate.splitDateIntoInt("1-1-2022"));
			}

			@Test
			@DisplayName ("1 1 2022 throws IllegalArgumentException")
			public void spaces () {
				Assertions.assertThrows(IllegalArgumentException.class,
				                        () -> CalendarDate.splitDateIntoInt("1 1 2022"));
			}

			@Test
			@DisplayName ("MM/DD/YYYY throws IllegalArgumentException")
			public void letters () {
				Assertions.assertThrows(IllegalArgumentException.class,
				                        () -> CalendarDate.splitDateIntoInt("MM/DD/YYYY"));
			}

			@Test
			@DisplayName ("JAN 1, 2022 throws IllegalArgumentException")
			public void correctDateInWrongFormat () {
				Assertions.assertThrows(IllegalArgumentException.class,
				                        () -> CalendarDate.splitDateIntoInt("JAN 1, 2022"));
			}
		}
	}

	@Nested
	@DisplayName ("Test jumpAhead method")
	class TestJumpAhead {
		// SECTION: Jump ahead with single effective argument.
		@Test
		@DisplayName ("0 MONTHS / 13 DAYS / 0 YEARS")
		public void daysOnly () {
			CalendarDate date = new CalendarDate(1, 1, 2022);
			CalendarDate newDate = date.jumpAhead(0, 13, 0);
			Assertions.assertEquals("JAN 14, 2022", newDate.toString());
		}

		@Test
		@DisplayName ("4 MONTHS / 0 DAYS / 0 YEARS")
		public void monthsOnly () {
			CalendarDate date = new CalendarDate(1, 1, 2022);
			CalendarDate newDate = date.jumpAhead(4, 0, 0);
			Assertions.assertEquals("MAY 01, 2022", newDate.toString());
		}

		@Test
		@DisplayName ("0 MONTHS / 0 DAYS / 4 YEARS")
		public void yearsOnly () {
			CalendarDate date = new CalendarDate(1, 1, 2022);
			CalendarDate newDate = date.jumpAhead(0, 0, 4);
			Assertions.assertEquals("JAN 01, 2026", newDate.toString());
		}
		// END-SECTION

		// SECTION: Jump ahead with multiple effective arguments.
		@Test
		@DisplayName ("4 MONTHS / 13 DAYS / 0 YEARS")
		public void monthAndDay () {
			CalendarDate date = new CalendarDate(1, 1, 2022);
			CalendarDate newDate = date.jumpAhead(4, 13, 0);
			Assertions.assertEquals("MAY 14, 2022", newDate.toString());
		}

		@Test
		@DisplayName ("4 MONTHS / 13 DAYS / 22 YEARS")
		public void monthDayYear () {
			CalendarDate date = new CalendarDate(1, 1, 2022);
			CalendarDate newDate = date.jumpAhead(4, 13, 22);
			Assertions.assertEquals("MAY 14, 2044", newDate.toString());
		}
		// END-SECTION

		// SECTION: Test jumping years without specifying it in the year argument.
		@Test
		@DisplayName ("12 MONTHS / 0 DAYS / 0 YEARS")
		public void twelveMonths () {
			CalendarDate date = new CalendarDate(1, 1, 2022);
			CalendarDate newDate = date.jumpAhead(12, 0, 0);
			Assertions.assertEquals("JAN 01, 2023", newDate.toString());
		}

		@Test
		@DisplayName ("0 MONTHS / 365 DAYS / 0 YEARS")
		public void threeSixtyFiveDays () {
			CalendarDate date = new CalendarDate(1, 1, 2022);
			CalendarDate newDate = date.jumpAhead(0, 365, 0);
			Assertions.assertEquals("JAN 01, 2023", newDate.toString());
		}

		@Test
		@DisplayName ("0 MONTHS / 730 DAYS / 0 YEARS")
		public void sevenThirtyDays () {
			CalendarDate date = new CalendarDate(1, 1, 2022);
			CalendarDate newDate = date.jumpAhead(0, 730, 0);
			Assertions.assertEquals("JAN 01, 2024", newDate.toString());
		}

		@Test
		@DisplayName ("12 MONTHS / 365 DAYS / 0 YEARS")
		public void twoYearsInMonthsAndDays () {
			CalendarDate date = new CalendarDate(1, 1, 2022);
			CalendarDate newDate = date.jumpAhead(12, 365, 0);
			Assertions.assertEquals("JAN 01, 2024", newDate.toString());
		}
		// END-SECTION

		// SECTION: Jump ahead by random values to find any errors.
		@Test
		@DisplayName ("15 MONTHS / 0 DAYS / 0 YEARS")
		public void fifteenMonths () {
			CalendarDate date = new CalendarDate(1, 1, 2022);
			CalendarDate newDate = date.jumpAhead(15, 0, 0);
			Assertions.assertEquals("APR 01, 2023", newDate.toString());
		}

		@Test
		@DisplayName ("15 MONTHS / 30 DAYS / 0 YEARS")
		public void fifteenMonthsThirtyDays () {
			CalendarDate date = new CalendarDate(1, 1, 2022);
			CalendarDate newDate = date.jumpAhead(15, 30, 0);
			Assertions.assertEquals("MAY 01, 2023", newDate.toString());
		}

		@Test
		@DisplayName ("30 MONTHS / 365 DAYS / 0 YEARS")
		public void threeYearsAndAHalf () {
			CalendarDate date = new CalendarDate(1, 1, 2022);
			CalendarDate newDate = date.jumpAhead(30, 365, 0);
			Assertions.assertEquals("JUL 01, 2025", newDate.toString());
		}

		@Test
		@DisplayName ("0 MONTHS / 31 DAYS / 0 YEARS")
		public void oneMonthInDays () {
			CalendarDate date = new CalendarDate(1, 1, 2022);
			CalendarDate newDate = date.jumpAhead(0, 31, 0);
			Assertions.assertEquals("FEB 01, 2022", newDate.toString());
		}

		@Test
		@DisplayName ("0 MONTHS / 59 DAYS / 0 YEARS")
		public void twoMonthsInDays () {
			CalendarDate date = new CalendarDate(1, 1, 2022);
			CalendarDate newDate = date.jumpAhead(0, 59, 0);
			Assertions.assertEquals("MAR 01, 2022", newDate.toString());
		}

		@Test
		@DisplayName ("0 MONTHS / 62 DAYS / 0 YEARS")
		public void twoMonthsInDaysFromJuly () {
			CalendarDate date = new CalendarDate(7, 1, 2022);
			CalendarDate newDate = date.jumpAhead(0, 62, 0);
			Assertions.assertEquals("SEP 01, 2022", newDate.toString());
		}
		// END-SECTION
	}

	/** Test the isValid method of CalendarDate class. */
	@Nested
	@DisplayName ("Test isValid method")
	class TestIsValid {
		/**
		 * Test valid dates are indeed valid. Checks all the valid edge values checked in isValid method.
		 */
		@Nested
		@DisplayName ("Valid CalendarDate instances")
		class TestValidDates {
			@Test
			@DisplayName ("First month in valid range")
			public void january () {
				Assertions.assertTrue(new CalendarDate(1, 1, 2022).isValid());
			}

			@Test
			@DisplayName ("Last month in valid range")
			public void december () {
				Assertions.assertTrue(new CalendarDate(12, 1, 2022).isValid());
			}

			@Test
			@DisplayName ("31st day")
			public void thirtyOne () {
				Assertions.assertTrue(new CalendarDate(1, 31, 2022).isValid());
			}

			@Test
			@DisplayName ("30th day")
			public void thirty () {
				Assertions.assertTrue(new CalendarDate(4, 30, 2022).isValid());
			}

			@Test
			@DisplayName ("29th day")
			public void twentyNine () {
				Assertions.assertTrue(new CalendarDate(2, 29, 2022).isValid());
			}
		}

		/**
		 * Test valid dates are indeed invalid. Checks all the invalid edge values checked in isValid method.
		 */
		@Nested
		@DisplayName ("Invalid CalendarDate instances")
		class TestInvalidDates {
			@Test
			@DisplayName ("Negative month")
			public void negativeMonth () {
				Assertions.assertFalse(new CalendarDate(-1, 1, 2022).isValid());
			}

			@Test
			@DisplayName ("13th month")
			public void thirteenMonth () {
				Assertions.assertFalse(new CalendarDate(13, 1, 2022).isValid());
			}

			@Test
			@DisplayName ("32nd day of January")
			public void thirtyTwo () {
				Assertions.assertFalse(new CalendarDate(1, 32, 2022).isValid());
			}

			@Test
			@DisplayName ("31st day of April")
			public void thirtyOne () {
				Assertions.assertFalse(new CalendarDate(4, 31, 2022).isValid());
			}

			@Test
			@DisplayName ("30th day of February")
			public void thirty () {
				Assertions.assertFalse(new CalendarDate(2, 30, 2022).isValid());
			}

			@Test
			@DisplayName ("Negative year")
			public void twentyNine () {
				Assertions.assertFalse(new CalendarDate(1, 1, -1).isValid());
			}
		}
	}

	/**
	 * Test the toString method of CalendarDate class. Also works as constructor check by
	 * checking that the same dates creating in the 3 different formats all print the correct
	 * toString, which is set by the private fields set in the constructor.
	 */
	@Nested
	@DisplayName ("Test toString method")
	class TestToString {
		/** ArrayList of 3-letter month codes, used in testMonths method. */
		private final List<String> months = new ArrayList<>(
			Arrays.asList("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"));

		/** Test there is a zero added in front of any digit less than 10. Assumes no valid CalendarDate. */
		@Test
		@DisplayName ("Zero in front of day < 10")
		public void addZero () {
			Assertions.assertEquals("JAN 01, 2022", new CalendarDate(1, 1, 2022).toString());
		}

		/** Test every month prints correctly when creating instances with integer arguments. */
		@DisplayName ("15th of every month / int constructor")
		@RepeatedTest (value = 12, name = "{currentRepetition}/15/2022")
		public void testMonthsInt (RepetitionInfo repetitionInfo) {
			String currMonth = months.get(repetitionInfo.getCurrentRepetition() - 1);
			CalendarDate date = new CalendarDate(repetitionInfo.getCurrentRepetition(), 15, 2022);
			Assertions.assertEquals(currMonth + " 15, 2022", date.toString());
		}

		/** Test every month prints correctly when creating instances with String argument in the format MM/DD/YYYY. */
		@DisplayName ("15th of every month / String constructor / MM/DD/YYYY")
		@RepeatedTest (value = 12, name = "{currentRepetition}/15/2022")
		public void testMonthsStringMMDDYYYY (RepetitionInfo repetitionInfo) {
			String currMonth = months.get(repetitionInfo.getCurrentRepetition() - 1);
			CalendarDate date = new CalendarDate(repetitionInfo.getCurrentRepetition() + "/15/2022");
			Assertions.assertEquals(currMonth + " 15, 2022", date.toString());
		}

		/** Test every month prints correctly when creating instances with String argument in the format MMM DD, YYYY. */
		@DisplayName ("15th of every month / String constructor / MMM DD, YYYY")
		@RepeatedTest (value = 12, name = "{currentRepetition}/15/2022")
		public void testMonthsStringMMMDDYYYY (RepetitionInfo repetitionInfo) {
			String currMonth = months.get(repetitionInfo.getCurrentRepetition() - 1);
			CalendarDate date = new CalendarDate(currMonth + " 15, 2022");
			Assertions.assertEquals(currMonth + " 15, 2022", date.toString());
		}
	}
}
