package ca.bc.gov.restapi.results.common.projection.opening;


public interface OpeningStockingNotificationProjection {
  String getStockingStandardUnitId();

  String getStandardsUnitId();

  String getSilvMilestoneTypeCode();

  String getDueLateDate();

  String getNotificationType();
}
