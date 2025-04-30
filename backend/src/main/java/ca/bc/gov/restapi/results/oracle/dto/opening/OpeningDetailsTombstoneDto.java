package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import java.time.LocalDate;
import lombok.With;

@With
public record OpeningDetailsTombstoneDto(
    String openingNumber,
    CodeDescriptionDto openingStatus,
    String orgUnitCode,
    String orgUnitName,
    CodeDescriptionDto openCategory,
    ForestClientDto client,
    String fileId,
    String cutBlockID,
    String cuttingPermitId,
    String timberMark,
    String maxAllowedAccess,
    Float openingGrossArea,
    String createdBy,
    LocalDate createdOn,
    LocalDate lastUpdatedOn,
    LocalDate disturbanceStartDate
) {

}
