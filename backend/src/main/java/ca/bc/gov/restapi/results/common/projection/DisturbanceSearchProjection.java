package ca.bc.gov.restapi.results.common.projection;

import java.time.LocalDateTime;

/** Projection interface used for disturbance search native queries. */
public interface DisturbanceSearchProjection {

  Long getActivityId();

  String getDisturbanceCode();

  String getBaseDescription();

  String getSilvSystemCode();

  String getSilvSystemDescription();

  String getVariantCode();

  String getVariantDescription();

  String getCutPhaseCode();

  String getCutPhaseDescription();

  String getOrgUnitCode();

  String getOrgUnitDescription();

  String getFileId();

  String getCutBlock();

  Long getOpeningId();

  String getOpeningCategoryCode();

  String getOpeningCategoryDescription();

  String getOpeningClientCode();

  Long getTotalCount();

  LocalDateTime getUpdateTimestamp();
}
