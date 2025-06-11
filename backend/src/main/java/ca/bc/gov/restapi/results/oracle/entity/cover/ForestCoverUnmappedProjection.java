package ca.bc.gov.restapi.results.oracle.entity.cover;

public interface ForestCoverUnmappedProjection {

  Long getUnmappedAreaId();

  Float getArea();

  String getStockingStatusCode();
  String getStockingStatusName();

  String getStockingTypeCode();
  String getStockingTypeName();

}
