package ca.bc.gov.restapi.results.oracle.entity.opening;

import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import java.time.LocalDate;

public interface OpeningTombstoneProjection {

  Long getOpeningId();

  String getOpeningNumber();

  OpeningStatusEnum getOpeningStatus();

  String getOrgUnitCode();

  String getOrgUnitName();

  OpeningCategoryEnum getOpenCategory();

  String getClient();

  String getFileId();

  String getCutBlockID();

  String getCuttingPermitId();

  String getTimberMark();

  String getMaxAllowedAccess();

  Float getOpeningGrossArea();

  String getCreatedBy();

  LocalDate getCreatedOn();

  LocalDate getLastUpdatedOn();

  LocalDate getDisturbanceStartDate();

}
