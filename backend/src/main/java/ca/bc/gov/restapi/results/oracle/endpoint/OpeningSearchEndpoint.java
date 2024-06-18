package ca.bc.gov.restapi.results.oracle.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/opening-search")
public class OpeningSearchEndpoint {

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
      // Org unit
      @RequestParam(value = "orgUnit", required = false)
          @Parameter(
              name = "orgUnit",
              in = ParameterIn.QUERY,
              description = "The Org Unit code to filter, same as District",
              required = false,
              example = "DCR")
          String orgUnitCode,
      // Category
      // Status
      @RequestParam(value = "status", required = false)
          @Parameter(
              name = "status",
              in = ParameterIn.QUERY,
              description = "The Openings Status code to filter",
              required = false,
              example = "APP")
          String statusCode,
      // User id
      // Submitted to RFPA yes or no
      // block status
      // disturbance date start
      @RequestParam(value = "clientNumber", required = false)
          @Parameter(
              name = "clientNumber",
              in = ParameterIn.QUERY,
              description = "The Client Number to filter",
              required = false,
              example = "00012797")
          String clientNumber,
      @RequestParam(value = "entryDateStart", required = false)
          @Parameter(
              name = "entryDateStart",
              in = ParameterIn.QUERY,
              description = "The Openings entry timestamp start date to filter, format yyyy-MM-dd",
              required = false,
              example = "2024-03-11")
          String entryDateStart,
      @RequestParam(value = "entryDateEnd", required = false)
          @Parameter(
              name = "entryDateEnd",
              in = ParameterIn.QUERY,
              description = "The Openings entry timestamp end date to filter, format yyyy-MM-dd",
              required = false,
              example = "2024-03-11")
          String entryDateEnd) {
    return null;
  }
}
