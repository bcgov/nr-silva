package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.service.impl.AbstractStockingStandardsService;
import ca.bc.gov.restapi.results.oracle.repository.StockingStandardsOracleRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
@Service
public class StockingStandardsOracleService extends AbstractStockingStandardsService {

  public StockingStandardsOracleService(
      StockingStandardsOracleRepository stockingStandardsRepository,
      ForestClientService forestClientService) {
    super(stockingStandardsRepository, forestClientService);
  }
}
