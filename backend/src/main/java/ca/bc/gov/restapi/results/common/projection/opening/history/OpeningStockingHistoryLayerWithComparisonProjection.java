package ca.bc.gov.restapi.results.common.projection.opening.history;

public interface OpeningStockingHistoryLayerWithComparisonProjection {
    Long getStockingEventHistoryId();
    Long getSsuId();
    Long getOldLayerId();
    Long getNewLayerId();
    String getOldStockingLayerCode();
    String getNewStockingLayerCode();
    String getOldStockingLayerDescription();
    String getNewStockingLayerDescription();
    Double getOldMinHorizontalDistance();
    Double getNewMinHorizontalDistance();
    Integer getOldMinPerfStockingStandard();
    Integer getNewMinPerfStockingStandard();
    Integer getOldMinStockingStandard();
    Integer getNewMinStockingStandard();
    Integer getOldMinPostSpacing();
    Integer getNewMinPostSpacing();
    Double getOldResidualBasalArea();
    Double getNewResidualBasalArea();
    Integer getOldTargetWellSpacedTrees();
    Integer getNewTargetWellSpacedTrees();
    Integer getOldHeightRelativeToComp();
    Integer getNewHeightRelativeToComp();
    Integer getOldMaxConifer();
    Integer getNewMaxConifer();
    Integer getOldMaxPostSpacing();
    Integer getNewMaxPostSpacing();
}
