package ca.bc.gov.restapi.results.oracle.entity.opening.history;

public interface OpeningStandardUnitHistoryProjection {
    Long getStockingEventHistoryId();
    Integer getAmendmentNumber();
    String getEventTimestamp();
    Integer getSuCount();
    Double getTotalNar();
    String getAuditActionCode();
    String getAuditActionDescription();
    String getEsfSubmissionId();
    String getSubmittedByUserId();
    String getApprovedByUserId();
}
