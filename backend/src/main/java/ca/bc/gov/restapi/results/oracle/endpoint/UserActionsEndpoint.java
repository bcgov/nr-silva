package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.oracle.service.OpeningTrendsService;
import ca.bc.gov.restapi.results.oracle.service.UserActionsService;
import ca.bc.gov.restapi.results.postgres.dto.MyRecentActionsRequestsDto;
import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearDto;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserActionsEndpoint {

  private final UserActionsService userActionsService;
  private final OpeningTrendsService openingTrendsService;

  @GetMapping("/recent-actions")
  public ResponseEntity<List<MyRecentActionsRequestsDto>> getUserRecentOpeningsActions() {
    List<MyRecentActionsRequestsDto> actionsDto =
        userActionsService.getResultsAuditActivity();

    log.info("Returning {} recent actions", actionsDto.size());

    if (actionsDto.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(actionsDto);
  }

  /**
   * Gets data for the Opening submission trends Chart (Openings per year) on the Dashboard SILVA
   * page.
   *
   * @param orgUnits       Optional district code filter.
   * @param statusCodes    Optional opening status code filter.
   * @param entryDateStart Optional opening entry timestamp start date filter.
   * @param entryDateEnd   Optional opening entry timestamp end date filter.
   * @return A list of values to populate the chart or 204 no content if no data.
   */
  @GetMapping("/submission-trends")
  public ResponseEntity<List<OpeningsPerYearDto>> getOpeningsSubmissionTrends(
      @RequestParam(value = "orgUnitCode", required = false)
      List<String> orgUnits,
      @RequestParam(value = "statusCode", required = false)
      List<String> statusCodes,
      @RequestParam(value = "entryDateStart", required = false)
      LocalDate entryDateStart,
      @RequestParam(value = "entryDateEnd", required = false)
      LocalDate entryDateEnd
  ) {

    List<OpeningsPerYearDto> resultList =
        openingTrendsService.getOpeningSubmissionTrends(
            getDateOrDefault(entryDateStart,LocalDate.now().minusYears(1)),
            getDateOrDefault(entryDateEnd,
                //If we have an end date, we get it, otherwise we use the current date,
                // and no matter if we have the start date or not, we add a year to the end date
                getDateOrDefault(entryDateStart,LocalDate.now().minusYears(1)).plusYears(1)
            ),
            orgUnits,
            statusCodes
        );

    if (resultList.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(resultList);
  }

  private LocalDate getDateOrDefault(LocalDate date, LocalDate defaultDate) {
    return date != null ? date : defaultDate;
  }

}
