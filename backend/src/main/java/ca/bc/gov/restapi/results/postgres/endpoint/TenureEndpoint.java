package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.postgres.dto.TenureRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.service.TenureValidationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Tenure validation endpoint. */
@RestController
@RequestMapping(path = "/api/tenures", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class TenureEndpoint {

  private final TenureValidationService tenureValidationService;

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
  @PostMapping(value = "/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public TenureValidationResponseDto validateTenures(
      @RequestParam String clientNumber, @Valid @RequestBody List<TenureRequestDto> tenures) {
    return tenureValidationService.validateTenures(tenures, clientNumber);
  }
}
