package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.common.projection.OpeningTrendsProjection;
import ca.bc.gov.restapi.results.common.projection.SilvicultureSearchProjection;
import ca.bc.gov.restapi.results.common.projection.opening.*;
import ca.bc.gov.restapi.results.common.projection.opening.history.*;
import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresQueryConstants;
import ca.bc.gov.restapi.results.postgres.entity.opening.OpeningEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CRUD operations and custom queries against the
 * `silva.opening` and related tables in PostgreSQL.
 */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface OpeningPostgresRepository extends OpeningRepository<OpeningEntity> {

  @Override
  @Query(
      value = SilvaPostgresQueryConstants.SILVICULTURE_SEARCH,
      nativeQuery = true
  )
  List<SilvicultureSearchProjection> searchBy(
      OpeningSearchFiltersDto filter,
      List<Long> openingIds,
      long page, long size
  );

  @Override
  @Query(
      nativeQuery = true,
      value = SilvaPostgresQueryConstants.OPENING_TRENDS_QUERY
  )
  List<OpeningTrendsProjection> getOpeningTrends(
      String startDate,
      String endDate,
      List<String> statusList,
      List<String> orgUnitList
  );

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_TOMBSTONE)
  Optional<OpeningTombstoneProjection> getOpeningTombstoneByOpeningId(Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_OVERVIEW_OPENING)
  Optional<OpeningTombstoneOverviewOpeningProjection> getOpeningTombstoneOverviewByOpeningId(Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_OVERVIEW_MILESTONE)
  Optional<OpeningTombstoneOverviewMilestoneProjection> getOpeningTombstoneMilestoneByOpeningId(Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS)
  List<OpeningStockingDetailsProjection> getOpeningStockingDetailsByOpeningId(Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS_SPECIES)
  List<OpeningStockingSpeciesProjection> getOpeningStockingSpeciesByOpeningId(Long openingId, String preferred, Long ssuId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS_LAYER)
  List<OpeningStockingLayerProjection> getOpeningStockingLayerByOpeningId(Long openingId, Long ssuId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS_MILESTONES)
  Optional<OpeningStockingMilestoneProjection> getOpeningStockingMilestoneBySsuId(Long ssuId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS_NOTIFICATIONS)
  List<OpeningStockingNotificationProjection> getOpeningStockingNotificationsByOpeningId(Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_LIST)
  List<OpeningStockingHistoryProjection> getOpeningStandardUnitHistoryByOpeningId(Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_WITH_COMPARISON_LIST)
  List<OpeningStockingHistoryDetailsWithComparisonProjection> getOpeningStandardUnitHistoryDetailsWithComparisonByOpeningIdAndHistoryId(
      Long openingId,
      Long historyId
  );

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_LAYERS_WITH_COMPARISON)
  List<OpeningStockingHistoryLayerWithComparisonProjection> getOpeningStandardUnitHistoryLayerDetailsWithComparisonByOpeningIdAndHistoryId(
      Long openingId,
      Long historyId
  );

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_SPECIES_WITH_COMPARISON)
  List<OpeningStockingHistoryLayerSpeciesWithComparisonProjection> getOpeningStandardUnitHistoryLayerSpeciesDetailsWithComparisonByOpeningIdAndHistoryId(
      Long openingId,
      Long historyId
  );

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS_ARCHIVE)
  List<OpeningStockingHistoryDetailsProjection> getOpeningStockingHistoryDetailsByOpeningIdAndEventHistoryId(
      Long openingId,
      Long eventHistoryId
  );

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS_SPECIES_ARCHIVE)
  List<OpeningStockingSpeciesHistoryProjection> getOpeningStockingSpeciesHistoryByOpeningIdAndEventHistoryId(
      Long openingId,
      Long eventHistoryId,
      String preferred,
      Long ssuId
  );

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_SS_LAYER_ARCHIVE)
  List<OpeningStockingLayerHistoryProjection> getOpeningStockingLayerHistoryByOpeningIdAndEventHistoryId(
      Long openingId,
      Long eventHistoryId,
      Long ssuId
  );

}
