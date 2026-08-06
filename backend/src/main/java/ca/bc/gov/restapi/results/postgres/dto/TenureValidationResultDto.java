package ca.bc.gov.restapi.results.postgres.dto;

/**
 * Represents validation result for a single tenure in the validation list.
 *
 * @param tenureIndex the 0-based index of this tenure in the submitted list
 * @param isValid true if the tenure passed all validation checks
 * @param fieldError a single error message if validation failed; null if valid. Shows first error
 *     encountered; user fixes it, then the next error is revealed on re-validation.
 */
public record TenureValidationResultDto(int tenureIndex, boolean isValid, String fieldError) {}
