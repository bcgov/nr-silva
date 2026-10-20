package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTuFundSrceEntity;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTuFundSrceEntityId;
import java.util.Collection;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository for the {@code silva.activity_tu_fund_srce} table. */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface ActivityTuFundSrcePostgresRepository
    extends JpaRepository<ActivityTuFundSrceEntity, ActivityTuFundSrceEntityId> {

  /** Funding sources of the given activities with their descriptions, in the order entered. */
  @Query(
      """
      SELECT f.activityTreatmentUnitId AS activityTreatmentUnitId,
             f.resultsInd AS resultsInd,
             f.silvFundSrceCode AS code,
             c.description AS description
      FROM ActivityTuFundSrceEntity f
      JOIN SilvFundSrceCodePostgresEntity c ON c.code = f.silvFundSrceCode
      WHERE f.activityTreatmentUnitId IN :activityIds
      ORDER BY f.activityTreatmentUnitId, f.fundSrceOrder
      """)
  List<ActivityAdditionalCodeProjection> findCodesByActivityIds(
      @Param("activityIds") Collection<Long> activityIds);
}
