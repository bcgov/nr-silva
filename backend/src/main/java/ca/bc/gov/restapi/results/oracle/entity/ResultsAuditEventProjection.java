package ca.bc.gov.restapi.results.oracle.entity;

import java.time.LocalDateTime;

public interface ResultsAuditEventProjection {
  Long getResultsAuditEventId();
  Long getOpeningId();
  String getActionCode();
  String getActionCodeDescription();
  String getCategoryCode();
  String getCategoryCodeDescription();
  LocalDateTime getEntryTimestamp();
  String getEntryUserid();
}
