package ca.bc.gov.restapi.results.oracle.dto;

import java.time.LocalDateTime;

/** This records represents a stocking event history in the database. */
public record DashboardStockingEventDto(
    String resultsAuditActionCode,
    String entryUserid,
    Long openingId,
    LocalDateTime entryTimestamp,
    LocalDateTime amendEventTimestamp,
    LocalDateTime actionTimestamp) {}
