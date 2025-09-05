package ca.bc.gov.restapi.results.oracle.service.opening.history;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.history.*;
import ca.bc.gov.restapi.results.oracle.entity.opening.history.OpeningStockingHistoryDetailsWithComparisonProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.history.OpeningStockingHistoryLayerWithComparisonProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.history.OpeningStockingHistoryLayerSpeciesWithComaprisonProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.history.OpeningStockingHistoryProjection;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningStandardUnitHistoryService {

    private final OpeningRepository openingRepository;

    public List<OpeningStockingHistoryOverviewDto> getStandardUnitOverviewHistoryList(Long openingId) {
        List<OpeningStockingHistoryProjection> projections = openingRepository
                .getOpeningStandardUnitHistoryByOpeningId(openingId);

        return projections
                .stream()
                .map(toOverviewDto())
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

    private Function<OpeningStockingHistoryProjection, OpeningStockingHistoryOverviewDto> toOverviewDto() {
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
                projection.getApprovedByUserId()
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
}
