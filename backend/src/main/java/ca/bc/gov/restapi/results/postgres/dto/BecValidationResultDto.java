package ca.bc.gov.restapi.results.postgres.dto;

import ca.bc.gov.restapi.results.postgres.enums.BecValidationErrorCode;

/** Per-entry result returned by stocking-standard BEC validation. */
public record BecValidationResultDto(
    int becIndex, boolean isValid, BecValidationErrorCode errorCode, String errorMessage) {}
