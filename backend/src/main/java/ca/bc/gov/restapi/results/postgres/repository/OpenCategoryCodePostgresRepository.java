package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.repository.GenericCodeRepository;
import ca.bc.gov.restapi.results.postgres.entity.code.OpenCategoryCodePostgresEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for CRUD operations and custom queries against the
 * `silva.open_category_code` table in PostgreSQL.
 */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface OpenCategoryCodePostgresRepository
    extends GenericCodeRepository<OpenCategoryCodePostgresEntity> {}
