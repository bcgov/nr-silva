package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginatedViaQuery;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.entity.OpenCategoryCodeEntity;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.oracle.service.OpenCategoryCodeService;
import ca.bc.gov.restapi.results.oracle.service.OpeningService;
import ca.bc.gov.restapi.results.oracle.service.OrgUnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
@Tag(
    name = "Search Openings (THE)",
    description = "Endpoints to handle the Opening Search feature in the `THE` schema.")
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
  @Operation(
      summary = "Search for Openings",
      description = "Opening search feature with filters and pagination.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "An array with found objects, or an empty array.",
              content = @Content(mediaType = "application/json")),
          @ApiResponse(
              responseCode = "401",
              description = "Access token is missing or invalid",
              content = @Content(schema = @Schema(implementation = Void.class)))
      })
  @PaginatedViaQuery
  public PaginatedResult<OpeningSearchResponseDto> openingSearch(
      @RequestParam(value = "mainSearchTerm", required = false)
      @Parameter(
          name = "mainSearchTerm",
          in = ParameterIn.QUERY,
          description =
              "Search term representing one of Opening ID | Opening Number | Timber Mark ID |"
              + " Forest File ID. Eg: Opening ID 1022833, Opening Number 1012, Timber Mark"
              + " EM2184, Forest File ID TFL47",
          required = false)
      String mainSearchTerm,
      @RequestParam(value = "orgUnit", required = false)
      @Parameter(
          name = "orgUnit",
          in = ParameterIn.QUERY,
          description = "Org Unit code filter, same as District. E.g.: DCR, FTL47",
          required = false)
      String orgUnit,
      @RequestParam(value = "category", required = false)
      @Parameter(
          name = "category",
          in = ParameterIn.QUERY,
          description = "Opening category code filter. E.g.: FTML",
          required = false)
      String category,
      @RequestParam(value = "statusList", required = false)
      @Parameter(
          name = "statusList",
          in = ParameterIn.QUERY,
          description = "Opening status code filter. E.g.: APP",
          required = false)
      List<String> statusList,
      @RequestParam(value = "myOpenings", required = false)
      @Parameter(
          name = "myOpenings",
          in = ParameterIn.QUERY,
          description = "Openings created by the request user",
          required = false)
      Boolean myOpenings,
      @RequestParam(value = "submittedToFrpa", required = false)
      @Parameter(
          name = "submittedToFrpa",
          in = ParameterIn.QUERY,
          description = "Submitted to FRPA Section 108, true or false.",
          required = false)
      Boolean submittedToFrpa,
      @RequestParam(value = "disturbanceDateStart", required = false)
      @Parameter(
          name = "disturbanceDateStart",
          in = ParameterIn.QUERY,
          description = "Disturbance start date filter, format yyyy-MM-dd.",
          required = false)
      LocalDate disturbanceDateStart,
      @RequestParam(value = "disturbanceDateEnd", required = false)
      @Parameter(
          name = "disturbanceDateEnd",
          in = ParameterIn.QUERY,
          description = "Disturbance end date filter, format yyyy-MM-dd.",
          required = false)
      LocalDate disturbanceDateEnd,
      @RequestParam(value = "regenDelayDateStart", required = false)
      @Parameter(
          name = "regenDelayDateStart",
          in = ParameterIn.QUERY,
          description = "Regen delay start date filter, format yyyy-MM-dd.",
          required = false)
      LocalDate regenDelayDateStart,
      @RequestParam(value = "regenDelayDateEnd", required = false)
      @Parameter(
          name = "regenDelayDateEnd",
          in = ParameterIn.QUERY,
          description = "Regen delay end date filter, format yyyy-MM-dd.",
          required = false)
      LocalDate regenDelayDateEnd,
      @RequestParam(value = "freeGrowingDateStart", required = false)
      @Parameter(
          name = "freeGrowingDateStart",
          in = ParameterIn.QUERY,
          description = "Free growing start date filter, format yyyy-MM-dd.",
          required = false)
      LocalDate freeGrowingDateStart,
      @RequestParam(value = "freeGrowingDateEnd", required = false)
      @Parameter(
          name = "freeGrowingDateEnd",
          in = ParameterIn.QUERY,
          description = "Free growing end date filter, format yyyy-MM-dd.",
          required = false)
      LocalDate freeGrowingDateEnd,
      @RequestParam(value = "updateDateStart", required = false)
      @Parameter(
          name = "updateDateStart",
          in = ParameterIn.QUERY,
          description = "Opening update start date filter, format yyyy-MM-dd.",
          required = false)
      LocalDate updateDateStart,
      @RequestParam(value = "updateDateEnd", required = false)
      @Parameter(
          name = "updateDateEnd",
          in = ParameterIn.QUERY,
          description = "Opening update end date filter, format yyyy-MM-dd.",
          required = false)
      LocalDate updateDateEnd,
      @RequestParam(value = "cuttingPermitId", required = false)
      @Parameter(
          name = "cuttingPermitId",
          in = ParameterIn.QUERY,
          description =
              "Identifier for a cutting permit associated with a quota type harvesting tenure.",
          required = false)
      String cuttingPermitId,
      @RequestParam(value = "cutBlockId", required = false)
      @Parameter(
          name = "cutBlockId",
          in = ParameterIn.QUERY,
          description =
              "Identifier for a cut block of a harvesting tenure (within a cutting permit for"
              + " tenures with cp's).",
          required = false)
      String cutBlockId,
      @RequestParam(value = "timberMark", required = false)
      @Parameter(
          name = "timberMark",
          in = ParameterIn.QUERY,
          description =
              "Unique identifying set of characters to be stamped or marked on the end of each"
              + " log to associate the log with the specific authority to harvest and move"
              + " timber.",
          required = false)
      String timberMark,
      @Valid PaginationParameters paginationParameters) {
    OpeningSearchFiltersDto filtersDto =
        new OpeningSearchFiltersDto(
            orgUnit,
            category,
            statusList,
            myOpenings,
            submittedToFrpa,
            getLocalDateTime(disturbanceDateStart,LocalTime.MIN),
            getLocalDateTime(disturbanceDateEnd,LocalTime.MAX),
            getLocalDateTime(regenDelayDateStart,LocalTime.MIN),
            getLocalDateTime(regenDelayDateEnd,LocalTime.MAX),
            getLocalDateTime(freeGrowingDateStart,LocalTime.MIN),
            getLocalDateTime(freeGrowingDateEnd,LocalTime.MAX),
            getLocalDateTime(updateDateStart,LocalTime.MIN),
            getLocalDateTime(updateDateEnd,LocalTime.MAX),
            cuttingPermitId,
            cutBlockId,
            timberMark,
            mainSearchTerm);
    return openingService.openingSearch(filtersDto, paginationParameters);
  }

  /**
   * Get all opening categories. Optionally you can ask for the expired ones.
   *
   * @param includeExpired Query param to include expired categories.
   * @return List of OpenCategoryCodeEntity with found categories.
   */
  @GetMapping("/categories")
  @Operation(
      summary = "Get all opening categories",
      description = "Get all opening categories. Optionally you can ask for the expired ones.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "An array with found objects, or an empty array.",
              content = @Content(mediaType = "application/json")),
          @ApiResponse(
              responseCode = "401",
              description = "Access token is missing or invalid",
              content = @Content(schema = @Schema(implementation = Void.class)))
      })
  public List<OpenCategoryCodeEntity> getOpeningCategories(
      @RequestParam(value = "includeExpired", required = false)
      @Parameter(
          name = "includeExpired",
          in = ParameterIn.QUERY,
          description = "Defines if the API should include expired categories",
          required = false)
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
  @Operation(
      summary = "Get all opening org units",
      description = "Get all opening org units. Optionally you can ask for the expired ones.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "An array with found objects, or an empty array.",
              content = @Content(mediaType = "application/json")),
          @ApiResponse(
              responseCode = "401",
              description = "Access token is missing or invalid",
              content = @Content(schema = @Schema(implementation = Void.class)))
      })
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
  @Operation(
      summary = "Get all opening org units by code",
      description = "Get all opening org units by code.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "An array with found objects, or an empty array.",
              content = @Content(mediaType = "application/json")),
          @ApiResponse(
              responseCode = "401",
              description = "Access token is missing or invalid",
              content = @Content(schema = @Schema(implementation = Void.class)))
      })
  public List<OrgUnitEntity> getOpeningOrgUnitsByCode(
      @RequestParam(value = "orgUnitCodes", required = true)
      @Parameter(
          name = "orgUnitCodes",
          in = ParameterIn.QUERY,
          description = "Defines the org units that should be included in the search",
          required = false)
      String[] codes) {
    return orgUnitService.findAllOrgUnitsByCode(codes);
  }

  private LocalDateTime getLocalDateTime(LocalDate date, LocalTime time) {
    return date != null ? date.atTime(time) : null;
  }
}
