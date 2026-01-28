package ca.bc.gov.restapi.results.common.service.opening.details.impl;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.comment.CommentDto;
import ca.bc.gov.restapi.results.common.dto.opening.*;
import ca.bc.gov.restapi.results.common.entity.BaseOpeningEntity;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningStockingDetailsProjection;
import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.common.service.opening.conversion.OpeningDetailsCommentConverter;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsStockingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AbstractOpeningDetailsStockingService implements OpeningDetailsStockingService {
  protected final OpeningRepository<? extends BaseOpeningEntity> openingRepository;
  protected final SilvicultureCommentRepository commentRepository;

  @Override
  public List<OpeningDetailsStockingDto> getOpeningStockingDetails(Long openingId) {
    return openingRepository.getOpeningStockingDetailsByOpeningId(openingId).stream()
        .map(getDetails())
        .map(detailsDto -> getMilestones(detailsDto.stocking().ssuId()).apply(detailsDto))
        .map(getSpecies(openingId))
        .map(getLayer(openingId))
        .map(getComments())
        .toList();
  }
  private static Function<OpeningStockingDetailsProjection, OpeningDetailsStockingDto>
  getDetails() {
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

      OpeningDetailsStockingDetailsDto stockingDetails =
          new OpeningDetailsStockingDetailsDto(
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
              null);

      return new OpeningDetailsStockingDto(stockingDetails, List.of(), List.of(), null, List.of());
    };
  }

  private Function<OpeningDetailsStockingDto, OpeningDetailsStockingDto> getLayer(Long openingId) {
    return detailsDto ->
        detailsDto.withLayers(
            openingRepository
                .getOpeningStockingLayerByOpeningId(openingId, detailsDto.stocking().ssuId())
                .stream()
                .map(
                    layer ->
                        new OpeningDetailsStockingLayerDto(
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
                .toList());
  }

  private Function<OpeningDetailsStockingDto, OpeningDetailsStockingDto> getSpecies(
      Long openingId) {
    return detailsDto ->
        detailsDto
            .withAcceptableSpecies(
                getOpeningDetailsStockingSpeciesDto(openingId, false, detailsDto))
            .withPreferredSpecies(getOpeningDetailsStockingSpeciesDto(openingId, true, detailsDto));
  }

  private List<OpeningDetailsStockingSpeciesDto> getOpeningDetailsStockingSpeciesDto(
      Long openingId, boolean preferred, OpeningDetailsStockingDto detailsDto) {
    return openingRepository
        .getOpeningStockingSpeciesByOpeningId(
            openingId, BooleanUtils.toString(preferred, "Y", "N"), detailsDto.stocking().ssuId())
        .stream()
        .map(
            species ->
                new OpeningDetailsStockingSpeciesDto(
                    species.getLayerCode(),
                    new CodeDescriptionDto(species.getSpeciesCode(), species.getSpeciesName()),
                    species.getMinHeight()))
        .toList();
  }

  private Function<OpeningDetailsStockingDto, OpeningDetailsStockingDto> getComments() {
    return tombstone ->
        tombstone.withComments(
            commentRepository
                .getCommentById(null, null, tombstone.stocking().ssuId(), null, null)
                .stream()
                .map(OpeningDetailsCommentConverter.mapComments())
                .toList());
  }

  private Function<OpeningDetailsStockingDto, OpeningDetailsStockingDto> getMilestones(Long ssuId) {
    return detailsDto ->
        detailsDto.withStocking(
            detailsDto
                .stocking()
                .withMilestones(getOpeningDetailsStockingDetailsMilestoneDto(ssuId)));
  }

  private OpeningDetailsStockingDetailsMilestoneDto getOpeningDetailsStockingDetailsMilestoneDto(
      Long ssuId) {
    return openingRepository
        .getOpeningStockingMilestoneBySsuId(ssuId)
        .map(
            projection ->
                new OpeningDetailsStockingDetailsMilestoneDto(
                    projection.getPostHarvestDeclaredDate(),
                    projection.getRegenDeclaredDate(),
                    projection.getRegenOffsetYears(),
                    projection.getRegenDueDate(),
                    projection.getNoRegenDeclaredDate(),
                    projection.getNoRegenOffsetYears(),
                    projection.getNoRegenDueDate(),
                    projection.getFreeGrowingDeclaredDate(),
                    projection.getFreeGrowingOffsetYears(),
                    projection.getFreeGrowingDueDate(),
                    projection.getNoRegenIndicated(),
                    projection.getExtentDeclared(),
                    getMilestonesComments(ssuId)))
        .orElse(
            new OpeningDetailsStockingDetailsMilestoneDto(
                null, null, null, null, null, null, null, null, null, null, false, false,
                List.of()));
  }

  private List<CommentDto> getMilestonesComments(Long ssuMId) {
    return commentRepository.getCommentById(null, null, null, ssuMId, null).stream()
        .map(OpeningDetailsCommentConverter.mapComments())
        .toList();
  }

}
