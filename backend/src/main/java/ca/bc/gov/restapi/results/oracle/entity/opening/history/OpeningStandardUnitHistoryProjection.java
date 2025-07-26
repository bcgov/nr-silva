package ca.bc.gov.restapi.results.oracle.entity.opening.history;

import java.time.LocalDateTime;

public interface OpeningStandardUnitHistoryProjection {
    Long getStockingEventHistoryId();
    Integer getAmendmentNumber();
    LocalDateTime getEventTimestamp();
    Integer getSuCount();
    Double getTotalNar();
    String getAuditActionCode();
    String getAuditActionDescription();
    String getEsfSubmissionId();
    String getSubmittedByUserId();
    String getApprovedByUserId();
}
