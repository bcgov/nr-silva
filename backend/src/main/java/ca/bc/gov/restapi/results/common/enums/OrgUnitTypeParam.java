package ca.bc.gov.restapi.results.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Filter type for the org-units endpoint. */
public enum OrgUnitTypeParam {
  ALL,
  DISTRICT;

  @JsonValue
  public String toValue() {
    return name().toLowerCase();
  }

  @JsonCreator
  public static OrgUnitTypeParam fromValue(String value) {
    if (value == null) {
      return ALL;
    }
    return valueOf(value.trim().toUpperCase());
  }
}
