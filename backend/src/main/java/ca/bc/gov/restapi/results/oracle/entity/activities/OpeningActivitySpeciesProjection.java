package ca.bc.gov.restapi.results.oracle.entity.activities;

public interface OpeningActivitySpeciesProjection {
  String getSpeciesCode();
  String getSpeciesName();
  Long getPlantedNumber();
  Long getNumberBeyondTransferLimit();
  boolean isCbst();
  Long getRequestId();
  Long getLot();
  Float getBidPricePerTree();
}
