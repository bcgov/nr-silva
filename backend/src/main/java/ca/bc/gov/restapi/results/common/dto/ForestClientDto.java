package ca.bc.gov.restapi.results.common.dto;

import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import lombok.Builder;
import lombok.With;

/**
 * This record represents a Forest Client object.
 */
@Builder
@With
public record ForestClientDto(
    String clientNumber,
    String clientName,
    String legalFirstName,
    String legalMiddleName,
    ForestClientStatusEnum clientStatusCode,
    ForestClientTypeEnum clientTypeCode,
    String acronym
) {

}
