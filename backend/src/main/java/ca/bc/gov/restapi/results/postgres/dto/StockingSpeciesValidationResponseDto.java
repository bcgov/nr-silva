package ca.bc.gov.restapi.results.postgres.dto;

import java.util.List;

/** Response for {@code POST /api/stocking-standards/validate/species}. */
public record StockingSpeciesValidationResponseDto(
    List<StockingSpeciesLayerValidationResultDto> validationResults,
    List<SpeciesDuplicateConflictDto> duplicateConflicts,
    boolean isValid) {}
