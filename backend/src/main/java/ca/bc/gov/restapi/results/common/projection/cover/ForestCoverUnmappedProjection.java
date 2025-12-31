package ca.bc.gov.restapi.results.common.projection.cover;

public interface ForestCoverUnmappedProjection {

  String getUnmappedAreaId();

  Float getArea();

  String getStockingStatusCode();
  String getStockingStatusName();

  String getStockingTypeCode();
  String getStockingTypeName();

}
