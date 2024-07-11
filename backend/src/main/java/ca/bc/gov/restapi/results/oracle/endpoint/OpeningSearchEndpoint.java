package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.common.util.TimestampUtil;
import ca.bc.gov.restapi.results.oracle.dto.SearchOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.SearchOpeningFiltersDto;
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

@RestController
@RequestMapping("/api/opening-search")
@RequiredArgsConstructor
@Tag(
    name = "Search Openings (THE)",
    description = "Endpoints to handle the Opening Search feature in the `THE` schema.")
public class OpeningSearchEndpoint {

  private final OpeningService openingService;

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
  public PaginatedResult<SearchOpeningDto> openingSearch(
      @RequestParam(value = "number", required = false)
          @Parameter(
              name = "number",
              in = ParameterIn.QUERY,
              description =
                  "Number representing one of Opening ID | Opening Number | Timber Mark ID | File"
                      + " ID. E.g.: Opening ID 1022833, Opening Number 1012, Timber Mark EM2184"
                      + ", File ID 407",
              required = false)
          String number,
      // Org unit - ok
      @RequestParam(value = "orgUnit", required = false)
          @Parameter(
              name = "orgUnit",
              in = ParameterIn.QUERY,
              description = "Org Unit code filter, same as District. E.g.: DCR, FTL47",
              required = false)
          String orgUnit,
      // Category - ok
      @RequestParam(value = "category", required = false)
          @Parameter(
              name = "category",
              in = ParameterIn.QUERY,
              description = "Opening category code filter. E.g.: FTML",
              required = false)
          String category,
      // Status - ok
      @RequestParam(value = "status", required = false)
          @Parameter(
              name = "status",
              in = ParameterIn.QUERY,
              description = "Opening status code filter. E.g.: APP",
              required = false)
          String status,
      // User id - ok
      @RequestParam(value = "userId", required = false)
          @Parameter(
              name = "userId",
              in = ParameterIn.QUERY,
              description = "Opening entry user id filter",
              required = false)
          String userId,
      // Submitted to FRPA Y or N - ok
      @RequestParam(value = "submittedToFrpa", required = false)
          @Parameter(
              name = "submittedToFrpa",
              in = ParameterIn.QUERY,
              description = "Submitted to FRPA, true or false.",
              required = false,
              example = "submittedToFrpa")
          Boolean submittedToFrpa,
      // disturbance date start - ok
      @RequestParam(value = "disturbanceDateStart", required = false)
          @Parameter(
              name = "disturbanceDateStart",
              in = ParameterIn.QUERY,
              description = "Disturbance start date filter, format yyyy-MM-dd.",
              required = false)
          String disturbanceDateStart,
      // disturbance date end - ok
      @RequestParam(value = "disturbanceDateEnd", required = false)
          @Parameter(
              name = "disturbanceDateEnd",
              in = ParameterIn.QUERY,
              description = "Disturbance end date filter, format yyyy-MM-dd.",
              required = false)
          String disturbanceDateEnd,
      // Regen delay date start
      @RequestParam(value = "regenDelayDateStart", required = false)
          @Parameter(
              name = "regenDelayDateStart",
              in = ParameterIn.QUERY,
              description = "Regen delay start date filter, format yyyy-MM-dd.",
              required = false)
          String regenDelayDateStart,
      // Regen delay date end
      @RequestParam(value = "regenDelayDateEnd", required = false)
          @Parameter(
              name = "regenDelayDateEnd",
              in = ParameterIn.QUERY,
              description = "Regen delay end date filter, format yyyy-MM-dd.",
              required = false)
          String regenDelayDateEnd,
      // Free growing date start
      @RequestParam(value = "freeGrowingDateStart", required = false)
          @Parameter(
              name = "freeGrowingDateStart",
              in = ParameterIn.QUERY,
              description = "Free growing start date filter, format yyyy-MM-dd.",
              required = false)
          String freeGrowingDateStart,
      // Free growing date end
      @RequestParam(value = "freeGrowingDateEnd", required = false)
          @Parameter(
              name = "freeGrowingDateEnd",
              in = ParameterIn.QUERY,
              description = "Free growing end date filter, format yyyy-MM-dd.",
              required = false)
          String freeGrowingDateEnd,
      // Update date start
      @RequestParam(value = "updateDateStart", required = false)
          @Parameter(
              name = "updateDateStart",
              in = ParameterIn.QUERY,
              description = "Opening update start date filter, format yyyy-MM-dd.",
              required = false)
          String updateDateStart,
      // Update date end
      @RequestParam(value = "updateDateEnd", required = false)
          @Parameter(
              name = "updateDateEnd",
              in = ParameterIn.QUERY,
              description = "Opening update end date filter, format yyyy-MM-dd.",
              required = false)
          String updateDateEnd,
      @Valid PaginationParameters paginationParameters) {
    SearchOpeningFiltersDto filtersDto =
        new SearchOpeningFiltersDto(
            orgUnit,
            category,
            status,
            userId,
            submittedToFrpa,
            TimestampUtil.parseDateString(disturbanceDateStart),
            TimestampUtil.parseDateString(disturbanceDateEnd),
            TimestampUtil.parseDateString(regenDelayDateStart),
            TimestampUtil.parseDateString(regenDelayDateEnd),
            TimestampUtil.parseDateString(freeGrowingDateStart),
            TimestampUtil.parseDateString(freeGrowingDateEnd),
            TimestampUtil.parseDateString(updateDateStart),
            TimestampUtil.parseDateString(updateDateEnd));

    // Pagination
    return openingService.searchOpening(filtersDto, paginationParameters, number);
  }
}
