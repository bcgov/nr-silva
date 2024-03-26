package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearDto;
import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearFiltersDto;
import ca.bc.gov.restapi.results.postgres.service.DashboardMetricsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard-metrics")
@Tag(name = "Dashboard Metrics")
@RequiredArgsConstructor
public class DashboardMetricsEndpoint {

  private final DashboardMetricsService dashboardMetricsService;

  @GetMapping("/submission-trends")
  public List<OpeningsPerYearDto> getOpeningsSubmissionTrends(
      @RequestParam(value = "orgUnit", required = false) String orgUnitCode,
      @RequestParam(value = "status", required = false) String statusCode,
      @RequestParam(value = "entryDateStart", required = false) String entryDateStart,
      @RequestParam(value = "entryDateEnd", required = false) String entryDateEnd) {
    LocalDateTime entryDateStartDate = null;
    LocalDateTime entryDateEndDate = null;
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    if (!Objects.isNull(entryDateStart)) {
      LocalDate entryDateStartLd = LocalDate.parse(entryDateStart, fmt);
      entryDateStartDate = entryDateStartLd.atStartOfDay();
    }

    if (!Objects.isNull(entryDateEnd)) {
      LocalDate entryDateEndLd = LocalDate.parse(entryDateEnd, fmt);
      entryDateEndDate = entryDateEndLd.atStartOfDay();
    }

    OpeningsPerYearFiltersDto filtersDto =
        new OpeningsPerYearFiltersDto(
            orgUnitCode, statusCode, entryDateStartDate, entryDateEndDate);
    return dashboardMetricsService.getOpeningsSubmissionTrends(filtersDto);
  }
}
