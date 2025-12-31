package ca.bc.gov.restapi.results.common.projection.cover;

public interface ForestCoverDetailedSpeciesProjection extends ForestCoverSpeciesProjection {

  Long getSpeciesPercent();

  Long getAverageAge();

  Float getAverageHeight();

}
