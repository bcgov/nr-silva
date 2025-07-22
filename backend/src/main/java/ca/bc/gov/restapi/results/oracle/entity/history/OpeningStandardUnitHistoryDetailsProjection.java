package ca.bc.gov.restapi.results.oracle.entity.history;

public interface OpeningStandardUnitHistoryDetailsProjection {
    Long getStockingEventHistoryId();
    Long getStockingStandardUnitId();
    Long getOldRegimeId();
    Long getNewRegimeId();
    Double getOldNetArea();
    Double getNewNetArea();
    Double getOldMaxSoilDisturbance();
    Double getNewMaxSoilDisturbance();
    String getOldVarianceIndicator();
    String getNewVarianceIndicator();
    String getOldRegenObligationIndicator();
    String getNewRegenObligationIndicator();
    Integer getOldNoRegenEarlyOffsetYears();
    Integer getNewNoRegenEarlyOffsetYears();
    Integer getOldNoRegenLateOffsetYears();
    Integer getNewNoRegenLateOffsetYears();
    Integer getOldFreeGrowingEarlyOffsetYears();
    Integer getNewFreeGrowingEarlyOffsetYears();
    Integer getOldFreeGrowingLateOffsetYears();
    Integer getNewFreeGrowingLateOffsetYears();
    String getOldBgcZone();
    String getNewBgcZone();
    String getOldBgcSubzone();
    String getNewBgcSubzone();
    String getOldBgcVariant();
    String getNewBgcVariant();
    String getOldBgcPhase();
    String getNewBgcPhase();
    String getOldBecSiteSeries();
    String getNewBecSiteSeries();
    String getOldBecSiteType();
    String getNewBecSiteType();
    String getOldBecSeral();
    String getNewBecSeral();
}
