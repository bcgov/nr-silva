package ca.bc.gov.restapi.results.oracle.entity.opening;

import java.time.LocalDate;

public interface OpeningTombstoneOverviewMilestoneProjection {

  String getStandardsUnitId();

  LocalDate getPostHarvestDeclaredDate();

  LocalDate getRegenDeclaredDate();

  Integer getRegenOffsetYears();

  LocalDate getRegenDueDate();

  LocalDate getFreeGrowingDeclaredDate();

  Integer getFreeGrowingOffsetYears();

  LocalDate getFreeGrowingDueDate();

}
