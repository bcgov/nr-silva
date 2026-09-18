package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeSiteSeriesEntity;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** JPA access to stocking standard BEC assignments. */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface StandardsRegimeSiteSeriesPostgresRepository
    extends JpaRepository<StandardsRegimeSiteSeriesEntity, Long> {
  List<StandardsRegimeSiteSeriesEntity> findByStandardsRegimeIdOrderById(Long standardsRegimeId);
}
