package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsBecDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingDetailsDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingLayerDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingSpeciesDto;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingDetailsProjection;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.oracle.service.conversion.opening.OpeningDetailsCommentConverter;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningDetailsStockingService {

  private final OpeningRepository openingRepository;
  private final SilvicultureCommentRepository commentRepository;

  public List<OpeningDetailsStockingDto> getOpeningStockingDetails(Long openingId) {

    return openingRepository
        .getOpeningStockingDetailsByOpeningId(openingId)
        .stream()
        .map(getDetails())
        .map(getSpecies(openingId))
        .map(getLayer(openingId))
        .map(getComments())
        .toList();
  }

  private static Function<OpeningStockingDetailsProjection, OpeningDetailsStockingDto> getDetails() {
    return projection -> {
      OpeningDetailsBecDto bec = new OpeningDetailsBecDto(
          projection.getBecZoneCode(),
          projection.getBecSubzoneCode(),
          projection.getBecVariant(),
          projection.getBecPhase(),
          projection.getBecSiteSeries(),
          projection.getBecSiteType(),
          projection.getBecSeral()
      );

      OpeningDetailsStockingDetailsDto stockingDetails = new OpeningDetailsStockingDetailsDto(
          projection.getStockingStandardUnit(),
          projection.getSsid(),
          BooleanUtils.toBooleanDefaultIfNull(projection.getDefaultMof(), false),
          BooleanUtils.toBooleanDefaultIfNull(projection.getManualEntry(), false),
          projection.getFspId(),
          projection.getNetArea(),
          projection.getSoilDisturbancePercent(),
          bec,
          projection.getRegenDelay(),
          projection.getFreeGrowingLate(),
          projection.getFreeGrowingEarly(),
          projection.getAdditionalStandards()
      );

      return new OpeningDetailsStockingDto(
          stockingDetails,
          List.of(),
          List.of(),
          null,
          List.of()
      );
    };
  }

  private Function<OpeningDetailsStockingDto, OpeningDetailsStockingDto> getLayer(
      Long openingId
  ) {
    return detailsDto ->
        detailsDto.withLayers(
        openingRepository
            .getOpeningStockingLayerByOpeningId(openingId, detailsDto.stocking().ssid())
            .stream()
            .map(layer ->
                new OpeningDetailsStockingLayerDto(
                    new CodeDescriptionDto(
                        layer.getLayerCode(),
                        layer.getLayerName()
                    ),
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
            .toList()
        );
  }

  private Function<OpeningDetailsStockingDto, OpeningDetailsStockingDto> getSpecies(
      Long openingId
  ) {
    return detailsDto ->
        detailsDto
            .withAcceptableSpecies(
                getOpeningDetailsStockingSpeciesDto(openingId, false, detailsDto)
            )
            .withPreferredSpecies(
                getOpeningDetailsStockingSpeciesDto(openingId, true, detailsDto)
            );
  }

  private List<OpeningDetailsStockingSpeciesDto> getOpeningDetailsStockingSpeciesDto(
      Long openingId,
      boolean preferred,
      OpeningDetailsStockingDto detailsDto
  ) {
    return openingRepository
        .getOpeningStockingSpeciesByOpeningId(
            openingId,
            BooleanUtils.toString(preferred,"Y", "N"),
            detailsDto.stocking().ssid()
        )
        .stream()
        .map(species ->
            new OpeningDetailsStockingSpeciesDto(
                species.getLayerCode(),
                new CodeDescriptionDto(
                    species.getSpeciesCode(),
                    species.getSpeciesName()
                ),
                species.getMinHeight()
            )
        )
        .toList();
  }

  private Function<OpeningDetailsStockingDto, OpeningDetailsStockingDto> getComments() {
    return tombstone ->
        tombstone
            .withComments(
                commentRepository
                    .getCommentById(
                        null,
                        null,
                        tombstone.stocking().ssid(),
                        null,
                        null,
                        null
                    )
                    .stream()
                    .map(OpeningDetailsCommentConverter.mapComments())
                    .toList()

            );
  }


}
