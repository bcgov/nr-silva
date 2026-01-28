package ca.bc.gov.restapi.results.common.service.opening.details;

import ca.bc.gov.restapi.results.common.dto.activity.OpeningActivityBaseDto;
import ca.bc.gov.restapi.results.common.dto.cover.OpeningForestCoverDetailsDto;
import ca.bc.gov.restapi.results.common.dto.cover.OpeningForestCoverDto;
import ca.bc.gov.restapi.results.common.dto.cover.history.OpeningForestCoverHistoryDetailsDto;
import ca.bc.gov.restapi.results.common.dto.cover.history.OpeningForestCoverHistoryDto;
import ca.bc.gov.restapi.results.common.dto.cover.history.OpeningForestCoverHistoryOverviewDto;
import ca.bc.gov.restapi.results.common.dto.opening.*;
import ca.bc.gov.restapi.results.common.dto.opening.history.OpeningStockingHistoryDto;
import ca.bc.gov.restapi.results.common.dto.opening.history.OpeningStockingHistoryOverviewDto;
import ca.bc.gov.restapi.results.common.dto.opening.history.OpeningStockingHistoryWithComparisonDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OpeningDetailsService {
  Optional<OpeningDetailsTombstoneOverviewDto> getOpeningTombstone(Long openingId);

  List<OpeningDetailsStockingDto> getOpeningStockingDetails(Long openingId);

  Page<OpeningDetailsActivitiesDisturbanceDto> getOpeningActivitiesDisturbances(
      Long openingId, Pageable pageable);

  Page<OpeningDetailsActivitiesActivitiesDto> getOpeningActivitiesActivities(
      Long openingId, String mainSearchTerm, Pageable pageable);

  OpeningActivityBaseDto getOpeningActivitiesActivity(Long openingId, Long atuId);

  OpeningDetailsTenuresDto getOpeningTenures(
      Long openingId, String filter, Pageable pageable);

  List<OpeningForestCoverDto> getOpeningForestCoverList(
      Long openingId, String mainSearchTerm);

  OpeningForestCoverDetailsDto getOpeningForestCoverDetails(Long forestCoverId);

  List<OpeningForestCoverHistoryOverviewDto> getOpeningForestCoverHistoryOverviewList(
      Long openingId);

  List<OpeningForestCoverHistoryDto> getOpeningForestCoverHistoryList(
      Long openingId, String updateDate, String mainSearchTerm);

  OpeningForestCoverHistoryDetailsDto getOpeningForestCoverHistoryDetails(
      Long forestCoverId, String archiveDate);

  List<OpeningDetailsAttachmentMetaDto> getOpeningAttachments(Long openingId);

  String generateAttachmentDownloadUrl(String guid);

  List<OpeningStockingHistoryOverviewDto> getOpeningStandardUnitOverviewHistoryList(
      Long openingId);

  List<OpeningStockingHistoryWithComparisonDto> getOpeningStandardUnitHistoryDetailsWithComparison(
      Long openingId, Long stockingEventHistoryId);

  List<OpeningStockingHistoryDto> getOpeningStockingHistoryDetails(
      Long openingId,
      Long eventHistoryId
  );

}
