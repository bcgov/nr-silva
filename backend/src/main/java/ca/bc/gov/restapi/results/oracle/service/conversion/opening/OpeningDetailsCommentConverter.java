package ca.bc.gov.restapi.results.oracle.service.conversion.opening;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.comment.CommentDto;
import ca.bc.gov.restapi.results.oracle.entity.comments.CommentProjection;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class OpeningDetailsCommentConverter {

  public static Function<CommentProjection, CommentDto> mapComments() {
    return projection -> new CommentDto(
          new CodeDescriptionDto(
              projection.getCommentSourceCode(),
              projection.getCommentSourceName()
          ),
          new CodeDescriptionDto(
              projection.getCommentTypeCode(),
              projection.getCommentTypeName()
          ),
          projection.getCommentText()
      );
  }
}
