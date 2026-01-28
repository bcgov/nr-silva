package ca.bc.gov.restapi.results.common.dto.opening;

import lombok.With;

@With
public record OpeningTombstoneDto(
    String openingNumber,
    String openingStatus,
    String openingType,
    String orgUnit,
    String openingCategory,
    String client,
    String fileId,
    String cutBlock,
    String cuttingPermit,
    String timberMark,
    String maxAllowedAccess,
    String openingGrossArea,
    String createdBy,
    String createdOn,
    String lastUpdatedOn,
    String disturbanceStartDate,
    String licenseeOpeningId,
    String tenureType,
    String managementUnitType,
    String managementUnitId,
    String timberSalesOffice,
    String commentType,
    String milestonePostHarverstDeclaredDate,
    String milestoneRegenDeclaredDate,
    String milestoneRegenOffset,
    String milestoneRegenDueDate,
    String milestoneFreeGrowingDeclaredDate,
    String milestoneFreeGrowingOffset,
    String milestoneFreeGrowingDueDate
) {

}
