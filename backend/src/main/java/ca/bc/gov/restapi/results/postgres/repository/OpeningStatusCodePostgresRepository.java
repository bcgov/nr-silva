package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.repository.OpeningStatusCodeRepository;
import ca.bc.gov.restapi.results.postgres.entity.code.OpeningStatusCodeEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface OpeningStatusCodePostgresRepository extends OpeningStatusCodeRepository<OpeningStatusCodeEntity> {
  @Override
  @NonNull
  List<OpeningStatusCodeEntity> findAll();
}
