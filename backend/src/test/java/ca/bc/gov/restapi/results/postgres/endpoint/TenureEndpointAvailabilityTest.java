package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import ca.bc.gov.restapi.results.common.provider.TenureProvider;
import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTreatmentUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.CutBlockOpenAdminPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningPostgresRepository;
import ca.bc.gov.restapi.results.postgres.security.OperationAccessPolicy;
import ca.bc.gov.restapi.results.postgres.service.OpeningTenureAssociationHistoryService;
import ca.bc.gov.restapi.results.postgres.service.OpeningTenureAssociationService;
import ca.bc.gov.restapi.results.postgres.service.TenureValidationService;
import ca.bc.gov.restapi.results.postgres.service.UpdateTenuresService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/** Verifies database-mode isolation for tenure validation and update components. */
@DisplayName("Unit Test | TenureEndpoint availability")
class TenureEndpointAvailabilityTest {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withUserConfiguration(
              TenureEndpoint.class,
              TenureValidationService.class,
              UpdateTenuresService.class)
          .withBean(OperationAccessPolicy.class, () -> mock(OperationAccessPolicy.class))
          .withBean(TenureProvider.class, () -> mock(TenureProvider.class))
          .withBean(
              CutBlockOpenAdminPostgresRepository.class,
              () -> mock(CutBlockOpenAdminPostgresRepository.class))
          .withBean(OpeningPostgresRepository.class, () -> mock(OpeningPostgresRepository.class))
          .withBean(
              ActivityTreatmentUnitPostgresRepository.class,
              () -> mock(ActivityTreatmentUnitPostgresRepository.class))
          .withBean(
              OpeningTenureAssociationService.class,
              () -> mock(OpeningTenureAssociationService.class))
          .withBean(
              OpeningTenureAssociationHistoryService.class,
              () -> mock(OpeningTenureAssociationHistoryService.class))
          .withBean(LoggedUserHelper.class, () -> mock(LoggedUserHelper.class));

  @Test
  void registersInPostgresMode() {
    contextRunner
        .withPropertyValues("server.primary-db=postgres")
        .run(
            context -> {
              assertThat(context).hasSingleBean(TenureEndpoint.class);
              assertThat(context).hasSingleBean(TenureValidationService.class);
              assertThat(context).hasSingleBean(UpdateTenuresService.class);
            });
  }

  @Test
  void doesNotRegisterInOracleMode() {
    contextRunner
        .withPropertyValues("server.primary-db=oracle")
        .run(
            context -> {
              assertThat(context).doesNotHaveBean(TenureEndpoint.class);
              assertThat(context).doesNotHaveBean(TenureValidationService.class);
              assertThat(context).doesNotHaveBean(UpdateTenuresService.class);
            });
  }

  @Test
  void doesNotRegisterWithoutAnExplicitPostgresMode() {
    contextRunner
        .withPropertyValues("server.primary-db=")
        .run(
            context -> {
              assertThat(context).doesNotHaveBean(TenureEndpoint.class);
              assertThat(context).doesNotHaveBean(TenureValidationService.class);
              assertThat(context).doesNotHaveBean(UpdateTenuresService.class);
            });
  }
}
