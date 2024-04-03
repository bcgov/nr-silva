package ca.bc.gov.restapi.results.postgres.dto;

import java.time.LocalDateTime;

/** This record contains all possible filters for the dashboard openings per years api. */
public record OpeningsPerYearFiltersDto(
    String orgUnit,
    String status,
    LocalDateTime entryDateStart,
    LocalDateTime entryDateEnd,
    String clientNumber) {}
