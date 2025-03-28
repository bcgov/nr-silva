package ca.bc.gov.restapi.results.oracle.entity.opening;

import java.time.LocalDate;

public interface OpeningTombstoneProjection {

  Long getOpeningId();

  String getOpeningNumber();

  String getOpeningStatusCode();

  String getOpeningStatusDesc();

  String getOpeningType();

  String getOrgUnitCode();

  String getOrgUnitName();

  String getOpenCategoryCode();

  String getOpenCategoryDesc();

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

  String getLicenseeOpeningId();

  String getTenureType();

  String getManagementUnitType();

  String getManagementUnitId();

  String getTimberSalesOffice();

  String getCommentType();

  LocalDate getMilestonePostHarverstDeclaredDate();

  LocalDate getMilestoneRegenDeclaredDate();

  String getMilestoneRegenOffset();

  LocalDate getMilestoneRegenDueDate();

  LocalDate getMilestoneFreeGrowingDeclaredDate();

  String getMilestoneFreeGrowingOffset();

  LocalDate getMilestoneFreeGrowingDueDate();/**/

}
