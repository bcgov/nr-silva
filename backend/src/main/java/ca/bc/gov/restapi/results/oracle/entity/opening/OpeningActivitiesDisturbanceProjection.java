package ca.bc.gov.restapi.results.oracle.entity.opening;

import java.time.LocalDate;

public interface OpeningActivitiesDisturbanceProjection {

  Long getAtuId();

  String getDisturbanceCode();
  String getDisturbanceName();

  String getSystemCode();
  String getSystemName();

  String getVariantCode();
  String getVariantName();

  String getCutPhaseCode();
  String getCutPhaseName();

  Float getDisturbanceArea();

  LocalDate getLastUpdate();
  LocalDate getStartDate();
  LocalDate getEndDate();

  String getLicenseeActivityId();

  String getDisturbanceLocationClient();
  String getDisturbanceLocationCode();

  String getLicenceNumber();

  String getCuttingPermitId();

  String getCutBlock();

}
