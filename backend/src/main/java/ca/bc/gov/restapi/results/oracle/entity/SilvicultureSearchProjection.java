package ca.bc.gov.restapi.results.oracle.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface SilvicultureSearchProjection {

  Long getOpeningId();

  String getOpeningNumber();

  String getCategory();

  String getStatus();

  String getCuttingPermitId();

  String getTimberMark();

  String getCutBlockId();

  BigDecimal getOpeningGrossArea();

  LocalDateTime getDisturbanceStartDate();

  String getForestFileId();

  String getOrgUnitCode();

  String getOrgUnitName();

  String getClientNumber();

  String getClientLocation();

  LocalDateTime getRegenDelayDate();

  LocalDateTime getEarlyFreeGrowingDate();

  LocalDateTime getLateFreeGrowingDate();

  LocalDateTime getUpdateTimestamp();

  String getEntryUserId();

  Long getSubmittedToFrpa108();
}
