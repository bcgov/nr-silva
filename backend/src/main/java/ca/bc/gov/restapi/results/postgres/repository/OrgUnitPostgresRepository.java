package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.projection.OrgUnitProjection;
import ca.bc.gov.restapi.results.common.repository.OrgUnitRepository;
import ca.bc.gov.restapi.results.postgres.entity.OrgUnitEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for CRUD operations and custom queries against the
 * `silva.org_unit` and related tables in PostgreSQL.
 */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface OrgUnitPostgresRepository extends JpaRepository<OrgUnitEntity, Long>,
    OrgUnitRepository {
  @Override
  List<OrgUnitProjection> findAllByOrgUnitCodeIn(List<String> orgUnitCodes);
}
