package ca.bc.gov.restapi.results.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/** This enum contains all forest client types codes and descriptions. */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ForestClientTypeEnum {
  ASSOCIATION('A', "Association"),
  FIRST_NATION_BAND('B', "First Nation Band"),
  CORPORATION('C', "Corporation"),
  MINISTRY_OF_FORESTS_AND_RANGE('F', "Ministry of Forests and Range"),
  GOVERNMENT('G', "Government"),
  INDIVIDUAL('I', "Individual"),
  LIMITED_PARTNERSHIP('L', "Limited Partnership"),
  GENERAL_PARTNERSHIP('P', "General Partnership"),
  FIRST_NATION_GROUP('R', "First Nation Group"),
  SOCIETY('S', "Society"),
  FIRST_NATION_TRIBAL_COUNCIL('T', "First Nation Tribal Council"),
  UNREGISTERED_COMPANY('U', "Unregistered Company");

  private final Character code;
  private final String description;

  ForestClientTypeEnum(Character code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Get a {@link ForestClientTypeEnum} instance given the type code.
   *
   * @param code The type code.
   * @return ForestClientTypeEnum
   */
  public static ForestClientTypeEnum of(Character code) {
    for (ForestClientTypeEnum type : values()) {
      if (type.code.equals(code)) {
        return type;
      }
    }
    return null;
  }
}
