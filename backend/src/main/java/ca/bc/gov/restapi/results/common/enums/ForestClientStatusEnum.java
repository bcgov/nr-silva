package ca.bc.gov.restapi.results.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/** This enum contains all forest client statuses codes and descriptions. */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ForestClientStatusEnum {
  ACTIVE("ACT", "Active"),
  DEACTIVATED("DAC", "Deactivated"),
  DECEASED("DEC", "Deceased"),
  RECEIVERSHIP("REC", "Receivership"),
  SUSPENDED("SPN", "Suspended");

  private final String code;
  private final String description;

  ForestClientStatusEnum(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Get a {@link ForestClientStatusEnum} instance given the status code.
   *
   * @param code The status code.
   * @return ForestClientStatusEnum
   */
  public static ForestClientStatusEnum of(String code) {
    for (ForestClientStatusEnum status : values()) {
      if (status.code.equals(code)) {
        return status;
      }
    }
    return null;
  }
}
