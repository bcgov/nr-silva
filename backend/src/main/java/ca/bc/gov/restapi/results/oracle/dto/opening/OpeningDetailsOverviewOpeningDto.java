package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.comment.CommentDto;
import java.util.List;
import lombok.With;

@With
public record OpeningDetailsOverviewOpeningDto(
    String licenseeId,
    CodeDescriptionDto tenureType,
    CodeDescriptionDto managementUnitType,
    String managementUnitId,
    CodeDescriptionDto timberSaleOffice,
    List<CommentDto> comments
) {

}
