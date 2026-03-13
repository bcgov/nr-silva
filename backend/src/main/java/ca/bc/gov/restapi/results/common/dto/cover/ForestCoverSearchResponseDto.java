package ca.bc.gov.restapi.results.common.dto.cover;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import java.time.LocalDateTime;
import java.util.List;

public record ForestCoverSearchResponseDto(
    Long forestCoverId,
    String polygonId,
    String standardUnitId,
    List<CodeDescriptionDto> damageAgents,
    CodeDescriptionDto stockingType,
    CodeDescriptionDto stockingStatus,
    String fileId,
    Long openingId,
    CodeDescriptionDto openingCategory,
    CodeDescriptionDto orgUnit,
    ForestClientDto openingClient,
    LocalDateTime updateTimestamp,
    LocalDateTime regenDueDate,
    LocalDateTime freeGrowingDueDate
) {}
