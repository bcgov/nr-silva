package ca.bc.gov.restapi.results.oracle.dto;

import java.time.LocalDateTime;

/** This record contains all possible filters when using the Opening Search API. */
public record SearchOpeningFiltersDto(
    String orgUnit,
    String category,
    String status,
    String userId,
    Boolean submittedToFrpa,
    LocalDateTime disturbanceDateStart,
    LocalDateTime disturbanceDateEnd,
    LocalDateTime regenDelayDateStart,
    LocalDateTime regenDelayDateEnd,
    LocalDateTime freeGrowingDateStart,
    LocalDateTime freeGrowingDateEnd,
    LocalDateTime updateDateStart,
    LocalDateTime updateDateEnd) {}
