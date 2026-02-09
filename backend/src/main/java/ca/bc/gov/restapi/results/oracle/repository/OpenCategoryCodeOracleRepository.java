package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.repository.GenericCodeRepository;
import ca.bc.gov.restapi.results.oracle.entity.code.OpenCategoryCodeOracleEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

/** This interface provides methods to get, save, and manage data in the database. */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public interface OpenCategoryCodeOracleRepository
    extends GenericCodeRepository<OpenCategoryCodeOracleEntity> {}
