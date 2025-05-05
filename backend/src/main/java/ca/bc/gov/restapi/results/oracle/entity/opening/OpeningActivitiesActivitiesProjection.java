package ca.bc.gov.restapi.results.oracle.entity.opening;

import java.time.LocalDate;

public interface OpeningActivitiesActivitiesProjection {

  Long getAtuId();

  String getStatusCode();

  String getBaseCode();
  String getBaseName();

  String getTechCode();
  String getTechName();

  String getMethodCode();
  String getMethodName();

  String getObjective1Code();
  String getObjective1Name();

  String getObjective2Code();
  String getObjective2Name();

  String getObjective3Code();
  String getObjective3Name();

  Float getArea();

  String getFundingCode();
  String getFundingName();

  String getProjectId();

  LocalDate getLastUpdate();
  LocalDate getPlannedDate();
  LocalDate getEndDate();

}
