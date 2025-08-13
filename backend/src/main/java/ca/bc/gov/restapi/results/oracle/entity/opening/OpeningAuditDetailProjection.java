package ca.bc.gov.restapi.results.oracle.entity.opening;

import java.time.LocalDateTime;

public interface OpeningAuditDetailProjection {
    long getAuditEventId();

    long getAuditDetailId();

    String getBusinessIdentifier();

    String getTableName();

    String getColumnName();

    String getOldValue();

    String getNewValue();

    String getEntryUserId();

    LocalDateTime getEntryTimestamp();
}
