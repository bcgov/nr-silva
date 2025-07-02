package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.common.exception.NotFoundGenericException;
import ca.bc.gov.restapi.results.oracle.dto.activity.OpeningActivityBaseDto;
import ca.bc.gov.restapi.results.oracle.dto.cover.OpeningForestCoverDetailsDto;
import ca.bc.gov.restapi.results.oracle.dto.cover.OpeningForestCoverDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.*;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningDetailsService {

  private final OpeningDetailsTombstoneService tombstoneService;
  private final OpeningDetailsStockingService stockingService;
  private final OpeningDetailsActivitiesService activitiesService;
  private final OpeningDetailsTenureService tenureService;
  private final OpeningDetailsForestCoverService forestCoverService;
  private final OpeningDetailsAttachmentService attachmentService;


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
    log.info("Fetched {} stocking standards details for opening with id: {}",
        stockingDetails.size(), openingId);
    return stockingDetails;
  }

  public Page<OpeningDetailsActivitiesDisturbanceDto> getOpeningActivitiesDisturbances(
      Long openingId, Pageable pageable) {
    log.info("Fetching disturbances for opening with id: {} and page: {}", openingId, pageable);
    var disturbances = activitiesService.getOpeningActivitiesDisturbances(openingId, pageable);
    log.info("Fetched {}/{} disturbances for opening with id: {}",
        disturbances.getNumberOfElements(),
        disturbances.getTotalElements(),
        openingId
    );
    return disturbances;
  }

  public Page<OpeningDetailsActivitiesActivitiesDto> getOpeningActivitiesActivities(
      Long openingId, String mainSearchTerm, Pageable pageable) {
    log.info("Fetching activities for opening with id: {}, search term: {}, and page: {}",
        openingId, mainSearchTerm, pageable);
    var activities = activitiesService.getOpeningActivitiesActivities(openingId, mainSearchTerm,
        pageable);
    log.info("Fetched {}/{} activities for opening with id: {}",
        activities.getNumberOfElements(),
        activities.getTotalElements(),
        openingId
    );
    return activities;
  }

  public OpeningActivityBaseDto getOpeningActivitiesActivity(Long openingId, Long atuId) {
    log.info("Fetching activity details with activity id: {} for opening with id: {}", atuId,
        openingId);
    var activityDetails = activitiesService.getOpeningActivitiesActivity(openingId, atuId);
    log.info("Fetched activity details with id: {} for opening with id: {}", atuId, openingId);
    return activityDetails;
  }

  public OpeningDetailsTenuresDto getOpeningTenures(Long openingId, String filter,
      Pageable pageable) {
    log.info("Fetching tenures for opening with id: {}, filter: {}, and page: {}", openingId,
        filter, pageable);
    return tenureService.getOpeningTenures(openingId, filter, pageable);
  }

  public List<OpeningForestCoverDto> getOpeningForestCoverList(Long openingId,
      String mainSearchTerm) {
    log.info("Fetching forest cover list for opening with id: {} and search term: {}", openingId,
        mainSearchTerm);
    return forestCoverService.getOpeningForestCoverList(openingId, mainSearchTerm);
  }

  public OpeningForestCoverDetailsDto getOpeningForestCoverDetails(Long forestCoverId) {
    log.info("Fetching forest cover details for forest cover with id: {}", forestCoverId);
    return forestCoverService.getDetails(forestCoverId)
        .orElseThrow(
            () -> new NotFoundGenericException("Forest cover polygon with id " + forestCoverId));
  }

  public List<OpeningDetailsAttachmentMetaDto> getOpeningAttachments(Long openingId) {
    log.info("Fetching attachment list for opening with id: {}", openingId);

    return attachmentService.getAttachmentList(openingId);
  }
}
