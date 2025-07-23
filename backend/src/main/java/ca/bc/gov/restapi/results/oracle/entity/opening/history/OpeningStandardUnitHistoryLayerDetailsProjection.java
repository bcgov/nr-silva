package ca.bc.gov.restapi.results.oracle.entity.opening.history;

public interface OpeningStandardUnitHistoryLayerDetailsProjection {
    Long getStockingEventHistoryId();
    Long getSsuId();
    Long getOldLayerId();
    Long getNewLayerId();
    String getOldStockingLayerCode();
    String getNewStockingLayerCode();
    String getOldStockingLayerDescription();
    String getNewStockingLayerDescription();
    Integer getOldMinHorizontalDistance();
    Integer getNewMinHorizontalDistance();
    Integer getOldMinPerfStockingStandard();
    Integer getNewMinPerfStockingStandard();
    Integer getOldMinStockingStandard();
    Integer getNewMinStockingStandard();
    Integer getOldMinPostSpacing();
    Integer getNewMinPostSpacing();
    Double getOldResidualBasalArea();
    Double getNewResidualBasalArea();
    Integer getOldTargetStocking();
    Integer getNewTargetStocking();
    Integer getOldHeightRelativeToComp();
    Integer getNewHeightRelativeToComp();
    Integer getOldMaxConifer();
    Integer getNewMaxConifer();
    Integer getOldMaxPostSpacing();
    Integer getNewMaxPostSpacing();
}
