package ca.bc.gov.restapi.results.oracle.entity.opening;

import java.time.LocalDateTime;

public interface OpeningAuditProjection {
    long getOpeningId();

    long getAuditEventId();

    long getRegimeId();

    long getProjectId();

    String getActionCode();

    LocalDateTime getActionTimestamp();

    String getDescription();

    String getUserId();

    Boolean getIsEmailSent();

    long getXmlSubmissionId();

    long getOpeningAmendmentNumber();

    String getEntryUserId();

    LocalDateTime getEntryTimestamp();
}
