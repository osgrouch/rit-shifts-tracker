package tracker.datetime;

import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Test the CalendarDate class with JUnit. */
public class CalendarDateTest extends TestCase {

	/** Test the splitDateIntoInt method of CalendarDate. */
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

	/** Test the isValid method of CalendarDate. */
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
				Assertions.assertThrows(NullPointerException.class, () -> new CalendarDate(-1, 1, 2022).isValid());
			}

			@Test
			@DisplayName ("13th month")
			public void thirteenMonth () {
				Assertions.assertThrows(NullPointerException.class, () -> new CalendarDate(13, 1, 2022).isValid());
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

}
