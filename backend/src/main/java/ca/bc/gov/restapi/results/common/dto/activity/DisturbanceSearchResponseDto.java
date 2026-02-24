package ca.bc.gov.restapi.results.common.dto.activity;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import java.time.LocalDateTime;

public record DisturbanceSearchResponseDto(
    Long activityId,
    CodeDescriptionDto disturbance,
    CodeDescriptionDto silvSystem,
    CodeDescriptionDto variant,
    CodeDescriptionDto cutPhase,
    String fileId,
    String cutBlock,
    Long openingId,
    CodeDescriptionDto openingCategory,
    CodeDescriptionDto orgUnit,
    ForestClientDto openingClient,
    LocalDateTime updateTimestamp) {}
