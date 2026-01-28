package ca.bc.gov.restapi.results.common.projection.opening.history;

public interface OpeningStockingHistoryLayerSpeciesWithComparisonProjection {
    Long getSsuId();
    Long getOldStockingLayerId();
    Long getNewStockingLayerId();
    String getOldLayerCode();
    String getNewLayerCode();
    String getOldSpeciesCode();
    String getNewSpeciesCode();
    String getOldSpeciesDescription();
    String getNewSpeciesDescription();
    Boolean getOldPreferredInd();
    Boolean getNewPreferredInd();
    Double getOldMinHeight();
    Double getNewMinHeight();
}
