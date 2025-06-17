package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.comment.CommentDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.*;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingDetailsProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingMilestoneProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingNotificationProjection;
import ca.bc.gov.restapi.results.oracle.enums.OpeningDetailsNotificationStatusEnum;
import ca.bc.gov.restapi.results.oracle.enums.StockingMilestoneTypeEnum;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.oracle.service.conversion.opening.OpeningDetailsCommentConverter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        .map(detailsDto -> getMilestones(detailsDto.stocking().ssid()).apply(detailsDto))
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
          projection.getAdditionalStandards(),
null
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

  private Function<OpeningDetailsStockingDto, OpeningDetailsStockingDto> getMilestones(
      Long ssuId
  ) {
    return detailsDto ->
            detailsDto.
                    withStocking(
                            detailsDto.stocking().withMilestones(getOpeningDetailsStockingDetailsMilestoneDto(ssuId))
                    );
  }

  private OpeningDetailsStockingDetailsMilestoneDto getOpeningDetailsStockingDetailsMilestoneDto(Long ssuId) {
      return openingRepository
              .getOpeningStockingMilestoneBySsuId(ssuId)
              .map(projection -> new OpeningDetailsStockingDetailsMilestoneDto(
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
                      getMilestonesComments(ssuId)
              )).orElse(
                      new OpeningDetailsStockingDetailsMilestoneDto(
                              null,
                              null,
                              null,
                              null,
                              null,
                              null,
                              null,
                              null,
                              null,
                              null,
                              false,
                              false,
                              List.of()
                      )
              );
  }

    private List<CommentDto> getMilestonesComments(Long ssuId) {
        return Arrays.stream(StockingMilestoneTypeEnum.values())
                .flatMap(type ->
                        commentRepository.getCommentById(
                                null,
                                null,
                                null,
                                ssuId,
                                type.getCode(),
                                null

                        ).stream()
                ).map(OpeningDetailsCommentConverter.mapComments())
                .toList();
    }

    public List<OpeningDetailsNotificationDto> getOpeningNotifications(Long openingId) {
        // Fetch projections from the repository
        List<OpeningStockingNotificationProjection> projections =
                openingRepository.getOpeningStockingNotificationsByOpeningId(openingId);

        // Group by notificationType and silvMilestoneTypeCode
        return projections.stream()
                .collect(Collectors.groupingBy(
                        projection -> Map.entry(projection.getNotificationType(), projection.getSilvMilestoneTypeCode()),
                        Collectors.toList()
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    // Extract notification type and milestone type code
                    String notificationType = entry.getKey().getKey();
                    String milestoneTypeCode = entry.getKey().getValue();

                    // Aggregate standardsUnitId for the title
                    String title = "\"" + entry.getValue().stream()
                            .map(OpeningStockingNotificationProjection::getStandardsUnitId)
                            .distinct()
                            .sorted()
                            .collect(Collectors.joining(", ")) + "\"";

                    // Get the description from StockingMilestoneTypeEnum
                    String milestoneDescription = Arrays.stream(StockingMilestoneTypeEnum.values())
                            .filter(enumValue -> enumValue.getCode().equals(milestoneTypeCode))
                            .findFirst()
                            .map(StockingMilestoneTypeEnum::getDescription)
                            .orElse("Unknown");

                    // Customize title and description based on notification type
                    String finalTitle;
                    String finalDescription;

                    if ("ERROR".equalsIgnoreCase(notificationType)) {
                        finalTitle = "Overdue milestone detected for standard unit " + title;
                        finalDescription = "Immediate action required!";
                    } else if ("WARNING".equalsIgnoreCase(notificationType)) {
                        finalTitle = "Upcoming milestone detected for standard unit " + title;
                        finalDescription = "Monitor progress closely to declare your " + milestoneDescription + " in time!";
                    } else {
                        finalTitle = title;
                        finalDescription = milestoneDescription;
                    }

                    // Map to OpeningDetailsNotificationDto
                    return new OpeningDetailsNotificationDto(
                            finalTitle,
                            finalDescription,
                            OpeningDetailsNotificationStatusEnum.valueOf(notificationType.toUpperCase())
                    );
                })
                .toList();
    }
}
