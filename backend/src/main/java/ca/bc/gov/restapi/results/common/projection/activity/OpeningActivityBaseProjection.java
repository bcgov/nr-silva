package ca.bc.gov.restapi.results.common.projection.activity;

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
