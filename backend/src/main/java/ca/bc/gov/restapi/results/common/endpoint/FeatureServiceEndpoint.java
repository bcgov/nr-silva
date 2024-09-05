package ca.bc.gov.restapi.results.common.endpoint;

import ca.bc.gov.restapi.results.common.service.RestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** This class holds resources for calling WFS. */
@RestController
@RequestMapping("/api/feature-service")
@AllArgsConstructor
@Tag(
    name = "Feature Service to call WFS (Common)",
    description = "Endpoints for handle WFS (Web Feature Service) within BC Geo Warehouse")
public class FeatureServiceEndpoint {

  private final RestService restService;

  /**
   * Fetch Opening data from WFS.
   *
   * @param openingId The Opening identification (id)
   * @return JSON object with response from WFS request
   */
  @GetMapping("/polygon-and-props/{openingId}")
  @Operation(
      summary = "Fetch Opening data from WFS",
      description =
          "Fetch Opening data (polygon raster data and properties) from WFS. These are the props "
              + "being fetched: OPENING_ID, GEOMETRY, REGION_NAME, REGION_CODE, DISTRICT_NAME, "
              + "DISTRICT_CODE, CLIENT_NAME, CLIENT_NUMBER, and OPENING_WHEN_CREATED",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "An object with the response from WFS request."),
        @ApiResponse(
            responseCode = "401",
            description = "Access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = Void.class)))
      })
  public Object getOpeningPolygonAndProperties(
      @Parameter(
              name = "openingId",
              in = ParameterIn.PATH,
              description = "The opening Id.",
              required = true)
          @PathVariable
          String openingId) {
    return restService.getOpeningPolygonAndProperties(openingId);
  }
}
