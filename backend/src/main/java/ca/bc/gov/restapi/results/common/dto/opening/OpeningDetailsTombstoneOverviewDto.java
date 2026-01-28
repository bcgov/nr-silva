package ca.bc.gov.restapi.results.common.dto.opening;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

import java.util.List;

@With
public record OpeningDetailsTombstoneOverviewDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Long openingId,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    OpeningDetailsTombstoneDto tombstone,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    OpeningDetailsOverviewDto overview,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<OpeningDetailsNotificationDto> notifications
) {

}
