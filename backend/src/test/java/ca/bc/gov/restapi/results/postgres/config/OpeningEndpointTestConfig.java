package ca.bc.gov.restapi.results.postgres.config;

import ca.bc.gov.restapi.results.common.clamav.VirusScanService;
import ca.bc.gov.restapi.results.postgres.service.CreateOpeningService;
import ca.bc.gov.restapi.results.postgres.service.OpeningSpatialFileService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/** Shared test configuration for Opening endpoint integration tests */
@TestConfiguration
public class OpeningEndpointTestConfig {

  @Bean
  public VirusScanService virusScanService() {
    return Mockito.mock(VirusScanService.class);
  }

  @Bean
  public CreateOpeningService createOpeningService() {
    return Mockito.mock(CreateOpeningService.class);
  }

  @Bean
  public OpeningSpatialFileService openingSpatialFileService() {
    return Mockito.mock(OpeningSpatialFileService.class);
  }
}
