package ca.bc.gov.restapi.results.postgres.dto;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.With;

/** DTO representing extracted geographical data from an uploaded spatial file. */
@Builder
@With
public record ExtractedGeoDataDto(
    @Schema(
            description = "Geometry area in hectares (NUMBER(11,4))",
            example = "12345.6789",
            minimum = "0.0",
            maximum = "999999999.9999")
        BigDecimal geometryArea,
    JsonNode geoJson) {}
