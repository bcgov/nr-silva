package ca.bc.gov.restapi.results.common.dto.cover.history;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningForestCoverHistoryPolygonDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Long forestCoverId,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto reserve,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto objective,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto siteClass,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long siteIndex,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto siteIndexSource,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto treeCoverPattern,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long reentryYear
) {

}
