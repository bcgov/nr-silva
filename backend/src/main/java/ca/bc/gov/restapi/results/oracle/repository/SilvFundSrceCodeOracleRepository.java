package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.repository.GenericCodeRepository;
import ca.bc.gov.restapi.results.oracle.entity.code.SilvFundSrceCodeOracleEntity;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public interface SilvFundSrceCodeOracleRepository
    extends GenericCodeRepository<SilvFundSrceCodeOracleEntity> {
  @Override
  @NonNull
  List<SilvFundSrceCodeOracleEntity> findAll();
}
