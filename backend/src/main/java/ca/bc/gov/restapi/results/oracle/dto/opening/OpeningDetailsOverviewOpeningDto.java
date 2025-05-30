package ca.bc.gov.restapi.results.oracle.dto.opening;

import java.util.List;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.comment.CommentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningDetailsOverviewOpeningDto(
    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String licenseeId,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto tenureType,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto managementUnitType,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String managementUnitId,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto timberSaleOffice,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<CommentDto> comments
) {

}
