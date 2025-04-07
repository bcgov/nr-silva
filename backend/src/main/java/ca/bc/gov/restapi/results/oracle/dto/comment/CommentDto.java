package ca.bc.gov.restapi.results.oracle.dto.comment;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;

public record CommentDto(
    CodeDescriptionDto commentSource,
    CodeDescriptionDto commentType,
    String commentText
) {

}
