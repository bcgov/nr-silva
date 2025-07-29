package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.entity.OpeningTrendsProjection;
import ca.bc.gov.restapi.results.oracle.entity.SilvicultureSearchProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.history.OpeningStandardUnitHistoryDetailsProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.history.OpeningStandardUnitHistoryLayerDetailsProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.history.OpeningStandardUnitHistoryLayerSpeciesDetailsProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.history.OpeningStandardUnitHistoryProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.*;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * This interface allows the service to fetch and save data into the database.
 */
@Repository
public interface OpeningRepository extends JpaRepository<OpeningEntity, Long> {

  @Query(
      value = SilvaOracleQueryConstants.SILVICULTURE_SEARCH,
      nativeQuery = true
  )
  List<SilvicultureSearchProjection> searchBy(
      OpeningSearchFiltersDto filter,
      List<Long> openingIds,
      long page, long size
  );

  @Query(
      nativeQuery = true,
      value = SilvaOracleQueryConstants.OPENING_TRENDS_QUERY
  )
  List<OpeningTrendsProjection> getOpeningTrends(
      String startDate,
      String endDate,
      List<String> statusList,
      List<String> orgUnitList
  );

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_TOMBSTONE)
  Optional<OpeningTombstoneProjection> getOpeningTombstoneByOpeningId(Long openingId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_OVERVIEW_OPENING)
  Optional<OpeningTombstoneOverviewOpeningProjection> getOpeningTombstoneOverviewByOpeningId(Long openingId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_OVERVIEW_MILESTONE)
  Optional<OpeningTombstoneOverviewMilestoneProjection> getOpeningTombstoneMilestoneByOpeningId(Long openingId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS)
  List<OpeningStockingDetailsProjection> getOpeningStockingDetailsByOpeningId(Long openingId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_SPECIES)
  List<OpeningStockingSpeciesProjection> getOpeningStockingSpeciesByOpeningId(Long openingId, String preferred, Long ssuId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_LAYER)
  List<OpeningStockingLayerProjection> getOpeningStockingLayerByOpeningId(Long openingId, Long ssuId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_MILESTONES)
  Optional<OpeningStockingMilestoneProjection> getOpeningStockingMilestoneBySsuId(Long ssuId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_SS_NOTIFICATIONS)
  List<OpeningStockingNotificationProjection> getOpeningStockingNotificationsByOpeningId(Long openingId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_LIST)
  List<OpeningStandardUnitHistoryProjection> getOpeningStandardUnitHistoryByOpeningId(Long openingId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_LIST)
  List<OpeningStandardUnitHistoryDetailsProjection> getOpeningStandardUnitHistoryDetailsByOpeningIdAndHistoryId(
      Long openingId,
      Long historyId
  );

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_LAYERS)
  List<OpeningStandardUnitHistoryLayerDetailsProjection> getOpeningStandardUnitHistoryLayerDetailsByOpeningIdAndHistoryId(
      Long openingId,
      Long historyId
  );

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_SPECIES)
  List<OpeningStandardUnitHistoryLayerSpeciesDetailsProjection> getOpeningStandardUnitHistoryLayerSpeciesDetailsByOpeningIdAndHistoryId(
      Long openingId,
      Long historyId
  );

}
