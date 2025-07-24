package ca.bc.gov.restapi.results.oracle.entity.opening.history;

public interface OpeningStandardUnitHistoryLayerSpeciesDetailsProjection {
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
    Integer getOldMinHeight();
    Integer getNewMinHeight();
}
