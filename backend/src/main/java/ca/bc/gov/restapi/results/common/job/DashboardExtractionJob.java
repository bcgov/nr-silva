package ca.bc.gov.restapi.results.common.job;

import ca.bc.gov.restapi.results.common.service.DashboardExtractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** This class contains the job responsible for extracting data from oracle for the dashboard. */
@Component
@RequiredArgsConstructor
public class DashboardExtractionJob {

  private final DashboardExtractionService dashboardExtractionService;

  /**
   * Scheduled job for extracting data from oracle and adding into Postgres. Spring Boot uses a
   * different cron expression, having: 1 - second (0-59), 2 - minute (0-59) 3, - hour (0-23), 4 -
   * day of month (1-31), 5 - month (1-12) or (JAN-DEC), 6 - day of the week (0-7) [0 or 7 is
   * Sunday] or (MON-SUN) More:
   * https://docs.spring.io/spring-framework/reference/integration/scheduling.html#scheduling-cron-expression
   */
  @Scheduled(cron = "0 0 23 * * MON-FRI")
  public void extractDataForTheDashboard() {
    dashboardExtractionService.extractDataForTheDashboard(null, null, null);
  }
}
