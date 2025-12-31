package ca.bc.gov.restapi.results.common.projection.opening;

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
