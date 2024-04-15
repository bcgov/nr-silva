package ca.bc.gov.restapi.results.oracle.dto;

/** This interface holds openings submission ids with client numbers. */
public interface DashboardOpeningSubmissionDto {

  Long getResultsSubmissionId();

  String getClientNumber();
}
