package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.postgres.dto.CreateStockingStandardRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.CreateStockingStandardResponseDto;
import ca.bc.gov.restapi.results.postgres.service.CreateStockingStandardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Create-stocking-standard endpoint — postgres-only mode. */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class StockingStandardEndpoint {

  private final CreateStockingStandardService createStockingStandardService;

  /**
   * Creates a new Stocking Standard.
   *
   * @param dto the stocking standard creation request
   * @return a {@link CreateStockingStandardResponseDto} containing the new standard's ID
   */
  @PostMapping(value = "/api/stocking-standards", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public CreateStockingStandardResponseDto createStockingStandard(
      @Valid @RequestBody CreateStockingStandardRequestDto dto) {
    return createStockingStandardService.create(dto);
  }
}
