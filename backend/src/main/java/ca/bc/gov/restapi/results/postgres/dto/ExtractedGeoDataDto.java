package ca.bc.gov.restapi.results.postgres.dto;

import tools.jackson.databind.JsonNode;
import java.util.List;
import lombok.Builder;
import lombok.With;
import org.jspecify.annotations.Nullable;

/**
 * DTO representing extracted geographical data, including metadata, geoJson, and tenure
 * information.
 */
@Builder
@With
public record ExtractedGeoDataDto(
    @Nullable GeoMetaDataDto metaData, JsonNode geoJson, List<TenureDto> tenureList) {}
