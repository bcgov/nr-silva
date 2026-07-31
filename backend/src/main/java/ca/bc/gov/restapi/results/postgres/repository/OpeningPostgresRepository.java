package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchExactFiltersDto;
import ca.bc.gov.restapi.results.common.projection.OpeningTrendsProjection;
import ca.bc.gov.restapi.results.common.projection.SilvicultureSearchProjection;
import ca.bc.gov.restapi.results.common.projection.opening.*;
import ca.bc.gov.restapi.results.common.projection.opening.history.*;
import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresQueryConstants;
import ca.bc.gov.restapi.results.postgres.entity.opening.OpeningEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for CRUD operations and custom queries against the `silva.opening` and
 * related tables in PostgreSQL.
 */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface OpeningPostgresRepository extends OpeningRepository<OpeningEntity> {

  @Override
  @Query(value = SilvaPostgresQueryConstants.SILVICULTURE_SEARCH_EXACT, nativeQuery = true)
  List<SilvicultureSearchProjection> searchByExact(
      OpeningSearchExactFiltersDto filter,
      @Param("openingIds") List<Long> openingIds,
      @Param("page") long page,
      @Param("size") long size);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.SILVICULTURE_SEARCH_BY_OPENING_IDS)
  List<SilvicultureSearchProjection> searchByOpeningIds(
      @Param("openingIds") List<Long> openingIds,
      @Param("page") long page,
      @Param("size") long size);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.OPENING_TRENDS_QUERY)
  List<OpeningTrendsProjection> getOpeningTrends(
      String startDate, String endDate, List<String> statusList, List<String> orgUnitList);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_TOMBSTONE)
  Optional<OpeningTombstoneProjection> getOpeningTombstoneByOpeningId(Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_OVERVIEW_OPENING)
  Optional<OpeningTombstoneOverviewOpeningProjection> getOpeningTombstoneOverviewByOpeningId(
      Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_OVERVIEW_MILESTONE)
  Optional<OpeningTombstoneOverviewMilestoneProjection> getOpeningTombstoneMilestoneByOpeningId(
      Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS)
  List<OpeningStockingDetailsProjection> getOpeningStockingDetailsByOpeningId(Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS_SPECIES)
  List<OpeningStockingSpeciesProjection> getOpeningStockingSpeciesByOpeningId(
      Long openingId, String preferred, Long ssuId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS_LAYER)
  List<OpeningStockingLayerProjection> getOpeningStockingLayerByOpeningId(
      Long openingId, Long ssuId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS_MILESTONES)
  Optional<OpeningStockingMilestoneProjection> getOpeningStockingMilestoneBySsuId(Long ssuId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS_NOTIFICATIONS)
  List<OpeningStockingNotificationProjection> getOpeningStockingNotificationsByOpeningId(
      Long openingId);

  @Override
  @Query(
      nativeQuery = true,
      value = SilvaPostgresQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_LIST)
  List<OpeningStockingHistoryProjection> getOpeningStandardUnitHistoryByOpeningId(Long openingId);

  @Override
  @Query(
      nativeQuery = true,
      value =
          SilvaPostgresQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_WITH_COMPARISON_LIST)
  List<OpeningStockingHistoryDetailsWithComparisonProjection>
      getOpeningStandardUnitHistoryDetailsWithComparisonByOpeningIdAndHistoryId(
          Long openingId, Long historyId);

  @Override
  @Query(
      nativeQuery = true,
      value =
          SilvaPostgresQueryConstants
              .GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_LAYERS_WITH_COMPARISON)
  List<OpeningStockingHistoryLayerWithComparisonProjection>
      getOpeningStandardUnitHistoryLayerDetailsWithComparisonByOpeningIdAndHistoryId(
          Long openingId, Long historyId);

  @Override
  @Query(
      nativeQuery = true,
      value =
          SilvaPostgresQueryConstants
              .GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_SPECIES_WITH_COMPARISON)
  List<OpeningStockingHistoryLayerSpeciesWithComparisonProjection>
      getOpeningStandardUnitHistoryLayerSpeciesDetailsWithComparisonByOpeningIdAndHistoryId(
          Long openingId, Long historyId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS_ARCHIVE)
  List<OpeningStockingHistoryDetailsProjection>
      getOpeningStockingHistoryDetailsByOpeningIdAndEventHistoryId(
          Long openingId, Long eventHistoryId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS_SPECIES_ARCHIVE)
  List<OpeningStockingSpeciesHistoryProjection>
      getOpeningStockingSpeciesHistoryByOpeningIdAndEventHistoryId(
          Long openingId, Long eventHistoryId, String preferred, Long ssuId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS_LAYER_ARCHIVE)
  List<OpeningStockingLayerHistoryProjection>
      getOpeningStockingLayerHistoryByOpeningIdAndEventHistoryId(
          Long openingId, Long eventHistoryId, Long ssuId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS_FSP_IDS)
  List<Long> getOpeningStockingFspIdsByStandardsRegimeId(
      @Param("standardsRegimeId") Long standardsRegimeId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS_FSP_IDS_BY_REGIMES)
  List<OpeningFspIdByRegimeProjection> getOpeningStockingFspIdsByStandardsRegimeIds(
      @Param("standardsRegimeIds") List<Long> standardsRegimeIds);

  /**
   * Returns the next available opening number within the given BCGS mapsheet tile.
   *
   * <p>The result is {@code MAX(existing_number) + 1}, capped at 9999. Only numeric
   * opening_number values are considered. Returns {@code 1} when no openings exist in the tile.
   *
   * @param grid the NTS 1:250K sheet number (mapsheet_grid)
   * @param letter the NTS 1:50K block letter (mapsheet_letter)
   * @param square the BCGS 1:20K block number (mapsheet_square)
   * @param quad the quadrant (mapsheet_quad)
   * @param subQuad the sub-quadrant (mapsheet_sub_quad)
   * @return the next available opening number, capped at 9999
   */
  @Query(
      nativeQuery = true,
      value =
          "SELECT LEAST(COALESCE(MAX(CAST(TRIM(opening_number) AS INT)), 0) + 1, 9999)"
              + " FROM silva.opening"
              + " WHERE mapsheet_grid = :grid"
              + " AND mapsheet_letter = :letter"
              + " AND mapsheet_square = :square"
              + " AND mapsheet_quad = :quad"
              + " AND mapsheet_sub_quad = :subQuad"
              + " AND opening_number ~ '^[0-9]+$'")
  Integer findNextOpeningNumber(
      @Param("grid") String grid,
      @Param("letter") String letter,
      @Param("square") String square,
      @Param("quad") String quad,
      @Param("subQuad") String subQuad);
}
