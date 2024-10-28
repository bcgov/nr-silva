package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.common.service.DashboardExtractionService;
import ca.bc.gov.restapi.results.postgres.configuration.DashboardUserManagerConfiguration;
import ca.bc.gov.restapi.results.postgres.entity.OracleExtractionLogsEntity;
import ca.bc.gov.restapi.results.postgres.repository.OracleExtractionLogsRepository;
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

/**
 * This class holds resources for the Dashboard extraction process.
 */
@RestController
@RequestMapping("/api/dashboard-extraction")
@AllArgsConstructor
public class DashboardExtractionEndpoint {

  private final OracleExtractionLogsRepository oracleExtractionLogsRepository;

  private final DashboardExtractionService dashboardExtractionService;

  private final DashboardUserManagerConfiguration dashboardUserManagerConfiguration;

  private final LoggedUserService loggedUserService;

  /**
   * Manually triggers the dashboard extraction job.
   *
   * @param months Optional. The number of months to extract data. Default: 24.
   * @param debug  Optional. Enables debug mode. Default: `false`.
   * @return Http codes 204 if success or 401 if unauthorized.
   */
  @PostMapping("/start")
  public ResponseEntity<Void> startExtractionProcessManually(
      @RequestParam(value = "months", required = false)
      Integer months,
      @RequestParam(value = "debug", required = false)
      Boolean debug) {

    if (dashboardUserManagerConfiguration.getUserList().isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    System.out.println("User list: " + dashboardUserManagerConfig.getUserList());

    String currentUser = loggedUserService.getLoggedUserIdirOrBceId();
    if (!dashboardUserManagerConfiguration.getUserList().contains(currentUser)) {
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
  public ResponseEntity<List<OracleExtractionLogsEntity>> getLastExtractionLogs() {
    List<OracleExtractionLogsEntity> logs =
        oracleExtractionLogsRepository.findAll(Sort.by("id").ascending());

    if (logs.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(logs);
  }
}
