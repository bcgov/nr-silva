package ca.bc.gov.restapi.results.common.dto.opening.history;

import ca.bc.gov.restapi.results.common.dto.comment.CommentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

import java.util.List;

@With
public record OpeningStockingHistoryDto(
    @Schema(types = {"object"}, requiredMode = Schema.RequiredMode.REQUIRED)
    OpeningStockingHistoryDetailsDto stocking,

    @Schema(types = {"array"}, requiredMode = Schema.RequiredMode.REQUIRED)
    List<OpeningStockingHistorySpeciesDto> preferredSpecies,

    @Schema(types = {"array"}, requiredMode = Schema.RequiredMode.REQUIRED)
    List<OpeningStockingHistorySpeciesDto> acceptableSpecies,

    @Schema(types = {"array"}, requiredMode = Schema.RequiredMode.REQUIRED)
    List<OpeningStockingHistoryLayerDto> layers,

    @Schema(types = {"array"}, requiredMode = Schema.RequiredMode.REQUIRED)
    List<CommentDto> comments
) {}
