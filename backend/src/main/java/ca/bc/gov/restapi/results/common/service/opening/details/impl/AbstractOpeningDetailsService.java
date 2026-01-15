package ca.bc.gov.restapi.results.common.service.opening.details.impl;

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
import ca.bc.gov.restapi.results.common.exception.NotFoundGenericException;
import ca.bc.gov.restapi.results.common.service.opening.details.*;
import ca.bc.gov.restapi.results.common.service.opening.history.OpeningForestCoverHistoryService;
import ca.bc.gov.restapi.results.common.service.opening.history.OpeningStandardUnitHistoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractOpeningDetailsService implements OpeningDetailsService {
  protected final OpeningDetailsTombstoneService tombstoneService;
  protected final OpeningDetailsStockingService stockingService;
  protected final OpeningDetailsActivitiesService activitiesService;
  protected final OpeningDetailsTenureService tenureService;
  protected final OpeningDetailsForestCoverService forestCoverService;
  protected final OpeningForestCoverHistoryService forestCoverHistoryService;
  protected final OpeningDetailsAttachmentService attachmentService;
  protected final OpeningStandardUnitHistoryService standardUnitHistoryService;

  public Optional<OpeningDetailsTombstoneOverviewDto> getOpeningTombstone(Long openingId) {
    log.info("Fetching tombstone for opening with id: {}", openingId);
    var tombstone = tombstoneService.getOpeningTombstone(openingId);
    if (tombstone.isEmpty()) {
      log.warn("No tombstone found for opening with id: {}", openingId);
      return Optional.empty();
    } else {
      log.info("Fetched tombstone for opening with id: {}", openingId);
    }
    return tombstone;
  }

  public List<OpeningDetailsStockingDto> getOpeningStockingDetails(Long openingId) {
    log.info("Fetching stocking standards details for opening with id: {}", openingId);
    var stockingDetails = stockingService.getOpeningStockingDetails(openingId);
    log.info(
        "Fetched {} stocking standards details for opening with id: {}",
        stockingDetails.size(),
        openingId);
    return stockingDetails;
  }

  public Page<OpeningDetailsActivitiesDisturbanceDto> getOpeningActivitiesDisturbances(
      Long openingId, Pageable pageable) {
    log.info("Fetching disturbances for opening with id: {} and page: {}", openingId, pageable);
    var disturbances = activitiesService.getOpeningActivitiesDisturbances(openingId, pageable);
    log.info(
        "Fetched {}/{} disturbances for opening with id: {}",
        disturbances.getNumberOfElements(),
        disturbances.getTotalElements(),
        openingId);
    return disturbances;
  }

  public Page<OpeningDetailsActivitiesActivitiesDto> getOpeningActivitiesActivities(
      Long openingId, String mainSearchTerm, Pageable pageable) {
    log.info(
        "Fetching activities for opening with id: {}, search term: {}, and page: {}",
        openingId,
        mainSearchTerm,
        pageable);
    var activities =
        activitiesService.getOpeningActivitiesActivities(openingId, mainSearchTerm, pageable);
    log.info(
        "Fetched {}/{} activities for opening with id: {}",
        activities.getNumberOfElements(),
        activities.getTotalElements(),
        openingId);
    return activities;
  }

  public OpeningActivityBaseDto getOpeningActivitiesActivity(Long openingId, Long atuId) {
    log.info(
        "Fetching activity details with activity id: {} for opening with id: {}", atuId, openingId);
    var activityDetails = activitiesService.getOpeningActivitiesActivity(openingId, atuId);
    log.info("Fetched activity details with id: {} for opening with id: {}", atuId, openingId);
    return activityDetails;
  }

  public OpeningDetailsTenuresDto getOpeningTenures(
      Long openingId, String filter, Pageable pageable) {
    log.info(
        "Fetching tenures for opening with id: {}, filter: {}, and page: {}",
        openingId,
        filter,
        pageable);
    return tenureService.getOpeningTenures(openingId, filter, pageable);
  }

  public List<OpeningForestCoverDto> getOpeningForestCoverList(
      Long openingId, String mainSearchTerm) {
    log.info(
        "Fetching forest cover list for opening with id: {} and search term: {}",
        openingId,
        mainSearchTerm);
    return forestCoverService.getOpeningForestCoverList(openingId, mainSearchTerm);
  }

  public OpeningForestCoverDetailsDto getOpeningForestCoverDetails(Long forestCoverId) {
    log.info("Fetching forest cover details for forest cover with id: {}", forestCoverId);
    return forestCoverService
        .getDetails(forestCoverId)
        .orElseThrow(
            () -> new NotFoundGenericException("Forest cover polygon with id " + forestCoverId));
  }

  public List<OpeningForestCoverHistoryOverviewDto> getOpeningForestCoverHistoryOverviewList(
      Long openingId) {
    log.info("Fetching forest cover history overview list for opening ID: {}", openingId);

    List<OpeningForestCoverHistoryOverviewDto> result = forestCoverHistoryService
        .getOpeningForestCoverHistoryOverviewList(openingId);

    if (result.isEmpty()) {
      throw new NotFoundGenericException("Forest cover history overview list for opening with id " + openingId);
    }

    return result;
  }

  public List<OpeningForestCoverHistoryDto> getOpeningForestCoverHistoryList(
      Long openingId, String updateDate, String mainSearchTerm) {
    log.info(
        "Fetching forest cover history list for opening ID: {} and update date: {}",
        openingId,
        updateDate);
    List<OpeningForestCoverHistoryDto> result =
        forestCoverHistoryService.getOpeningForestCoverList(openingId, updateDate, mainSearchTerm);

    if (result.isEmpty()) {
      throw new NotFoundGenericException(
          "Forest cover history list for opening with id "
              + openingId
              + " and update date "
              + updateDate);
    }
    return result;
  }

  public OpeningForestCoverHistoryDetailsDto getOpeningForestCoverHistoryDetails(
      Long forestCoverId, String archiveDate) {
    log.info(
        "Fetching forest cover history details for forest cover with id: {} and archive date: {}",
        forestCoverId,
        archiveDate);
    return forestCoverHistoryService
        .getDetails(forestCoverId, archiveDate)
        .orElseThrow(
            () ->
                new NotFoundGenericException(
                    "Forest cover history polygon with id "
                        + forestCoverId
                        + " and archive date "
                        + archiveDate));
  }

  public List<OpeningDetailsAttachmentMetaDto> getOpeningAttachments(Long openingId) {
    log.info("Fetching attachment list for opening with id: {}", openingId);

    return attachmentService.getAttachmentList(openingId);
  }

  public String generateAttachmentDownloadUrl(String guid) {
    log.info("Requesting S3 presigned url for attachment with guid: {}", guid);

    return attachmentService.getS3PresignedUrl(guid);
  }

  public List<OpeningStockingHistoryOverviewDto> getOpeningStandardUnitOverviewHistoryList(
      Long openingId) {
    log.info("Fetching standard unit overview history list for opening ID: {}", openingId);

    return standardUnitHistoryService.getStandardUnitOverviewHistoryList(openingId);
  }

  public List<OpeningStockingHistoryWithComparisonDto> getOpeningStandardUnitHistoryDetailsWithComparison(
      Long openingId, Long stockingEventHistoryId) {
    log.info(
        "Fetching standard unit history details with comparison for opening ID: {} and stocking event history ID:"
            + " {}",
        openingId,
        stockingEventHistoryId);

    List<OpeningStockingHistoryWithComparisonDto> historyDtos =
        standardUnitHistoryService.getStandardUnitHistoryDetailsWithComparison(openingId, stockingEventHistoryId);

    if (historyDtos.isEmpty()) {
      log.warn(
          "No standard unit history details found for openingId: {} and stockingEventHistoryId: {}",
          openingId,
          stockingEventHistoryId);
      throw new NotFoundGenericException("Standard unit history details");
    }

    return historyDtos;
  }

  public List<OpeningStockingHistoryDto> getOpeningStockingHistoryDetails(
      Long openingId,
      Long eventHistoryId
  ) {
    log.info("Fetching stocking standards history details for opening with id: {} and event history id: {}",
        openingId, eventHistoryId);
    var stockingDetails = standardUnitHistoryService.getOpeningStockingHistoryList(openingId, eventHistoryId);
    log.info(
        "Fetched {} stocking standards history details for opening with id: {} and event history id: {}",
        stockingDetails.size(),
        openingId,
        eventHistoryId);
    return stockingDetails;
  }


}
