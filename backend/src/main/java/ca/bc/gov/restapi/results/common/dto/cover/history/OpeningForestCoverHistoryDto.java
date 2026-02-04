package ca.bc.gov.restapi.results.common.dto.cover.history;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

import java.time.LocalDate;

@With
public record OpeningForestCoverHistoryDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Long coverId,

    @Schema(types = {"string"}, requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate archiveDate,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String polygonId,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String standardUnitId,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto unmappedArea,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Float grossArea,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Float netArea,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto status,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto coverType,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    OpeningForestCoverHistoryLayerListDescriptionDto inventoryLayer,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    OpeningForestCoverHistoryLayerListDescriptionDto silvicultureLayer,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Integer referenceYear,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    boolean isSingleLayer,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    boolean hasReserve) {}
