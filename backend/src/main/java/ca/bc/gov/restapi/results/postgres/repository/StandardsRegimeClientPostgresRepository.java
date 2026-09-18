package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeClientEntity;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeClientEntityId;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** JPA access to stocking standard client assignments. */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface StandardsRegimeClientPostgresRepository
    extends JpaRepository<StandardsRegimeClientEntity, StandardsRegimeClientEntityId> {
  List<StandardsRegimeClientEntity> findByStandardsRegimeIdOrderByClientNumber(
      Long standardsRegimeId);
}
