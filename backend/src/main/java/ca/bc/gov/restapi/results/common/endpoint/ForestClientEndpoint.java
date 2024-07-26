package ca.bc.gov.restapi.results.common.endpoint;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.exception.ForestClientNotFoundException;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
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

/** This class holds resources for the Forest Client API interaction. */
@RestController
@RequestMapping("/api/forest-clients")
@AllArgsConstructor
@Tag(
    name = "Forest Client (Common)",
    description = "Endpoints for fetching client information from Forest Client API")
public class ForestClientEndpoint {

  private final ForestClientService forestClientService;

  /**
   * Get a {@link ForestClientDto} given a client number.
   *
   * @param clientNumber The client number to be fetched.
   * @return ForestsClientDto
   * @throws ForestClientNotFoundException when client not found
   */
  @GetMapping("/{clientNumber}")
  @Operation(
      summary = "Fetch a forest client given its number.",
      description =
          "Fetch a forest client directly from the ForestClient API given the client number.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "An object with all client properties provided by the ForestClient API."),
        @ApiResponse(
            responseCode = "400",
            description = "Something wrong with the client number, make sure you're sending it."),
        @ApiResponse(
            responseCode = "401",
            description = "Access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = Void.class)))
      })
  public ForestClientDto getForestClient(
      @Parameter(
              name = "clientNumber",
              in = ParameterIn.PATH,
              description = "The client number to be fetched.",
              required = true)
          @PathVariable
          String clientNumber) {
    return forestClientService
        .getClientByNumber(clientNumber)
        .orElseThrow(ForestClientNotFoundException::new);
  }
}
