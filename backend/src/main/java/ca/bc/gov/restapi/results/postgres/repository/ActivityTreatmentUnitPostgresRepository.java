package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.dto.activity.ActivitySearchFiltersDto;
import ca.bc.gov.restapi.results.common.dto.activity.DisturbanceSearchFilterDto;
import ca.bc.gov.restapi.results.common.projection.ActivitySearchProjection;
import ca.bc.gov.restapi.results.common.projection.DisturbanceSearchProjection;
import ca.bc.gov.restapi.results.common.projection.activity.*;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningActivitiesActivitiesProjection;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningActivitiesDisturbanceProjection;
import ca.bc.gov.restapi.results.common.repository.ActivityTreatmentUnitRepository;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresQueryConstants;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTreatmentUnitEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for CRUD operations and custom queries against the
 * `silva.activity_treatment_unit` table in PostgreSQL.
 */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface ActivityTreatmentUnitPostgresRepository
    extends JpaRepository<ActivityTreatmentUnitEntity, Long>, ActivityTreatmentUnitRepository {

  /**
   * Checks whether an opening has an assigned activity of requested base type.
   *
   * @param openingId opening to inspect
   * @param silvBaseCode activity base code
   * @return true when an assigned matching activity exists
   */
  boolean existsByOpeningIdAndSilvBaseCodeAndCutBlockOpenAdminIdIsNotNull(
      Long openingId, String silvBaseCode);

  /** Returns activities for reconciliation of derived CBOA disturbance data. */
  List<ActivityTreatmentUnitEntity> findByOpeningIdAndSilvBaseCode(
      Long openingId, String silvBaseCode);

  /**
   * Allocates the id of a new activity. The sequence is first moved past the highest id in the
   * table because the ETL loads rows with their original ids without advancing the sequence, so a
   * plain nextval could return an id that already exists. The advisory lock keeps concurrent
   * allocations from computing the same value.
   */
  @Query(
      nativeQuery = true,
      value =
          """
          WITH lock AS (SELECT pg_advisory_xact_lock(hashtext('silva.activity_treatment_unit_id_seq')))
          SELECT setval('silva.activity_treatment_unit_id_seq',
                 GREATEST((SELECT COALESCE(MAX(activity_treatment_unit_id), 0)
                           FROM silva.activity_treatment_unit),
                          (SELECT last_value FROM silva.activity_treatment_unit_id_seq)) + 1)
          FROM lock
          """)
  Long nextActivityId();

  /**
   * Returns the sequence number of the next activity under an opening. Callers must hold the lock
   * on the opening row (see {@code OpeningPostgresRepository#findByIdForUpdate}), otherwise two
   * concurrent creates can get the same number.
   */
  @Query(
      "SELECT COALESCE(MAX(a.activityTuSeqNo), 0) + 1 FROM ActivityTreatmentUnitEntity a "
          + "WHERE a.openingId = :openingId")
  Integer nextSequenceNumber(@Param("openingId") Long openingId);

  @Override
  @Query(
      nativeQuery = true,
      value = SilvaPostgresQueryConstants.GET_OPENING_ACTIVITIES_DISTURBANCE,
      countQuery = SilvaPostgresQueryConstants.GET_OPENING_ACTIVITIES_DISTURBANCE_COUNT)
  Page<OpeningActivitiesDisturbanceProjection> getOpeningActivitiesDisturbanceByOpeningId(
      Long openingId, Pageable page);

  @Override
  @Query(
      nativeQuery = true,
      value = SilvaPostgresQueryConstants.GET_OPENING_ACTIVITIES_ACTIVITIES,
      countQuery = SilvaPostgresQueryConstants.GET_OPENING_ACTIVITIES_ACTIVITIES_COUNT)
  Page<OpeningActivitiesActivitiesProjection> getOpeningActivitiesActivitiesByOpeningId(
      Long openingId, String mainSearchTerm, Pageable page);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_ACTIVITIES_BASE)
  OpeningActivityBaseProjection getOpeningActivitiesBase(Long openingId, Long atuId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_ACTIVITIES_SU)
  Optional<OpeningActivitySurveyProjection> getOpeningActivitySU(Long openingId, Long atuId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_ACTIVITY_SPECIES)
  List<OpeningActivitySpeciesProjection> getOpeningActivitySpecies(Long openingId, Long atuId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_ACTIVITY_JS)
  Optional<OpeningActivityJuvenileProjection> getOpeningActivityJS(Long openingId, Long atuId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_ACTIVITY_PR)
  Optional<OpeningActivityPruningProjection> getOpeningActivityPR(Long openingId, Long atuId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_ACTIVITY_SP)
  Optional<Long> getOpeningActivitySP(Long openingId, Long atuId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.ACTIVITY_SEARCH)
  List<ActivitySearchProjection> activitySearch(
      @Param("filter") ActivitySearchFiltersDto filter,
      @Param("page") long offset,
      @Param("size") long size);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.DISTURBANCE_SEARCH)
  List<DisturbanceSearchProjection> disturbanceSearch(
      @Param("filter") DisturbanceSearchFilterDto filter,
      @Param("page") long offset,
      @Param("size") long size);
}
