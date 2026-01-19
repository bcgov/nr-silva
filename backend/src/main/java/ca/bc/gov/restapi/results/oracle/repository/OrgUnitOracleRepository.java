package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.projection.OrgUnitProjection;
import ca.bc.gov.restapi.results.common.repository.OrgUnitRepository;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** This interface provides methods to get, save, and manage org unit data in the database. */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public interface OrgUnitOracleRepository extends JpaRepository<OrgUnitEntity, Long>,
    OrgUnitRepository {

  @Override
  List<OrgUnitProjection> findAllByOrgUnitCodeIn(List<String> orgUnitCodes);
}
