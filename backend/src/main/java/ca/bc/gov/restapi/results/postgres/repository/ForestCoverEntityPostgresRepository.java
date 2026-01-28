package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.projection.cover.*;
import ca.bc.gov.restapi.results.common.projection.cover.history.*;
import ca.bc.gov.restapi.results.common.repository.ForestCoverRepository;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresQueryConstants;
import ca.bc.gov.restapi.results.postgres.entity.cover.ForestCoverEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CRUD operations and custom queries against the
 * `silva.forest_cover` table in PostgreSQL.
 */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface ForestCoverEntityPostgresRepository
    extends JpaRepository<ForestCoverEntity, Long>, ForestCoverRepository {

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_FOREST_COVER_LIST)
  List<ForestCoverProjection> findByOpeningDetails(Long openingId, String mainSearchTerm);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_FOREST_COVER_LIST_SPECIES)
  List<ForestCoverSpeciesProjection> findByOpeningDetailsSpecies(Long forestCoverId, String coverLayerCode);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_FOREST_COVER_POLYGON)
  Optional<ForestCoverPolygonProjection> findByOpeningDetailsPolygon(Long forestCoverId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_FOREST_COVER_UNMAPPED)
  List<ForestCoverUnmappedProjection> findByOpeningDetailsUnmapped(Long forestCoverId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_FOREST_COVER_LAYER)
  List<ForestCoverDetailsLayerProjection> findByOpeningDetailsLayer(Long forestCoverId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_FOREST_COVER_DETAILS_SPECIES)
  List<ForestCoverDetailedSpeciesProjection> findByOpeningDetailsDetailedSpecies(Long forestCoverLayerId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_FOREST_COVER_DAMAGE)
  List<ForestCoverDetailsDamageProjection> findByOpeningDetailsDamage(Long forestCoverLayerId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_FOREST_COVER_HISTORY_OVERVIEW_LIST)
  List<ForestCoverHistoryOverviewProjection> findHistoryOverviewByOpeningId(Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_FOREST_COVER_HISTORY_LIST)
  List<ForestCoverHistoryProjection> findHistoryByOpeningDetails(Long openingId, String updateDate, String mainSearchTerm);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_FOREST_COVER_HISTORY_LIST_SPECIES)
  List<ForestCoverHistorySpeciesProjection> findHistoryByOpeningDetailsSpecies(
      Long forestCoverId, String coverLayerCode, String archiveDate);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_FOREST_COVER_HISTORY_POLYGON)
  Optional<ForestCoverHistoryPolygonProjection> findHistoryByOpeningDetailsPolygon(
      Long forestCoverId, String archiveDate);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_FOREST_COVER_HISTORY_UNMAPPED)
  List<ForestCoverHistoryUnmappedProjection> findHistoryByOpeningDetailsUnmapped(
      Long forestCoverId, String archiveDate);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_FOREST_COVER_HISTORY_LAYER)
  List<ForestCoverHistoryDetailsLayerProjection> findHistoryByOpeningDetailsLayer(
      Long forestCoverId, String archiveDate);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_FOREST_COVER_HISTORY_DETAILS_SPECIES)
  List<ForestCoverHistoryDetailedSpeciesProjection> findHistoryByOpeningDetailsDetailedSpecies(
      Long forestCoverLayerId, String archiveDate);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_FOREST_COVER_HISTORY_DAMAGE)
  List<ForestCoverHistoryDetailsDamageProjection> findHistoryByOpeningDetailsDamage(
      Long forestCoverLayerId, String archiveDate);

}
