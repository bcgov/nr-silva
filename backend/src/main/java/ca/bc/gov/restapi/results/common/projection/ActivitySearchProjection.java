package ca.bc.gov.restapi.results.common.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Projection interface used for activity search native queries. */
public interface ActivitySearchProjection {

  Long getActivityId();

  String getBaseCode();

  String getBaseDescription();

  String getTechniqueCode();

  String getTechniqueDescription();

  String getMethodCode();

  String getMethodDescription();

  Long getIsComplete();

  String getFundingSourceCode();

  String getFundingSourceDescription();

  String getFileId();

  String getCutBlock();

  Long getOpeningId();

  String getCuttingPermit();

  BigDecimal getTreatmentAmountArea();

  String getIntraAgencyNumber();

  String getOpeningCategoryCode();

  String getOpeningCategoryDescription();

  String getOrgUnitCode();

  String getOrgUnitDescription();

  String getOpeningClientCode();

  Long getTotalCount();

  LocalDateTime getUpdateTimestamp();
}
