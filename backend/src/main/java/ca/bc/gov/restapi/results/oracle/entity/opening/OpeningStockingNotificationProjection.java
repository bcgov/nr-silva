package ca.bc.gov.restapi.results.oracle.entity.opening;


public interface OpeningStockingNotificationProjection {
  String getStockingStandardUnitId();

  String getStandardsUnitId();

  String getSilvMilestoneTypeCode();

  String getDueLateDate();

  String getNotificationType();
}
