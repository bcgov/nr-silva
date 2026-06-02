package ca.bc.gov.restapi.results.common.util;

import static org.junit.jupiter.api.Assertions.*;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@DisplayName("Unit Test | DateUtil")
class DateUtilTest {

  @Test
  @DisplayName("validateDateRange | valid dates (end after start) | should pass")
  void validateDateRange_validDates_shouldPass() {
    assertDoesNotThrow(() -> DateUtil.validateDateRange("2026-01-01", "2026-12-31"));
  }

  @Test
  @DisplayName("validateDateRange | equal dates | should pass")
  void validateDateRange_equalDates_shouldPass() {
    assertDoesNotThrow(() -> DateUtil.validateDateRange("2026-06-15", "2026-06-15"));
  }

  @Test
  @DisplayName("validateDateRange | end before start | should throw exception")
  void validateDateRange_endBeforeStart_shouldThrowException() {
    ResponseStatusException ex =
        assertThrows(
            ResponseStatusException.class,
            () -> DateUtil.validateDateRange("2026-12-31", "2026-01-01"));
    assertEquals("End date must be the same or after start date", ex.getReason());
  }

  @Test
  @DisplayName("validateDateRange | start is null | should not throw")
  void validateDateRange_startNull_shouldNotThrow() {
    assertDoesNotThrow(() -> DateUtil.validateDateRange(null, "2026-12-31"));
  }

  @Test
  @DisplayName("validateDateRange | end is null | should not throw")
  void validateDateRange_endNull_shouldNotThrow() {
    assertDoesNotThrow(() -> DateUtil.validateDateRange("2026-01-01", null));
  }

  @Test
  @DisplayName("validateDateRange | both dates null | should not throw")
  void validateDateRange_bothNull_shouldNotThrow() {
    assertDoesNotThrow(() -> DateUtil.validateDateRange(null, null));
  }

  @Test
  @DisplayName("validateDateRange | invalid date format | should throw exception")
  void validateDateRange_invalidDateFormat_shouldThrowException() {
    ResponseStatusException ex =
        assertThrows(
            ResponseStatusException.class,
            () -> DateUtil.validateDateRange("invalid-date", "2026-12-31"));
    assertEquals("Invalid date format. Expected yyyy-MM-dd", ex.getReason());
    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
  }

  @Test
  @DisplayName("validateDateRange | start invalid format | should throw exception")
  void validateDateRange_startInvalidFormat_shouldThrowException() {
    ResponseStatusException ex =
        assertThrows(
            ResponseStatusException.class,
            () -> DateUtil.validateDateRange("01-01-2026", "2026-12-31"));
    assertEquals("Invalid date format. Expected yyyy-MM-dd", ex.getReason());
    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
  }

  @Test
  @DisplayName("validateDateRange | end invalid format | should throw exception")
  void validateDateRange_endInvalidFormat_shouldThrowException() {
    ResponseStatusException ex =
        assertThrows(
            ResponseStatusException.class,
            () -> DateUtil.validateDateRange("2026-01-01", "12/31/2026"));
    assertEquals("Invalid date format. Expected yyyy-MM-dd", ex.getReason());
    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
  }

  @Test
  @DisplayName("validateDateRange | leap year date | should pass")
  void validateDateRange_leapYearDate_shouldPass() {
    assertDoesNotThrow(() -> DateUtil.validateDateRange("2024-02-29", "2024-12-31"));
  }

  @Test
  @DisplayName("validateDateRange | multi-year range | should pass")
  void validateDateRange_multiYearRange_shouldPass() {
    assertDoesNotThrow(() -> DateUtil.validateDateRange("2020-01-01", "2030-12-31"));
  }

  // -------------------------------------------------------------------------
  // isStockingStandardsExpired
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("isStockingStandardsExpired | null date | returns false")
  void isStockingStandardsExpired_nullDate_returnsFalse() {
    assertFalse(DateUtil.isExpired(null));
  }

  @Test
  @DisplayName("isStockingStandardsExpired | past date | returns true")
  void isStockingStandardsExpired_pastDate_returnsTrue() {
    LocalDateTime pastDate =
        LocalDate.now(ZoneId.of(SilvaConstants.VANCOUVER_ZONE_ID)).minusDays(1).atStartOfDay();
    assertTrue(DateUtil.isExpired(pastDate));
  }

  @Test
  @DisplayName("isStockingStandardsExpired | future date | returns false")
  void isStockingStandardsExpired_futureDate_returnsFalse() {
    LocalDateTime futureDate =
        LocalDate.now(ZoneId.of(SilvaConstants.VANCOUVER_ZONE_ID)).plusYears(1).atStartOfDay();
    assertFalse(DateUtil.isExpired(futureDate));
  }
}
