package ca.bc.gov.restapi.results.common.projection.opening;

public interface OpeningTenureProjection {

  long getId();

  boolean getPrimaryTenure();

  String getFileId();

  String getCutBlock();

  String getCuttingPermit();

  String getTimberMark();

  String getStatusCode();

  String getStatusName();

  Float getPlannedGrossArea();

  Float getPlannedNetArea();

}
