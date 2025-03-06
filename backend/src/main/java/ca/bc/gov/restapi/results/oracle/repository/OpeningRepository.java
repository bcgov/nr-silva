package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.entity.OpeningTrendsProjection;
import ca.bc.gov.restapi.results.oracle.entity.SilvicultureSearchProjection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * This interface allows the service to fetch and save data into the database.
 */
@Repository
public interface OpeningRepository extends JpaRepository<OpeningEntity, Long> {

  @Query(
      value = SilvaOracleQueryConstants.SILVICULTURE_SEARCH_QUERY,
      countQuery = SilvaOracleQueryConstants.SILVICULTURE_SEARCH_COUNT_QUERY,
      nativeQuery = true
  )
  Page<SilvicultureSearchProjection> searchBy(
      OpeningSearchFiltersDto filter,
      List<Long> openingIds,
      Pageable pageable
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
}
