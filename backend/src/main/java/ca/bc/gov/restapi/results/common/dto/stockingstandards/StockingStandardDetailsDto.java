package ca.bc.gov.restapi.results.common.dto.stockingstandards;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsBecDto;
import java.time.LocalDateTime;
import java.util.List;

/** Full stocking standard detail shared by the PostgreSQL and Oracle configurations. */
public record StockingStandardDetailsDto(
    Long stockingStandardId,
    String statusCode,
    LocalDateTime createdDate,
    LocalDateTime effectiveDate,
    LocalDateTime expiryDate,
    String objective,
    String name,
    String location,
    boolean ministryDefault,
    Boolean alternativeMethodSelected,
    List<CodeDescriptionDto> orgUnits,
    List<ForestClientDto> clients,
    List<OpeningDetailsBecDto> becData,
    String stockingType,
    Integer regenDelayYears,
    Integer freeGrowingYears,
    Integer earlyYears,
    Integer lateYears,
    String layerType,
    String additionalStandards,
    List<StockingStandardDetailsLayerDto> layers) {}
