package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.oracle.entity.activities.ActivityTreatmentUnitEntity;
import ca.bc.gov.restapi.results.oracle.entity.activities.OpeningActivityBaseProjection;
import ca.bc.gov.restapi.results.oracle.entity.activities.OpeningActivityJuvenileProjection;
import ca.bc.gov.restapi.results.oracle.entity.activities.OpeningActivityPruningProjection;
import ca.bc.gov.restapi.results.oracle.entity.activities.OpeningActivitySpeciesProjection;
import ca.bc.gov.restapi.results.oracle.entity.activities.OpeningActivitySurveyProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningActivitiesActivitiesProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningActivitiesDisturbanceProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ActivityTreatmentUnitRepository extends
    JpaRepository<ActivityTreatmentUnitEntity, Long> {

  @Query(
      nativeQuery = true,
      value = SilvaOracleQueryConstants.GET_OPENING_ACTIVITIES_DISTURBANCE,
      countQuery = SilvaOracleQueryConstants.GET_OPENING_ACTIVITIES_DISTURBANCE_COUNT
  )
  Page<OpeningActivitiesDisturbanceProjection> getOpeningActivitiesDisturbanceByOpeningId(
      Long openingId, Pageable page);

  @Query(
      nativeQuery = true,
      value = SilvaOracleQueryConstants.GET_OPENING_ACTIVITIES_ACTIVITIES,
      countQuery = SilvaOracleQueryConstants.GET_OPENING_ACTIVITIES_ACTIVITIES_COUNT
  )
  Page<OpeningActivitiesActivitiesProjection> getOpeningActivitiesActivitiesByOpeningId(
      Long openingId, String mainSearchTerm, Pageable page);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_ACTIVITIES_BASE)
  OpeningActivityBaseProjection getOpeningActivitiesBase(Long openingId, Long atuId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_ACTIVITIES_SU)
  Optional<OpeningActivitySurveyProjection> getOpeningActivitySU(Long openingId, Long atuId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_ACTIVITY_SPECIES)
  List<OpeningActivitySpeciesProjection> getOpeningActivitySpecies(Long openingId, Long atuId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_ACTIVITY_JS)
  Optional<OpeningActivityJuvenileProjection> getOpeningActivityJS(Long openingId, Long atuId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_ACTIVITY_PR)
  Optional<OpeningActivityPruningProjection> getOpeningActivityPR(Long openingId, Long atuId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_ACTIVITY_SP)
  Optional<Long> getOpeningActivitySP(Long openingId, Long atuId);

}
