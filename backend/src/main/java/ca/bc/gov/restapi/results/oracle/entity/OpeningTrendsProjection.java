package ca.bc.gov.restapi.results.oracle.entity;

import java.time.LocalDateTime;

public interface OpeningTrendsProjection {
  Long getOpeningId();
  String getUserId();
  LocalDateTime getEntryTimestamp();
  String getStatus();
  String getClientNumber();
}
