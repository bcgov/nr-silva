package ca.bc.gov.restapi.results.oracle.dto.opening.history;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

import java.time.LocalDateTime;

@With
public record OpeningStandardUnitHistoryOverviewDto(
    @Schema(types = {"number"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long stockingEventHistoryId,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer amendmentNumber,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime eventTimestamp,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer suCount,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Double totalNar,

    @Schema(types = {"object"}, requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto auditAction,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String esfSubmissionId,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String submittedByUserId,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String approvedByUserId
) {
}
