package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.common.service.DashboardExtractionService;
import ca.bc.gov.restapi.results.postgres.config.DashboardUserManagerConfig;
import ca.bc.gov.restapi.results.postgres.entity.OracleExtractionLogsEntity;
import ca.bc.gov.restapi.results.postgres.repository.OracleExtractionLogsRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** This class holds resources for the Dashboard extraction process. */
@RestController
@RequestMapping("/api/dashboard-extraction")
@AllArgsConstructor
@Tag(
    name = "Dashboard Extraction (Postgres)",
    description = "Endpoints for the Dashboard extraction process on `SILVA` schema")
public class DashboardExtractionEndpoint {

  private final OracleExtractionLogsRepository oracleExtractionLogsRepository;

  private final DashboardExtractionService dashboardExtractionService;

  private final DashboardUserManagerConfig dashboardUserManagerConfig;

  private final LoggedUserService loggedUserService;

  /**
   * Manually triggers the dashboard extraction job.
   *
   * @param months Optional. The number of months to extract data. Default: 24.
   * @param debug Optional. Enables debug mode. Default: `false`.
   * @return Http codes 204 if success or 401 if unauthorized.
   */
  @PostMapping("/start")
  @Operation(
      summary = "Manually triggers the dashboard extraction job.",
      description =
          "Manually calls the job responsible for fetching and loading data. There's a debug mode"
              + " that can be enabled",
      responses = {
        @ApiResponse(
            responseCode = "204",
            description = "Job successfully triggered. No response body (asynchronous task)."),
        @ApiResponse(
            responseCode = "401",
            description = "Access token is missing, invalid or user not allowed",
            content = @Content(schema = @Schema(implementation = Void.class)))
      })
  public ResponseEntity<Void> startExtractionProcessManually(
      @RequestParam(value = "months", required = false)
          @Parameter(
              name = "months",
              in = ParameterIn.QUERY,
              description = "Optional. The number of months to extract data. Default: 24",
              required = false,
              example = "12")
          Integer months,
      @RequestParam(value = "debug", required = false)
          @Parameter(
              name = "debug",
              in = ParameterIn.QUERY,
              description = "Optional. Enables debug mode. Default: `false`",
              required = false,
              example = "true")
          Boolean debug) {
    if (dashboardUserManagerConfig.getUserList().isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    System.out.println("User list: " + dashboardUserManagerConfig.getUserList());

    String currentUser = loggedUserService.getLoggedUserIdirOrBceId();
    if (!dashboardUserManagerConfig.getUserList().contains(currentUser)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    dashboardExtractionService.extractDataForTheDashboard(months, debug, true);
    return ResponseEntity.noContent().build();
  }

  /**
   * Gets all log messages from the last extraction process.
   *
   * @return A list of oracle logs records with the last extraction logs.
   */
  @GetMapping("/logs")
  @Operation(
      summary = "Gets all log messages from the last extraction process.",
      description = "Fetches all logged messages from the last extraction with or without debug.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "An array with all logs messages.",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(
            responseCode = "204",
            description = "No data found on the table. No response body."),
        @ApiResponse(
            responseCode = "401",
            description = "Access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = Void.class)))
      })
  public ResponseEntity<List<OracleExtractionLogsEntity>> getLastExtractionLogs() {
    List<OracleExtractionLogsEntity> logs =
        oracleExtractionLogsRepository.findAll(Sort.by("id").ascending());

    if (logs.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(logs);
  }
}
