package ca.bc.gov.restapi.results.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record ActivitySearchFiltersDto(
    @Schema(type = "array", nullable = true) List<String> bases,
    @Schema(type = "array", nullable = true) List<String> techniques,
    @Schema(type = "array", nullable = true) List<String> methods,
    @Schema(type = "boolean") boolean isComplete,
    @Schema(type = "array", nullable = true) List<String> objectives,
    @Schema(type = "array", nullable = true) List<String> fundingSources,
    @Schema(type = "array", nullable = true) List<String> orgUnits,
    @Schema(type = "array", nullable = true) List<String> openingCategories,
    @Schema(type = "string", format = "date", nullable = true) String updateDateStart,
    @Schema(type = "string", format = "date", nullable = true) String updateDateEnd) {}
