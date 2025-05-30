package ca.bc.gov.restapi.results.oracle.dto.comment;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record CommentDto(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  CodeDescriptionDto commentSource,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  CodeDescriptionDto commentType,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  String commentText
) {

}
