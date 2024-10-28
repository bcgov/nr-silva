package ca.bc.gov.restapi.results.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@DisplayName("Unit Test | Timestamp Util")
class TimestampUtilTest {

  @ParameterizedTest
  @MethodSource("startEndOfDay")
  @DisplayName("Date begin and end of day")
  void shouldGetStartOrEndOfDay(LocalDate date, boolean start){

    LocalDateTime dateTime = TimestampUtil.parseDateString(date, start ? LocalTime.MIN : LocalTime.MAX);

    Assertions.assertEquals(date, dateTime.toLocalDate());


    if(start){
      Assertions.assertEquals(0, dateTime.getHour());
      Assertions.assertEquals(0, dateTime.getMinute());
      Assertions.assertEquals(0, dateTime.getSecond());
      Assertions.assertEquals(0, dateTime.getNano());
    } else {
      Assertions.assertEquals(23, dateTime.getHour());
      Assertions.assertEquals(59, dateTime.getMinute());
      Assertions.assertEquals(59, dateTime.getSecond());
      Assertions.assertEquals(999999999, dateTime.getNano());
    }

  }

  @ParameterizedTest
  @NullAndEmptySource
  @DisplayName("Parse date string null or empty")
  void parseDateStringTest(String value) {
    LocalDateTime dateTimeNull = TimestampUtil.parseDateString(value);
    Assertions.assertNull(dateTimeNull);
  }

  @ParameterizedTest(name = "Parse date string {0} to {1}")
  @MethodSource("parseValuesString")
  @DisplayName("Parse date string")
  void parseDateStringTest(
      String dateStr, int year, int month, int day
  ) {
    LocalDateTime dateTimeParsed = TimestampUtil.parseDateString(dateStr);
    Assertions.assertNotNull(dateTimeParsed);
    Assertions.assertEquals(year, dateTimeParsed.getYear());
    Assertions.assertEquals(month, dateTimeParsed.getMonthValue());
    Assertions.assertEquals(day, dateTimeParsed.getDayOfMonth());
  }

  @ParameterizedTest(name = "Get local date time index for {1} months ago as {0}")
  @MethodSource("indexValues")
  @DisplayName("Get local date time index")
  void getLocalDateTimeIndexTest(
      int expectedIndex, long monthsAgo
  ) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime dateTime = now.minusMonths(monthsAgo);
    Assertions.assertEquals(expectedIndex, TimestampUtil.getLocalDateTimeIndex(dateTime));
  }

  private static Stream<Arguments> indexValues() {
    return Stream.of(
        Arguments.of(0, 1L),
        Arguments.of(0, 2L),
        Arguments.of(0, 3L),
        Arguments.of(0, 4L),
        Arguments.of(0, 5L),
        Arguments.of(1, 6L),
        Arguments.of(1, 7L),
        Arguments.of(1, 8L),
        Arguments.of(1, 9L),
        Arguments.of(1, 10L),
        Arguments.of(1, 11L),
        Arguments.of(2, 12L),
        Arguments.of(2, 13L),
        Arguments.of(2, 14L),
        Arguments.of(2, 15L),
        Arguments.of(2, 16L),
        Arguments.of(2, 17L),
        Arguments.of(3, 18L),
        Arguments.of(3, 36L)
    );
  }

  private static Stream<Arguments> parseValuesString(){
    return Stream.of(
        Arguments.of("2024-04-09", 2024, 4, 9),
        Arguments.of("1987-01-01", 1987,1,1),
        Arguments.of("2022-12-31", 2022,12,31),
        Arguments.of("2023-02-28", 2023,2,28),
        Arguments.of("2024-02-29", 2024,2,29)
    );
  }

  private static Stream<Arguments> startEndOfDay(){
    return Stream.of(
        Arguments.of(LocalDate.of(2024, 4, 9), true),
        Arguments.of(LocalDate.of(1987, 1, 1), true),
        Arguments.of(LocalDate.of(2022, 12, 31), true),
        Arguments.of(LocalDate.of(2023, 2, 28), true),
        Arguments.of(LocalDate.of(2024, 2, 29), true),
        Arguments.of(LocalDate.of(2024, 4, 9), false),
        Arguments.of(LocalDate.of(1987, 1, 1), false),
        Arguments.of(LocalDate.of(2022, 12, 31), false),
        Arguments.of(LocalDate.of(2023, 2, 28), false),
        Arguments.of(LocalDate.of(2024, 2, 29), false)
    );
  }
}
