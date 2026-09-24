package ca.bc.gov.restapi.results.postgres.enums;

/** Machine-readable outcomes returned by stocking-standard BEC validation. */
public enum BecValidationErrorCode {
  FIELD_INVALID,
  BEC_COMBINATION_NOT_FOUND,
  DUPLICATE_BEC_COMBINATION
}
