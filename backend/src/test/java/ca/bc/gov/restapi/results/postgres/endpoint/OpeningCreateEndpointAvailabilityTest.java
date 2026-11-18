package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import ca.bc.gov.restapi.results.common.clamav.VirusScanService;
import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.common.service.OpenMapsService;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTreatmentUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.CutBlockOpenAdminPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpenCategoryCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningGeometryPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OrgUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.service.CreateOpeningService;
import ca.bc.gov.restapi.results.postgres.service.OpeningTenureAssociationHistoryService;
import ca.bc.gov.restapi.results.postgres.service.OpeningTenureAssociationService;
import ca.bc.gov.restapi.results.postgres.service.SpatialFileService;
import ca.bc.gov.restapi.results.postgres.service.TenureValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.jdbc.core.JdbcTemplate;

/** Verifies database-mode isolation for opening creation and tenure association components. */
@DisplayName("Unit Test | OpeningCreateEndpoint availability")
class OpeningCreateEndpointAvailabilityTest {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withUserConfiguration(
              OpeningCreateEndpoint.class,
              CreateOpeningService.class,
              OpeningTenureAssociationService.class)
          .withBean(SpatialFileService.class, () -> mock(SpatialFileService.class))
          .withBean(VirusScanService.class, () -> mock(VirusScanService.class))
          .withBean(OpenMapsService.class, () -> mock(OpenMapsService.class))
          .withBean(OpeningPostgresRepository.class, () -> mock(OpeningPostgresRepository.class))
          .withBean(
              OpeningGeometryPostgresRepository.class,
              () -> mock(OpeningGeometryPostgresRepository.class))
          .withBean(
              OpenCategoryCodePostgresRepository.class,
              () -> mock(OpenCategoryCodePostgresRepository.class))
          .withBean(OrgUnitPostgresRepository.class, () -> mock(OrgUnitPostgresRepository.class))
          .withBean(TenureValidationService.class, () -> mock(TenureValidationService.class))
          .withBean(
              OpeningTenureAssociationHistoryService.class,
              () -> mock(OpeningTenureAssociationHistoryService.class))
          .withBean(
              CutBlockOpenAdminPostgresRepository.class,
              () -> mock(CutBlockOpenAdminPostgresRepository.class))
          .withBean(
              ActivityTreatmentUnitPostgresRepository.class,
              () -> mock(ActivityTreatmentUnitPostgresRepository.class))
          .withBean(LoggedUserHelper.class, () -> mock(LoggedUserHelper.class))
          .withBean(JdbcTemplate.class, () -> mock(JdbcTemplate.class))
          .withBean(ObjectMapper.class, ObjectMapper::new)
          .withBean(Validator.class, () -> mock(Validator.class));

  @Test
  void registersInPostgresMode() {
    contextRunner
        .withPropertyValues("server.primary-db=postgres")
        .run(
            context -> {
              assertThat(context).hasSingleBean(OpeningCreateEndpoint.class);
              assertThat(context).hasSingleBean(CreateOpeningService.class);
              assertThat(context).hasSingleBean(OpeningTenureAssociationService.class);
            });
  }

  @Test
  void doesNotRegisterInOracleMode() {
    contextRunner
        .withPropertyValues("server.primary-db=oracle")
        .run(
            context -> {
              assertThat(context).doesNotHaveBean(OpeningCreateEndpoint.class);
              assertThat(context).doesNotHaveBean(CreateOpeningService.class);
              assertThat(context).doesNotHaveBean(OpeningTenureAssociationService.class);
            });
  }

  @Test
  void doesNotRegisterWithoutAnExplicitPostgresMode() {
    contextRunner
        .withPropertyValues("server.primary-db=")
        .run(
            context -> {
              assertThat(context).doesNotHaveBean(OpeningCreateEndpoint.class);
              assertThat(context).doesNotHaveBean(CreateOpeningService.class);
              assertThat(context).doesNotHaveBean(OpeningTenureAssociationService.class);
            });
  }
}
