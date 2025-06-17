package ca.bc.gov.restapi.results.oracle.service.conversion.opening;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsOverviewDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsOverviewMilestoneDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsOverviewOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsTombstoneDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsTombstoneOverviewDto;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneOverviewMilestoneProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneOverviewOpeningProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneProjection;
import java.util.List;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpeningDetailsTombstoneConverter {

  public static Function<OpeningTombstoneProjection, OpeningDetailsTombstoneOverviewDto> mapTombstoneOverview() {
    return tombstone ->
        new OpeningDetailsTombstoneOverviewDto(
            tombstone.getOpeningId(),
            new OpeningDetailsTombstoneDto(
                tombstone.getOpeningNumber(),
                new CodeDescriptionDto(
                    tombstone.getOpeningStatusCode(),
                    tombstone.getOpeningStatusName()
                ),
                tombstone.getOrgUnitCode(),
                tombstone.getOrgUnitName(),
                new CodeDescriptionDto(
                    tombstone.getOpenCategoryCode(),
                    tombstone.getOpenCategoryName()
                ),
                new ForestClientDto(
                    tombstone.getClient(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                ),
                tombstone.getFileId(),
                tombstone.getCutBlockID(),
                tombstone.getCuttingPermitId(),
                tombstone.getTimberMark(),
                tombstone.getMaxAllowedAccess(),
                tombstone.getOpeningGrossArea(),
                tombstone.getCreatedBy(),
                tombstone.getCreatedOn(),
                tombstone.getLastUpdatedOn(),
                tombstone.getDisturbanceStartDate()
            ),
            new OpeningDetailsOverviewDto(
                new OpeningDetailsOverviewOpeningDto(

                    null,
                    null,
                    null,
                    null,
                    null,
                    List.of()
                ),
                new OpeningDetailsOverviewMilestoneDto(
                    null,
                    null,
                    null,
                    0,
                    null,
                    null,
                    0,
                    null
                )
            ),
            List.of()
        );
  }

  public static Function<OpeningTombstoneOverviewOpeningProjection, OpeningDetailsTombstoneOverviewDto> mapTombstoneOpeningOverview(
      OpeningDetailsTombstoneOverviewDto tombstone
  ) {
    return overview ->
        tombstone
            .withOverview(
                tombstone
                    .overview()
                    .withOpening(
                        new OpeningDetailsOverviewOpeningDto(
                            overview.getLicenseeOpeningId(),
                            new CodeDescriptionDto(
                                overview.getTenureTypeCode(),
                                overview.getTenureTypeName()
                            ),
                            new CodeDescriptionDto(
                                overview.getManagementUnitTypeCode(),
                                overview.getManagementUnitTypeName()
                            ),
                            overview.getManagementUnitId(),
                            new CodeDescriptionDto(
                                overview.getTimberSaleOfficeCode(),
                                overview.getTimberSaleOfficeName()
                            ),
                            tombstone.overview().opening().comments()
                        )
                    )
            );
  }

  public static Function<OpeningTombstoneOverviewMilestoneProjection, OpeningDetailsTombstoneOverviewDto> mapTombstoneMilestoneOverview(
      OpeningDetailsTombstoneOverviewDto tombstone
  ) {
    return overview ->
        tombstone
            .withOverview(
                tombstone
                    .overview()
                    .withMilestones(
                        new OpeningDetailsOverviewMilestoneDto(
                            overview.getStandardsUnitId(),
                            overview.getPostHarvestDeclaredDate(),
                            overview.getRegenDeclaredDate(),
                            overview.getRegenOffsetYears(),
                            overview.getRegenDueDate(),
                            overview.getFreeGrowingDeclaredDate(),
                            overview.getFreeGrowingOffsetYears(),
                            overview.getFreeGrowingDueDate()
                        )
                    )
            );
  }


}
