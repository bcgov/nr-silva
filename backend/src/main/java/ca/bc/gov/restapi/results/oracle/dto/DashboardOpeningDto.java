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

  default String toLogString() {
    StringBuilder logDto = new StringBuilder("{");
    logDto.append("openingId=").append(getOpeningId()).append(", ");
    logDto.append("openingStatusCode='").append(getOpeningStatusCode());
    logDto.append("', entryUserId='").append(getEntryUserId()).append("', ");
    logDto.append("entryTimestamp='").append(getEntryTimestamp()).append("', ");
    logDto.append("updateTimestamp='").append(getUpdateTimestamp());
    logDto.append("', adminDistrictNo=").append(getAdminDistrictNo());
    logDto.append(", resultsSubmissionId=").append(getResultsSubmissionId());
    logDto.append(", actionTimestamp='").append(getActionTimestamp()).append("'}");
    return logDto.toString();
  }
}
