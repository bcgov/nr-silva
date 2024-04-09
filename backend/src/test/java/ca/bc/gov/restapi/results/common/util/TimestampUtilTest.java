package ca.bc.gov.restapi.results.common.util;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TimestampUtilTest {

  @Test
  void parseDateStringTest() {
    LocalDateTime dateTimeParsed = TimestampUtil.parseDateString("2024-04-09");
    Assertions.assertNotNull(dateTimeParsed);
    Assertions.assertEquals(2024, dateTimeParsed.getYear());
    Assertions.assertEquals(4, dateTimeParsed.getMonthValue());
    Assertions.assertEquals(9, dateTimeParsed.getDayOfMonth());

    LocalDateTime dateTimeNull = TimestampUtil.parseDateString(null);
    Assertions.assertNull(dateTimeNull);
  }

  @Test
  void getLocalDateTimeIndexTest() {
    LocalDateTime now = LocalDateTime.now();

    Assertions.assertEquals(0, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(1L)));
    Assertions.assertEquals(0, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(2L)));
    Assertions.assertEquals(0, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(3L)));
    Assertions.assertEquals(0, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(4L)));
    Assertions.assertEquals(0, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(5L)));
    Assertions.assertEquals(1, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(6L)));
    Assertions.assertEquals(1, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(7L)));
    Assertions.assertEquals(1, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(8L)));
    Assertions.assertEquals(1, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(9L)));
    Assertions.assertEquals(1, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(10L)));
    Assertions.assertEquals(1, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(11L)));
    Assertions.assertEquals(2, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(12L)));
    Assertions.assertEquals(2, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(13L)));
    Assertions.assertEquals(2, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(14L)));
    Assertions.assertEquals(2, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(15L)));
    Assertions.assertEquals(2, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(16L)));
    Assertions.assertEquals(2, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(17L)));
    Assertions.assertEquals(3, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(18L)));
    Assertions.assertEquals(3, TimestampUtil.getLocalDateTimeIndex(now.minusMonths(36L)));
  }
}
