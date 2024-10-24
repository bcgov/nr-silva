package ca.bc.gov.restapi.results.oracle.entity;

import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OpeningSearchProjection {

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


  default OpeningCategoryEnum getFinalCategory() {
    return getCategory() != null ? OpeningCategoryEnum.of(getCategory()) : null;
  }

  default OpeningStatusEnum getFinalStatus() {
    return getStatus() != null ? OpeningStatusEnum.of(getStatus()) : null;
  }

  default boolean getSubmittedToFrpa() {
    return getSubmittedToFrpa108() > 0;
  }

  default long getSilvaReliefAppId() {
    return getSubmittedToFrpa108();
  }

}
