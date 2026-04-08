package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.dto.StandardUnitSearchFilterDto;
import ca.bc.gov.restapi.results.common.projection.StandardUnitSearchProjection;
import ca.bc.gov.restapi.results.common.repository.StandardUnitRepository;
import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.oracle.entity.StockingStandardUnitEntity;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public interface StockingStandardUnitOracleRepository
    extends JpaRepository<StockingStandardUnitEntity, Long>, StandardUnitRepository {

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.STANDARD_UNIT_SEARCH)
  List<StandardUnitSearchProjection> standardUnitSearch(
      @Param("filter") StandardUnitSearchFilterDto filters,
      @Param("page") long offset,
      @Param("size") long size);
}
