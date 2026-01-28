package ca.bc.gov.restapi.results.common.dto.opening;

import java.time.LocalDate;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record OpeningDetailsActivitiesActivitiesDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Long atuId,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto status,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto base,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto tech,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto method,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto objective1,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto objective2,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto objective3,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Float area,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto funding,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String projectId,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate lastUpdate,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate plannedDate,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate endDate
) {

}
