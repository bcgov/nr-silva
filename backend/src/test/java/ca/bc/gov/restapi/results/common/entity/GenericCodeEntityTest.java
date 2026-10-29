package ca.bc.gov.restapi.results.common.entity;

import static org.assertj.core.api.Assertions.assertThat;

import ca.bc.gov.restapi.results.common.util.DateUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test | GenericCodeEntity")
class GenericCodeEntityTest {

  @Test
  @DisplayName("isEffective should return true when within effective and expiry date window")
  void isEffective_withinWindow_shouldReturnTrue() {
    LocalDate today = DateUtil.todayInVancouver();
    TestCodeEntity entity =
        TestCodeEntity.builder()
            .code("ACTIVE")
            .description("Active code")
            .effectiveDate(today.minusDays(10))
            .expiryDate(today.plusDays(10))
            .updateTimestamp(LocalDateTime.now())
            .build();

    assertThat(entity.isEffective()).isTrue();
    assertThat(entity.isExpired()).isFalse();
  }

  @Test
  @DisplayName("isEffective should return true when effective date is today and expiry date is today")
  void isEffective_onBoundariesToday_shouldReturnTrue() {
    LocalDate today = DateUtil.todayInVancouver();
    TestCodeEntity entity =
        TestCodeEntity.builder()
            .code("TODAY")
            .description("Effective and expiring today")
            .effectiveDate(today)
            .expiryDate(today)
            .updateTimestamp(LocalDateTime.now())
            .build();

    assertThat(entity.isEffective()).isTrue();
    assertThat(entity.isExpired()).isFalse();
  }

  @Test
  @DisplayName("isEffective should return false when effective date is in future")
  void isEffective_futureEffectiveDate_shouldReturnFalse() {
    LocalDate today = DateUtil.todayInVancouver();
    TestCodeEntity entity =
        TestCodeEntity.builder()
            .code("FUTURE")
            .description("Not yet active")
            .effectiveDate(today.plusDays(1))
            .expiryDate(today.plusYears(1))
            .updateTimestamp(LocalDateTime.now())
            .build();

    assertThat(entity.isEffective()).isFalse();
    assertThat(entity.isExpired()).isFalse();
  }

  @Test
  @DisplayName("isEffective should return false when expiry date is in past")
  void isEffective_pastExpiryDate_shouldReturnFalse() {
    LocalDate today = DateUtil.todayInVancouver();
    TestCodeEntity entity =
        TestCodeEntity.builder()
            .code("EXPIRED")
            .description("Expired code")
            .effectiveDate(today.minusYears(2))
            .expiryDate(today.minusDays(1))
            .updateTimestamp(LocalDateTime.now())
            .build();

    assertThat(entity.isEffective()).isFalse();
    assertThat(entity.isExpired()).isTrue();
  }

  @Test
  @DisplayName("isEffective should return true when dates are null or default max")
  void isEffective_nullDates_shouldHandleSafely() {
    TestCodeEntity entity =
        TestCodeEntity.builder()
            .code("NO_DATES")
            .description("No date limits")
            .effectiveDate(null)
            .expiryDate(null)
            .updateTimestamp(LocalDateTime.now())
            .build();

    assertThat(entity.isEffective()).isTrue();
    assertThat(entity.isExpired()).isFalse();
  }

  @Test
  @DisplayName("isEffective with explicit date should evaluate against that date")
  void isEffective_explicitDate_shouldEvaluateCorrectly() {
    LocalDate targetDate = LocalDate.of(2025, 6, 1);
    TestCodeEntity entity =
        TestCodeEntity.builder()
            .code("TARGET")
            .description("Target window")
            .effectiveDate(LocalDate.of(2025, 1, 1))
            .expiryDate(LocalDate.of(2025, 12, 31))
            .build();

    assertThat(entity.isEffective(targetDate)).isTrue();
    assertThat(entity.isEffective(LocalDate.of(2024, 12, 31))).isFalse();
    assertThat(entity.isEffective(LocalDate.of(2026, 1, 1))).isFalse();
    assertThat(entity.isEffective(null)).isFalse();
  }

  @SuperBuilder
  static class TestCodeEntity extends GenericCodeEntity {}
}
