package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeClientEntity;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeClientEntityId;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface StandardsRegimeClientPostgresRepository
    extends JpaRepository<StandardsRegimeClientEntity, StandardsRegimeClientEntityId> {}
