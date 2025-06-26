package ca.bc.gov.restapi.results.oracle.entity.cover;

public interface ForestCoverUnmappedProjection {

  String getUnmappedAreaId();

  Float getArea();

  String getStockingStatusCode();
  String getStockingStatusName();

  String getStockingTypeCode();
  String getStockingTypeName();

}
