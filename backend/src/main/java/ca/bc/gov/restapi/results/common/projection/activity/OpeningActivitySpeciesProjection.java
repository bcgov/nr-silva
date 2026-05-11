package ca.bc.gov.restapi.results.common.projection.activity;

public interface OpeningActivitySpeciesProjection {
  String getSpeciesCode();

  String getSpeciesName();

  Long getPlantedNumber();

  Long getNumberBeyondTransferLimit();

  boolean isCbst();

  String getRequestId();

  Long getLot();

  Float getBidPricePerTree();
}
