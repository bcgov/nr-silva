package ca.bc.gov.restapi.results.common.service.opening.history;

import ca.bc.gov.restapi.results.common.dto.opening.history.OpeningStockingHistoryDto;
import ca.bc.gov.restapi.results.common.dto.opening.history.OpeningStockingHistoryOverviewDto;
import ca.bc.gov.restapi.results.common.dto.opening.history.OpeningStockingHistoryWithComparisonDto;

import java.util.List;

public interface OpeningStandardUnitHistoryService {
  List<OpeningStockingHistoryOverviewDto> getStandardUnitOverviewHistoryList(Long openingId);

  List<OpeningStockingHistoryDto> getOpeningStockingHistoryList(
      Long openingId,
      Long eventHistoryId
  );

  List<OpeningStockingHistoryWithComparisonDto> getStandardUnitHistoryDetailsWithComparison(
      Long openingId,
      Long stockingEventHistoryId
  );
}
