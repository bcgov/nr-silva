package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.util.TimestampUtil;
import ca.bc.gov.restapi.results.postgres.dto.DashboardFiltersDto;
import ca.bc.gov.restapi.results.postgres.dto.FreeGrowingMilestonesDto;
import ca.bc.gov.restapi.results.postgres.service.DashboardMetricsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class holds resources for the dashboard metrics page.
 */
@RestController
@RequestMapping("/api/dashboard-metrics")
@RequiredArgsConstructor
public class DashboardMetricsEndpoint {

  private final DashboardMetricsService dashboardMetricsService;


  /**
   * Gets data for the Free growing Chart on the Dashboard SILVA page.
   *
   * @param orgUnitCode    Optional district code filter.
   * @param clientNumber   Optional client number filter.
   * @param entryDateStart Optional opening entry timestamp start date filter.
   * @param entryDateEnd   Optional opening entry timestamp end date filter.
   * @return A list of values to populate the chart or 204 no content if no data.
   */
  @GetMapping("/free-growing-milestones")
  public ResponseEntity<List<FreeGrowingMilestonesDto>> getFreeGrowingMilestonesData(
      @RequestParam(value = "orgUnitCode", required = false)
      String orgUnitCode,
      @RequestParam(value = "clientNumber", required = false)
      String clientNumber,
      @RequestParam(value = "entryDateStart", required = false)
      String entryDateStart,
      @RequestParam(value = "entryDateEnd", required = false)
      String entryDateEnd) {
    DashboardFiltersDto filtersDto =
        new DashboardFiltersDto(
            orgUnitCode,
            null,
            TimestampUtil.parseDateString(entryDateStart),
            TimestampUtil.parseDateString(entryDateEnd),
            clientNumber);
    List<FreeGrowingMilestonesDto> milestonesDto =
        dashboardMetricsService.getFreeGrowingMilestoneChartData(filtersDto);

    if (milestonesDto.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(milestonesDto);
  }

}
