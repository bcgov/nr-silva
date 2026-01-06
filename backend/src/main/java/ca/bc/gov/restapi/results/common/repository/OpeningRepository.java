package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.common.projection.opening.*;
import ca.bc.gov.restapi.results.common.projection.opening.history.*;
import ca.bc.gov.restapi.results.common.projection.OpeningTrendsProjection;
import ca.bc.gov.restapi.results.common.projection.SilvicultureSearchProjection;

import java.util.List;
import java.util.Optional;

public interface OpeningRepository {
  List<SilvicultureSearchProjection> searchBy(
      OpeningSearchFiltersDto filter,
      List<Long> openingIds,
      long page,
      long size
  );

  List<OpeningTrendsProjection> getOpeningTrends(
      String startDate,
      String endDate,
      List<String> statusList,
      List<String> orgUnitList
  );

  Optional<OpeningTombstoneProjection> getOpeningTombstoneByOpeningId(Long openingId);

  Optional<OpeningTombstoneOverviewOpeningProjection> getOpeningTombstoneOverviewByOpeningId(Long openingId);

  Optional<OpeningTombstoneOverviewMilestoneProjection> getOpeningTombstoneMilestoneByOpeningId(Long openingId);

  List<OpeningStockingDetailsProjection> getOpeningStockingDetailsByOpeningId(Long openingId);

  List<OpeningStockingSpeciesProjection> getOpeningStockingSpeciesByOpeningId(Long openingId, String preferred, Long ssuId);

  List<OpeningStockingLayerProjection> getOpeningStockingLayerByOpeningId(Long openingId, Long ssuId);

  Optional<OpeningStockingMilestoneProjection> getOpeningStockingMilestoneBySsuId(Long ssuId);

  List<OpeningStockingNotificationProjection> getOpeningStockingNotificationsByOpeningId(Long openingId);

  List<OpeningStockingHistoryProjection> getOpeningStandardUnitHistoryByOpeningId(Long openingId);

  List<OpeningStockingHistoryDetailsWithComparisonProjection> getOpeningStandardUnitHistoryDetailsWithComparisonByOpeningIdAndHistoryId(
      Long openingId,
      Long historyId
  );

  List<OpeningStockingHistoryLayerWithComparisonProjection> getOpeningStandardUnitHistoryLayerDetailsWithComparisonByOpeningIdAndHistoryId(
      Long openingId,
      Long historyId
  );

  List<OpeningStockingHistoryLayerSpeciesWithComparisonProjection> getOpeningStandardUnitHistoryLayerSpeciesDetailsWithComparisonByOpeningIdAndHistoryId(
      Long openingId,
      Long historyId
  );

  List<OpeningStockingHistoryDetailsProjection> getOpeningStockingHistoryDetailsByOpeningIdAndEventHistoryId(
      Long openingId,
      Long eventHistoryId
  );

  List<OpeningStockingSpeciesHistoryProjection> getOpeningStockingSpeciesHistoryByOpeningIdAndEventHistoryId(
      Long openingId,
      Long eventHistoryId,
      String preferred,
      Long ssuId
  );
  List<OpeningStockingLayerHistoryProjection> getOpeningStockingLayerHistoryByOpeningIdAndEventHistoryId(
      Long openingId,
      Long eventHistoryId,
      Long ssuId
  );
}
