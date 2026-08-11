package ca.bc.gov.restapi.results.postgres.dto;

import ca.bc.gov.restapi.results.postgres.enums.TenureValidationErrorCode;

/**
 * Represents validation result for a single tenure in the validation list.
 *
 * @param tenureIndex the 0-based index of this tenure in the submitted list
 * @param isValid true if the tenure passed all validation checks
 * @param errorCode machine-readable code; null if valid
 * @param errorMessage human-readable description; null if valid
 */
public record TenureValidationResultDto(
    int tenureIndex, boolean isValid, TenureValidationErrorCode errorCode, String errorMessage) {}
