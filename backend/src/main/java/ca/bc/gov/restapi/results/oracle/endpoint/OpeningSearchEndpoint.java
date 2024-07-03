package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.common.util.TimestampUtil;
import ca.bc.gov.restapi.results.oracle.dto.SearchOpeningFiltersDto;
import ca.bc.gov.restapi.results.oracle.service.OpeningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/opening-search")
@RequiredArgsConstructor
public class OpeningSearchEndpoint {

  private final OpeningService openingService;

  @GetMapping("/{number}")
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
  public Object openingSearch(
      @PathVariable
          @Parameter(
              name = "number",
              in = ParameterIn.PATH,
              description =
                  "Number representing one of Opening ID | Opening Number | Timber Mark ID | File"
                      + " ID",
              required = false,
              example = "DCR")
          String number,
      // Org unit - ok
      @RequestParam(value = "orgUnit", required = false)
          @Parameter(
              name = "orgUnit",
              in = ParameterIn.QUERY,
              description = "The Org Unit code to filter, same as District",
              required = false,
              example = "DCR")
          String orgUnit,
      // Category - ok
      @RequestParam(value = "category", required = false)
          @Parameter(
              name = "category",
              in = ParameterIn.QUERY,
              description = "The Opening category code",
              required = false,
              example = "FTML")
          String category,
      // Status - ok
      @RequestParam(value = "status", required = false)
          @Parameter(
              name = "status",
              in = ParameterIn.QUERY,
              description = "The Openings Status code to filter",
              required = false,
              example = "APP")
          String status,
      // User id - ok
      @RequestParam(value = "userId", required = false)
          @Parameter(
              name = "userId",
              in = ParameterIn.QUERY,
              description = "The Opening entry user id to filter",
              required = false,
              example = "USERIDIR")
          String userId,
      // Submitted to FRPA Y or N - ok
      @RequestParam(value = "submittedToFrpa", required = false)
          @Parameter(
              name = "submittedToFrpa",
              in = ParameterIn.QUERY,
              description = "Submitted to RFPA, send Y or N ",
              required = false,
              example = "submittedToFrpa")
          String submittedToFrpa,
      // block status !?
      // disturbance date start - ok
      @RequestParam(value = "disturbanceDateStart", required = false)
          @Parameter(
              name = "disturbanceDateStart",
              in = ParameterIn.QUERY,
              description = "Disturbance start date to filter, format yyyy-MM-dd",
              required = false,
              example = "2024-06-25")
          String disturbanceDateStart,
      // disturbance date end - ok
      @RequestParam(value = "disturbanceDateEnd", required = false)
          @Parameter(
              name = "disturbanceDateEnd",
              in = ParameterIn.QUERY,
              description = "Disturbance end date to filter, format yyyy-MM-dd",
              required = false,
              example = "2024-06-25")
          String disturbanceDateEnd,
      // Regen delay date start
      @RequestParam(value = "regenDelayDateStart", required = false)
          @Parameter(
              name = "regenDelayDateStart",
              in = ParameterIn.QUERY,
              description = "Regen delay start date to filter, format yyyy-MM-dd",
              required = false,
              example = "2024-03-11")
          String regenDelayDateStart,
      // Regen delay date end
      @RequestParam(value = "regenDelayDateEnd", required = false)
          @Parameter(
              name = "regenDelayDateEnd",
              in = ParameterIn.QUERY,
              description = "Regen delay start date to filter, format yyyy-MM-dd",
              required = false,
              example = "2024-03-11")
          String regenDelayDateEnd,
      // Free growing date start
      @RequestParam(value = "freeGrowingDateStart", required = false)
          @Parameter(
              name = "freeGrowingDateStart",
              in = ParameterIn.QUERY,
              description = "Free growing start date to filter, format yyyy-MM-dd",
              required = false,
              example = "2024-03-11")
          String freeGrowingDateStart,
      // Free growing date end
      @RequestParam(value = "freeGrowingDateEnd", required = false)
          @Parameter(
              name = "freeGrowingDateEnd",
              in = ParameterIn.QUERY,
              description = "Free growing end date to filter, format yyyy-MM-dd",
              required = false,
              example = "2024-03-11")
          String freeGrowingDateEnd,
      // Update date start
      @RequestParam(value = "updateDateStart", required = false)
          @Parameter(
              name = "updateDateStart",
              in = ParameterIn.QUERY,
              description = "Opening update start date to filter, format yyyy-MM-dd",
              required = false,
              example = "2024-03-11")
          String updateDateStart,
      // Update date end
      @RequestParam(value = "updateDateEnd", required = false)
          @Parameter(
              name = "updateDateEnd",
              in = ParameterIn.QUERY,
              description = "Opening update end date to filter, format yyyy-MM-dd",
              required = false,
              example = "2024-03-11")
          String updateDateEnd,
      @Valid PaginationParameters paginationParameters) {
    SearchOpeningFiltersDto filtersDto =
        new SearchOpeningFiltersDto(
            orgUnit,
            category,
            status,
            userId,
            "Y".equals(submittedToFrpa),
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
