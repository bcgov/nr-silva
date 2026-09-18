package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeLayerSpeciesEntity;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeLayerSpeciesEntityId;
import java.util.Collection;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** JPA access to stocking standard layer species. */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface StandardsRegimeLayerSpeciesPostgresRepository
    extends JpaRepository<StandardsRegimeLayerSpeciesEntity, StandardsRegimeLayerSpeciesEntityId> {
  List<StandardsRegimeLayerSpeciesEntity> findByStandardsRegimeLayerIdInOrderBySpeciesOrder(
      Collection<Long> standardsRegimeLayerIds);
}
