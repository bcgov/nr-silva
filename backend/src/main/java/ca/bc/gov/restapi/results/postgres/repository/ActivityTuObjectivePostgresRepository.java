package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTuObjectiveEntity;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTuObjectiveEntityId;
import java.util.Collection;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository for the {@code silva.activity_tu_objective} table. */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface ActivityTuObjectivePostgresRepository
    extends JpaRepository<ActivityTuObjectiveEntity, ActivityTuObjectiveEntityId> {

  /** Objectives of the given activities with their descriptions, in the order entered. */
  @Query(
      """
      SELECT o.activityTreatmentUnitId AS activityTreatmentUnitId,
             o.resultsInd AS resultsInd,
             o.silvObjectiveCode AS code,
             c.description AS description
      FROM ActivityTuObjectiveEntity o
      JOIN SilvObjectiveCodePostgresEntity c ON c.code = o.silvObjectiveCode
      WHERE o.activityTreatmentUnitId IN :activityIds
      ORDER BY o.activityTreatmentUnitId, o.objectiveOrder
      """)
  List<ActivityAdditionalCodeProjection> findCodesByActivityIds(
      @Param("activityIds") Collection<Long> activityIds);
}
