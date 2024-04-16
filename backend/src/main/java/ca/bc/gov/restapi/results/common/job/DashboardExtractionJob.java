package ca.bc.gov.restapi.results.common.job;

import ca.bc.gov.restapi.results.common.dto.OracleExtractionDto;
import ca.bc.gov.restapi.results.oracle.service.OracleExtractionService;
import ca.bc.gov.restapi.results.postgres.service.DashboardInsertionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DashboardExtractionJob {

  private final OracleExtractionService oracleExtractionService;

  private final DashboardInsertionService dashboardInsertionService;

  // @Scheduled(cron = "0 0 */2 * * *", initialDelay = 1000 * 2)
  @Scheduled(fixedDelay = 1000 * 60 * 60 * 24, initialDelay = 1000 * 2)
  public void extractDataForTheDashboard() {
    log.info("Starting extraction!");

    OracleExtractionDto extactionDto = oracleExtractionService.getOpeningActivities();

    dashboardInsertionService.loadDashboardData(extactionDto);

    log.info("Extraction finished!");
  }
}
