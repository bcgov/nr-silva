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
};
