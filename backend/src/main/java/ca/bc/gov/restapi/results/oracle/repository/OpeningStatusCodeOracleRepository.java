package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.repository.OpeningStatusCodeRepository;
import ca.bc.gov.restapi.results.oracle.entity.code.OpeningStatusCodeEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public interface OpeningStatusCodeOracleRepository extends OpeningStatusCodeRepository<OpeningStatusCodeEntity> {
  @Override
  @NonNull
  List<OpeningStatusCodeEntity> findAll();
}
