package ca.bc.gov.restapi.results.postgres.dto;

import ca.bc.gov.restapi.results.postgres.enums.SpeciesValidationErrorCode;

/** Per-species result for a layer in the stocking-standard species validator. */
public record StockingSpeciesValidationResultDto(
    int speciesIndex,
    boolean isValid,
    SpeciesValidationErrorCode errorCode,
    String errorMessage) {}
