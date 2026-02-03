package ca.bc.gov.restapi.results.common.repository;

import java.util.List;
import java.util.Optional;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchExactFiltersDto;
import ca.bc.gov.restapi.results.common.entity.BaseOpeningEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import ca.bc.gov.restapi.results.common.projection.OpeningTrendsProjection;
import ca.bc.gov.restapi.results.common.projection.SilvicultureSearchProjection;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningBaseProjection;
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
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface OpeningRepository<T extends BaseOpeningEntity> extends JpaRepository<T, Long> {

  List<SilvicultureSearchProjection> searchByExact(
      OpeningSearchExactFiltersDto filter,
      @Param("openingIds") List<Long> openingIds,
      @Param("page") long page,
      @Param("size") long size);

  List<SilvicultureSearchProjection> searchByOpeningIds(
      @Param("openingIds") List<Long> openingIds,
      @Param("page") long page,
      @Param("size") long size);

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

  Optional<OpeningBaseProjection> findProjectionById(Long openingId);

  boolean existsById(Long openingId);
}
