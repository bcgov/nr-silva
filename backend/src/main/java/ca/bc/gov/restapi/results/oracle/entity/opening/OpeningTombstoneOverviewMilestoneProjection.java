package ca.bc.gov.restapi.results.oracle.entity.opening;

import java.time.LocalDate;

public interface OpeningTombstoneOverviewMilestoneProjection {

  String getStandardsUnitId();

  LocalDate getPostHarvestDeclaredDate();

  LocalDate getRegenDeclaredDate();

  int getRegenOffsetYears();

  LocalDate getRegenDueDate();

  LocalDate getFreeGrowingDeclaredDate();

  int getFreeGrowingOffsetYears();

  LocalDate getFreeGrowingDueDate();

}
