package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.common.exception.OpeningNotFoundException;
import ca.bc.gov.restapi.results.common.util.MimeTypeResolver;
import ca.bc.gov.restapi.results.oracle.dto.activity.OpeningActivityBaseDto;
import ca.bc.gov.restapi.results.oracle.dto.cover.OpeningForestCoverDetailsDto;
import ca.bc.gov.restapi.results.oracle.dto.cover.OpeningForestCoverDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.*;
import ca.bc.gov.restapi.results.oracle.dto.opening.history.OpeningStandardUnitHistoryDetailsDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.history.OpeningStandardUnitHistoryDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.history.OpeningStandardUnitHistoryOverviewDto;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningAttachmentEntity;
import ca.bc.gov.restapi.results.oracle.service.OpeningSearchService;
import ca.bc.gov.restapi.results.oracle.service.opening.details.OpeningDetailsService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** This class contains resources for the opening search api. */
@RestController
@RequestMapping("/api/openings")
@RequiredArgsConstructor
@Slf4j
public class OpeningEndpoint {

  private final OpeningSearchService openingSearchService;
  private final OpeningDetailsService openingService;

  /**
   * Get the Opening Tombstone/Summary information.
   *
   * @param openingId Opening ID
   * @return OpeningTombstoneDto
   * @throws OpeningNotFoundException if no tombstone information is found for the given Opening ID.
   */
  @GetMapping("/{openingId}/tombstone")
  public OpeningDetailsTombstoneOverviewDto getOpeningTombstone(@PathVariable Long openingId) {
    return openingService
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
    return openingService.getOpeningStockingDetails(openingId);
  }

  /**
   * Get the history of Standard Stocking Unit (SSU) for a given Opening ID.
   *
   * @param openingId Opening ID
   * @return List of {@link OpeningStandardUnitHistoryOverviewDto} containing SSU history.
   */
  @GetMapping("/{openingId}/ssu/history")
  public List<OpeningStandardUnitHistoryOverviewDto> getOpeningSsuHistory(
      @PathVariable Long openingId) {
    return openingService.getOpeningStandardUnitOverviewHistoryList(openingId);
  }

    /**
     * Get the details of a specific Standard Stocking Unit (SSU) history entry.
     *
     * @param openingId Opening ID
     * @param historyId History ID of the SSU
     * @return List of {@link OpeningStandardUnitHistoryDto} containing detailed information about the
     *     SSU history entry.
     */
  @GetMapping("/{openingId}/ssu/history/{historyId}")
  public List<OpeningStandardUnitHistoryDto> getOpeningSsuHistoryDetails(
      @PathVariable Long openingId, @PathVariable Long historyId) {
    return openingService.getOpeningStandardUnitHistoryDetails(openingId, historyId);
  }

  @GetMapping("/{openingId}/disturbances")
  public Page<OpeningDetailsActivitiesDisturbanceDto> getOpeningDisturbances(
      @PathVariable Long openingId, @ParameterObject Pageable pageable) {
    return openingService.getOpeningActivitiesDisturbances(openingId, pageable);
  }

  @GetMapping("/{openingId}/activities")
  public Page<OpeningDetailsActivitiesActivitiesDto> getOpeningActivities(
      @PathVariable Long openingId,
      @RequestParam(name = "filter", required = false) String filter,
      @ParameterObject Pageable pageable) {
    return openingService.getOpeningActivitiesActivities(openingId, filter, pageable);
  }

  @GetMapping("/{openingId}/activities/{atuId}")
  public OpeningActivityBaseDto getOpeningActivity(
      @PathVariable Long openingId, @PathVariable Long atuId) {
    return openingService.getOpeningActivitiesActivity(openingId, atuId);
  }

  @GetMapping("/{openingId}/tenures")
  public OpeningDetailsTenuresDto getTenures(
      @PathVariable Long openingId,
      @RequestParam(name = "filter", required = false) String filter,
      @ParameterObject Pageable pageable) {
    return openingService.getOpeningTenures(openingId, filter, pageable);
  }

  @GetMapping("/{openingId}/cover")
  public List<OpeningForestCoverDto> getCover(
      @PathVariable Long openingId,
      @RequestParam(name = "mainSearchTerm", required = false) String mainSearchTerm) {
    return openingService.getOpeningForestCoverList(openingId, mainSearchTerm);
  }

  @GetMapping("/{openingId}/cover/{forestCoverId}")
  public OpeningForestCoverDetailsDto getCoverDetails(
      @PathVariable Long openingId, @PathVariable Long forestCoverId) {
    return openingService.getOpeningForestCoverDetails(forestCoverId);
  }

