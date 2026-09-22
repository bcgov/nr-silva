package ca.bc.gov.restapi.results.postgres.enums;

/** Machine-readable outcomes returned by stocking-standard species validation. */
public enum SpeciesValidationErrorCode {
  FIELD_INVALID,
  LAYER_CODE_INVALID,
  SPECIES_CODE_UNKNOWN_OR_INACTIVE,
  SPECIES_MIN_HEIGHT_REQUIRED,
  SPECIES_MIN_HEIGHT_INVALID,
  DUPLICATE_SPECIES_CODE
}
