package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.entity.OpeningTrendsProjection;
import ca.bc.gov.restapi.results.oracle.entity.SilvicultureSearchProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingDetailsProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingLayerProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingSpeciesProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneOverviewMilestoneProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneOverviewOpeningProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneProjection;
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
  Optional<OpeningStockingLayerProjection> getOpeningStockingLayerByOpeningId(Long openingId, Long ssuId);



}
