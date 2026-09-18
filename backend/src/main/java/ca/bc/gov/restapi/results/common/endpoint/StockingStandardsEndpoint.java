package ca.bc.gov.restapi.results.common.endpoint;

import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardDetailsDto;
import ca.bc.gov.restapi.results.common.exception.NotFoundGenericException;
import ca.bc.gov.restapi.results.common.service.StockingStandardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Common read endpoints for stocking standards. */
@RestController("commonStockingStandardsEndpoint")
@RequestMapping(
    path = "/api/stocking-standards",
    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
@RequiredArgsConstructor
public class StockingStandardsEndpoint {

  private final StockingStandardsService stockingStandardsService;

  /** Gets a stocking standard by its identifier. */
  @GetMapping("/{stockingStandardId}")
  public StockingStandardDetailsDto getStockingStandard(
      @PathVariable Long stockingStandardId) {
    return stockingStandardsService
        .getStockingStandardDetails(stockingStandardId)
        .orElseThrow(() -> new NotFoundGenericException("Stocking standard"));
  }
}
