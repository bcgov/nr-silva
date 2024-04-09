package ca.bc.gov.restapi.results.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/** This class contains useful methods for parsing and handling timestamps. */
public class TimestampUtil {

  /**
   * Parses a date string to a {@link LocalDateTime} instance. Format: yyyy-MM-dd.
   *
   * @param dateStr The date to be parsed
   * @return LocalDateTime parsed or null if a null value is found.
   */
  public static LocalDateTime parseDateString(String dateStr) {
    if (Objects.isNull(dateStr)) {
      return null;
    }

    LocalDate entryDateStartLd =
        LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    return entryDateStartLd.atStartOfDay();
  }

  /**
   * Extract the number based on the difference between today and the day the Opening got created.
   *
   * @param entryLocalDateTime The LocalDateTime representing the opening creation timestamp.
   * @return
   */
  public static int getLocalDateTimeIndex(LocalDateTime entryLocalDateTime) {
    // index 0 -> 0 to 5 months
    // index 1 -> 6 to 11 months
    // index 2 -> 12 to 17 months
    // index 3 -> 18+ months
    LocalDate entryLocalDate = entryLocalDateTime.toLocalDate();
    LocalDate now = LocalDate.now();

    Period diff = Period.between(entryLocalDate, now);
    int months = diff.getMonths();
    if (months <= 5) {
      return 0;
    } else if (months <= 11) {
      return 1;
    } else if (months <= 17) {
      return 2;
    } else {
      return 3;
    }
  }
}
