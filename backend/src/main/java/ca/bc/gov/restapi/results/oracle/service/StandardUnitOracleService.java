package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.service.impl.AbstractStandardUnitService;
import ca.bc.gov.restapi.results.oracle.repository.StockingStandardUnitOracleRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
@Service
public class StandardUnitOracleService extends AbstractStandardUnitService {

  public StandardUnitOracleService(
      StockingStandardUnitOracleRepository standardUnitRepository,
      ForestClientService forestClientService) {
    super(standardUnitRepository, forestClientService);
  }
}
