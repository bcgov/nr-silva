package ca.bc.gov.restapi.results.common.util;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.entity.GenericCodeEntity;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Utility for converting code entities to DTOs. */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeConverterUtil {

  /**
   * Convert a list of GenericCodeEntity to CodeDescriptionDto.
   *
   * @param codes List of code entities to convert.
   * @return List of CodeDescriptionDto.
   */
  public static List<CodeDescriptionDto> toCodeDescriptionDtos(
      List<? extends GenericCodeEntity> codes) {
    return codes.stream()
        .map(entity -> new CodeDescriptionDto(entity.getCode(), entity.getDescription()))
        .toList();
  }
}
