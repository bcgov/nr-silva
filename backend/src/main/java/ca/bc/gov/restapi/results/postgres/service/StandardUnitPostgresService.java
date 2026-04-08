package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.service.impl.AbstractStandardUnitService;
import ca.bc.gov.restapi.results.postgres.repository.StockingStandardUnitPostgresRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class StandardUnitPostgresService extends AbstractStandardUnitService {

  public StandardUnitPostgresService(
      StockingStandardUnitPostgresRepository standardUnitRepository,
      ForestClientService forestClientService) {
    super(standardUnitRepository, forestClientService);
  }
}
