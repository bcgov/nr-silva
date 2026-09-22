package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.postgres.dto.BecDataDto;
import ca.bc.gov.restapi.results.postgres.dto.BecValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingSpeciesLayerValidationRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingSpeciesValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.service.StockingStandardBecValidationService;
import ca.bc.gov.restapi.results.postgres.service.StockingStandardSpeciesValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Validation endpoints for the PostgreSQL stocking-standard workflow. */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class StockingStandardValidationEndpoint {

  private final StockingStandardBecValidationService becValidationService;
  private final StockingStandardSpeciesValidationService speciesValidationService;

  /** Validates BEC combinations before stocking-standard submission. */
  @PostMapping(
      value = "/api/stocking-standards/validate/bec", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Validate stocking-standard BEC combinations")
  @ApiResponse(
      responseCode = "200",
      description = "Per-entry BEC validation results and duplicate conflicts",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = BecValidationResponseDto.class)))
  public BecValidationResponseDto validateBec(@RequestBody List<BecDataDto> becData) {
    return becValidationService.validate(becData);
  }

  /** Validates layer-aware species entries before stocking-standard submission. */
  @PostMapping(
      value = "/api/stocking-standards/validate/species", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Validate stocking-standard species entries")
  @ApiResponse(
      responseCode = "200",
      description = "Layer-aware species validation results and duplicate conflicts",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = StockingSpeciesValidationResponseDto.class)))
  public StockingSpeciesValidationResponseDto validateSpecies(
      @RequestBody List<StockingSpeciesLayerValidationRequestDto> layers) {
    return speciesValidationService.validate(layers);
  }
}
