package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.util.TimestampUtil;
import ca.bc.gov.restapi.results.postgres.dto.DashboardFiltesDto;
import ca.bc.gov.restapi.results.postgres.dto.FreeGrowingMilestonesDto;
import ca.bc.gov.restapi.results.postgres.dto.MyRecentActionsRequestsDto;
import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearDto;
import ca.bc.gov.restapi.results.postgres.service.DashboardMetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** This class holds resources for the dashboard metrics page. */
@RestController
@RequestMapping("/api/dashboard-metrics")
@Tag(
    name = "Dashboard Metrics (SILVA)",
    description = "Endpoints fot the Dashboard metrics charts in the `SILVA` schema")
@RequiredArgsConstructor
public class DashboardMetricsEndpoint {

  private final DashboardMetricsService dashboardMetricsService;

  /**
   * Gets data for the Opening submission trends Chart (Openings per year) on the Dashboard SILVA
   * page.
   *
   * @param orgUnitCode Optional district code filter.
   * @param statusCode Optional opening status code filter.
   * @param entryDateStart Optional opening entry timestamp start date filter.
   * @param entryDateEnd Optional opening entry timestamp end date filter.
   * @return A list of values to populate the chart or 204 no content if no data.
   */
  @GetMapping("/submission-trends")
  @Operation(
      summary = "Gets data for the Opening submission trends Chart (Openings per year).",
      description = "Fetches data from the last twelve months for the openings per year chart.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "An array with twelve objects for the last 12 months.",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(
            responseCode = "204",
            description = "No data found on the table. No response body."),
        @ApiResponse(
            responseCode = "401",
            description = "Access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = Void.class)))
      })
  public ResponseEntity<List<OpeningsPerYearDto>> getOpeningsSubmissionTrends(
      @RequestParam(value = "orgUnitCode", required = false)
          @Parameter(
              name = "orgUnitCode",
              in = ParameterIn.QUERY,
              description = "The Org Unit code to filter, same as District",
              required = false,
              example = "DCR")
          String orgUnitCode,
      @RequestParam(value = "statusCode", required = false)
          @Parameter(
              name = "statusCode",
              in = ParameterIn.QUERY,
              description = "The Openins Status code to filter",
              required = false,
              example = "APP")
          String statusCode,
      @RequestParam(value = "entryDateStart", required = false)
          @Parameter(
              name = "entryDateStart",
              in = ParameterIn.QUERY,
              description = "The Openins entry timestamp start date to filter, format yyyy-MM-dd",
              required = false,
              example = "2024-03-11")
          String entryDateStart,
      @RequestParam(value = "entryDateEnd", required = false)
          @Parameter(
              name = "entryDateEnd",
              in = ParameterIn.QUERY,
              description = "The Openins entry timestamp end date to filter, format yyyy-MM-dd",
              required = false,
              example = "2024-03-11")
          String entryDateEnd) {
    DashboardFiltesDto filtersDto =
        new DashboardFiltesDto(
            orgUnitCode,
            statusCode,
            TimestampUtil.parseDateString(entryDateStart),
            TimestampUtil.parseDateString(entryDateEnd),
            null);

    List<OpeningsPerYearDto> resultList =
        dashboardMetricsService.getOpeningsSubmissionTrends(filtersDto);

    if (resultList.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(resultList);
  }

  /**
   * Gets data for the Free growing Chart on the Dashboard SILVA page.
   *
   * @param orgUnitCode Optional district code filter.
   * @param clientNumber Optional client number filter.
   * @param entryDateStart Optional opening entry timestamp start date filter.
   * @param entryDateEnd Optional opening entry timestamp end date filter.
   * @return A list of values to populate the chart or 204 no content if no data.
   */
  @GetMapping("/free-growing-milestones")
  @Operation(
      summary = "Gets data for the Free growing Chart on the Dashboard SILVA page.",
      description =
          "Fetches data from the last 24 months and group them into periods for the Free growing"
              + " chart.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "An array with four objects, one for each piece of the chart.",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(
            responseCode = "204",
            description = "No data found on the table. No response body."),
        @ApiResponse(
            responseCode = "401",
            description = "Access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = Void.class)))
      })
  public ResponseEntity<List<FreeGrowingMilestonesDto>> getFreeGrowingMilestonesData(
      @RequestParam(value = "orgUnitCode", required = false)
          @Parameter(
              name = "orgUnitCode",
              in = ParameterIn.QUERY,
              description = "The Org Unit code to filter, same as District",
              required = false,
              example = "DCR")
          String orgUnitCode,
      @RequestParam(value = "clientNumber", required = false)
          @Parameter(
              name = "clientNumber",
              in = ParameterIn.QUERY,
              description = "The Client Number to filter",
              required = false,
              example = "00012797")
          String clientNumber,
      @RequestParam(value = "entryDateStart", required = false)
          @Parameter(
              name = "entryDateStart",
              in = ParameterIn.QUERY,
              description = "The Openins entry timestamp start date to filter, format yyyy-MM-dd",
              required = false,
              example = "2024-03-11")
          String entryDateStart,
      @RequestParam(value = "entryDateEnd", required = false)
          @Parameter(
              name = "entryDateEnd",
              in = ParameterIn.QUERY,
              description = "The Openins entry timestamp end date to filter, format yyyy-MM-dd",
              required = false,
              example = "2024-03-11")
          String entryDateEnd) {
    DashboardFiltesDto filtersDto =
        new DashboardFiltesDto(
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

  /**
   * Gets the last 5 most recent updated openings for the request user.
   *
   * @return A list of values to populate the chart or 204 no content if no data.
   */
  @GetMapping("/my-recent-actions/requests")
  @Operation(
      summary = "Gets the last 5 most recent updated openings for the request user.",
      description = "Fetches data for the My recent actions table, Requests tab",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "An array with five objects, one for opening row.",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(
            responseCode = "204",
            description = "No data found for the user. No response body."),
        @ApiResponse(
            responseCode = "401",
            description = "Access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = Void.class)))
      })
  public ResponseEntity<List<MyRecentActionsRequestsDto>> getUserRecentOpeningsActions() {
    List<MyRecentActionsRequestsDto> actionsDto =
        dashboardMetricsService.getUserRecentOpeningsActions();

    if (actionsDto.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(actionsDto);
  }
}
