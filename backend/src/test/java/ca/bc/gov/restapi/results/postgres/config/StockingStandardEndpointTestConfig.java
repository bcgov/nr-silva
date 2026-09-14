package ca.bc.gov.restapi.results.postgres.config;

import ca.bc.gov.restapi.results.postgres.service.CreateStockingStandardService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/** Test configuration for stocking-standard endpoint integration tests. */
@TestConfiguration
public class StockingStandardEndpointTestConfig {

  @Bean
  public CreateStockingStandardService createStockingStandardService() {
    return Mockito.mock(CreateStockingStandardService.class);
  }
}
