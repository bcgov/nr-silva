package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.dto.StandardUnitSearchFilterDto;
import ca.bc.gov.restapi.results.common.projection.StandardUnitSearchProjection;
import ca.bc.gov.restapi.results.common.repository.StandardUnitRepository;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresQueryConstants;
import ca.bc.gov.restapi.results.postgres.entity.StockingStandardUnitEntity;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface StockingStandardUnitPostgresRepository
    extends JpaRepository<StockingStandardUnitEntity, Long>, StandardUnitRepository {

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.STANDARD_UNIT_SEARCH)
  List<StandardUnitSearchProjection> standardsUnitSearch(
      @Param("filter") StandardUnitSearchFilterDto filters,
      @Param("page") long offset,
      @Param("size") long size);
}
