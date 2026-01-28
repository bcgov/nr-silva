package ca.bc.gov.restapi.results.common.projection.cover;

public interface ForestCoverPolygonProjection {

  Long getForestCoverId();

  String getReserveCode();
  String getReserveName();

  String getObjectiveCode();
  String getObjectiveName();

  String getSiteClassCode();
  String getSiteClassName();

  Long getSiteIndex();

  String getSiteIndexSourceCode();
  String getSiteIndexSourceName();

  String getTreeCoverPatternCode();
  String getTreeCoverPatternName();

  Long getReentryYear();

}
