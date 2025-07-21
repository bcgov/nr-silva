package ca.bc.gov.restapi.results.oracle.dto.cover;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningForestCoverDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED) Long coverId,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String polygonId,
    @Schema(
            types = {"string", "null"},
            requiredMode = Schema.RequiredMode.REQUIRED)
        String standardUnitId,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED) CodeDescriptionDto unmappedArea,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED) Float grossArea,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED) Float netArea,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED) CodeDescriptionDto status,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED) CodeDescriptionDto coverType,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        OpeningForestCoverLayerListDescriptionDto inventoryLayer,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        OpeningForestCoverLayerListDescriptionDto silvicultureLayer,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED) Integer referenceYear,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED) boolean isSingleLayer,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED) boolean hasReserve) {}
