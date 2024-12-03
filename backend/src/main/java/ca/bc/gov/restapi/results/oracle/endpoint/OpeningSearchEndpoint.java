package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginatedViaQuery;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.oracle.service.OpenCategoryCodeService;
import ca.bc.gov.restapi.results.oracle.service.OpeningService;
import ca.bc.gov.restapi.results.oracle.service.OrgUnitService;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class contains resources for the opening search api.
 */
@RestController
@RequestMapping("/api/opening-search")
@RequiredArgsConstructor
public class OpeningSearchEndpoint {

  private final OpeningService openingService;

  private final OpenCategoryCodeService openCategoryCodeService;

  private final OrgUnitService orgUnitService;

  /**
   * Search for Openings with different filters.
   *
   * @param mainSearchTerm       Number representing one of Opening ID | Opening Number | Timber
   *                             Mark ID | File
   * @param orgUnit              Org Unit code filter, same as District.
   * @param category             Opening category code filter.
   * @param statusList           Opening statuses codes filter.
   * @param myOpenings           Openings created by the request user
   * @param submittedToFrpa      Submitted to FRPA
   * @param disturbanceDateStart Disturbance start date filter
   * @param disturbanceDateEnd   Disturbance end date filter
   * @param regenDelayDateStart  Regen start date filter
   * @param regenDelayDateEnd    Regen end date filter
   * @param freeGrowingDateStart Free growing start date filter
   * @param freeGrowingDateEnd   Free growing end date filter
   * @param updateDateStart      Opening update start date filter
   * @param updateDateEnd        Opening update end date filter
   * @param cuttingPermitId      The cutting permit identification filter
   * @param cutBlockId           Cute block identification filter
   * @param timberMark           Timber mark filter
   * @param paginationParameters Pagination settings
   * @return PaginatedResult with found records.
   */
  @GetMapping
  @PaginatedViaQuery
  public PaginatedResult<OpeningSearchResponseDto> openingSearch(
      @RequestParam(value = "mainSearchTerm", required = false)
      String mainSearchTerm,
      @RequestParam(value = "orgUnit", required = false)
      List<String> orgUnit,
      @RequestParam(value = "category", required = false)
      List<String> category,
      @RequestParam(value = "statusList", required = false)
      List<String> statusList,
      @RequestParam(value = "myOpenings", required = false)
      Boolean myOpenings,
      @RequestParam(value = "submittedToFrpa", required = false)
      Boolean submittedToFrpa,
      @RequestParam(value = "disturbanceDateStart", required = false)
      String disturbanceDateStart,
      @RequestParam(value = "disturbanceDateEnd", required = false)
      String disturbanceDateEnd,
      @RequestParam(value = "regenDelayDateStart", required = false)
      String regenDelayDateStart,
      @RequestParam(value = "regenDelayDateEnd", required = false)
      String regenDelayDateEnd,
      @RequestParam(value = "freeGrowingDateStart", required = false)
      String freeGrowingDateStart,
      @RequestParam(value = "freeGrowingDateEnd", required = false)
      String freeGrowingDateEnd,
      @RequestParam(value = "updateDateStart", required = false)
      String updateDateStart,
      @RequestParam(value = "updateDateEnd", required = false)
      String updateDateEnd,
      @RequestParam(value = "cuttingPermitId", required = false)
      String cuttingPermitId,
      @RequestParam(value = "cutBlockId", required = false)
      String cutBlockId,
      @RequestParam(value = "clientLocationCode", required = false)
      String clientLocationCode,
      @RequestParam(value = "timberMark", required = false)
      String timberMark,
      @Valid PaginationParameters paginationParameters) {
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
            mainSearchTerm);
    return openingService.openingSearch(filtersDto, paginationParameters);
  }

  /**
   * Get all opening categories. Optionally you can ask for the expired ones.
   *
   * @param includeExpired Query param to include expired categories.
   * @return List of {@link CodeDescriptionDto} with found categories.
   */
  @GetMapping("/categories")
  public List<CodeDescriptionDto> getOpeningCategories(
      @RequestParam(value = "includeExpired", required = false, defaultValue = "true")
      Boolean includeExpired) {
    boolean addExpired = Boolean.TRUE.equals(includeExpired);
    return openCategoryCodeService.findAllCategories(addExpired);
  }

  /**
   * Get the Org units list for the openings search API.
   *
   * @return List of OrgUnitEntity with found org units.
   */
  @GetMapping("/org-units")
  public List<OrgUnitEntity> getOpeningOrgUnits() {
    return orgUnitService.findAllOrgUnits();
  }

  /**
   * Get all org units by code.
   *
   * @param codes Org Unit codes to search for.
   * @return List of OrgUnitEntity with found org units.
   */
  @GetMapping("/org-units-by-code")
  public List<OrgUnitEntity> getOpeningOrgUnitsByCode(
      @RequestParam(value = "orgUnitCodes")
      String[] codes) {
    return orgUnitService.findAllOrgUnitsByCode(Arrays.asList(codes));
  }
}
