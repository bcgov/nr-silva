package ca.bc.gov.restapi.results.postgres.dto;

import ca.bc.gov.restapi.results.postgres.enums.TenureValidationErrorCode;

/** Validation failure for an existing tenure omitted from a replacement list. */
public record TenureRemovalValidationResultDto(
    Long cboaId, TenureValidationErrorCode errorCode, String errorMessage) {}
