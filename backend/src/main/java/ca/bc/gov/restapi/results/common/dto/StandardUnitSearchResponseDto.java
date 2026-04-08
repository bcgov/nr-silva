package ca.bc.gov.restapi.results.common.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record StandardUnitSearchResponseDto(
    Long stockingStandardUnitId,
    Long openingId,
    String fileId,
    String cutBlock,
    String cuttingPermit,
    String standardsUnitId,
    Long standardsRegimeId, // Standard ID
    boolean isStandardsRegimeExpired,
    BigDecimal netArea, // NAR (ha)
    LocalDate regenDueDate,
    LocalDate freeGrowingDueDate,
    Integer totalLayer,
    List<CodeDescriptionDto> preferredSpecies,
    String bgcZone,
    String bgcSubZone,
    String bgcVariant,
    String bgcPhase,
    String becSiteSeries,
    String becSiteType, // aka Site Phase
    String becSeral,
    CodeDescriptionDto orgUnit,
    ForestClientDto openingClient,
    LocalDateTime updateTimestamp) {}
