package ca.bc.gov.restapi.results.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 * This class contains useful methods for parsing and handling timestamps.
 */
public class TimestampUtil {

  private TimestampUtil() {
  }

  /**
   * Parses a date string to a {@link LocalDateTime} instance. Format: yyyy-MM-dd.
   *
   * @param dateStr The date to be parsed
   * @return LocalDateTime parsed or null if a null value is found.
   */
  public static LocalDateTime parseDateString(String dateStr) {
    if (StringUtils.isEmpty(dateStr)) {
      return null;
    }
    return parseDateString(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
  }

/**
 * Parses a LocalDate to a LocalDateTime instance with the time set to LocalTime.MIN.
 *
 * @param localDate The LocalDate to be parsed
 * @return LocalDateTime parsed or null if a null value is found.
 */
public static LocalDateTime parseDateString(LocalDate localDate) {
  return parseDateString(localDate, LocalTime.MIN);
}

/**
 * Parses a LocalDate to a LocalDateTime instance with the specified time.
 *
 * @param localDate The LocalDate to be parsed
 * @param time The LocalTime to be set
 * @return LocalDateTime parsed or null if a null value is found.
 */
public static LocalDateTime parseDateString(LocalDate localDate, LocalTime time) {
  if (Objects.isNull(localDate)) {
    return null;
  }
  return localDate.atTime(time);
}

  /**
   * Extract the number based on the difference between today and the day the Opening got created.
   *
   * @param entryLocalDateTime The LocalDateTime representing the opening creation timestamp.
   * @return An integer representing the index
   */
  public static int getLocalDateTimeIndex(LocalDateTime entryLocalDateTime) {
    // index 0 -> 0 to 5 months
    // index 1 -> 6 to 11 months
    // index 2 -> 12 to 17 months
    // index 3 -> 18+ months
    LocalDate entryLocalDate = entryLocalDateTime.toLocalDate();
    LocalDate now = LocalDate.now();

    Period diff = Period.between(entryLocalDate, now);
    int totalMonths = diff.getMonths() + (diff.getYears() * 12);
    if (totalMonths <= 5) {
      return 0;
    } else if (totalMonths <= 11) {
      return 1;
    } else if (totalMonths <= 17) {
      return 2;
    } else {
      return 3;
    }
  }
}
