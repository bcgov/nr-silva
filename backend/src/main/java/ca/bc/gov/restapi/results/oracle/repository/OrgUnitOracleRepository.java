package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.projection.OrgUnitProjection;
import ca.bc.gov.restapi.results.common.repository.OrgUnitRepository;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** This interface provides methods to get, save, and manage org unit data in the database. */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public interface OrgUnitOracleRepository
    extends JpaRepository<OrgUnitEntity, Long>, OrgUnitRepository {

  @Override
  @Query(
      "SELECT o FROM OrgUnitEntity o"
          + " WHERE o.orgUnitCode LIKE 'D%'"
          + " AND LENGTH(o.orgUnitCode) = 3"
          + " AND o.expiryDate > :today"
          + " ORDER BY o.orgUnitCode")
  List<OrgUnitProjection> findActiveDistricts(@Param("today") LocalDate today);

  @Override
  @Query(
      "SELECT o FROM OrgUnitEntity o"
          + " WHERE (o.orgUnitNo = o.rollupRegionNo"
          + "        OR o.orgUnitNo = o.rollupDistNo"
          + "        OR (o.orgUnitCode LIKE 'H%' AND LENGTH(o.orgUnitCode) = 3))"
          + " AND o.expiryDate > :today"
          + " ORDER BY o.orgUnitCode")
  List<OrgUnitProjection> findAllActive(@Param("today") LocalDate today);
}
