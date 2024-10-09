package ca.bc.gov.restapi.results.postgres.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record TrackOpeningDto(
    @Schema(
            description = "System generated value uniquely identifying the opening.",
            example = "1541297")
        Long openingId,
    @Schema(description = "List of steps including the opening history.")
        List<TrackOpeningStepDto> steps) {}
