package ca.bc.gov.restapi.results.oracle.dto;

import java.time.LocalDateTime;

/** This interface holds all opening columns required for the dashboard metrics. */
public interface DashboardOpeningDto {

  Long getOpeningId();

  String getOpeningStatusCode();

  String getEntryUserId();

  LocalDateTime getEntryTimestamp();

  LocalDateTime getUpdateTimestamp();

  Long getAdminDistrictNo();

  Long getResultsSubmissionId();

  LocalDateTime getActionTimestamp();
}
