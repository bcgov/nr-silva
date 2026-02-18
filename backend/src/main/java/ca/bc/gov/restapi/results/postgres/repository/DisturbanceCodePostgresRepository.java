package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.repository.GenericCodeRepository;
import ca.bc.gov.restapi.results.postgres.entity.code.DisturbanceCodePostgresEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface DisturbanceCodePostgresRepository
    extends GenericCodeRepository<DisturbanceCodePostgresEntity> {}
