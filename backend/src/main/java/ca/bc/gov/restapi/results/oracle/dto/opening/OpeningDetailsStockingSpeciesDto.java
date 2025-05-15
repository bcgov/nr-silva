package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import lombok.With;

@With
public record OpeningDetailsStockingSpeciesDto(
    String layer,
    CodeDescriptionDto species,
    Float minHeight
) {

}
