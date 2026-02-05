package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.repository.GenericCodeRepository;
import ca.bc.gov.restapi.results.common.service.impl.AbstractCodeService;
import ca.bc.gov.restapi.results.oracle.repository.SilvBaseCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvFundSrceCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvMethodCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvObjectiveCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvTechniqueCodeOracleRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** Service for accessing Silviculture code lookup tables (Oracle). */
@Service
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public class CodeOracleService extends AbstractCodeService {
  private final SilvBaseCodeOracleRepository silvBaseCodeRepository;
  private final SilvTechniqueCodeOracleRepository silvTechniqueCodeRepository;
  private final SilvMethodCodeOracleRepository silvMethodCodeRepository;
  private final SilvObjectiveCodeOracleRepository silvObjectiveCodeRepository;
  private final SilvFundSrceCodeOracleRepository silvFundSrceCodeRepository;

  public CodeOracleService(
      SilvBaseCodeOracleRepository silvBaseCodeRepository,
      SilvTechniqueCodeOracleRepository silvTechniqueCodeRepository,
      SilvMethodCodeOracleRepository silvMethodCodeRepository,
      SilvObjectiveCodeOracleRepository silvObjectiveCodeRepository,
      SilvFundSrceCodeOracleRepository silvFundSrceCodeRepository) {
    this.silvBaseCodeRepository = silvBaseCodeRepository;
    this.silvTechniqueCodeRepository = silvTechniqueCodeRepository;
    this.silvMethodCodeRepository = silvMethodCodeRepository;
    this.silvObjectiveCodeRepository = silvObjectiveCodeRepository;
    this.silvFundSrceCodeRepository = silvFundSrceCodeRepository;
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
}
