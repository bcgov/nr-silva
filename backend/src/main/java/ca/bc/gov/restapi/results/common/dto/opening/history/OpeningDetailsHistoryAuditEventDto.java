package ca.bc.gov.restapi.results.common.dto.opening.history;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

import java.time.LocalDateTime;

@With
public record OpeningDetailsHistoryAuditEventDto(
        @Schema(types = {"integer"}, requiredMode = Schema.RequiredMode.REQUIRED)
        Long openingId,

        @Schema(types = {"integer"}, requiredMode = Schema.RequiredMode.REQUIRED)
        Long auditEventId,

        @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        Long regimeId,

        @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        Long projectId,

        @Schema(types = {"object", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        CodeDescriptionDto action,

        @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime actionTimestamp,

        @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        String description,

        @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        String userId,

        @Schema(types = {"boolean"}, requiredMode = Schema.RequiredMode.REQUIRED)
        Boolean isEmailSent,

        @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        Long xmlSubmissionId,

        @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        Long openingAmendmentNumber,

        @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        String entryUserId,

        @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime entryTimestamp
) {

}
