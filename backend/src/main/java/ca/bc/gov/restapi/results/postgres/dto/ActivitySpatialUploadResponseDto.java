package ca.bc.gov.restapi.results.postgres.dto;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * Result of processing the spatial file of an activity, with the opening geometry needed to
 * compare it with the activity.
 *
 * @param geometryArea area of the activity geometry in hectares
 * @param geoJson activity geometry as GeoJSON in EPSG:4326
 * @param openingGeoJson geometry of the opening as a GeoJSON feature collection in EPSG:4326, when
 *     the opening has one
 */
public record ActivitySpatialUploadResponseDto(
    @Schema(
            description = "Activity geometry area in hectares",
            example = "12345.6789",
            minimum = "0.0")
        BigDecimal geometryArea,
    @Schema(description = "Activity geometry as GeoJSON in EPSG:4326") JsonNode geoJson,
    @Schema(
            description = "Opening geometry as a GeoJSON feature collection in EPSG:4326; "
                + "null when the opening has no geometry",
            nullable = true)
        JsonNode openingGeoJson) {}
