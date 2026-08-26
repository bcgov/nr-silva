package ca.bc.gov.restapi.results.postgres.config;

import ca.bc.gov.restapi.results.postgres.service.TenureValidationService;
import ca.bc.gov.restapi.results.postgres.service.UpdateTenuresService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/** Shared test configuration for Tenure endpoint integration tests. */
@TestConfiguration
public class TenureEndpointTestConfig {

  @Bean
  public TenureValidationService tenureValidationService() {
    return Mockito.mock(TenureValidationService.class);
  }

  @Bean
  public UpdateTenuresService updateTenuresService() {
    return Mockito.mock(UpdateTenuresService.class);
  }
}
