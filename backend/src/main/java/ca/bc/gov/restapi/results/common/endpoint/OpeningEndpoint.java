package ca.bc.gov.restapi.results.common.endpoint;

import ca.bc.gov.restapi.results.common.exception.NotFoundGenericException;
import ca.bc.gov.restapi.results.common.exception.OpeningNotFoundException;
import ca.bc.gov.restapi.results.common.service.OpeningSearchService;
import ca.bc.gov.restapi.results.common.dto.activity.OpeningActivityBaseDto;
import ca.bc.gov.restapi.results.common.dto.cover.OpeningForestCoverDetailsDto;
import ca.bc.gov.restapi.results.common.dto.cover.OpeningForestCoverDto;
import ca.bc.gov.restapi.results.common.dto.cover.history.OpeningForestCoverHistoryDetailsDto;
import ca.bc.gov.restapi.results.common.dto.cover.history.OpeningForestCoverHistoryDto;
import ca.bc.gov.restapi.results.common.dto.cover.history.OpeningForestCoverHistoryOverviewDto;
import ca.bc.gov.restapi.results.common.dto.opening.*;
import ca.bc.gov.restapi.results.common.dto.opening.history.OpeningStockingHistoryDto;
import ca.bc.gov.restapi.results.common.dto.opening.history.OpeningStockingHistoryOverviewDto;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ca.bc.gov.restapi.results.postgres.service.OpeningSpatialFileService;
import ca.bc.gov.restapi.results.postgres.service.UserOpeningService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController("commonOpeningEndpoint")
@RequestMapping(path = "/api/openings", produces = {
    MediaType.APPLICATION_JSON_VALUE,
    MediaType.APPLICATION_PROBLEM_JSON_VALUE
})
@RequiredArgsConstructor
public class OpeningEndpoint {
  private final UserOpeningService userOpeningService;
  private final OpeningSpatialFileService openingSpatialFileService;
  private final OpeningDetailsService openingDetailsService;
  private final OpeningSearchService openingSearchService;

  /**
   * Get the Opening Tombstone/Summary information.
   *
   * @param openingId Opening ID
   * @return OpeningTombstoneDto
   * @throws OpeningNotFoundException if no tombstone information is found for the given Opening ID.
   */
  @GetMapping("/{openingId}/tombstone")
  public OpeningDetailsTombstoneOverviewDto getOpeningTombstone(@PathVariable Long openingId) {
    return openingDetailsService
        .getOpeningTombstone(openingId)
        .orElseThrow(() -> new OpeningNotFoundException(openingId));
  }

  /**
   * Get the Opening Stocking Details (SSU) for a given Opening ID.
   *
   * @param openingId Opening ID
   * @return List of {@link OpeningDetailsStockingDto} containing stocking details.
   * @throws OpeningNotFoundException if no stocking details are found for the given Opening ID.
   */
  @GetMapping("/{openingId}/ssu")
  public List<OpeningDetailsStockingDto> getOpeningSsu(@PathVariable Long openingId) {
    return openingDetailsService.getOpeningStockingDetails(openingId);
  }

  /**
   * Get the history of Standard Stocking Unit (SSU) for a given Opening ID.
   *
   * @param openingId Opening ID
   * @return List of {@link OpeningStockingHistoryOverviewDto} containing SSU history.
   */
  @GetMapping("/{openingId}/ssu/history")
  public List<OpeningStockingHistoryOverviewDto> getOpeningSsuHistory(
      @PathVariable Long openingId) {
    return openingDetailsService.getOpeningStandardUnitOverviewHistoryList(openingId);
  }

  /**
   * Get the details of a specific Standard Stocking Unit (SSU) history entry.
   *
   * @param openingId Opening ID
   * @param eventHistoryId History ID of the SSU
   * @return List of {@link OpeningStockingHistoryDto} containing detailed information about the SSU
   *     history entry.
   */
  @GetMapping("/{openingId}/ssu/history/{eventHistoryId}")
  public List<OpeningStockingHistoryDto> getOpeningSsuHistoryDetails(
      @PathVariable Long openingId, @PathVariable Long eventHistoryId) {
    return openingDetailsService.getOpeningStockingHistoryDetails(openingId, eventHistoryId);
  }

  @GetMapping("/{openingId}/disturbances")
  public Page<OpeningDetailsActivitiesDisturbanceDto> getOpeningDisturbances(
      @PathVariable Long openingId, @ParameterObject Pageable pageable) {
    return openingDetailsService.getOpeningActivitiesDisturbances(openingId, pageable);
  }

