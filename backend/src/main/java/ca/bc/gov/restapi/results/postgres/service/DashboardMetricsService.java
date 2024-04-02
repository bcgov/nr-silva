package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearDto;
import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearFiltersDto;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsLastYearRepository;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/** This class contains methods for gathering and grouping data for the dashboard metrics screen. */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardMetricsService {

  private final OpeningsLastYearRepository openingsLastYearRepository;

  /**
   * Get openings submission trends data for the opening per year chart.
   *
   * @param filters Possible filter, see {@link OpeningsPerYearFiltersDto} for more.
   * @return A list of {@link OpeningsPerYearDto} for the opening chart.
   */
  public List<OpeningsPerYearDto> getOpeningsSubmissionTrends(OpeningsPerYearFiltersDto filters) {
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

    for (OpeningsLastYearEntity entity : entities) {
      // Org Unit filter - District
      if (!Objects.isNull(filters.orgUnit())
          && !entity.getOrgUnitCode().equals(filters.orgUnit())) {
        continue;
      }

      // Status filter
      if (!Objects.isNull(filters.status()) && !entity.getStatus().equals(filters.status())) {
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
      log.info("Month: {}", monthName);
      chartData.add(new OpeningsPerYearDto(monthKey, monthName, monthDataList.size()));
    }

    return chartData;
  }
}
