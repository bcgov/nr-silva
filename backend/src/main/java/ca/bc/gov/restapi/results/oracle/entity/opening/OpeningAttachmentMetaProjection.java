package ca.bc.gov.restapi.results.oracle.entity.opening;

import java.time.LocalDateTime;
import java.util.UUID;

public interface OpeningAttachmentMetaProjection {
    Long getOpeningId();
    String getAttachmentName();
    String getAttachmentDescription();
    String getMimeTypeCode();
    String getEntryUserId();
    LocalDateTime getEntryTimestamp();
    String getUpdateUserId();
    LocalDateTime getUpdateTimestamp();
    int getRevisionCount();
    String getAttachmentGuid();
    Long getAttachmentSize();
}