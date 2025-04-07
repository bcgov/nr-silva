package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import java.time.LocalDate;
import lombok.With;

@With
public record OpeningDetailsTombstoneDto(
    String openingNumber,
    OpeningStatusEnum openingStatus,
    String orgUnitCode,
    String orgUnitName,
    OpeningCategoryEnum openCategory,
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
