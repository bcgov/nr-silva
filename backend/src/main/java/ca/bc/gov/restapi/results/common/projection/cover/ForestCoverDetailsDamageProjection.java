package ca.bc.gov.restapi.results.common.projection.cover;

public interface ForestCoverDetailsDamageProjection {

  String getDamageAgentCode();
  String getDamageAgentName();

  Integer getForestHealthIncidence();

  Float getIncidenceArea();
}
