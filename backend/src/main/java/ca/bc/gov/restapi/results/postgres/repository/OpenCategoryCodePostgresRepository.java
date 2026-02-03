package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.projection.OpenCategoryCodeProjection;
import ca.bc.gov.restapi.results.common.repository.OpenCategoryCodeRepository;
import ca.bc.gov.restapi.results.postgres.entity.code.OpenCategoryCodeEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for CRUD operations and custom queries against the
 * `silva.open_category_code` table in PostgreSQL.
 */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface OpenCategoryCodePostgresRepository extends OpenCategoryCodeRepository<OpenCategoryCodeEntity> {
  @Override
  List<OpenCategoryCodeProjection> findAllBy();

  @Override
  List<OpenCategoryCodeProjection> findAllByExpiryDateAfter(LocalDate now);

  @Override
  @NonNull
  List<OpenCategoryCodeEntity> findAll();
}
