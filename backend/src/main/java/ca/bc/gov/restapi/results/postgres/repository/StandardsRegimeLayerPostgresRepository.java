package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeLayerEntity;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** JPA access to tree stocking criteria layers. */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface StandardsRegimeLayerPostgresRepository
    extends JpaRepository<StandardsRegimeLayerEntity, Long> {
  List<StandardsRegimeLayerEntity> findByStandardsRegimeIdOrderByStockingLayerCode(
      Long standardsRegimeId);
}
