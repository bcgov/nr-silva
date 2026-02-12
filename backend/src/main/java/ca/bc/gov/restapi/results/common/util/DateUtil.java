package ca.bc.gov.restapi.results.common.util;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

  /**
   * Validates that the end date is the same or after the start date.
   *
   * @param start the start date string (yyyy-MM-dd format)
   * @param end the end date string (yyyy-MM-dd format)
   * @throws ResponseStatusException if end date is before start date
   */
  public static void validateDateRange(String start, String end) {
    if (start == null || end == null) {
      return;
    }
    LocalDate startDate = LocalDate.parse(start);
    LocalDate endDate = LocalDate.parse(end);
    if (endDate.isBefore(startDate)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "End date must be the same or after start date");
    }
  }
}
