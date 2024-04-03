package ca.bc.gov.restapi.results.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
}
