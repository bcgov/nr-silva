package ca.bc.gov.restapi.results.oracle.entity.opening.history;

public interface OpeningStandardUnitHistoryLayerSpeciesDetailsProjection {
    Long getSsuId();
    Long getStockingLayerId();
    String getOldSpeciesCode();
    String getNewSpeciesCode();
    String getOldSpeciesDescription();
    String getNewSpeciesDescription();
    String getOldPreferredInd();
    String getNewPreferredInd();
    Integer getOldMinHeight();
    Integer getNewMinHeight();
}
