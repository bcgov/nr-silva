package ca.bc.gov.restapi.results.common.service.opening.history;

import ca.bc.gov.restapi.results.common.dto.cover.history.OpeningForestCoverHistoryDetailsDto;
import ca.bc.gov.restapi.results.common.dto.cover.history.OpeningForestCoverHistoryDto;
import ca.bc.gov.restapi.results.common.dto.cover.history.OpeningForestCoverHistoryOverviewDto;

import java.util.List;
import java.util.Optional;

public interface OpeningForestCoverHistoryService {
  List<OpeningForestCoverHistoryOverviewDto> getOpeningForestCoverHistoryOverviewList(Long openingId);
  List<OpeningForestCoverHistoryDto> getOpeningForestCoverList(
      Long openingId, String updateDate, String mainSearchTerm);
  Optional<OpeningForestCoverHistoryDetailsDto> getDetails(Long coverId, String archiveDate);
}
