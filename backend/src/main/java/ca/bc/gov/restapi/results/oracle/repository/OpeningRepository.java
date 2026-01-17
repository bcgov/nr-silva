package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningSearchExactFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.entity.OpeningTrendsProjection;
import ca.bc.gov.restapi.results.oracle.entity.SilvicultureSearchProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.*;
import ca.bc.gov.restapi.results.oracle.entity.opening.history.*;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/** This interface allows the service to fetch and save data into the database. */
@Repository
public interface OpeningRepository extends JpaRepository<OpeningEntity, Long> {

  @Query(value = SilvaOracleQueryConstants.SILVICULTURE_SEARCH, nativeQuery = true)
  List<SilvicultureSearchProjection> searchBy(
      OpeningSearchFiltersDto filter, List<Long> openingIds, long page, long size);

  @Query(value = SilvaOracleQueryConstants.SILVICULTURE_SEARCH_EXACT, nativeQuery = true)
  List<SilvicultureSearchProjection> searchByExact(
      OpeningSearchExactFiltersDto filter, List<Long> openingIds, long page, long size);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.OPENING_TRENDS_QUERY)
  List<OpeningTrendsProjection> getOpeningTrends(
      String startDate, String endDate, List<String> statusList, List<String> orgUnitList);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_TOMBSTONE)
  Optional<OpeningTombstoneProjection> getOpeningTombstoneByOpeningId(Long openingId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_OVERVIEW_OPENING)
  Optional<OpeningTombstoneOverviewOpeningProjection> getOpeningTombstoneOverviewByOpeningId(
      Long openingId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_OVERVIEW_MILESTONE)
  Optional<OpeningTombstoneOverviewMilestoneProjection> getOpeningTombstoneMilestoneByOpeningId(
      Long openingId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS)
  List<OpeningStockingDetailsProjection> getOpeningStockingDetailsByOpeningId(Long openingId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_SPECIES)
  List<OpeningStockingSpeciesProjection> getOpeningStockingSpeciesByOpeningId(
      Long openingId, String preferred, Long ssuId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_LAYER)
  List<OpeningStockingLayerProjection> getOpeningStockingLayerByOpeningId(
      Long openingId, Long ssuId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_MILESTONES)
  Optional<OpeningStockingMilestoneProjection> getOpeningStockingMilestoneBySsuId(Long ssuId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_NOTIFICATIONS)
  List<OpeningStockingNotificationProjection> getOpeningStockingNotificationsByOpeningId(
      Long openingId);

  @Query(
      nativeQuery = true,
      value = SilvaOracleQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_LIST)
  List<OpeningStockingHistoryProjection> getOpeningStandardUnitHistoryByOpeningId(Long openingId);

  @Query(
      nativeQuery = true,
      value =
          SilvaOracleQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_WITH_COMPARISON_LIST)
  List<OpeningStockingHistoryDetailsWithComparisonProjection>
      getOpeningStandardUnitHistoryDetailsWithComparisonByOpeningIdAndHistoryId(
          Long openingId, Long historyId);

  @Query(
      nativeQuery = true,
      value =
          SilvaOracleQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_LAYERS_WITH_COMPARISON)
  List<OpeningStockingHistoryLayerWithComparisonProjection>
      getOpeningStandardUnitHistoryLayerDetailsWithComparisonByOpeningIdAndHistoryId(
          Long openingId, Long historyId);

  @Query(
      nativeQuery = true,
      value =
          SilvaOracleQueryConstants
              .GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_SPECIES_WITH_COMPARISON)
  List<OpeningStockingHistoryLayerSpeciesWithComparisonProjection>
      getOpeningStandardUnitHistoryLayerSpeciesDetailsWithComparisonByOpeningIdAndHistoryId(
          Long openingId, Long historyId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_ARCHIVE)
  List<OpeningStockingHistoryDetailsProjection>
      getOpeningStockingHistoryDetailsByOpeningIdAndEventHistoryId(
          Long openingId, Long eventHistoryId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_SPECIES_ARCHIVE)
  List<OpeningStockingSpeciesHistoryProjection>
      getOpeningStockingSpeciesHistoryByOpeningIdAndEventHistoryId(
          Long openingId, Long eventHistoryId, String preferred, Long ssuId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_LAYER_ARCHIVE)
  List<OpeningStockingLayerHistoryProjection>
      getOpeningStockingLayerHistoryByOpeningIdAndEventHistoryId(
          Long openingId, Long eventHistoryId, Long ssuId);
}
