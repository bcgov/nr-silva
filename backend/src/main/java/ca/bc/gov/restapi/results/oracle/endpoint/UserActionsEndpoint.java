package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.oracle.service.OpeningTrendsService;
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

  private final OpeningTrendsService openingTrendsService;

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

    LocalDate endDate = getDateOrDefault(entryDateEnd, LocalDate.now());
    LocalDate startDate = getDateOrDefault(entryDateStart, endDate.minusYears(1));

    List<OpeningsPerYearDto> resultList =
        openingTrendsService.getOpeningSubmissionTrends(startDate, endDate, orgUnits, statusCodes);

    if (resultList.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(resultList);
  }

  private LocalDate getDateOrDefault(LocalDate date, LocalDate defaultDate) {
    return date != null ? date : defaultDate;
  }

}
