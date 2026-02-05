package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.repository.GenericCodeRepository;
import ca.bc.gov.restapi.results.postgres.entity.code.SilvFundSrceCodePostgresEntity;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface SilvFundSrceCodePostgresRepository
    extends GenericCodeRepository<SilvFundSrceCodePostgresEntity> {
  @Override
  @NonNull
  List<SilvFundSrceCodePostgresEntity> findAll();
}
