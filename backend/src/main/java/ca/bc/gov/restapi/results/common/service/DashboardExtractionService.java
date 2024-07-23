package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.OracleExtractionDto;
import ca.bc.gov.restapi.results.common.dto.OracleExtractionParamsDto;
import ca.bc.gov.restapi.results.common.dto.OracleLogDto;
import ca.bc.gov.restapi.results.oracle.service.OracleExtractionService;
import ca.bc.gov.restapi.results.postgres.service.DashboardInsertionService;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * This class contains the service responsible for extracting data from oracle for the dashboard.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardExtractionService {

  private final OracleExtractionService oracleExtractionService;

  private final DashboardInsertionService dashboardInsertionService;

  /**
   * Service for extracting data from oracle and adding into Postgres.
   *
   * @param months Optional number of months.
   * @param debug Optional debug mode enabled.
   * @param manuallyTriggered Optional option.
   */
  @Async
  public void extractDataForTheDashboard(Integer months, Boolean debug, Boolean manuallyTriggered) {
    OracleExtractionParamsDto params = getParams(months, debug, manuallyTriggered);

    LocalDateTime startDateTime = LocalDateTime.now();
    String message = "Starting extraction";
    log.info(message);

    OracleExtractionDto extractionDto = oracleExtractionService.getOpeningActivities(params);

    // Add the first log message
    extractionDto.logMessages().add(0, new OracleLogDto(message, startDateTime));

    dashboardInsertionService.loadDashboardData(extractionDto, startDateTime, params);
  }

  private OracleExtractionParamsDto getParams(
      Integer months, Boolean debug, Boolean manuallyTriggered) {
    if (Objects.isNull(months)) {
      months = 24;
    }
    if (Objects.isNull(debug)) {
      debug = Boolean.FALSE;
    }
    if (Objects.isNull(manuallyTriggered)) {
      manuallyTriggered = Boolean.FALSE;
    }

    return new OracleExtractionParamsDto(months, debug, manuallyTriggered);
  }
}
