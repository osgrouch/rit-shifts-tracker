package tracker.datetime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Class to test Time class with JUnit5. */
class TimeTest {
	@Nested
	@DisplayName ("Test getter methods")
	class TestGetters {
		@Nested
		@DisplayName ("Test Hour Getter")
		class TestHourGetter {
			@Test
			@DisplayName ("00:00 / Midnight")
			public void midnight () {
				Assertions.assertEquals(0, new Time("00:00").getHour());
			}

			@Test
			@DisplayName ("12:00 AM / Midnight")
			public void midnightAMPM () {
				Assertions.assertEquals(0, new Time("12:00 AM").getHour());
			}

			@Test
			@DisplayName ("01:00 / After Midnight")
			public void afterMidnight () {
				Assertions.assertEquals(1, new Time("01:00").getHour());
			}

			@Test
			@DisplayName ("1:00 AM / After Midnight / No Zero in Hour")
			public void afterMidnightWithoutZeroAMPM () {
				Assertions.assertEquals(1, new Time("1:00 AM").getHour());
			}

			@Test
			@DisplayName ("01:00 AM / After Midnight / Zero in Hour")
			public void afterMidnightWithZeroAMPM () {
				Assertions.assertEquals(1, new Time("01:00 AM").getHour());
			}

			@Test
			@DisplayName ("11:00 / Before Noon")
			public void beforeNoon () {
				Assertions.assertEquals(11, new Time("11:00").getHour());
			}

			@Test
			@DisplayName ("11:00 AM / Before Noon")
			public void beforeNoonAMPM () {
				Assertions.assertEquals(11, new Time("11:00 AM").getHour());
			}

			@Test
			@DisplayName ("12:00 / Noon")
			public void noon () {
				Assertions.assertEquals(12, new Time("12:00").getHour());
			}

			@Test
			@DisplayName ("12:00 PM / Noon")
			public void noonAMPM () {
				Assertions.assertEquals(12, new Time("12:00 PM").getHour());
			}

			@Test
			@DisplayName ("13:00 / After Noon")
			public void afterNoon () {
				Assertions.assertEquals(13, new Time("13:00").getHour());
			}

			@Test
			@DisplayName ("1:00 PM / After Noon / No Zero in Hour")
			public void afterNoonWithoutZeroAMPM () {
				Assertions.assertEquals(13, new Time("1:00 PM").getHour());
			}

			@Test
			@DisplayName ("01:00 PM / After Noon / Zero in Hour")
			public void afterNoonWithZeroAMPM () {
				Assertions.assertEquals(13, new Time("01:00 PM").getHour());
			}

			@Test
			@DisplayName ("23:00 / Before Midnight")
			public void beforeMidnight () {
				Assertions.assertEquals(23, new Time("23:00").getHour());
			}

			@Test
			@DisplayName ("11:00 PM / Before Midnight")
			public void beforeMidnightAMPM () {
				Assertions.assertEquals(23, new Time("11:00 PM").getHour());
			}
		}

		@Nested
		@DisplayName ("Test Minute Getter")
		class TestMinuteGetter {
			@Test
			@DisplayName ("00:00")
			public void startOfHour () {
				Assertions.assertEquals(0, new Time("00:00").getMinutes());
			}

			@Test
			@DisplayName ("00:30")
			public void halfHour () {
				Assertions.assertEquals(30, new Time("00:30").getMinutes());
			}

			@Test
			@DisplayName ("00:59")
			public void endOfHour () {
				Assertions.assertEquals(59, new Time("00:59").getMinutes());
			}
		}
	}

	@Nested
	@DisplayName ("Test toString method")
	class TestToString {
		@Test
		@DisplayName ("Time without 0s / 24h")
		public void noZeros24 () {
			Assertions.assertEquals("23:59", new Time("23:59").toString());
		}

		@Test
		@DisplayName ("Time without 0s / 12h")
		public void noZeros12 () {
			Assertions.assertEquals("23:59", new Time("11:59 PM").toString());
		}

		@Test
		@DisplayName ("Time with 0 hour / 24h")
		public void zeroHour24 () {
			Assertions.assertEquals("00:59", new Time("00:59").toString());
		}

		@Test
		@DisplayName ("Time with 0 hour / 12h")
		public void zeroHour12 () {
			Assertions.assertEquals("00:59", new Time("12:59 AM").toString());
		}

		@Test
		@DisplayName ("Time with 0 in front of hour / 24h")
		public void zeroInFrontOfHour24 () {
			Assertions.assertEquals("01:59", new Time("01:59").toString());
		}

		@Test
		@DisplayName ("Time with 0 in front of hour / 12h")
		public void zeroInFrontOfHour12 () {
			Assertions.assertEquals("01:59", new Time("01:59 AM").toString());
		}

		@Test
		@DisplayName ("Time with 0 in front of minute / 24h")
		public void zeroInFrontOfMinute24 () {
			Assertions.assertEquals("23:01", new Time("23:01").toString());
		}

		@Test
		@DisplayName ("Time with 0 minute / 24h")
		public void zeroMinute24 () {
			Assertions.assertEquals("23:00", new Time("23:00").toString());
		}

		@Test
		@DisplayName ("Time with 0 minute / 12h")
		public void zeroMinute12 () {
			Assertions.assertEquals("23:00", new Time("11:00 PM").toString());
		}

		@Test
		@DisplayName ("Time with 0 in front of minute / 12h")
		public void zeroInFrontOfMinute12 () {
			Assertions.assertEquals("23:01", new Time("11:01 PM").toString());
		}
	}
}
