package ca.bc.gov.restapi.results.postgres.enums;

/** Which milestone(s) a species entry applies to, defaults to {@code BOTH} on the frontend. */
public enum StockingSpeciesMilestone {
  REGEN,
  FREE_GROWING,
  BOTH;
}
