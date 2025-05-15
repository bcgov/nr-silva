package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.oracle.dto.comment.CommentDto;
import java.util.List;
import lombok.With;

@With
public record OpeningDetailsStockingDto(
    OpeningDetailsStockingDetailsDto stocking,
    List<OpeningDetailsStockingSpeciesDto> preferredSpecies,
    List<OpeningDetailsStockingSpeciesDto> acceptableSpecies,
    List<OpeningDetailsStockingLayerDto> layers,
    List<CommentDto> comments
) {
}
