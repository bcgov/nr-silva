package ca.bc.gov.restapi.results.common.projection.opening;

import java.time.LocalDateTime;

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
}
