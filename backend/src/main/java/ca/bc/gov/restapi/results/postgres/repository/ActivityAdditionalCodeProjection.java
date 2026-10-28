package ca.bc.gov.restapi.results.postgres.repository;

/** Code and description of an activity objective or funding source stored in a child table. */
public interface ActivityAdditionalCodeProjection {

  Long getActivityTreatmentUnitId();

  String getResultsInd();

  String getCode();

  String getDescription();
}
