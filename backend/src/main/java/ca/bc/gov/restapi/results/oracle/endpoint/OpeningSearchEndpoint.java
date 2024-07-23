package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginatedViaQuery;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.service.OpeningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** This class contains resources for the opening search api. */
@RestController
@RequestMapping("/api/opening-search")
@RequiredArgsConstructor
@Tag(
    name = "Search Openings (THE)",
    description = "Endpoints to handle the Opening Search feature in the `THE` schema.")
public class OpeningSearchEndpoint {

  private final OpeningService openingService;

  /**
   * Search for Openings with different filters.
   *
   * @param mainSearchTerm Number representing one of Opening ID | Opening Number | Timber Mark ID |
   *     File
   * @param orgUnit Org Unit code filter, same as District.
   * @param category Opening category code filter.
   * @param status Opening status code filter.
   * @param entryUserId Opening entry user id filter.
   * @param submittedToFrpa Submitted to FRPA
   * @param disturbanceDateStart Disturbance start date filter
   * @param disturbanceDateEnd Disturbance end date filter
   * @param regenDelayDateStart Regen start date filter
   * @param regenDelayDateEnd Regen end date filter
   * @param freeGrowingDateStart Free growing start date filter
   * @param freeGrowingDateEnd Free growing end date filter
   * @param updateDateStart Opening update start date filter
   * @param updateDateEnd Opening update end date filter
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
                      + " File ID. E.g.: Opening ID 1022833, Opening Number 1012, Timber Mark"
                      + " EM2184, File ID 407",
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
      @RequestParam(value = "status", required = false)
          @Parameter(
              name = "status",
              in = ParameterIn.QUERY,
              description = "Opening status code filter. E.g.: APP",
              required = false)
          String status,
      @RequestParam(value = "entryUserId", required = false)
          @Parameter(
              name = "entryUserId",
              in = ParameterIn.QUERY,
              description = "Opening entry user id filter",
              required = false)
          String entryUserId,
      @RequestParam(value = "submittedToFrpa", required = false)
          @Parameter(
              name = "submittedToFrpa",
              in = ParameterIn.QUERY,
              description = "Submitted to FRPA Section 108, true or false.",
              required = false,
              example = "submittedToFrpa")
          Boolean submittedToFrpa,
      @RequestParam(value = "disturbanceDateStart", required = false)
          @Parameter(
              name = "disturbanceDateStart",
              in = ParameterIn.QUERY,
              description = "Disturbance start date filter, format yyyy-MM-dd.",
              required = false)
          String disturbanceDateStart,
      @RequestParam(value = "disturbanceDateEnd", required = false)
          @Parameter(
              name = "disturbanceDateEnd",
              in = ParameterIn.QUERY,
              description = "Disturbance end date filter, format yyyy-MM-dd.",
              required = false)
          String disturbanceDateEnd,
      @RequestParam(value = "regenDelayDateStart", required = false)
          @Parameter(
              name = "regenDelayDateStart",
              in = ParameterIn.QUERY,
              description = "Regen delay start date filter, format yyyy-MM-dd.",
              required = false)
          String regenDelayDateStart,
      @RequestParam(value = "regenDelayDateEnd", required = false)
          @Parameter(
              name = "regenDelayDateEnd",
              in = ParameterIn.QUERY,
              description = "Regen delay end date filter, format yyyy-MM-dd.",
              required = false)
          String regenDelayDateEnd,
      @RequestParam(value = "freeGrowingDateStart", required = false)
          @Parameter(
              name = "freeGrowingDateStart",
              in = ParameterIn.QUERY,
              description = "Free growing start date filter, format yyyy-MM-dd.",
              required = false)
          String freeGrowingDateStart,
      @RequestParam(value = "freeGrowingDateEnd", required = false)
          @Parameter(
              name = "freeGrowingDateEnd",
              in = ParameterIn.QUERY,
              description = "Free growing end date filter, format yyyy-MM-dd.",
              required = false)
          String freeGrowingDateEnd,
      @RequestParam(value = "updateDateStart", required = false)
          @Parameter(
              name = "updateDateStart",
              in = ParameterIn.QUERY,
              description = "Opening update start date filter, format yyyy-MM-dd.",
              required = false)
          String updateDateStart,
      @RequestParam(value = "updateDateEnd", required = false)
          @Parameter(
              name = "updateDateEnd",
              in = ParameterIn.QUERY,
              description = "Opening update end date filter, format yyyy-MM-dd.",
              required = false)
          String updateDateEnd,
      @Valid PaginationParameters paginationParameters) {
    OpeningSearchFiltersDto filtersDto =
        new OpeningSearchFiltersDto(
            orgUnit,
            category,
            status,
            entryUserId,
            submittedToFrpa,
            disturbanceDateStart,
            disturbanceDateEnd,
            regenDelayDateStart,
            regenDelayDateEnd,
            freeGrowingDateStart,
            freeGrowingDateEnd,
            updateDateStart,
            updateDateEnd,
            mainSearchTerm);
    return openingService.openingSearch(filtersDto, paginationParameters);
  }
}
