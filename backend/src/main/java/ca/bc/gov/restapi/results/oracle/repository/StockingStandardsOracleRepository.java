package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.dto.StockingStandardsSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardsCommentSearchFilterDto;
import ca.bc.gov.restapi.results.common.projection.StockingStandardsCommentSearchProjection;
import ca.bc.gov.restapi.results.common.projection.StockingStandardsSearchProjection;
import ca.bc.gov.restapi.results.common.repository.StockingStandardsRepository;
import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.oracle.entity.StandardsRegimeEntity;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public interface StockingStandardsOracleRepository
    extends JpaRepository<StandardsRegimeEntity, Long>, StockingStandardsRepository {

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.STOCKING_STANDARDS_SEARCH)
  List<StockingStandardsSearchProjection> stockingStandardsSearch(
      @Param("filter") StockingStandardsSearchFilterDto filters,
      @Param("page") long offset,
      @Param("size") long size);

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.STOCKING_STANDARDS_COMMENT_SEARCH)
  List<StockingStandardsCommentSearchProjection> stockingStandardsCommentSearch(
      @Param("filter") StockingStandardsCommentSearchFilterDto filters,
      @Param("page") long offset,
      @Param("size") long size);
}
