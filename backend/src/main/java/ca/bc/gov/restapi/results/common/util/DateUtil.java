package ca.bc.gov.restapi.results.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

  private static final ZoneId VANCOUVER = ZoneId.of("America/Vancouver");

  /**
   * Returns true if the given expiry date is in the past (relative to today in Vancouver time).
   * Returns false if the expiry date is null.
   */
  public static boolean isExpired(LocalDateTime expiryDate) {
    if (expiryDate == null) {
      return false;
    }
    return expiryDate.toLocalDate().isBefore(LocalDate.now(VANCOUVER));
  }

  /**
   * Validates that the end date is the same or after the start date.
   *
   * @param start the start date string (yyyy-MM-dd format)
   * @param end the end date string (yyyy-MM-dd format)
   * @throws ResponseStatusException if inputs are invalid (bad format) or end date is before start
   *     date
   */
  public static void validateDateRange(String start, String end) {
    if (start == null || end == null) {
      return;
    }
    try {
      LocalDate startDate = LocalDate.parse(start);
      LocalDate endDate = LocalDate.parse(end);
      if (endDate.isBefore(startDate)) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "End date must be the same or after start date");
      }
    } catch (DateTimeParseException e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Invalid date format. Expected yyyy-MM-dd");
    }
  }
}
