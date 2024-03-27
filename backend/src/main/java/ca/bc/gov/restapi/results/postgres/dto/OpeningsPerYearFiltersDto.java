package ca.bc.gov.restapi.results.postgres.dto;

import java.time.LocalDateTime;

public record OpeningsPerYearFiltersDto(
    String orgUnit, String status, LocalDateTime entryDateStart, LocalDateTime entryDateEnd) {}
