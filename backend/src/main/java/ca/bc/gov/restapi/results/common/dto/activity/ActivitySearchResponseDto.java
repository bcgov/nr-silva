package ca.bc.gov.restapi.results.common.dto.activity;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ActivitySearchResponseDto(
    Long activityId,
    CodeDescriptionDto base,
    CodeDescriptionDto technique,
    CodeDescriptionDto method,
    boolean isComplete,
    CodeDescriptionDto fundingSource,
    String fileId,
    String cutBlock,
    Long openingId,
    String cuttingPermit,
    BigDecimal treatmentAmountArea,
    String intraAgencyNumber,
    CodeDescriptionDto openingCategory,
    CodeDescriptionDto orgUnit,
    ForestClientDto openingClient,
    LocalDateTime updateTimestamp) {}
