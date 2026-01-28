package ca.bc.gov.restapi.results.common.projection.activity;

public interface OpeningActivityPruningProjection {

  Float getTotalStemsPerHa();
  Float getStemsPerHaToPrune();
  Float getTargetIntertreeDistance();
  Float getMinimumIntertreeDistance();
  Float getHeightAboveGround();
  Float getMinimumLiveCrown();

}
