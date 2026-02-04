package ca.bc.gov.restapi.results.common.projection;

import java.time.LocalDateTime;

public interface ResultsAuditEventProjection {

  Long getOpeningId();

  String getActionCodeDescription();

  String getCategoryCode();

  String getCategoryCodeDescription();

  LocalDateTime getEntryTimestamp();
}
