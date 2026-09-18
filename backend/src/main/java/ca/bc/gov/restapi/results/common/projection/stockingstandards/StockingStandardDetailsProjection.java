package ca.bc.gov.restapi.results.common.projection.stockingstandards;

import java.time.LocalDateTime;

/** Header values for a stocking standard detail response. */
public interface StockingStandardDetailsProjection {
  Long getStockingStandardId();

  String getStatusCode();

  LocalDateTime getCreatedDate();

  LocalDateTime getEffectiveDate();

  LocalDateTime getExpiryDate();

  String getObjective();

  String getName();

  String getLocation();

  String getMinistryDefaultInd();

  String getAlternativeMethodSelected();

  String getRegenObligationInd();

  Integer getRegenDelayYears();

  Integer getFreeGrowingEarlyYears();

  Integer getFreeGrowingLateYears();

  Integer getEarlyYears();

  Integer getLateYears();

  String getAdditionalStandards();
}
