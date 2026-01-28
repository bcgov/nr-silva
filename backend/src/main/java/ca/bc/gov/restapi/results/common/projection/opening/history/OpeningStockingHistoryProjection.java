package ca.bc.gov.restapi.results.common.projection.opening.history;

import java.time.LocalDateTime;

public interface OpeningStockingHistoryProjection {
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
