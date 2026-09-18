package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardDetailsDto;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.service.impl.AbstractStockingStandardsService;
import ca.bc.gov.restapi.results.oracle.repository.StockingStandardDetailsOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.StockingStandardsOracleRepository;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
@Service
public class StockingStandardsOracleService extends AbstractStockingStandardsService {

  public StockingStandardsOracleService(
      StockingStandardsOracleRepository stockingStandardsRepository,
      StockingStandardDetailsOracleRepository stockingStandardDetailsRepository,
      ForestClientService forestClientService) {
    super(stockingStandardsRepository, forestClientService);
    this.stockingStandardDetailsRepository = stockingStandardDetailsRepository;
  }

  private final StockingStandardDetailsOracleRepository stockingStandardDetailsRepository;

  @Override
  public Optional<StockingStandardDetailsDto> getStockingStandardDetails(Long stockingStandardId) {
    return stockingStandardDetailsRepository
        .findStockingStandardDetails(stockingStandardId)
        .map(
            header ->
                mapStockingStandardDetails(
                    header,
                    stockingStandardDetailsRepository.findStockingStandardClientNumbers(
                        stockingStandardId),
                    stockingStandardDetailsRepository.findStockingStandardOrgUnits(stockingStandardId),
                    stockingStandardDetailsRepository.findStockingStandardBecData(stockingStandardId),
                    stockingStandardDetailsRepository.findStockingStandardLayers(stockingStandardId),
                    stockingStandardDetailsRepository.findStockingStandardSpecies(stockingStandardId)));
  }
}
