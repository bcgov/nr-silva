package ca.bc.gov.restapi.results.common.dto.opening;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record OpeningDetailsTenureDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    long id,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    boolean primaryTenure,

    @Schema(types = {"null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String fileId,

    @Schema(types = {"null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String cutBlock,

    @Schema(types = {"null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String cuttingPermit,

    @Schema(types = {"null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String timberMark,

    @Schema(types = {"object", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto status,

    @Schema(types = {"null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Float plannedGrossArea,

    @Schema(types = {"null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Float plannedNetArea
) {

}
