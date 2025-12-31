package ca.bc.gov.restapi.results.common.dto.opening.history;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

import java.util.List;

@With
public record OpeningStockingHistoryWithComparisonDto(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) OpeningStockingHistoryDetailsWithComparisonDto standardUnit,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        List<OpeningStockingHistoryLayerWithComparisonDto> layers
) {
}
