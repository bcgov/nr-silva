package ca.bc.gov.restapi.results.oracle.service.opening.history;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.cover.history.*;
import ca.bc.gov.restapi.results.oracle.entity.cover.history.ForestCoverHistoryOverviewProjection;
import ca.bc.gov.restapi.results.oracle.entity.cover.history.ForestCoverHistoryPolygonProjection;
import ca.bc.gov.restapi.results.oracle.repository.ForestCoverEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningForestCoverHistoryService {
    private final ForestCoverEntityRepository coverRepository;

    public List<OpeningForestCoverHistoryOverviewDto> getOpeningForestCoverHistoryOverviewList(Long openingId) {
        List<ForestCoverHistoryOverviewProjection> projections = coverRepository
                .findHistoryOverviewByOpeningId(openingId);

        return projections
                .stream()
                .map(toOverviewDto())
                .toList();
    }

    public List<OpeningForestCoverHistoryDto> getOpeningForestCoverList(
            Long openingId, String updateDate) {

        return coverRepository.findHistoryByOpeningDetails(openingId, updateDate).stream()
                .map(
                        projection -> {
                            boolean isSingleLayer =
                                    "Y"
                                            .equalsIgnoreCase(
                                                    Optional.ofNullable(projection.getIsSingleLayer()).orElse("N"));

                            boolean hasReserve =
                                    (projection.getReserveCode() != null && !projection.getReserveCode().isBlank())
                                            || (projection.getObjectiveCode() != null
                                            && !projection.getObjectiveCode().isBlank());

                            OpeningForestCoverHistoryDto dto =
                                    new OpeningForestCoverHistoryDto(
                                            projection.getCoverId(),
                                            projection.getArchiveDate(),
                                            projection.getPolygonId(),
                                            projection.getStandardUnitId(),
                                            new CodeDescriptionDto(
                                                    projection.getUnmappedCode(), projection.getUnmappedName()),
                                            projection.getGrossArea(),
                                            projection.getNetArea(),
                                            new CodeDescriptionDto(
                                                    projection.getStatusCode(), projection.getStatusName()),
                                            new CodeDescriptionDto(projection.getTypeCode(), projection.getTypeName()),
                                            new OpeningForestCoverHistoryLayerListDescriptionDto(
                                                    List.of(),
                                                    projection.getTotal(),
                                                    projection.getInventoryTotalWellSpaced(),
                                                    projection.getInventoryWellSpaced(),
                                                    projection.getInventoryFreeGrowing()),
                                            new OpeningForestCoverHistoryLayerListDescriptionDto(
                                                    List.of(),
                                                    null,
                                                    projection.getSilvicultureTotalWellSpaced(),
                                                    projection.getSilvicultureWellSpaced(),
                                                    projection.getSilvicultureFreeGrowing()),
                                            projection.getReferenceYear(),
                                            isSingleLayer,
                                            hasReserve);

                            return dto.withSilvicultureLayer(
                                            dto.silvicultureLayer().withSpecies(getLayerSpecies(dto.coverId(), "S", projection.getArchiveDate().toString())))
                                    .withInventoryLayer(
                                            dto.inventoryLayer().withSpecies(getLayerSpecies(dto.coverId(), "I", projection.getArchiveDate().toString())));
                        })
                .toList();
    }

    public Optional<OpeningForestCoverHistoryDetailsDto> getDetails(Long coverId, String archiveDate) {

        return coverRepository
                .findHistoryByOpeningDetailsPolygon(coverId, archiveDate)
                .map(convertProjectionToPolygon())
                .map(getUnmappedArea(coverId, archiveDate))
                .map(dto -> dto.withLayers(getDetailsLayer(coverId, archiveDate)))
                .map(checkLayerType());
    }

    private Function<ForestCoverHistoryOverviewProjection, OpeningForestCoverHistoryOverviewDto> toOverviewDto() {
        return projection -> new OpeningForestCoverHistoryOverviewDto(
                projection.getFcDate(),
                projection.getNp(),
                projection.getNsr(),
                projection.getImm(),
                projection.getOther(),
                projection.getTotal(),
                projection.getHasDetails(),
                projection.getIsCurrent()
        );
    }

    private Function<OpeningForestCoverHistoryDetailsDto, OpeningForestCoverHistoryDetailsDto> checkLayerType() {
        return dto ->
                dto.withSingleLayer(
                        // If no layers, then it is a single layer
                        CollectionUtils.isEmpty(dto.layers())
                                || dto.layers().stream()
                                .map(OpeningForestCoverHistoryLayerDto::layer)
                                .map(CodeDescriptionDto::code)
                                // If any layer is S or I, then it is a single layer
                                .anyMatch(code -> code.equals("S") || code.equals("I")));
    }

    private Function<OpeningForestCoverHistoryDetailsDto, OpeningForestCoverHistoryDetailsDto> getUnmappedArea(
            Long coverId, String archiveDate) {
        return dto ->
                dto.withUnmapped(
                        coverRepository.findHistoryByOpeningDetailsUnmapped(coverId, archiveDate).stream()
                                .map(
                                        projection ->
                                                new OpeningForestCoverHistoryUnmappedDto(
                                                        projection.getUnmappedAreaId(),
                                                        projection.getArea(),
                                                        new CodeDescriptionDto(
                                                                projection.getStockingStatusCode(),
                                                                projection.getStockingStatusName()),
                                                        new CodeDescriptionDto(
                                                                projection.getStockingTypeCode(),
                                                                projection.getStockingTypeName())))
                                .toList());
    }

    private Function<ForestCoverHistoryPolygonProjection, OpeningForestCoverHistoryDetailsDto>
    convertProjectionToPolygon() {
        return projection ->
                new OpeningForestCoverHistoryDetailsDto(
                        new OpeningForestCoverHistoryPolygonDto(
                                projection.getForestCoverId(),
                                new CodeDescriptionDto(projection.getReserveCode(), projection.getReserveName()),
                                new CodeDescriptionDto(
                                        projection.getObjectiveCode(), projection.getObjectiveName()),
                                new CodeDescriptionDto(
                                        projection.getSiteClassCode(), projection.getSiteClassName()),
                                projection.getSiteIndex(),
                                new CodeDescriptionDto(
                                        projection.getSiteIndexSourceCode(), projection.getSiteIndexSourceName()),
                                new CodeDescriptionDto(
                                        projection.getTreeCoverPatternCode(), projection.getTreeCoverPatternName()),
                                projection.getReentryYear()),
                        true,
                        List.of(),
                        List.of());
    }

    private List<OpeningForestCoverHistoryLayerDto> getDetailsLayer(Long coverId, String archiveDate) {
        return coverRepository.findHistoryByOpeningDetailsLayer(coverId, archiveDate).stream()
                .map(
                        projection ->
                                new OpeningForestCoverHistoryLayerDto(
                                        projection.getLayerId(),
                                        new CodeDescriptionDto(projection.getLayerCode(), projection.getLayerName()),
                                        projection.getCrownClosure(),
                                        projection.getBasalAreaSt(),
                                        projection.getTotalStems(),
                                        projection.getTotalWellSpaced(),
                                        projection.getWellSpaced(),
                                        projection.getFreeGrowing(),
                                        getDetailedSpecies(projection.getLayerId(), archiveDate),
                                        getDamage(projection.getLayerId(), archiveDate)))
                .toList();
    }

    private List<OpeningForestCoverHistoryDetailedSpeciesDto> getDetailedSpecies(Long coverLayerId, String archiveDate) {
        return coverRepository.findHistoryByOpeningDetailsDetailedSpecies(coverLayerId, archiveDate).stream()
                .map(
                        projection ->
                                new OpeningForestCoverHistoryDetailedSpeciesDto(
                                        new CodeDescriptionDto(
                                                projection.getSpeciesCode(), projection.getSpeciesName()),
                                        projection.getSpeciesPercent(),
                                        projection.getAverageAge(),
                                        projection.getAverageHeight()))
                .toList();
    }

    private List<OpeningForestCoverHistoryDamageDto> getDamage(Long coverLayerId, String archiveDate) {
        return coverRepository.findHistoryByOpeningDetailsDamage(coverLayerId, archiveDate).stream()
                .map(
                        projection ->
                                new OpeningForestCoverHistoryDamageDto(
                                        new CodeDescriptionDto(
                                                projection.getDamageAgentCode(), projection.getDamageAgentName()),
                                        projection.getForestHealthIncidence(),
                                        projection.getIncidenceArea()))
                .toList();
    }

    private List<CodeDescriptionDto> getLayerSpecies(Long coverId, String layerCodeIndicator, String archiveDate) {
        return coverRepository.findHistoryByOpeningDetailsSpecies(coverId, layerCodeIndicator, archiveDate).stream()
                .map(
                        projection ->
                                new CodeDescriptionDto(projection.getSpeciesCode(), projection.getSpeciesName()))
                .toList();
    }

}
