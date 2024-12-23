package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.common.util.TimestampUtil;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresConstants;
import ca.bc.gov.restapi.results.postgres.dto.DashboardFiltersDto;
import ca.bc.gov.restapi.results.postgres.dto.FreeGrowingMilestonesDto;
import ca.bc.gov.restapi.results.postgres.dto.MyRecentActionsRequestsDto;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntity;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsActivityRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsLastYearRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * This class contains methods for gathering and grouping data for the dashboard metrics screen.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardMetricsService {

  private final OpeningsLastYearRepository openingsLastYearRepository;

  private final OpeningsActivityRepository openingsActivityRepository;

  private final LoggedUserService loggedUserService;



  /**
   * Get free growing milestone declarations data for the chart.
   *
   * @param filters Possible filter, see {@link DashboardFiltersDto} for more.
   * @return A list of {@link FreeGrowingMilestonesDto} for the chart.
   */
  public List<FreeGrowingMilestonesDto> getFreeGrowingMilestoneChartData(
      DashboardFiltersDto filters) {
    log.info("Getting Free growing milestones with filters {}", filters.toString());

    List<OpeningsLastYearEntity> entities =
        openingsLastYearRepository.findAll(Sort.by("openingId").ascending());

    if (entities.isEmpty()) {
      log.info("No Free growing milestones data found!");
      return List.of();
    }

    Map<Integer, List<OpeningsLastYearEntity>> resultMap = new LinkedHashMap<>();

    // Fill with all four pieces
    for (int i = 0; i < 4; i++) {
      resultMap.put(i, new ArrayList<>());
    }

    int totalRecordsFiltered = 0;

    // Iterate over the found records filtering and putting them into the right piece
    for (OpeningsLastYearEntity entity : entities) {
      // Org Unit filter - District
      if (!Objects.isNull(filters.orgUnit())
          && !filters.orgUnit().equals(entity.getOrgUnitCode())) {
        continue;
      }

      // Client number
      if (!Objects.isNull(filters.clientNumber())) {
        int numericValue = 0;
        if (!Objects.isNull(entity.getClientNumber())) {
          numericValue = Integer.parseInt(entity.getClientNumber());
        }
        boolean onlyNumbers = filters.clientNumber().equals(String.valueOf(numericValue));
        boolean wholeCode = filters.clientNumber().equals(entity.getClientNumber());
        if (!onlyNumbers && !wholeCode) {
          continue;
        }
      }

      // Entry start date filter
      if (!Objects.isNull(filters.entryDateStart())
          && entity.getEntryTimestamp().isBefore(filters.entryDateStart())) {
        continue;
      }

      // Entry end date filter
      if (!Objects.isNull(filters.entryDateEnd())
          && entity.getEntryTimestamp().isAfter(filters.entryDateEnd())) {
        continue;
      }

      int index = TimestampUtil.getLocalDateTimeIndex(entity.getEntryTimestamp());
      resultMap.get(index).add(entity);
      totalRecordsFiltered++;
    }

    List<FreeGrowingMilestonesDto> chartData = new ArrayList<>();
    BigDecimal hundred = new BigDecimal("100");
    BigDecimal hundredSum = new BigDecimal("100");
    for (Integer index : resultMap.keySet()) {
      List<OpeningsLastYearEntity> groupList = resultMap.get(index);
      String label = SilvaPostgresConstants.LABELS_MAP.get(index);
      int value = groupList.size();
      log.info("{} openings of {} for label '{}'", value, totalRecordsFiltered, label);

      BigDecimal percentage = BigDecimal.ZERO;
      if (totalRecordsFiltered > 0) {
        percentage =
            new BigDecimal(String.valueOf(value))
                .divide(
                    new BigDecimal(String.valueOf(totalRecordsFiltered)), 10, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP)
                .multiply(hundred);
      }

      if (index < 3) {
        hundredSum = hundredSum.subtract(percentage);
      } else if (index == 3 && totalRecordsFiltered > 0) {
        percentage = hundredSum;
      }

      log.info("Percentage {}% for the label: {}", percentage, label);
      chartData.add(new FreeGrowingMilestonesDto(index, label, value, percentage));
    }

    return chartData;
  }

  /**
   * Get My recent actions table data for the Request tabs.
   *
   * @return A list of {@link MyRecentActionsRequestsDto} with 5 rows.
   */
  public List<MyRecentActionsRequestsDto> getUserRecentOpeningsActions() {
    log.info("Getting up to the last 5 openings activities for the requests tab");

    List<OpeningsActivityEntity> openingList =
        openingsActivityRepository
            .findAllByEntryUserid(
                loggedUserService.getLoggedUserId(),
                PageRequest.of(0, 5, Sort.by("lastUpdated").descending())
            );

    if (openingList.isEmpty()) {
      log.info("No recent activities data found for the user!");
      return List.of();
    }
    return
        openingList
            .stream()
            .map(entity ->
                new MyRecentActionsRequestsDto(
                    Objects.toString(entity.getActivityTypeDesc(), "Created"),
                    entity.getOpeningId(),
                    entity.getStatusCode(),
                    entity.getStatusDesc(),
                    entity.getLastUpdated()
                )
            )
            .toList();
  }

}
