package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.projection.cover.history.*;
import ca.bc.gov.restapi.results.common.repository.ForestCoverRepository;
import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.common.projection.cover.ForestCoverDetailedSpeciesProjection;
import ca.bc.gov.restapi.results.common.projection.cover.ForestCoverDetailsDamageProjection;
import ca.bc.gov.restapi.results.common.projection.cover.ForestCoverDetailsLayerProjection;
import ca.bc.gov.restapi.results.oracle.entity.cover.ForestCoverEntity;
import ca.bc.gov.restapi.results.common.projection.cover.ForestCoverPolygonProjection;
import ca.bc.gov.restapi.results.common.projection.cover.ForestCoverProjection;
import ca.bc.gov.restapi.results.common.projection.cover.ForestCoverSpeciesProjection;
import ca.bc.gov.restapi.results.common.projection.cover.ForestCoverUnmappedProjection;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public interface ForestCoverEntityOracleRepository
    extends JpaRepository<ForestCoverEntity,Long>, ForestCoverRepository{

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_LIST)
  List<ForestCoverProjection> findByOpeningDetails(Long openingId, String mainSearchTerm);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_LIST_SPECIES)
  List<ForestCoverSpeciesProjection> findByOpeningDetailsSpecies(Long forestCoverId, String coverLayerCode);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_POLYGON)
  Optional<ForestCoverPolygonProjection> findByOpeningDetailsPolygon(Long forestCoverId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_UNMAPPED)
  List<ForestCoverUnmappedProjection> findByOpeningDetailsUnmapped(Long forestCoverId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_LAYER)
  List<ForestCoverDetailsLayerProjection> findByOpeningDetailsLayer(Long forestCoverId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_DETAILS_SPECIES)
  List<ForestCoverDetailedSpeciesProjection> findByOpeningDetailsDetailedSpecies(Long forestCoverLayerId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_DAMAGE)
  List<ForestCoverDetailsDamageProjection> findByOpeningDetailsDamage(Long forestCoverLayerId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_HISTORY_OVERVIEW_LIST)
  List<ForestCoverHistoryOverviewProjection> findHistoryOverviewByOpeningId(Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_HISTORY_LIST)
  List<ForestCoverHistoryProjection> findHistoryByOpeningDetails(Long openingId, String updateDate, String mainSearchTerm);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_HISTORY_LIST_SPECIES)
  List<ForestCoverHistorySpeciesProjection> findHistoryByOpeningDetailsSpecies(
      Long forestCoverId, String coverLayerCode, String archiveDate);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_HISTORY_POLYGON)
  Optional<ForestCoverHistoryPolygonProjection> findHistoryByOpeningDetailsPolygon(
          Long forestCoverId, String archiveDate);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_HISTORY_UNMAPPED)
  List<ForestCoverHistoryUnmappedProjection> findHistoryByOpeningDetailsUnmapped(
          Long forestCoverId, String archiveDate);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_HISTORY_LAYER)
  List<ForestCoverHistoryDetailsLayerProjection> findHistoryByOpeningDetailsLayer(
          Long forestCoverId, String archiveDate);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_HISTORY_DETAILS_SPECIES)
  List<ForestCoverHistoryDetailedSpeciesProjection> findHistoryByOpeningDetailsDetailedSpecies(
          Long forestCoverLayerId, String archiveDate);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_HISTORY_DAMAGE)
  List<ForestCoverHistoryDetailsDamageProjection> findHistoryByOpeningDetailsDamage(
          Long forestCoverLayerId, String archiveDate);

}
