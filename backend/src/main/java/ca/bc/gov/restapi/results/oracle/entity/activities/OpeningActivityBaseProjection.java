package ca.bc.gov.restapi.results.oracle.entity.activities;

public interface OpeningActivityBaseProjection {

  String getLicenseeActivityId();

  String getIntraAgencyNumber();

  String getActivityClient();

  String getActivityLocation();

  Float getPlannedAmount();

  Float getTreatedAmount();

  Float getPlannedCost();

  Float getActualCost();

  String getKind();

  Long getTotalPlanting();

}
