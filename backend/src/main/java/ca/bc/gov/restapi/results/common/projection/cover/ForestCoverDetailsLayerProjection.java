package ca.bc.gov.restapi.results.common.projection.cover;

public interface ForestCoverDetailsLayerProjection {

  Long getLayerId();

  String getLayerCode();

  String getLayerName();

  Long getCrownClosure();

  Long getBasalAreaSt();

  Long getTotalStems();

  Long getTotalWellSpaced();

  Long getWellSpaced();

  Long getFreeGrowing();

}
