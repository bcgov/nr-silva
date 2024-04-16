package ca.bc.gov.restapi.results.oracle.dto;

import java.time.LocalDateTime;

/** This record holds columns for the results audit table. */
public interface DashboardResultsAuditDto {

  String getResultsAuditActionCode();

  LocalDateTime getActionDate();

  LocalDateTime getEntryTimestamp();

  String getEntryUserid();

  Long getOpeningId();

  LocalDateTime getActionTimestamp();
}
