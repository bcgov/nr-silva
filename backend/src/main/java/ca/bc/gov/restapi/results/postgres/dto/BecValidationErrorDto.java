package ca.bc.gov.restapi.results.postgres.dto;

import ca.bc.gov.restapi.results.postgres.enums.BecValidationErrorCode;

/** Request-level BEC validation error that cannot be associated with an individual BEC entry. */
public record BecValidationErrorDto(BecValidationErrorCode errorCode, String errorMessage) {}
