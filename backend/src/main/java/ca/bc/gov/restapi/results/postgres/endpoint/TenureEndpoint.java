package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.postgres.dto.TenureRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureUpdateItemDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureUpdateValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.service.TenureValidationService;
import ca.bc.gov.restapi.results.postgres.service.UpdateTenuresService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Tenure validation endpoint. */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class TenureEndpoint {

  private final TenureValidationService tenureValidationService;
  private final UpdateTenuresService updateTenuresService;

  /**
   * Validate a list of tenures.
   *
   * <p>This endpoint is invoked when a user clicks the next step on a form containing tenure data.
   * It validates each tenure individually and detects duplicate tenure combinations (same fileId +
   * cuttingPermit + cutBlock).
   *
   * @param tenures the list of tenures to validate
   * @return a {@link TenureValidationResponseDto} containing per-tenure validation results and any
   *     detected duplicates
   */
  @PostMapping(value = "/api/tenures/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public TenureValidationResponseDto validateTenures(
      @RequestParam String clientNumber,
      @Valid @NotNull @RequestBody List<TenureRequestDto> tenures) {
    return tenureValidationService.validateTenures(tenures, clientNumber);
  }

  /**
   * Replaces an opening's complete tenure list.
   *
   * @param openingId opening whose tenures are replaced
   * @param clientNumber client used for authorization and licensee validation
   * @param tenures final tenure list, including CBOA identity for existing rows
   * @return {@code 204} after persistence or a structured {@code 422} validation response
   */
  @PutMapping(value = "/api/openings/{openingId}/tenures", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TenureUpdateValidationResponseDto> updateTenures(
      @PathVariable Long openingId,
      @RequestParam String clientNumber,
      @Valid @NotNull @RequestBody List<TenureUpdateItemDto> tenures) {
    return updateTenuresService
        .updateTenures(openingId, clientNumber, tenures)
        .map(response -> ResponseEntity.unprocessableEntity().body(response))
        .orElseGet(() -> ResponseEntity.noContent().build());
  }
}