  /**
   * Search for Openings with different filters.
   *
   * @param mainSearchTerm Number representing one of Opening ID | Opening Number | Timber Mark ID |
   *     File
   * @param orgUnit Org Unit code filter, same as District.
   * @param category Opening category code filter.
   * @param statusList Opening statuses codes filter.
   * @param myOpenings Openings created by the request user
   * @param submittedToFrpa Submitted to FRPA
   * @param disturbanceDateStart Disturbance start date filter
   * @param disturbanceDateEnd Disturbance end date filter
   * @param regenDelayDateStart Regen start date filter
   * @param regenDelayDateEnd Regen end date filter
   * @param freeGrowingDateStart Free growing start date filter
   * @param freeGrowingDateEnd Free growing end date filter
   * @param updateDateStart Opening update start date filter
   * @param updateDateEnd Opening update end date filter
   * @param cuttingPermitId The cutting permit identification filter
   * @param cutBlockId Cute block identification filter
   * @param timberMark Timber mark filter
   * @param paginationParameters Pagination settings
   * @return PaginatedResult with found records.
   */
  @GetMapping("/search")
  public Page<OpeningSearchResponseDto> openingSearch(
      @RequestParam(value = "mainSearchTerm", required = false) String mainSearchTerm,
      @RequestParam(value = "orgUnit", required = false) List<String> orgUnit,
      @RequestParam(value = "category", required = false) List<String> category,
      @RequestParam(value = "statusList", required = false) List<String> statusList,
      @RequestParam(value = "myOpenings", required = false) Boolean myOpenings,
      @RequestParam(value = "submittedToFrpa", required = false) Boolean submittedToFrpa,
      @RequestParam(value = "disturbanceDateStart", required = false) String disturbanceDateStart,
      @RequestParam(value = "disturbanceDateEnd", required = false) String disturbanceDateEnd,
      @RequestParam(value = "regenDelayDateStart", required = false) String regenDelayDateStart,
      @RequestParam(value = "regenDelayDateEnd", required = false) String regenDelayDateEnd,
      @RequestParam(value = "freeGrowingDateStart", required = false) String freeGrowingDateStart,
      @RequestParam(value = "freeGrowingDateEnd", required = false) String freeGrowingDateEnd,
      @RequestParam(value = "updateDateStart", required = false) String updateDateStart,
      @RequestParam(value = "updateDateEnd", required = false) String updateDateEnd,
      @RequestParam(value = "cuttingPermitId", required = false) String cuttingPermitId,
      @RequestParam(value = "cutBlockId", required = false) String cutBlockId,
      @RequestParam(value = "clientLocationCode", required = false) String clientLocationCode,
      @RequestParam(value = "clientNumber", required = false) String clientNumber,
      @RequestParam(value = "timberMark", required = false) String timberMark,
      @ParameterObject Pageable paginationParameters) {
    OpeningSearchFiltersDto filtersDto =
        new OpeningSearchFiltersDto(
            orgUnit,
            category,
            statusList,
            myOpenings,
            submittedToFrpa,
            disturbanceDateStart,
            disturbanceDateEnd,
            regenDelayDateStart,
            regenDelayDateEnd,
            freeGrowingDateStart,
            freeGrowingDateEnd,
            updateDateStart,
            updateDateEnd,
            cuttingPermitId,
            cutBlockId,
            timberMark,
            clientLocationCode,
            clientNumber,
            mainSearchTerm);
    return openingSearchService.openingSearch(filtersDto, paginationParameters);
  }

  @GetMapping("/{openingId}/attachments")
  @PreAuthorize("@auth.isIdirUser()")
  public List<OpeningDetailsAttachmentMetaDto> getAttachments(@PathVariable Long openingId) {
    return openingService.getOpeningAttachments(openingId);
  }

  @GetMapping("/{openingId}/attachments/{guid}")
  @PreAuthorize("@auth.isIdirUser()")
  public ResponseEntity<byte[]> getAttachmentByGuid(
      @PathVariable("openingId") Long openingId, @PathVariable UUID guid) {
    OpeningAttachmentEntity attachment = openingService.getOpeningAttachmentContent(guid);

    return ResponseEntity.ok()
        .header(
            "Content-Disposition",
            "attachment; filename=\"" + attachment.getAttachmentName() + "\"")
        .header("Content-Type", MimeTypeResolver.resolve(attachment.getMimeTypeCode()))
        .body(attachment.getAttachmentData());
  }
}
