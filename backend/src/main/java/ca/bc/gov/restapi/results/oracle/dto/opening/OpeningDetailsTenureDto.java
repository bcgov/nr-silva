package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;

public record OpeningDetailsTenureDto(
    long id,
    boolean primaryTenure,
    String fileId,
    String cutBlock,
    String cuttingPermit,
    String timberMark,
    CodeDescriptionDto status,
    Float plannedGrossArea,
    Float plannedNetArea
) {

}
