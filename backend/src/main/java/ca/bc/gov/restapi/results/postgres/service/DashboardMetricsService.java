package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.common.util.TimestampUtil;
import ca.bc.gov.restapi.results.postgres.dto.DashboardFiltesDto;
import ca.bc.gov.restapi.results.postgres.dto.FreeGrowingMilestonesDto;
import ca.bc.gov.restapi.results.postgres.dto.MyRecentActionsRequestsDto;
import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearDto;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntity;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsActivityRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsLastYearRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/** This class contains methods for gathering and grouping data for the dashboard metrics screen. */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardMetricsService {

  private final OpeningsLastYearRepository openingsLastYearRepository;

  private final OpeningsActivityRepository openingsActivityRepository;

  private final LoggedUserService loggedUserService;

  /**
   * Get openings submission trends data for the opening per year chart.
   *
   * @param filters Possible filter, see {@link DashboardFiltesDto} for more.
   * @return A list of {@link OpeningsPerYearDto} for the opening chart.
   */
  public List<OpeningsPerYearDto> getOpeningsSubmissionTrends(DashboardFiltesDto filters) {
    log.info("Getting Opening Submission Trends with filters {}", filters.toString());

    List<OpeningsLastYearEntity> entities =
        openingsLastYearRepository.findAll(Sort.by("entryTimestamp").ascending());

    if (entities.isEmpty()) {
      log.info("No Opening Submission Trends data found!");
      return List.of();
    }

    Map<Integer, List<OpeningsLastYearEntity>> resultMap = new LinkedHashMap<>();
    Map<Integer, String> monthNamesMap = new HashMap<>();

    // Fill with 12 months
    Integer monthValue = entities.get(0).getEntryTimestamp().getMonthValue();
    log.info("First month: {}", monthValue);
    while (resultMap.size() < 12) {
      resultMap.put(monthValue, new ArrayList<>());

      String monthName = Month.of(monthValue).name().toLowerCase();
      monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1, 3);
      monthNamesMap.put(monthValue, monthName);

      monthValue += 1;
      if (monthValue == 13) {
        monthValue = 1;
      }
    }

    // Iterate over the found records filtering and putting them into the right month
    for (OpeningsLastYearEntity entity : entities) {
      // Org Unit filter - District
      if (!Objects.isNull(filters.orgUnit())
          && !filters.orgUnit().equals(entity.getOrgUnitCode())) {
        continue;
      }

      // Status filter
      if (!Objects.isNull(filters.status()) && !filters.status().equals(entity.getStatus())) {
        continue;
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

      resultMap.get(entity.getEntryTimestamp().getMonthValue()).add(entity);
    }

    List<OpeningsPerYearDto> chartData = new ArrayList<>();
    for (Integer monthKey : resultMap.keySet()) {
      List<OpeningsLastYearEntity> monthDataList = resultMap.get(monthKey);
      String monthName = monthNamesMap.get(monthKey);
      log.info("Value {} for the month: {}", monthDataList.size(), monthName);
      chartData.add(new OpeningsPerYearDto(monthKey, monthName, monthDataList.size()));
    }

    return chartData;
  }

  /**
   * Get free growing milestone declarations data for the chart.
   *
   * @param filters Possible filter, see {@link DashboardFiltesDto} for more.
   * @return A list of {@link FreeGrowingMilestonesDto} for the chart.
   */
  public List<FreeGrowingMilestonesDto> getFreeGrowingMilestoneChartData(
      DashboardFiltesDto filters) {
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

    Map<Integer, String> labelsMap = new HashMap<>();
    labelsMap.put(0, "0 - 5 months");
    labelsMap.put(1, "6 - 11 months");
    labelsMap.put(2, "12 - 17 months");
    labelsMap.put(3, "18 months");

    int totalRecordsFiltered = 0;

    // Iterate over the found records filtering and putting them into the right piece
    for (OpeningsLastYearEntity entity : entities) {
      // Org Unit filter - District
      if (!Objects.isNull(filters.orgUnit())
          && !filters.orgUnit().equals(entity.getOrgUnitCode())) {
        continue;
      }

      // Client number
      if (!Objects.isNull(filters.clientNumber())
          && !filters.clientNumber().equals(entity.getClientNumber())) {
        continue;
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
      String label = labelsMap.get(index);
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

    String userId = loggedUserService.getLoggedUserId();

    Sort sort = Sort.by("lastUpdated").descending();
    List<OpeningsActivityEntity> openingList =
        openingsActivityRepository.findAllByEntryUserid(userId, sort);

    if (openingList.isEmpty()) {
      log.info("No recent activities data found for the user!");
      return List.of();
    }

    PrettyTime prettyTime = new PrettyTime();

    List<MyRecentActionsRequestsDto> chartData = new ArrayList<>();
    for (OpeningsActivityEntity entity : openingList) {
      String statusDesc = entity.getActivityTypeDesc();
      if (Objects.isNull(statusDesc)) {
        statusDesc = "Created";
      }

      MyRecentActionsRequestsDto dto =
          new MyRecentActionsRequestsDto(
              statusDesc,
              entity.getOpeningId(),
              entity.getStatusCode(),
              entity.getStatusDesc(),
              prettyTime.format(entity.getLastUpdated()),
              entity.getLastUpdated());

      chartData.add(dto);

      if (chartData.size() == 5) {
        break;
      }
    }

    return chartData;
  }
}
