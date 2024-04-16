package ca.bc.gov.restapi.results.oracle.dto;

import java.time.LocalDateTime;

/** This interface represents a stocking event history in the database. */
public interface DashboardStockingEventDto {

  String getResultsAuditActionCode();

  String getEntryUserid();

  Long getOpeningId();

  LocalDateTime getEntryTimestamp();

  LocalDateTime getAmendEventTimestamp();

  LocalDateTime getActionTimestamp();

  default String toLogString() {
    StringBuilder logDto = new StringBuilder("{");
    logDto.append("resultsAuditActionCode='").append(getResultsAuditActionCode());
    logDto.append("', entryUserid='").append(getEntryUserid()).append("', ");
    logDto.append("openingId=").append(getOpeningId()).append(", ");
    logDto.append("entryTimestamp='").append(getEntryTimestamp()).append("', ");
    logDto.append("amendEventTimestamp='").append(getAmendEventTimestamp());
    logDto.append("', actionTimestamp='").append(getActionTimestamp()).append("'}");
    return logDto.toString();
  }
};
