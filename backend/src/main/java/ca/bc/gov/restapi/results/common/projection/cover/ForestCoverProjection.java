package ca.bc.gov.restapi.results.common.projection.cover;

public interface ForestCoverProjection {

  Long getCoverId();

  String getPolygonId();

  String getStandardUnitId();

  String getUnmappedCode();

  String getUnmappedName();

  Float getGrossArea();

  Float getNetArea();

  String getStatusCode();

  String getStatusName();

  String getTypeCode();

  String getTypeName();

  Float getTotal();

  Float getInventoryTotalWellSpaced();

  Float getInventoryWellSpaced();

  Float getInventoryFreeGrowing();

  Float getSilvicultureTotalWellSpaced();

  Float getSilvicultureWellSpaced();

  Float getSilvicultureFreeGrowing();

  Integer getReferenceYear();

  String getIsSingleLayer();

  String getReserveCode();

  String getObjectiveCode();
}
