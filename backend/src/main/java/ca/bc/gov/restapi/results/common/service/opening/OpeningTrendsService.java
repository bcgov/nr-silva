package ca.bc.gov.restapi.results.common.service.opening;

import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearDto;

import java.time.LocalDate;
import java.util.List;

public interface OpeningTrendsService {
  List<OpeningsPerYearDto> getOpeningSubmissionTrends(
      LocalDate startDate,
      LocalDate endDate,
      List<String> orgUnits,
      List<String> statusCodes
  );
}
