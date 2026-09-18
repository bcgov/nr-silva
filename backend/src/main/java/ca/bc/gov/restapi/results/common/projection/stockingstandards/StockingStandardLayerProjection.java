package ca.bc.gov.restapi.results.common.projection.stockingstandards;

import java.math.BigDecimal;

/** Tree stocking criteria layer assigned to a stocking standard. */
public interface StockingStandardLayerProjection {
  Long getLayerId();

  String getLayerCode();

  Integer getMinWellSpacedTrees();

  Integer getMinPreferredWellSpacedTrees();

  BigDecimal getMinHorizontalDistanceWellSpacedTrees();

  Integer getTargetWellSpacedTrees();

  Integer getMinResidualBasalArea();

  Integer getMinPostSpacingDensity();

  Integer getMaxPostSpacingDensity();

  Integer getMaxConiferous();

  Integer getHeightRelativeToComp();

  String getHeightRelativeToCompUnitCode();
}
