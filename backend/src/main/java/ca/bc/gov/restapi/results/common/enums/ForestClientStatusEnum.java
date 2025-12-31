package ca.bc.gov.restapi.results.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * This enum contains all forest client statuses codes and descriptions.
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Schema(
  type = "object",
  implementation = CodeDescriptionDto.class,
  description = "Status code and description"
)
public enum ForestClientStatusEnum {
  @JsonProperty("ACT")
  ACTIVE("ACT", "Active"),
  @JsonProperty("DAC")
  DEACTIVATED("DAC", "Deactivated"),
  @JsonProperty("DEC")
  DECEASED("DEC", "Deceased"),
  @JsonProperty("REC")
  RECEIVERSHIP("REC", "Receivership"),
  @JsonProperty("SPN")
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
