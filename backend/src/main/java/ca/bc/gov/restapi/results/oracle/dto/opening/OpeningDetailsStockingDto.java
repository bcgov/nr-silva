package ca.bc.gov.restapi.results.oracle.dto.opening;

import java.util.List;

import ca.bc.gov.restapi.results.oracle.dto.comment.CommentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningDetailsStockingDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    OpeningDetailsStockingDetailsDto stocking,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<OpeningDetailsStockingSpeciesDto> preferredSpecies,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<OpeningDetailsStockingSpeciesDto> acceptableSpecies,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<OpeningDetailsStockingLayerDto> layers,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<CommentDto> comments
) {
}
