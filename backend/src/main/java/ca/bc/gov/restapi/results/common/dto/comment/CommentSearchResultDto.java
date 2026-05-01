package ca.bc.gov.restapi.results.common.dto.comment;

import ca.bc.gov.restapi.results.common.enums.ActivityKindCode;
import ca.bc.gov.restapi.results.common.enums.CommentLocationCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommentSearchResultDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED) Long openingId,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED) CommentLocationCode commentLocation,
    @Schema(
            types = {"string", "null"},
            nullable = true)
        ActivityKindCode activityKind,
    @Schema(
            types = {"string", "null"},
            requiredMode = Schema.RequiredMode.REQUIRED)
        String commentText,
    @Schema(
            types = {"string", "null"},
            nullable = true)
        LocalDateTime updateTimestamp) {}
