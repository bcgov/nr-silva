package ca.bc.gov.restapi.results.postgres.enums;

/** Species type on a Stocking Standard layer, stored as {@code species_type_code}. */
public enum StockingSpeciesType {
  PREFERRED("PRF"),
  ACCEPTABLE("ACC"),
  ECOLOGICALLY_SUITABLE("ECO");

  private final String code;

  StockingSpeciesType(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
