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

  default String toLogString() {
    StringBuilder logDto = new StringBuilder("{");
    logDto.append("resultsAuditActionCode='").append(getResultsAuditActionCode());
    logDto.append("', actionDate='").append(getActionDate()).append(", ");
    logDto.append("entryTimestamp='").append(getEntryTimestamp()).append("', ");
    logDto.append("entryUserid='").append(getEntryUserid()).append("', ");
    logDto.append("openingId=").append(getOpeningId()).append(", ");
    logDto.append("actionTimestamp=").append(getActionTimestamp()).append("'}");
    return logDto.toString();
  }
}
