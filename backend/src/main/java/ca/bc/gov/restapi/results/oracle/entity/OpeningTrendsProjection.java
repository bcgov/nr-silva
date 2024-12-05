package ca.bc.gov.restapi.results.oracle.entity;

import java.time.LocalDateTime;

public interface OpeningTrendsProjection {
  Long getOpeningId();
  String getUserId();
  LocalDateTime getEntryTimestamp();
  LocalDateTime getUpdateTimestamp();
  String getStatus();
  String getOrgUnitCode();
  String getOrgUnitName();
  String getClientNumber();
}
