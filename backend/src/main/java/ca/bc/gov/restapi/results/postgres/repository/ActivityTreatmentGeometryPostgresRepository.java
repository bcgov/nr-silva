package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTreatmentGeometryEntity;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository for the {@code silva.activity_treatment_geometry} table. */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface ActivityTreatmentGeometryPostgresRepository
    extends JpaRepository<ActivityTreatmentGeometryEntity, Long> {

  /** Returns [unit, geometry] pairs of the opening's activities that have geometry, except DN. */
  @Query(
      "select u, g from ActivityTreatmentUnitEntity u, ActivityTreatmentGeometryEntity g "
          + "where g.activityTreatmentUnitId = u.id and u.openingId = :openingId "
          + "and u.silvBaseCode <> 'DN'")
  List<Object[]> findNonDisturbanceByOpeningId(@Param("openingId") Long openingId);
}
