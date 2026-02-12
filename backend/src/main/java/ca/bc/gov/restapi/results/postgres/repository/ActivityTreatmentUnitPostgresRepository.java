package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.dto.activity.ActivitySearchFiltersDto;
import ca.bc.gov.restapi.results.common.projection.ActivitySearchProjection;
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
      @Param("filter") ActivitySearchFiltersDto filters,
      @Param("page") long offset,
      @Param("size") long size);
}
