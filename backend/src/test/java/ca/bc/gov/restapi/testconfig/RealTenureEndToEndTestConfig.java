package ca.bc.gov.restapi.testconfig;

import ca.bc.gov.restapi.results.common.provider.TenureProvider;
import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.common.service.OpenMapsService;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTreatmentUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.CutBlockOpenAdminPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpenCategoryCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningGeometryPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OrgUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.security.OperationAccessPolicy;
import ca.bc.gov.restapi.results.postgres.service.CreateOpeningService;
import ca.bc.gov.restapi.results.postgres.service.OpeningTenureAssociationHistoryService;
import ca.bc.gov.restapi.results.postgres.service.OpeningTenureAssociationService;
import ca.bc.gov.restapi.results.postgres.service.SpatialFileService;
import ca.bc.gov.restapi.results.postgres.service.TenureValidationService;
import ca.bc.gov.restapi.results.postgres.service.UpdateTenuresService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Provides real service beans for end-to-end tenure integration tests, overriding the global
 * Mockito mocks registered by {@code TenureEndpointTestConfig} and {@code
 * OpeningEndpointTestConfig} under {@code ResultsApplication}'s component scan.
 */
@TestConfiguration
public class RealTenureEndToEndTestConfig {

  /** Creates the real {@link SpatialFileService} bean for E2E tests. */
  @Bean
  @Primary
  public SpatialFileService realSpatialFileService() {
    return new SpatialFileService();
  }

  /** Creates the real {@link TenureValidationService} bean backed by {@link TenureProvider}. */
  @Bean
  @Primary
  public TenureValidationService realTenureValidationService(
      OperationAccessPolicy accessPolicy,
      TenureProvider tenureProvider,
      CutBlockOpenAdminPostgresRepository cutBlockOpenAdminRepository) {
    return new TenureValidationService(accessPolicy, tenureProvider, cutBlockOpenAdminRepository);
  }

  /** Creates the real {@link UpdateTenuresService} bean for E2E tests. */
  @Bean
  @Primary
  public UpdateTenuresService realUpdateTenuresService(
      OpeningPostgresRepository openingRepository,
      CutBlockOpenAdminPostgresRepository cboaRepository,
      ActivityTreatmentUnitPostgresRepository activityRepository,
      TenureValidationService realTenureValidationService,
      OpeningTenureAssociationService tenureAssociationService,
      OpeningTenureAssociationHistoryService historyService,
      LoggedUserHelper loggedUserHelper) {
    return new UpdateTenuresService(
        openingRepository,
        cboaRepository,
        activityRepository,
        realTenureValidationService,
        tenureAssociationService,
        historyService,
        loggedUserHelper);
  }

  /** Creates the real {@link CreateOpeningService} bean for E2E tests. */
  @Bean
  @Primary
  public CreateOpeningService realCreateOpeningService(
      SpatialFileService realSpatialFileService,
      OpenMapsService openMapsService,
      OpeningPostgresRepository openingRepository,
      OpeningGeometryPostgresRepository openingGeometryRepository,
      OpenCategoryCodePostgresRepository openCategoryCodeRepository,
      OrgUnitPostgresRepository orgUnitRepository,
      TenureValidationService realTenureValidationService,
      OpeningTenureAssociationService tenureAssociationService,
      OpeningTenureAssociationHistoryService tenureAssociationHistoryService,
      LoggedUserHelper loggedUserHelper,
      JdbcTemplate jdbcTemplate,
      ObjectMapper objectMapper) {
    return new CreateOpeningService(
        realSpatialFileService,
        openMapsService,
        openingRepository,
        openingGeometryRepository,
        openCategoryCodeRepository,
        orgUnitRepository,
        realTenureValidationService,
        tenureAssociationService,
        tenureAssociationHistoryService,
        loggedUserHelper,
        jdbcTemplate,
        objectMapper);
  }
}
