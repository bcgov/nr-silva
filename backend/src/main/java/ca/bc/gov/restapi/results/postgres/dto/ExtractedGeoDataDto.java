package ca.bc.gov.restapi.results.postgres.dto;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.Builder;
import lombok.With;
import org.springframework.lang.Nullable;

/** This record contains all possible filters for the dashboard openings per years api. */
@Builder
@With
public record ExtractedGeoDataDto(
    @Nullable GeoMetaDataDto metaData, JsonNode geoJson, List<TenureDto> tenureList) {}
