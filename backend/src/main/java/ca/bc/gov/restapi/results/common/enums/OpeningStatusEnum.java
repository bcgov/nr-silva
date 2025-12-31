package ca.bc.gov.restapi.results.common.enums;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This enumeration represents all possible values for the Opening Status column from the Opening
 * table.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(
        type = "object",
        implementation = CodeDescriptionDto.class,
        description = "Status code and description"
)
public enum OpeningStatusEnum {
  AMD("AMD", "Amended"),
  AMG("AMG", "Amalgamate"),
  APP("APP", "Approved"),
  DFT("DFT", "Draft"),
  FG("FG", "Free Growing"),
  RET("RET", "Retired"),
  SUB("SUB", "Submitted");

  private final String code;
  private final String description;

  public CodeDescriptionDto toCodeDescriptionDto() {
    return new CodeDescriptionDto(code, description);
  }

  /**
   * Get a {@link OpeningStatusEnum} given its code.
   *
   * @param code The code to be get.
   * @return OpeningStatusEnum or null if not found.
   */
  public static OpeningStatusEnum of(String code) {
    if (!Objects.isNull(code) && code.contains("\'")) {
      code = code.replace("'", "");
    }
    for (OpeningStatusEnum status : values()) {
      if (status.code.equals(code)) {
        return status;
      }
    }
    return null;
  }
}
