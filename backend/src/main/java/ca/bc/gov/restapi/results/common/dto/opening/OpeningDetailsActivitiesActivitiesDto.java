package ca.bc.gov.restapi.results.common.dto.opening;

import java.time.LocalDate;
import java.util.List;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
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
    LocalDate endDate,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
        description = "All objectives of the activity. objective1..3 repeat the first three.")
    List<CodeDescriptionDto> objectives,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
        description = "All funding sources of the activity. funding repeats the first one.")
    List<CodeDescriptionDto> fundingSources
) {

}
