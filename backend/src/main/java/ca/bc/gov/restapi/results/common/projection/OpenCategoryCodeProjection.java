package ca.bc.gov.restapi.results.common.projection;

import java.time.LocalDate;

public interface OpenCategoryCodeProjection {
  String getCode();
  String getDescription();
  LocalDate getEffectiveDate();
  LocalDate getExpiryDate();
  LocalDate getUpdateTimestamp();
  default boolean getIsExpired() {
    LocalDate expiryDate = getExpiryDate();
    return expiryDate != null && LocalDate.now().isAfter(expiryDate);
  }
}
