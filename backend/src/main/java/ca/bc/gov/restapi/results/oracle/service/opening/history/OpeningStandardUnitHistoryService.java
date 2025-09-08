package ca.bc.gov.restapi.results.oracle.service.opening.history;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsBecDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingSpeciesDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.history.*;
import ca.bc.gov.restapi.results.oracle.entity.opening.history.*;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.oracle.service.conversion.opening.OpeningDetailsCommentConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningStandardUnitHistoryService {

    private final OpeningRepository openingRepository;
    private final SilvicultureCommentRepository commentRepository;

    public List<OpeningStockingHistoryOverviewDto> getStandardUnitOverviewHistoryList(Long openingId) {
        List<OpeningStockingHistoryProjection> projections = openingRepository
                .getOpeningStandardUnitHistoryByOpeningId(openingId);

        if (projections.isEmpty()) {
            return List.of();
        }

        LocalDateTime maxTimestamp = projections.stream()
            .map(OpeningStockingHistoryProjection::getEventTimestamp)
            .max(LocalDateTime::compareTo)
            .orElse(null);

        LocalDateTime minTimestamp = projections.stream()
            .map(OpeningStockingHistoryProjection::getEventTimestamp)
            .min(LocalDateTime::compareTo)
            .orElse(null);

        return projections
                .stream()
                .map(toOverviewDto(maxTimestamp, minTimestamp))
                .toList();
    }

    public List<OpeningStockingHistoryDto> getOpeningStockingHistoryList(
        Long openingId,
        Long eventHistoryId) {
        return openingRepository.getOpeningStockingHistoryDetailsByOpeningIdAndEventHistoryId
            (openingId, eventHistoryId)
            .stream()
            .map(getDetails())
            .map(getSpecies(openingId, eventHistoryId))
            .map(getLayer(openingId, eventHistoryId))
            .map(getComments())
            .toList();
    }

    public List<OpeningStockingHistoryWithComparisonDto> getStandardUnitHistoryDetailsWithComparison(Long openingId, Long stockingEventHistoryId) {
        List<OpeningStockingHistoryDetailsWithComparisonProjection> suDetailProjections = openingRepository
                .getOpeningStandardUnitHistoryDetailsWithComparisonByOpeningIdAndHistoryId(openingId, stockingEventHistoryId);

        if (suDetailProjections.isEmpty()) {
            return List.of();
        }

        List<OpeningStockingHistoryLayerWithComparisonProjection> layerDetailProjections = openingRepository
                .getOpeningStandardUnitHistoryLayerDetailsWithComparisonByOpeningIdAndHistoryId(openingId, stockingEventHistoryId);

        List<OpeningStockingHistoryLayerSpeciesWithComaprisonProjection> layerSpeciesProjections = openingRepository
                .getOpeningStandardUnitHistoryLayerSpeciesDetailsWithComparisonByOpeningIdAndHistoryId(openingId, stockingEventHistoryId);

        return suDetailProjections
                .stream()
                .map(addDetailsWithComparison())
                .map(addLayersWithComparison(layerDetailProjections, layerSpeciesProjections))
                .toList();

    }

    private Function<OpeningStockingHistoryProjection, OpeningStockingHistoryOverviewDto> toOverviewDto(LocalDateTime maxTimestamp, LocalDateTime minTimestamp) {
        return projection -> new OpeningStockingHistoryOverviewDto(
                projection.getStockingEventHistoryId(),
                projection.getAmendmentNumber(),
                projection.getEventTimestamp(),
                projection.getSuCount(),
                projection.getTotalNar(),
                new CodeDescriptionDto(
                        projection.getAuditActionCode(),
                        projection.getAuditActionDescription()
                ),
                projection.getEsfSubmissionId(),
                projection.getSubmittedByUserId(),
                projection.getApprovedByUserId(),
                projection.getEventTimestamp().equals(maxTimestamp),
                projection.getEventTimestamp().equals(minTimestamp)
        );
    }

    private Function<OpeningStockingHistoryDetailsWithComparisonProjection, OpeningStockingHistoryWithComparisonDto> addDetailsWithComparison() {
        return projection -> {
            OpeningStockingHistoryDetailsWithComparisonDto
                detailsDto = new OpeningStockingHistoryDetailsWithComparisonDto(
                    projection.getStockingStandardUnitId(),
                    projection.getStandardsUnitId(),
                    projection.getOldRegimeId(),
                    projection.getNewRegimeId(),
                    projection.getOldNetArea(),
                    projection.getNewNetArea(),
                    projection.getOldMaxSoilDisturbance(),
                    projection.getNewMaxSoilDisturbance(),
                    projection.getOldVarianceIndicator(),
                    projection.getNewVarianceIndicator(),
                    projection.getOldRegenObligationIndicator(),
                    projection.getNewRegenObligationIndicator(),
                    projection.getOldNoRegenEarlyOffsetYears(),
                    projection.getNewNoRegenEarlyOffsetYears(),
                    projection.getOldNoRegenLateOffsetYears(),
                    projection.getNewNoRegenLateOffsetYears(),
                    projection.getOldRegenOffsetYears(),
                    projection.getNewRegenOffsetYears(),
                    projection.getOldFreeGrowingEarlyOffsetYears(),
                    projection.getNewFreeGrowingEarlyOffsetYears(),
                    projection.getOldFreeGrowingLateOffsetYears(),
                    projection.getNewFreeGrowingLateOffsetYears(),
                    projection.getOldBgcZone(),
                    projection.getNewBgcZone(),
                    projection.getOldBgcSubzone(),
                    projection.getNewBgcSubzone(),
                    projection.getOldBgcVariant(),
                    projection.getNewBgcVariant(),
                    projection.getOldBgcPhase(),
                    projection.getNewBgcPhase(),
                    projection.getOldBecSiteSeries(),
                    projection.getNewBecSiteSeries(),
                    projection.getOldBecSiteType(),
                    projection.getNewBecSiteType(),
                    projection.getOldBecSeral(),
                    projection.getNewBecSeral()
            );

            return new OpeningStockingHistoryWithComparisonDto(
                    detailsDto,
                    List.of()
            );
        };
    }

    private Function<OpeningStockingHistoryWithComparisonDto, OpeningStockingHistoryWithComparisonDto> addLayersWithComparison(
            List<OpeningStockingHistoryLayerWithComparisonProjection> allLayers,
            List<OpeningStockingHistoryLayerSpeciesWithComaprisonProjection> allSpecies
    ) {
        return dto -> dto.withLayers(
                allLayers.stream()
                        .filter(layer -> layer.getSsuId().equals(dto.standardUnit().stockingStandardUnitId()))
                        .map(createLayerWithComparisonDto(allSpecies))
                        .toList()
        );
    }

    private Function<OpeningStockingHistoryLayerWithComparisonProjection, OpeningStockingHistoryLayerWithComparisonDto> createLayerWithComparisonDto(
            List<OpeningStockingHistoryLayerSpeciesWithComaprisonProjection> allSpecies
    ) {
        return layer -> {
            OpeningStockingHistoryLayerWithComparisonDto
                layerDto = new OpeningStockingHistoryLayerWithComparisonDto(
                    layer.getOldLayerId(),
                    layer.getNewLayerId(),
                    new CodeDescriptionDto(
                            layer.getOldStockingLayerCode(),
                            layer.getOldStockingLayerDescription()
                    ),
                    new CodeDescriptionDto(
                            layer.getNewStockingLayerCode(),
                            layer.getNewStockingLayerDescription()
                    ),
                    layer.getOldMinHorizontalDistance(),
                    layer.getNewMinHorizontalDistance(),
                    layer.getOldMinPerfStockingStandard(),
                    layer.getNewMinPerfStockingStandard(),
                    layer.getOldMinStockingStandard(),
                    layer.getNewMinStockingStandard(),
                    layer.getOldMinPostSpacing(),
                    layer.getNewMinPostSpacing(),
                    layer.getOldResidualBasalArea(),
                    layer.getNewResidualBasalArea(),
                    layer.getOldTargetWellSpacedTrees(),
                    layer.getNewTargetWellSpacedTrees(),
                    layer.getOldHeightRelativeToComp(),
                    layer.getNewHeightRelativeToComp(),
                    layer.getOldMaxConifer(),
                    layer.getNewMaxConifer(),
                    layer.getOldMaxPostSpacing(),
                    layer.getNewMaxPostSpacing(),
                    List.of(),
                    List.of()
            );

            return layerDto
                    .withPreferredSpecies(
                        createSpeciesWithComparisonList(layer.getNewLayerId(), true, allSpecies))
                    .withAcceptableSpecies(
                        createSpeciesWithComparisonList(layer.getNewLayerId(), false, allSpecies));
        };
    }

    private List<OpeningStockingHistorySpeciesWithComparisonDto> createSpeciesWithComparisonList(
            Long stockingLayerId,
            Boolean isPreferred,
            List<OpeningStockingHistoryLayerSpeciesWithComaprisonProjection> allSpecies) {
        return allSpecies
                .stream()
                .filter(species ->
                        stockingLayerId.equals(species.getNewStockingLayerId()) && isPreferred.equals(species.getNewPreferredInd()))
                .map(species -> new OpeningStockingHistorySpeciesWithComparisonDto(
                        species.getOldLayerCode(),
                        species.getNewLayerCode(),
                        new CodeDescriptionDto(
                                species.getOldSpeciesCode(),
                                species.getOldSpeciesDescription()
                        ),
                        new CodeDescriptionDto(
                                species.getNewSpeciesCode(),
                                species.getNewSpeciesDescription()
                        ),
                        species.getOldMinHeight(),
                        species.getNewMinHeight()
                ))
                .toList();
    }

    private static Function<OpeningStockingHistoryDetailsProjection, OpeningStockingHistoryDto> getDetails() {
        return projection -> {
            OpeningDetailsBecDto bec =
                new OpeningDetailsBecDto(
                    projection.getBecZoneCode(),
                    projection.getBecSubzoneCode(),
                    projection.getBecVariant(),
                    projection.getBecPhase(),
                    projection.getBecSiteSeries(),
                    projection.getBecSiteType(),
                    projection.getBecSeral());

            OpeningStockingHistoryDetailsDto detailsDto =
                new OpeningStockingHistoryDetailsDto(
                    projection.getStockingStandardUnit(),
                    projection.getSsuid(),
                    projection.getSrid(),
                    BooleanUtils.toBooleanDefaultIfNull(projection.getDefaultMof(), false),
                    BooleanUtils.toBooleanDefaultIfNull(projection.getManualEntry(), false),
                    projection.getFspId(),
                    projection.getNetArea(),
                    projection.getSoilDisturbancePercent(),
                    bec,
                    projection.getRegenDelay(),
                    projection.getFreeGrowingLate(),
                    projection.getFreeGrowingEarly(),
                    projection.getAdditionalStandards(),
                    projection.getAmendmentComment()
                );

            return new OpeningStockingHistoryDto(detailsDto, List.of(), List.of(), null, List.of());
        };
    }

    private Function<OpeningStockingHistoryDto, OpeningStockingHistoryDto> getLayer(
        Long openingId,
        Long eventHistoryId) {
        return detailsDto ->
            detailsDto.withLayers(
                openingRepository.getOpeningStockingLayerHistoryByOpeningIdAndEventHistoryId(
                        openingId, eventHistoryId, detailsDto.stocking().ssuId())
                    .stream()
                    .map(layer ->
                        new OpeningStockingHistoryLayerDto(
                            new CodeDescriptionDto(layer.getLayerCode(), layer.getLayerName()),
                            layer.getMinWellspacedTrees(),
                            layer.getMinPreferredWellspacedTrees(),
                            layer.getMinHorizontalDistanceWellspacedTrees(),
                            layer.getTargetWellspacedTrees(),
                            layer.getMinResidualBasalArea(),
                            layer.getMinPostspacingDensity(),
                            layer.getMaxPostspacingDensity(),
                            layer.getMaxConiferous(),
                            layer.getHeightRelativeToComp()))
                    .toList()
            );
    }

    private Function<OpeningStockingHistoryDto, OpeningStockingHistoryDto> getSpecies(
        Long openingId,
        Long eventHistoryId) {
        return detailsDto ->
            detailsDto
                .withAcceptableSpecies(getOpeningStockingSpeciesHistoryDto(openingId, eventHistoryId, false, detailsDto))
                .withPreferredSpecies(getOpeningStockingSpeciesHistoryDto(openingId, eventHistoryId, true, detailsDto));
    }

    private List<OpeningStockingHistorySpeciesDto> getOpeningStockingSpeciesHistoryDto(
        Long openingId, Long eventHistoryId, boolean preferred, OpeningStockingHistoryDto detailsDto) {
        return openingRepository
            .getOpeningStockingSpeciesHistoryByOpeningIdAndEventHistoryId(
                openingId, eventHistoryId, BooleanUtils.toString(preferred, "Y", "N"), detailsDto.stocking().ssuId())
            .stream()
            .map(
                species ->
                    new OpeningStockingHistorySpeciesDto(
                        species.getLayerCode(),
                        new CodeDescriptionDto(species.getSpeciesCode(), species.getSpeciesName()),
                        species.getMinHeight()))
            .toList();
    }

    private Function<OpeningStockingHistoryDto, OpeningStockingHistoryDto> getComments() {
        return tombstone ->
            tombstone.withComments(
                commentRepository
                    .getCommentById(null, null, tombstone.stocking().ssuId(), null, null)
                    .stream()
                    .map(OpeningDetailsCommentConverter.mapComments())
                    .toList());
    }
}
