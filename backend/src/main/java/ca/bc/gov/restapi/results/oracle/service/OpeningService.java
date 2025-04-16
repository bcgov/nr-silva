package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsBecDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingDetailsDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingLayerDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingSpeciesDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsTombstoneOverviewDto;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.oracle.service.conversion.opening.OpeningDetailsCommentConverter;
import ca.bc.gov.restapi.results.oracle.service.conversion.opening.OpeningDetailsTombstoneConverter;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

/**
 * This class holds methods for fetching and handling {@link OpeningEntity} in general.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningService {

  private final OpeningRepository openingRepository;
  private final ForestClientService forestClientService;
  private final SilvicultureCommentRepository commentRepository;

  public Optional<OpeningDetailsTombstoneOverviewDto> getOpeningTombstone(Long openingId) {

    return
        //Everything starts with the base tombstone search/load because if no data is found
        // then we will not load anything else
        openingRepository
            .getOpeningTombstoneByOpeningId(openingId)
            //The map starts to convert to the final object
            .map(OpeningDetailsTombstoneConverter.mapTombstoneOverview())
            //We then follow up with client information load, if any exists, or we move ahead
            .map(tombstone ->
                Optional
                    .ofNullable(tombstone.tombstone().client().clientNumber())
                    .flatMap(forestClientService::getClientByNumber)
                    .map(
                        client -> tombstone
                            .withTombstone(
                                tombstone
                                    .tombstone()
                                    .withClient(client)
                            )
                    )
                    .orElse(tombstone)
            )
            //Then we move to the overview. We start with the opening part of it
            .map(tombstone ->
                openingRepository
                    .getOpeningTombstoneOverviewByOpeningId(openingId)
                    .map(OpeningDetailsTombstoneConverter.mapTombstoneOpeningOverview(tombstone))
                    .orElse(tombstone)
            )
            //Then we move to the milestone part of it. This is split to avoid multiple returns when
            // it's not required
            .map(tombstone ->
                openingRepository
                    .getOpeningTombstoneMilestoneByOpeningId(openingId)
                    .map(OpeningDetailsTombstoneConverter.mapTombstoneMilestoneOverview(tombstone))
                    .orElse(tombstone)
            )
            //Finally we load the comments. This is the last step, and it loads comments associated to
            // the opening. This is done in a separate step to avoid loading comments if no tombstone
            // is found. This is a performance optimization.
            .map(tombstone ->
                tombstone
                    .withOverview(
                        tombstone
                            .overview()
                            .withOpening(
                                tombstone
                                    .overview()
                                    .opening()
                                    .withComments(
                                        commentRepository
                                            .getCommentById(openingId)
                                            .stream()
                                            .map(OpeningDetailsCommentConverter.mapComments())
                                            .toList()
                                    )
                            )
                    )
            );
  }

  public List<OpeningDetailsStockingDto> getOpeningStockingDetails(Long openingId) {

    return openingRepository
        .getOpeningStockingDetailsByOpeningId(openingId)
        .stream()
        .map(projection ->
            new OpeningDetailsStockingDto(
                new OpeningDetailsStockingDetailsDto(
                    projection.getStockingStandardUnit(),
                    projection.getSsid(),
                    BooleanUtils.toBooleanDefaultIfNull(projection.getDefaultMof(), false),
                    BooleanUtils.toBooleanDefaultIfNull(projection.getManualEntry(), false),
                    projection.getFspId(),
                    projection.getNetArea(),
                    projection.getSoilDisturbancePercent(),
                    new OpeningDetailsBecDto(
                        projection.getBecZoneCode(),
                        projection.getBecSubzoneCode(),
                        projection.getBecVariant(),
                        projection.getBecPhase(),
                        projection.getBecSiteSeries(),
                        projection.getBecSiteType(),
                        projection.getBecSeral()
                    ),
                    projection.getRegenDelay(),
                    projection.getFreeGrowingLate()
                ),
                List.of(),
                List.of(),
                null
            )
        )
        .map(detailsDto ->
            detailsDto
                .withAcceptableSpecies(
                    openingRepository
                        .getOpeningStockingSpeciesByOpeningId(
                            openingId,
                            "N",
                            detailsDto.stocking().ssid()
                        )
                        .stream()
                        .map(species ->
                            new OpeningDetailsStockingSpeciesDto(
                                new CodeDescriptionDto(
                                    species.getSpeciesCode(),
                                    species.getSpeciesName()
                                ),
                                species.getMinHeight()
                            )
                        )
                        .toList()
                )
                .withPreferredSpecies(
                    openingRepository
                        .getOpeningStockingSpeciesByOpeningId(
                            openingId,
                            "Y",
                            detailsDto.stocking().ssid()
                        )
                        .stream()
                        .map(species ->
                            new OpeningDetailsStockingSpeciesDto(
                                new CodeDescriptionDto(
                                    species.getSpeciesCode(),
                                    species.getSpeciesName()
                                ),
                                species.getMinHeight()
                            )
                        )
                        .toList()
                )
        )
        .map(detailsDto ->
            openingRepository
                .getOpeningStockingLayerByOpeningId(openingId,detailsDto.stocking().ssid())
                .map(layer ->
                    new OpeningDetailsStockingLayerDto(
                        layer.getMinWellspacedTrees(),
                        layer.getMinPreferredWellspacedTrees(),
                        layer.getMinHorizontalDistanceWellspacedTrees(),
                        layer.getTargetWellspacedTrees(),
                        layer.getMinResidualBasalArea(),
                        layer.getMinPostspacingDensity(),
                        layer.getMaxPostspacingDensity(),
                        layer.getMaxConiferous(),
                        layer.getHeightRelativeToComp()
                    )
                )
                .map(detailsDto::withLayer)
                .orElse(detailsDto)
        )
        .toList();
  }


}