  @GetMapping("/{openingId}/activities")
  public Page<OpeningDetailsActivitiesActivitiesDto> getOpeningActivities(
      @PathVariable Long openingId,
      @RequestParam(name = "filter", required = false) String filter,
      @ParameterObject Pageable pageable) {
    return openingDetailsService.getOpeningActivitiesActivities(openingId, filter, pageable);
  }

  @GetMapping("/{openingId}/activities/{atuId}")
  public OpeningActivityBaseDto getOpeningActivity(
      @PathVariable Long openingId, @PathVariable Long atuId) {
    return openingDetailsService.getOpeningActivitiesActivity(openingId, atuId);
  }

  @GetMapping("/{openingId}/tenures")
  public OpeningDetailsTenuresDto getTenures(
      @PathVariable Long openingId,
      @RequestParam(name = "filter", required = false) String filter,
      @ParameterObject Pageable pageable) {
    return openingDetailsService.getOpeningTenures(openingId, filter, pageable);
  }

  @GetMapping("/{openingId}/cover")
  public List<OpeningForestCoverDto> getCover(
      @PathVariable Long openingId,
      @RequestParam(name = "mainSearchTerm", required = false) String mainSearchTerm) {
    return openingDetailsService.getOpeningForestCoverList(openingId, mainSearchTerm);
  }

  @GetMapping("/{openingId}/cover/{forestCoverId}")
  public OpeningForestCoverDetailsDto getCoverDetails(
      @PathVariable Long openingId, @PathVariable Long forestCoverId) {
    return openingDetailsService.getOpeningForestCoverDetails(forestCoverId);
  }

  /**
   * Retrieves the forest cover history overview for a given opening.
   *
   * @param openingId the ID of the opening
   * @return a list of {@link OpeningForestCoverHistoryOverviewDto} representing the forest cover
   *     history overview
   * @throws NotFoundGenericException if no overview is found for the given opening ID
   */
  @GetMapping("/{openingId}/cover/history/overview")
  public List<OpeningForestCoverHistoryOverviewDto> getCoverHistoryOverview(
      @PathVariable Long openingId) throws NotFoundGenericException {
    return openingDetailsService.getOpeningForestCoverHistoryOverviewList(openingId);
  }

  /**
   * Retrieves the forest cover history for a given opening and update date, optionally filtered by
   * a search term.
   *
   * @param openingId the ID of the opening
   * @param updateDate the update date to filter history (required, format: yyyy-MM-dd)
   * @param mainSearchTerm an optional search term to filter results
   * @return a list of {@link OpeningForestCoverHistoryDto} representing the forest cover history
   * @throws NotFoundGenericException if no history is found for the given parameters
   */
  @GetMapping("/{openingId}/cover/history")
  public List<OpeningForestCoverHistoryDto> getCoverHistory(
      @PathVariable Long openingId,
      @RequestParam(name = "updateDate", required = true) String updateDate,
      @RequestParam(name = "mainSearchTerm", required = false) String mainSearchTerm)
      throws NotFoundGenericException {
    return openingDetailsService.getOpeningForestCoverHistoryList(openingId, updateDate, mainSearchTerm);
  }

  /**
   * Retrieves the details of a specific forest cover history entry for a given opening, forest
   * cover ID, and archive date.
   *
   * @param openingId the ID of the opening
   * @param forestCoverId the ID of the forest cover
   * @param archiveDate the archive date of the forest cover history (required, format: yyyy-MM-dd)
   * @return the {@link OpeningForestCoverHistoryDetailsDto} containing detailed information
   * @throws NotFoundGenericException if no details are found for the given parameters
   */
  @GetMapping("/{openingId}/cover/history/{forestCoverId}")
  public OpeningForestCoverHistoryDetailsDto getForestCoverHistoryDetails(
      @PathVariable Long openingId,
      @PathVariable Long forestCoverId,
      @RequestParam(name = "archiveDate", required = true) String archiveDate)
      throws NotFoundGenericException {
    return openingDetailsService.getOpeningForestCoverHistoryDetails(forestCoverId, archiveDate);
  }

  @GetMapping("/{openingId}/attachments")
  @PreAuthorize("@auth.isIdirUser()")
  public List<OpeningDetailsAttachmentMetaDto> getAttachments(@PathVariable Long openingId) {
    return openingDetailsService.getOpeningAttachments(openingId);
  }

  @GetMapping("/{openingId}/attachments/{guid}")
  @PreAuthorize("@auth.isIdirUser()")
  public ResponseEntity<String> getAttachmentByGuid(
      @PathVariable("openingId") Long openingId, @PathVariable String guid) {

    String presignedUrl = openingDetailsService.generateAttachmentDownloadUrl(guid);
    return ResponseEntity.ok(presignedUrl);
  }
}
