package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import ca.bc.gov.restapi.results.common.clamav.VirusScanService;
import ca.bc.gov.restapi.results.postgres.service.SpatialFileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/** Verifies database-mode isolation without starting either database. */
@DisplayName("Unit Test | ActivityEndpoint availability")
class ActivityEndpointAvailabilityTest {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withUserConfiguration(ActivityEndpoint.class)
          .withBean(SpatialFileService.class, () -> mock(SpatialFileService.class))
          .withBean(VirusScanService.class, () -> mock(VirusScanService.class));

  @Test
  void registersInPostgresMode() {
    contextRunner
        .withPropertyValues("server.primary-db=postgres")
        .run(context -> assertThat(context).hasSingleBean(ActivityEndpoint.class));
  }

  @Test
  void doesNotRegisterInOracleMode() {
    contextRunner
        .withPropertyValues("server.primary-db=oracle")
        .run(context -> assertThat(context).doesNotHaveBean(ActivityEndpoint.class));
  }

  @Test
  void doesNotRegisterWithoutAnExplicitPostgresMode() {
    contextRunner
        .withPropertyValues("server.primary-db=")
        .run(context -> assertThat(context).doesNotHaveBean(ActivityEndpoint.class));
  }
}
