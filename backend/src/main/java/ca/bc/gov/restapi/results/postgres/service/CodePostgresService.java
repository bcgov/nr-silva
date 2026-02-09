package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.common.repository.GenericCodeRepository;
import ca.bc.gov.restapi.results.common.repository.OrgUnitRepository;
import ca.bc.gov.restapi.results.common.service.impl.AbstractCodeService;
import ca.bc.gov.restapi.results.postgres.repository.OpenCategoryCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningStatusCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OrgUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvBaseCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvFundSrceCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvMethodCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvObjectiveCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvTechniqueCodePostgresRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** Service for accessing Silviculture code lookup tables (Postgres). */
@Service
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class CodePostgresService extends AbstractCodeService {
  private final SilvBaseCodePostgresRepository silvBaseCodeRepository;
  private final SilvTechniqueCodePostgresRepository silvTechniqueCodeRepository;
  private final SilvMethodCodePostgresRepository silvMethodCodeRepository;
  private final SilvObjectiveCodePostgresRepository silvObjectiveCodeRepository;
  private final SilvFundSrceCodePostgresRepository silvFundSrceCodeRepository;
  private final OpenCategoryCodePostgresRepository openCategoryCodeRepository;
  private final OpeningStatusCodePostgresRepository openingStatusCodeRepository;
  private final OrgUnitPostgresRepository orgUnitRepository;
  private final SilvaConfiguration silvaConfiguration;

  public CodePostgresService(
      SilvBaseCodePostgresRepository silvBaseCodeRepository,
      SilvTechniqueCodePostgresRepository silvTechniqueCodeRepository,
      SilvMethodCodePostgresRepository silvMethodCodeRepository,
      SilvObjectiveCodePostgresRepository silvObjectiveCodeRepository,
      SilvFundSrceCodePostgresRepository silvFundSrceCodeRepository,
      OpenCategoryCodePostgresRepository openCategoryCodeRepository,
      OpeningStatusCodePostgresRepository openingStatusCodeRepository,
      OrgUnitPostgresRepository orgUnitRepository,
      SilvaConfiguration silvaConfiguration) {
    this.silvBaseCodeRepository = silvBaseCodeRepository;
    this.silvTechniqueCodeRepository = silvTechniqueCodeRepository;
    this.silvMethodCodeRepository = silvMethodCodeRepository;
    this.silvObjectiveCodeRepository = silvObjectiveCodeRepository;
    this.silvFundSrceCodeRepository = silvFundSrceCodeRepository;
    this.openCategoryCodeRepository = openCategoryCodeRepository;
    this.openingStatusCodeRepository = openingStatusCodeRepository;
    this.orgUnitRepository = orgUnitRepository;
    this.silvaConfiguration = silvaConfiguration;
  }

  @Override
  protected GenericCodeRepository<?> getSilvBaseCodeRepository() {
    return silvBaseCodeRepository;
  }

  @Override
  protected GenericCodeRepository<?> getSilvTechniqueCodeRepository() {
    return silvTechniqueCodeRepository;
  }

  @Override
  protected GenericCodeRepository<?> getSilvMethodCodeRepository() {
    return silvMethodCodeRepository;
  }

  @Override
  protected GenericCodeRepository<?> getSilvObjectiveCodeRepository() {
    return silvObjectiveCodeRepository;
  }

  @Override
  protected GenericCodeRepository<?> getSilvFundSrceCodeRepository() {
    return silvFundSrceCodeRepository;
  }

  @Override
  protected GenericCodeRepository<?> getOpenCategoryCodeRepository() {
    return openCategoryCodeRepository;
  }

  @Override
  protected GenericCodeRepository<?> getOpenStatusCodeRepository() {
    return openingStatusCodeRepository;
  }

  @Override
  protected OrgUnitRepository getOrgUnitRepository() {
    return orgUnitRepository;
  }

  @Override
  protected SilvaConfiguration getSilvaConfiguration() {
    return silvaConfiguration;
  }
}
