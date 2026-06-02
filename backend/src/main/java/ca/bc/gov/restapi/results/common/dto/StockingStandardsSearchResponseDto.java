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
    List<String> fspIds,
    List<String> bgcList,
    boolean isDefaultStandard,
    List<CodeDescriptionDto> orgUnits,
    List<ForestClientDto> clients,
    LocalDateTime approvedDate) {}
