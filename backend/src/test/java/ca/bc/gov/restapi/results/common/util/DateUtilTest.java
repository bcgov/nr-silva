package ca.bc.gov.restapi.results.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

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
    assertThrows(
        Exception.class,
        () -> DateUtil.validateDateRange("invalid-date", "2026-12-31"));
  }

  @Test
  @DisplayName("validateDateRange | start invalid format | should throw exception")
  void validateDateRange_startInvalidFormat_shouldThrowException() {
    assertThrows(
        Exception.class,
        () -> DateUtil.validateDateRange("01-01-2026", "2026-12-31"));
  }

  @Test
  @DisplayName("validateDateRange | end invalid format | should throw exception")
  void validateDateRange_endInvalidFormat_shouldThrowException() {
    assertThrows(
        Exception.class,
        () -> DateUtil.validateDateRange("2026-01-01", "12/31/2026"));
  }

  @Test
  @DisplayName("validateDateRange | leap year date | should pass")
  void validateDateRange_leapYearDate_shouldPass() {
    assertDoesNotThrow(() -> DateUtil.validateDateRange("2024-02-29", "2024-12-31"));
  }

  @Test
  @DisplayName("validateDateRange | single day range | should pass")
  void validateDateRange_singleDay_shouldPass() {
    assertDoesNotThrow(() -> DateUtil.validateDateRange("2026-06-15", "2026-06-15"));
  }

  @Test
  @DisplayName("validateDateRange | multi-year range | should pass")
  void validateDateRange_multiYearRange_shouldPass() {
    assertDoesNotThrow(() -> DateUtil.validateDateRange("2020-01-01", "2030-12-31"));
  }
}
