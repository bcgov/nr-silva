package ca.bc.gov.restapi.results.oracle.entity.opening;

import java.time.LocalDate;

public interface OpeningStockingMilestoneProjection {
    String getStandardsUnitId();

    LocalDate getPostHarvestDeclaredDate();

    LocalDate getRegenDeclaredDate();

    Integer getRegenOffsetYears();

    LocalDate getRegenDueDate();

    LocalDate getNoRegenDeclaredDate();

    Integer getNoRegenOffsetYears();

    LocalDate getNoRegenDueDate();

    LocalDate getFreeGrowingDeclaredDate();

    Integer getFreeGrowingOffsetYears();

    LocalDate getFreeGrowingDueDate();

    Boolean getNoRegenIndicated();

}
