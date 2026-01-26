package ca.bc.gov.restapi.results.oracle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchExactFiltersDto;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.common.projection.OpeningTrendsProjection;
import ca.bc.gov.restapi.results.common.projection.SilvicultureSearchProjection;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningStockingDetailsProjection;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningStockingLayerProjection;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningStockingMilestoneProjection;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningStockingNotificationProjection;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningStockingSpeciesProjection;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningTombstoneOverviewMilestoneProjection;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningTombstoneOverviewOpeningProjection;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningTombstoneProjection;
import ca.bc.gov.restapi.results.common.projection.opening.history.OpeningStockingHistoryDetailsProjection;
import ca.bc.gov.restapi.results.common.projection.opening.history.OpeningStockingHistoryDetailsWithComparisonProjection;
import ca.bc.gov.restapi.results.common.projection.opening.history.OpeningStockingHistoryLayerSpeciesWithComparisonProjection;
import ca.bc.gov.restapi.results.common.projection.opening.history.OpeningStockingHistoryLayerWithComparisonProjection;
import ca.bc.gov.restapi.results.common.projection.opening.history.OpeningStockingHistoryProjection;
import ca.bc.gov.restapi.results.common.projection.opening.history.OpeningStockingLayerHistoryProjection;
import ca.bc.gov.restapi.results.common.projection.opening.history.OpeningStockingSpeciesHistoryProjection;
import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningEntity;

/**
 * This interface allows the service to fetch and save data into the database.
 */

@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public interface OpeningOracleRepository extends OpeningRepository<OpeningEntity> {

  @Override
  @Query(
      value = SilvaOracleQueryConstants.SILVICULTURE_SEARCH,
      nativeQuery = true
  )
  List<SilvicultureSearchProjection> searchBy(
      OpeningSearchFiltersDto filter, List<Long> openingIds, long page, long size);

  @Override
  @Query(value = SilvaOracleQueryConstants.SILVICULTURE_SEARCH_EXACT, nativeQuery = true)
  List<SilvicultureSearchProjection> searchByExact(
      OpeningSearchExactFiltersDto filter, List<Long> openingIds, long page, long size);

  @Override
  @Query(
      nativeQuery = true,
      value = SilvaOracleQueryConstants.OPENING_TRENDS_QUERY
  )
  List<OpeningTrendsProjection> getOpeningTrends(
      String startDate, String endDate, List<String> statusList, List<String> orgUnitList);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_TOMBSTONE)
  Optional<OpeningTombstoneProjection> getOpeningTombstoneByOpeningId(Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_OVERVIEW_OPENING)
  Optional<OpeningTombstoneOverviewOpeningProjection> getOpeningTombstoneOverviewByOpeningId(
      Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_OVERVIEW_MILESTONE)
  Optional<OpeningTombstoneOverviewMilestoneProjection> getOpeningTombstoneMilestoneByOpeningId(
      Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS)
  List<OpeningStockingDetailsProjection> getOpeningStockingDetailsByOpeningId(Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_SPECIES)
  List<OpeningStockingSpeciesProjection> getOpeningStockingSpeciesByOpeningId(
      Long openingId, String preferred, Long ssuId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_LAYER)
  List<OpeningStockingLayerProjection> getOpeningStockingLayerByOpeningId(
      Long openingId, Long ssuId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_MILESTONES)
  Optional<OpeningStockingMilestoneProjection> getOpeningStockingMilestoneBySsuId(Long ssuId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_NOTIFICATIONS)
  List<OpeningStockingNotificationProjection> getOpeningStockingNotificationsByOpeningId(
      Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_LIST)
  List<OpeningStockingHistoryProjection> getOpeningStandardUnitHistoryByOpeningId(Long openingId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_WITH_COMPARISON_LIST)
  List<OpeningStockingHistoryDetailsWithComparisonProjection> getOpeningStandardUnitHistoryDetailsWithComparisonByOpeningIdAndHistoryId(
      Long openingId,
      Long historyId
  );

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_LAYERS_WITH_COMPARISON)
  List<OpeningStockingHistoryLayerWithComparisonProjection> getOpeningStandardUnitHistoryLayerDetailsWithComparisonByOpeningIdAndHistoryId(
      Long openingId,
      Long historyId
  );

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_SPECIES_WITH_COMPARISON)
  List<OpeningStockingHistoryLayerSpeciesWithComparisonProjection> getOpeningStandardUnitHistoryLayerSpeciesDetailsWithComparisonByOpeningIdAndHistoryId(
      Long openingId,
      Long historyId
  );

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_ARCHIVE)
  List<OpeningStockingHistoryDetailsProjection>
      getOpeningStockingHistoryDetailsByOpeningIdAndEventHistoryId(
          Long openingId, Long eventHistoryId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_SPECIES_ARCHIVE)
  List<OpeningStockingSpeciesHistoryProjection>
      getOpeningStockingSpeciesHistoryByOpeningIdAndEventHistoryId(
          Long openingId, Long eventHistoryId, String preferred, Long ssuId);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_LAYER_ARCHIVE)
  List<OpeningStockingLayerHistoryProjection>
      getOpeningStockingLayerHistoryByOpeningIdAndEventHistoryId(
          Long openingId, Long eventHistoryId, Long ssuId);
}
