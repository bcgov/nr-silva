package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.common.service.impl.AbstractCodeService;
import ca.bc.gov.restapi.results.oracle.repository.DisturbanceCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.OpenCategoryCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.OpeningStatusCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.OrgUnitOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvBaseCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvCutPhaseCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvFundSrceCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvMethodCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvObjectiveCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvSystemCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvSystemVariantCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvTechniqueCodeOracleRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** Service for accessing Silviculture code lookup tables (Oracle). */
@Service
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public class CodeOracleService extends AbstractCodeService {
  public CodeOracleService(
      SilvBaseCodeOracleRepository silvBaseCodeRepository,
      SilvTechniqueCodeOracleRepository silvTechniqueCodeRepository,
      SilvMethodCodeOracleRepository silvMethodCodeRepository,
      SilvObjectiveCodeOracleRepository silvObjectiveCodeRepository,
      SilvFundSrceCodeOracleRepository silvFundSrceCodeRepository,
      SilvSystemCodeOracleRepository silvSystemCodeRepository,
      SilvSystemVariantCodeOracleRepository silvSystemVariantCodeRepository,
      SilvCutPhaseCodeOracleRepository silvCutPhaseCodeRepository,
      DisturbanceCodeOracleRepository disturbanceCodeRepository,
      OpenCategoryCodeOracleRepository openCategoryCodeRepository,
      OpeningStatusCodeOracleRepository openingStatusCodeRepository,
      OrgUnitOracleRepository orgUnitRepository,
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
