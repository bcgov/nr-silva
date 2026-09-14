package ca.bc.gov.restapi.results.postgres.enums;

/** How a Stocking Standard's org unit/client authority is established. */
public enum StockingStandardAuthorityType {
  OPERATIONAL_PLAN,
  MINISTRY_DEFAULT_PROVINCIAL,
  MINISTRY_DEFAULT_OTHERS;

  public boolean isMinistryDefault() {
    return this == MINISTRY_DEFAULT_PROVINCIAL || this == MINISTRY_DEFAULT_OTHERS;
  }
}
