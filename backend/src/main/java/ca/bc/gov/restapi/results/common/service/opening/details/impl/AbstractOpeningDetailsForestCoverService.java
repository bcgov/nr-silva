package ca.bc.gov.restapi.results.common.service.opening.details.impl;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.cover.*;
import ca.bc.gov.restapi.results.common.projection.cover.ForestCoverPolygonProjection;
import ca.bc.gov.restapi.results.common.repository.ForestCoverRepository;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsForestCoverService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractOpeningDetailsForestCoverService
    implements OpeningDetailsForestCoverService {

  protected final ForestCoverRepository coverRepository;

  @Override
  public List<OpeningForestCoverDto> getOpeningForestCoverList(Long openingId,
      String mainSearchTerm) {
    return coverRepository.findByOpeningDetails(openingId, mainSearchTerm).stream()
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

              OpeningForestCoverDto dto =
                  new OpeningForestCoverDto(
                      projection.getCoverId(),
                      projection.getPolygonId(),
                      projection.getStandardUnitId(),
                      new CodeDescriptionDto(
                          projection.getUnmappedCode(), projection.getUnmappedName()),
                      projection.getGrossArea(),
                      projection.getNetArea(),
                      new CodeDescriptionDto(
                          projection.getStatusCode(), projection.getStatusName()),
                      new CodeDescriptionDto(projection.getTypeCode(), projection.getTypeName()),
                      new OpeningForestCoverLayerListDescriptionDto(
                          List.of(),
                          projection.getTotal(),
                          projection.getInventoryTotalWellSpaced(),
                          projection.getInventoryWellSpaced(),
                          projection.getInventoryFreeGrowing()),
                      new OpeningForestCoverLayerListDescriptionDto(
                          List.of(),
                          null,
                          projection.getSilvicultureTotalWellSpaced(),
                          projection.getSilvicultureWellSpaced(),
                          projection.getSilvicultureFreeGrowing()),
                      projection.getReferenceYear(),
                      isSingleLayer,
                      hasReserve);

              return dto.withSilvicultureLayer(
                      dto.silvicultureLayer().withSpecies(getLayerSpecies(dto.coverId(), "S")))
                  .withInventoryLayer(
                      dto.inventoryLayer().withSpecies(getLayerSpecies(dto.coverId(), "I")));
            })
        .toList();
  }

  @Override
  public Optional<OpeningForestCoverDetailsDto> getDetails(Long coverId) {
    return coverRepository
        .findByOpeningDetailsPolygon(coverId)
        .map(convertProjectionToPolygon())
        .map(getUnmappedArea(coverId))
        .map(dto -> dto.withLayers(getDetailsLayer(coverId)))
        .map(checkLayerType());
  }

  protected Function<OpeningForestCoverDetailsDto, OpeningForestCoverDetailsDto> checkLayerType() {
    return dto ->
        dto.withSingleLayer(
            // If no layers, then it is a single layer
            CollectionUtils.isEmpty(dto.layers())
                || dto.layers().stream()
                .map(OpeningForestCoverLayerDto::layer)
                .map(CodeDescriptionDto::code)
                // If any layer is S or I, then it is a single layer
                .anyMatch(code -> code.equals("S") || code.equals("I")));
  }

  protected Function<OpeningForestCoverDetailsDto, OpeningForestCoverDetailsDto> getUnmappedArea(
      Long coverId) {
    return dto ->
        dto.withUnmapped(
            coverRepository.findByOpeningDetailsUnmapped(coverId).stream()
                .map(
                    projection ->
                        new OpeningForestCoverUnmappedDto(
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

  protected Function<ForestCoverPolygonProjection, OpeningForestCoverDetailsDto>
  convertProjectionToPolygon() {
    return projection ->
        new OpeningForestCoverDetailsDto(
            new OpeningForestCoverPolygonDto(
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

  protected List<OpeningForestCoverLayerDto> getDetailsLayer(Long coverId) {
    return coverRepository.findByOpeningDetailsLayer(coverId).stream()
        .map(
            projection ->
                new OpeningForestCoverLayerDto(
                    projection.getLayerId(),
                    new CodeDescriptionDto(projection.getLayerCode(), projection.getLayerName()),
                    projection.getCrownClosure(),
                    projection.getBasalAreaSt(),
                    projection.getTotalStems(),
                    projection.getTotalWellSpaced(),
                    projection.getWellSpaced(),
                    projection.getFreeGrowing(),
                    getDetailedSpecies(projection.getLayerId()),
                    getDamage(projection.getLayerId())))
        .toList();
  }

  protected List<OpeningForestCoverDetailedSpeciesDto> getDetailedSpecies(Long coverLayerId) {
    return coverRepository.findByOpeningDetailsDetailedSpecies(coverLayerId).stream()
        .map(
            projection ->
                new OpeningForestCoverDetailedSpeciesDto(
                    new CodeDescriptionDto(
                        projection.getSpeciesCode(), projection.getSpeciesName()),
                    projection.getSpeciesPercent(),
                    projection.getAverageAge(),
                    projection.getAverageHeight()))
        .toList();
  }

  protected List<OpeningForestCoverDamageDto> getDamage(Long coverLayerId) {
    return coverRepository.findByOpeningDetailsDamage(coverLayerId).stream()
        .map(
            projection ->
                new OpeningForestCoverDamageDto(
                    new CodeDescriptionDto(
                        projection.getDamageAgentCode(), projection.getDamageAgentName()),
                    projection.getForestHealthIncidence(),
                    projection.getIncidenceArea()))
        .toList();
  }

  protected List<CodeDescriptionDto> getLayerSpecies(Long coverId, String layerCodeIndicator) {
    return coverRepository.findByOpeningDetailsSpecies(coverId, layerCodeIndicator).stream()
        .map(
            projection ->
                new CodeDescriptionDto(projection.getSpeciesCode(), projection.getSpeciesName()))
        .toList();
  }
}
