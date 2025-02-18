package ca.bc.gov.restapi.results.oracle.entity;

import java.time.LocalDateTime;

public interface ResultsAuditEventProjection {
  Long getOpeningId();
  String getActionCodeDescription();
  String getCategoryCode();
  String getCategoryCodeDescription();
  LocalDateTime getEntryTimestamp();
}
