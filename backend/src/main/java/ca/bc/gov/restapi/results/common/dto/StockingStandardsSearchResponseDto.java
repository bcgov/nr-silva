package ca.bc.gov.restapi.results.common.dto;

import java.time.LocalDateTime;
import java.util.List;

public record StockingStandardsSearchResponseDto(
    Long standardsRegimeId,
    String standardsRegimeName,
    boolean
        isExpired, // determined by comparing the current date to the standards regime's expiry_date
    String standardsObjective,
    List<CodeDescriptionDto> preferredSpecies,
    String fspId,
    String bgcZone,
    String bgcSubZone,
    String bgcVariant,
    String bgcPhase,
    String becSiteSeries,
    String becSiteType, // aka Site Phase
    String becSeral,
    CodeDescriptionDto orgUnit,
    ForestClientDto standardsRegimeClient,
    LocalDateTime updateTimestamp) {}
