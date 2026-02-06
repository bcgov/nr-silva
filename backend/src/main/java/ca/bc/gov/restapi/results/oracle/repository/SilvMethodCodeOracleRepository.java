package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.repository.GenericCodeRepository;
import ca.bc.gov.restapi.results.oracle.entity.code.SilvMethodCodeOracleEntity;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public interface SilvMethodCodeOracleRepository
    extends GenericCodeRepository<SilvMethodCodeOracleEntity> {
  @Override
  @NonNull
  List<SilvMethodCodeOracleEntity> findAll();
}
