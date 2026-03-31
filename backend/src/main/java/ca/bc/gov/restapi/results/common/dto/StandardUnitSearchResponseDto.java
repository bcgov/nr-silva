package ca.bc.gov.restapi.results.common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record StandardUnitSearchResponseDto(
    Long stockingStandardUnitId,
    Long openingId,
    String fileId,
    String cutBlock,
    String cuttingPermit,
    String standardsUnitId,
    BigDecimal netArea, // NAR (ha)
    Integer regenDelayYears,
    Integer freeGrowingEarlyYears,
    Integer freeGrowingLateYears,
    Integer totalLayer,
    Integer targetWellSpacedTrees,
    List<CodeDescriptionDto> preferredSpecies,
    String bgcZone,
    String bgcSubZone,
    String bgcVariant,
    String bgcPhase,
    String becSiteSeries,
    String becSiteType,
    String becSeral,
    CodeDescriptionDto orgUnit,
    ForestClientDto openingClient,
    LocalDateTime updateTimestamp) {}
