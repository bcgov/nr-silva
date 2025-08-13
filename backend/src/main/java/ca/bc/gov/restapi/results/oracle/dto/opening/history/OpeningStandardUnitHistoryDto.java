package ca.bc.gov.restapi.results.oracle.dto.opening.history;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

import java.util.List;

@With
public record OpeningStandardUnitHistoryDto(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        OpeningStandardUnitHistoryDetailsDto standardUnit,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        List<OpeningStandardUnitHistoryLayerDetailsDto> layers
) {
}
