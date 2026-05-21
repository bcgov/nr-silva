package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.service.impl.AbstractStockingStandardsService;
import ca.bc.gov.restapi.results.postgres.repository.StockingStandardsPostgresRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class StockingStandardsPostgresService extends AbstractStockingStandardsService {

  public StockingStandardsPostgresService(
      StockingStandardsPostgresRepository stockingStandardsRepository,
      ForestClientService forestClientService) {
    super(stockingStandardsRepository, forestClientService);
  }
}
