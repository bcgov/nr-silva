package ca.bc.gov.restapi.results.common.projection;

import java.time.LocalDate;

public interface OrgUnitProjection {
  Long getOrgUnitNo();
  String getOrgUnitCode();
  String getOrgUnitName();
  String getLocationCode();
  String getAreaCode();
  String getTelephoneNo();
  Character getOrgLevelCode();
  String getOfficeNameCode();
  Long getRollupRegionNo();
  String getRollupRegionCode();
  Long getRollupDistNo();
  String getRollupDistCode();
  LocalDate getEffectiveDate();
  LocalDate getExpiryDate();
  LocalDate getUpdateTimestamp();
}
