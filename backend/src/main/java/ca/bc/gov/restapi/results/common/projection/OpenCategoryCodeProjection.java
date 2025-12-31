package ca.bc.gov.restapi.results.common.projection;

import java.time.LocalDate;

public interface OpenCategoryCodeProjection {
  String getCode();
  String getDescription();
  LocalDate getEffectiveDate();
  LocalDate getExpiryDate();
  LocalDate getUpdateTimestamp();
  boolean getIsExpired();
}
