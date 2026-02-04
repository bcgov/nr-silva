package ca.bc.gov.restapi.results.common.dto.activity;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;

public record OpeningActivitySpeciesDetailsDto(
    CodeDescriptionDto species,
    Long plantedNumber,
    Long numberBeyondTransferLimit,
    boolean cbst,
    Long requestId,
    Long lot,
    Float bidPricePerTree
) {

}
