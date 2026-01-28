package ca.bc.gov.restapi.results.common.projection.opening;

import java.time.LocalDate;

public interface OpeningTombstoneProjection {

  Long getOpeningId();

  String getOpeningNumber();

  String getOpeningStatusCode();

  String getOpeningStatusName();

  Long getOrgUnitNumber();

  String getOrgUnitCode();

  String getOrgUnitName();

  String getOpenCategoryCode();
  String getOpenCategoryName();

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
