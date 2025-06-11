package ca.bc.gov.restapi.results.oracle.entity.cover;

public interface ForestCoverDetailsDamageProjection {

  String getDamageAgentCode();
  String getDamageAgentName();

  Integer getForestHealthIncidence();

  Float getIncidenceArea();
}
