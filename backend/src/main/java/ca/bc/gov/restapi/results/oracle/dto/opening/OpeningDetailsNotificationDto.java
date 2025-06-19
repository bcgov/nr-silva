package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.oracle.enums.OpeningDetailsNotificationStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningDetailsNotificationDto(
        @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        String description,

        @Schema(types = {"string"}, requiredMode = Schema.RequiredMode.REQUIRED)
        OpeningDetailsNotificationStatusEnum status
) {
}
