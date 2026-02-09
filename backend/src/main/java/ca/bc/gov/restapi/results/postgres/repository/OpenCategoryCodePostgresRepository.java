package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.repository.GenericCodeRepository;
import ca.bc.gov.restapi.results.postgres.entity.code.OpenCategoryCodeEntity;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for CRUD operations and custom queries against the
 * `silva.open_category_code` table in PostgreSQL.
 */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface OpenCategoryCodePostgresRepository
    extends GenericCodeRepository<OpenCategoryCodeEntity> {
  @Override
  @NonNull
  List<OpenCategoryCodeEntity> findAll();
}
