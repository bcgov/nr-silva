package ca.bc.gov.restapi.results.oracle.entity.opening;

public interface OpeningStockingLayerProjection {

  String getLayerCode();

  String getLayerName();

  Long getMinWellspacedTrees();

  Long getMinPreferredWellspacedTrees();

  Long getMinHorizontalDistanceWellspacedTrees();

  Long getTargetWellspacedTrees();

  Long getMinResidualBasalArea();

  Long getMinPostspacingDensity();

  Long getMaxPostspacingDensity();

  Long getMaxConiferous();

  Long getHeightRelativeToComp();
}
