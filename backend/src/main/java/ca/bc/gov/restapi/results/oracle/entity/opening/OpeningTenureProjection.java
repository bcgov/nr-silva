package ca.bc.gov.restapi.results.oracle.entity.opening;

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
