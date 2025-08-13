package ca.bc.gov.restapi.results.oracle.entity.cover;

public interface ForestCoverDetailedSpeciesProjection extends ForestCoverSpeciesProjection {

  Long getSpeciesPercent();

  Long getAverageAge();

  Float getAverageHeight();

}
