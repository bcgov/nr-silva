package ca.bc.gov.restapi.results.oracle.entity.opening;

import ca.bc.gov.restapi.results.oracle.enums.OpeningDetailsNotificationStatusEnum;

public interface OpeningStockingNotificationProjection {
    String getStockingStandardUnitId();
    String getStandardsUnitId();
    String getSilvMilestoneTypeCode();
    String getDueLateDate();
    String getNotificationType();
}
