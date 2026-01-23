package ca.bc.gov.restapi.results.common.service.opening.details.impl;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsNotificationDto;
import ca.bc.gov.restapi.results.common.entity.BaseOpeningEntity;
import ca.bc.gov.restapi.results.common.enums.OpeningDetailsNotificationStatusEnum;
import ca.bc.gov.restapi.results.common.enums.StockingMilestoneTypeEnum;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningStockingNotificationProjection;
import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsNotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractOpeningDetailsNotificationService implements
    OpeningDetailsNotificationService {

  protected final OpeningRepository<? extends BaseOpeningEntity> openingRepository;

  @Override
  public List<OpeningDetailsNotificationDto> getNotifications(Long openingId) {
    List<OpeningDetailsNotificationDto> ssuNotifications = getSsuNotifications(openingId);

    return Stream.of(ssuNotifications).flatMap(List::stream).toList();
  }

  protected List<OpeningDetailsNotificationDto> getSsuNotifications(Long openingId) {
    // Fetch projections from the repository
    List<OpeningStockingNotificationProjection> projections =
        openingRepository.getOpeningStockingNotificationsByOpeningId(openingId);

    List<String> order =
        List.of(
            OpeningDetailsNotificationStatusEnum.ERROR.toString(),
            OpeningDetailsNotificationStatusEnum.WARNING.toString(),
            OpeningDetailsNotificationStatusEnum.INFO.toString());

    // Group by notificationType and conditionally by milestoneTypeCode
    return projections.stream()
        .filter(p -> p.getNotificationType() != null && p.getSilvMilestoneTypeCode() != null)
        .collect(
            Collectors.groupingBy(
                p -> Map.entry(p.getNotificationType(), p.getSilvMilestoneTypeCode()),
                Collectors.toList()))
        .entrySet()
        .stream()
        .sorted(
            Comparator.comparingInt(entry -> order.indexOf(entry.getKey().getKey().toUpperCase())))
        .map(
            entry -> {
              // Extract notification type and milestone type code
              String notificationType = entry.getKey().getKey();
              String milestoneTypeCode = entry.getKey().getValue();

              // Aggregate standardsUnitId for the title
              String title =
                  "\""
                      + entry.getValue().stream()
                      .map(OpeningStockingNotificationProjection::getStandardsUnitId)
                      .distinct()
                      .sorted()
                      .collect(Collectors.joining(", "))
                      + "\"";

              // Get the description from StockingMilestoneTypeEnum
              String milestoneDescription =
                  Arrays.stream(StockingMilestoneTypeEnum.values())
                      .filter(enumValue -> enumValue.getCode().equals(milestoneTypeCode))
                      .findFirst()
                      .map(StockingMilestoneTypeEnum::getDescription)
                      .orElse("Unknown");

              // Customize title and description based on notification type
              String finalTitle;
              String finalDescription;

              if (OpeningDetailsNotificationStatusEnum.ERROR
                  .toString()
                  .equalsIgnoreCase(notificationType)) {
                finalTitle = "Free Growing milestone is overdue for standard unit " + title;
                finalDescription =
                    "Please contact your ministry representative as soon as possible!";
              } else if (OpeningDetailsNotificationStatusEnum.WARNING
                  .toString()
                  .equalsIgnoreCase(notificationType)) {
                if (milestoneTypeCode.equalsIgnoreCase(StockingMilestoneTypeEnum.FG.getCode())) {
                  finalTitle = "Upcoming milestone detected for standard unit " + title;
                  finalDescription =
                      "Monitor progress closely to declare your "
                          + milestoneDescription
                          + " in time!";
                } else {
                  finalTitle =
                      "Upcoming Regeneration milestone detected for standard unit " + title;
                  finalDescription = "Monitor progress closely to update your forest cover!";
                }
              } else if (OpeningDetailsNotificationStatusEnum.INFO
                  .toString()
                  .equalsIgnoreCase(notificationType)) {
                finalTitle = "Regeneration milestone reminder for standard unit " + title;
                finalDescription = "Please update your forest cover.";
              } else {
                finalTitle = title;
                finalDescription = milestoneDescription;
              }

              // Map to OpeningDetailsNotificationDto
              return new OpeningDetailsNotificationDto(
                  finalTitle,
                  finalDescription,
                  OpeningDetailsNotificationStatusEnum.valueOf(notificationType.toUpperCase()));
            })
        .toList();
  }
}
