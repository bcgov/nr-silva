package ca.bc.gov.restapi.results.common.job;

import ca.bc.gov.restapi.results.oracle.service.OracleExtractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DashboardExtractionJob {

  private final OracleExtractionService oracleExtractionService;

  //@Scheduled(cron = "0 0 */2 * * *", initialDelay = 1000 * 2)
  @Scheduled(fixedDelay = 1000 * 5, initialDelay = 1000 * 2)
  public void extractDataForTheDashboard() {
    log.info("Starting extraction!");
    oracleExtractionService.getOpeningActivities();
    log.info("Extraction finished!");
  }
}
