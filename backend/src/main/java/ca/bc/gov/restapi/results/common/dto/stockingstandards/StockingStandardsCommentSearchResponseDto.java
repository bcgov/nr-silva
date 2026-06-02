package ca.bc.gov.restapi.results.common.dto.stockingstandards;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.enums.StockingStandardsCommentLocationCode;
import java.time.LocalDateTime;
import java.util.List;

public record StockingStandardsCommentSearchResponseDto(
    Long standardsRegimeId,
    StockingStandardsCommentLocationCode commentLocation,
    CodeDescriptionDto standardRegimeStatus,
    String commentText,
    LocalDateTime updateTimestamp,
    LocalDateTime approvedTimestamp,
    List<ForestClientDto> clients,
    List<CodeDescriptionDto> orgUnits,
    List<String> fspIds) {}
