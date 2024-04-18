package ca.bc.gov.restapi.results.oracle.dto;

/** This interface holds openings submission ids with client numbers. */
public interface DashboardOpeningSubmissionDto {

  Long getResultsSubmissionId();

  String getClientNumber();

  /** Default method for logging the interface. */
  default String toLogString() {
    StringBuilder logDto = new StringBuilder("{");
    logDto.append("resultsSubmissionId=").append(getResultsSubmissionId());
    logDto.append(", clientNumber='").append(getClientNumber()).append("'}");
    return logDto.toString();
  }
}
