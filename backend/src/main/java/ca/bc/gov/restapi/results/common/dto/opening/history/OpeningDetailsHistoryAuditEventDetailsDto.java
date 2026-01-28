package ca.bc.gov.restapi.results.common.dto.opening.history;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

import java.time.LocalDateTime;

@With
public record OpeningDetailsHistoryAuditEventDetailsDto(
        @Schema(types = {"integer"}, requiredMode = Schema.RequiredMode.REQUIRED)
        Long auditEventId,

        @Schema(types = {"integer"}, requiredMode = Schema.RequiredMode.REQUIRED)
        Long auditDetailId,

        @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        String businessIdentifier,

        @Schema(types = {"string"}, requiredMode = Schema.RequiredMode.REQUIRED)
        String tableName,

        @Schema(types = {"string"}, requiredMode = Schema.RequiredMode.REQUIRED)
        String columnName,

        @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        String oldValue,

        @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        String newValue,

        @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        String entryUserId,

        @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime entryTimestamp
) {
}
