package ca.bc.gov.restapi.results.oracle.service.opening.history;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.history.*;
import ca.bc.gov.restapi.results.oracle.entity.opening.history.OpeningStandardUnitHistoryDetailsProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.history.OpeningStandardUnitHistoryLayerDetailsProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.history.OpeningStandardUnitHistoryLayerSpeciesDetailsProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.history.OpeningStandardUnitHistoryProjection;
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

    public List<OpeningStandardUnitHistoryOverviewDto> getStandardUnitOverviewHistoryList(Long openingId) {
        List<OpeningStandardUnitHistoryProjection> projections = openingRepository
                .getOpeningStandardUnitHistoryByOpeningId(openingId);

        return projections
                .stream()
                .map(toOverviewDto())
                .toList();
    }

    public List<OpeningStandardUnitHistoryDto> getStandardUnitHistoryDetails(Long openingId, Long stockingEventHistoryId) {
        List<OpeningStandardUnitHistoryDetailsProjection> suDetailProjections = openingRepository
                .getOpeningStandardUnitHistoryDetailsByOpeningIdAndHistoryId(openingId, stockingEventHistoryId);

        if (suDetailProjections.isEmpty()) {
            return List.of();
        }

        List<OpeningStandardUnitHistoryLayerDetailsProjection> layerDetailProjections = openingRepository
                .getOpeningStandardUnitHistoryLayerDetailsByOpeningIdAndHistoryId(openingId, stockingEventHistoryId);

        List<OpeningStandardUnitHistoryLayerSpeciesDetailsProjection> layerSpeciesProjections = openingRepository
                .getOpeningStandardUnitHistoryLayerSpeciesDetailsByOpeningIdAndHistoryId(openingId, stockingEventHistoryId);

        return suDetailProjections
                .stream()
                .map(addDetails())
                .map(addLayers(layerDetailProjections, layerSpeciesProjections))
                .toList();

    }

    private Function<OpeningStandardUnitHistoryProjection, OpeningStandardUnitHistoryOverviewDto> toOverviewDto() {
        return projection -> new OpeningStandardUnitHistoryOverviewDto(
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

    private Function<OpeningStandardUnitHistoryDetailsProjection, OpeningStandardUnitHistoryDto> addDetails() {
        return projection -> {
            OpeningStandardUnitHistoryDetailsDto detailsDto = new OpeningStandardUnitHistoryDetailsDto(
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

            return new OpeningStandardUnitHistoryDto(
                    detailsDto,
                    List.of()
            );
        };
    }

    private Function<OpeningStandardUnitHistoryDto, OpeningStandardUnitHistoryDto> addLayers(
            List<OpeningStandardUnitHistoryLayerDetailsProjection> allLayers,
            List<OpeningStandardUnitHistoryLayerSpeciesDetailsProjection> allSpecies
    ) {
        return dto -> dto.withLayers(
                allLayers.stream()
                        .filter(layer -> layer.getSsuId().equals(dto.standardUnit().stockingStandardUnitId()))
                        .map(createLayerDto(allSpecies))
                        .toList()
        );
    }

    private Function<OpeningStandardUnitHistoryLayerDetailsProjection, OpeningStandardUnitHistoryLayerDetailsDto> createLayerDto(
            List<OpeningStandardUnitHistoryLayerSpeciesDetailsProjection> allSpecies
    ) {
        return layer -> {
            OpeningStandardUnitHistoryLayerDetailsDto layerDto = new OpeningStandardUnitHistoryLayerDetailsDto(
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
                    .withPreferredSpecies(createSpeciesList(layer.getNewLayerId(), true, allSpecies))
                    .withAcceptableSpecies(createSpeciesList(layer.getNewLayerId(), false, allSpecies));
        };
    }

    private List<OpeningStandardUnitHistorySpeciesDetailsDto> createSpeciesList(
            Long stockingLayerId,
            Boolean isPreferred,
            List<OpeningStandardUnitHistoryLayerSpeciesDetailsProjection> allSpecies) {
        return allSpecies
                .stream()
                .filter(species ->
                        stockingLayerId.equals(species.getNewStockingLayerId()) && isPreferred.equals(species.getNewPreferredInd()))
                .map(species -> new OpeningStandardUnitHistorySpeciesDetailsDto(
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
