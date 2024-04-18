package ca.bc.gov.restapi.results.oracle.dto;

/** This interface represent an action code in the database. */
public interface DashboardActionCodeDto {

  String getResultsAuditActionCode();

  String getDescription();

  /** Default method for logging the interface. */
  default String toLogString() {
    StringBuilder logDto = new StringBuilder("{");
    logDto.append("resultsAuditActionCode='").append(getResultsAuditActionCode());
    logDto.append("', getDescription='").append(getDescription()).append("'}");
    return logDto.toString();
  }
}
