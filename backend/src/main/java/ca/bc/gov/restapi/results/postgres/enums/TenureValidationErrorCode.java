package ca.bc.gov.restapi.results.postgres.enums;

/** Error codes returned in tenure validation results. */
public enum TenureValidationErrorCode {
  FIELD_INVALID,
  TENURE_NOT_FOUND,
  CLIENT_NOT_LICENSEE,
  TENURE_DUPLICATE_OPENING,
  DUPLICATE_IN_REQUEST,
  DISTURBANCE_EXISTS,
  STALE_TENURE,
  TENURE_NOT_ASSOCIATED;
}
