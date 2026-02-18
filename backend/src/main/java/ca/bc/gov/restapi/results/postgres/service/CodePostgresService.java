package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.common.service.impl.AbstractCodeService;
import ca.bc.gov.restapi.results.postgres.repository.DisturbanceCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpenCategoryCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningStatusCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OrgUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvBaseCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvCutPhaseCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvFundSrceCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvMethodCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvObjectiveCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvSystemCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvSystemVariantCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvTechniqueCodePostgresRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** Service for accessing Silviculture code lookup tables (Postgres). */
@Service
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class CodePostgresService extends AbstractCodeService {
  public CodePostgresService(
      SilvBaseCodePostgresRepository silvBaseCodeRepository,
      SilvTechniqueCodePostgresRepository silvTechniqueCodeRepository,
      SilvMethodCodePostgresRepository silvMethodCodeRepository,
      SilvObjectiveCodePostgresRepository silvObjectiveCodeRepository,
      SilvFundSrceCodePostgresRepository silvFundSrceCodeRepository,
      SilvSystemCodePostgresRepository silvSystemCodeRepository,
      SilvSystemVariantCodePostgresRepository silvSystemVariantCodeRepository,
      SilvCutPhaseCodePostgresRepository silvCutPhaseCodeRepository,
      DisturbanceCodePostgresRepository disturbanceCodeRepository,
      OpenCategoryCodePostgresRepository openCategoryCodeRepository,
      OpeningStatusCodePostgresRepository openingStatusCodeRepository,
      OrgUnitPostgresRepository orgUnitRepository,
      SilvaConfiguration silvaConfiguration) {
    super(
        silvBaseCodeRepository,
        silvTechniqueCodeRepository,
        silvMethodCodeRepository,
        silvObjectiveCodeRepository,
        silvFundSrceCodeRepository,
        silvSystemCodeRepository,
        silvSystemVariantCodeRepository,
        silvCutPhaseCodeRepository,
        disturbanceCodeRepository,
        openCategoryCodeRepository,
        openingStatusCodeRepository,
        orgUnitRepository,
        silvaConfiguration);
  }
}
