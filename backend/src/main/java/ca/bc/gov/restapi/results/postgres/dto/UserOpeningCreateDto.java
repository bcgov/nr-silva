package ca.bc.gov.restapi.results.postgres.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** This record represents the JSON body request for saving Openings as favourite. */
public record UserOpeningCreateDto(
    @Schema(
            description = "System generated value uniquely identifying the opening.",
            example = "1541297")
        String openingId) {}
