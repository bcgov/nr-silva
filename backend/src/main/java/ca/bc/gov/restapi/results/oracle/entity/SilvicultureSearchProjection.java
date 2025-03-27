package ca.bc.gov.restapi.results.oracle.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface SilvicultureSearchProjection {

  Long getOpeningId();

  Long getTotalCount();

  String getOpeningNumber();

  String getCategory();

  String getStatus();

  String getCuttingPermitId();

  String getTimberMark();

  String getCutBlockId();

  BigDecimal getOpeningGrossArea();

  LocalDate getDisturbanceStartDate();

  String getForestFileId();

  String getOrgUnitCode();

  String getOrgUnitName();

  String getClientNumber();

  String getClientLocation();

  LocalDate getRegenDelayDate();
  LocalDate getEarlyFreeGrowingDate();
  LocalDate getLateFreeGrowingDate();

  LocalDateTime getUpdateTimestamp();

  String getEntryUserId();

  Long getSubmittedToFrpa108();
}
