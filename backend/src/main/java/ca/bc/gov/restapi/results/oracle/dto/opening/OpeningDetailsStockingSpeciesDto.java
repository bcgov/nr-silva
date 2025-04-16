package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import lombok.With;

@With
public record OpeningDetailsStockingSpeciesDto(
    CodeDescriptionDto species,
    Long minHeight
) {

}
