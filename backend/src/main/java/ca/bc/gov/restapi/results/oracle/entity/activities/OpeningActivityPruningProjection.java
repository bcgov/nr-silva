package ca.bc.gov.restapi.results.oracle.entity.activities;

public interface OpeningActivityPruningProjection {

  Float getTotalStemsPerHa();
  Float getStemsPerHaToPrune();
  Float getTargetIntertreeDistance();
  Float getMinimumIntertreeDistance();
  Float getHeightAboveGround();
  Float getMinimumLiveCrown();

}
