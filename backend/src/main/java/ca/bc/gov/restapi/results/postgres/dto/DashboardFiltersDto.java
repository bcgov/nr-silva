package ca.bc.gov.restapi.results.postgres.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.With;

/** This record contains all possible filters for the dashboard openings per years api. */
@Builder
@With
public record DashboardFiltersDto(
    String orgUnit,
    String status,
    LocalDateTime entryDateStart,
    LocalDateTime entryDateEnd,
    String clientNumber) {}
