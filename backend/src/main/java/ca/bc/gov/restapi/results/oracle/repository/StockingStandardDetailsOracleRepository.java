package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.projection.stockingstandards.StockingStandardBecProjection;
import ca.bc.gov.restapi.results.common.projection.stockingstandards.StockingStandardDetailsProjection;
import ca.bc.gov.restapi.results.common.projection.stockingstandards.StockingStandardLayerProjection;
import ca.bc.gov.restapi.results.common.projection.stockingstandards.StockingStandardOrgUnitProjection;
import ca.bc.gov.restapi.results.common.projection.stockingstandards.StockingStandardSpeciesProjection;
import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.oracle.entity.StandardsRegimeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Oracle SQL used to retrieve all components of one stocking standard. */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public interface StockingStandardDetailsOracleRepository
    extends org.springframework.data.repository.Repository<StandardsRegimeEntity, Long> {

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.STOCKING_STANDARD_DETAILS)
  Optional<StockingStandardDetailsProjection> findStockingStandardDetails(
      @Param("stockingStandardId") Long stockingStandardId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.STOCKING_STANDARD_ORG_UNITS)
  List<StockingStandardOrgUnitProjection> findStockingStandardOrgUnits(
      @Param("stockingStandardId") Long stockingStandardId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.STOCKING_STANDARD_CLIENT_NUMBERS)
  List<String> findStockingStandardClientNumbers(
      @Param("stockingStandardId") Long stockingStandardId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.STOCKING_STANDARD_BEC_DATA)
  List<StockingStandardBecProjection> findStockingStandardBecData(
      @Param("stockingStandardId") Long stockingStandardId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.STOCKING_STANDARD_LAYERS)
  List<StockingStandardLayerProjection> findStockingStandardLayers(
      @Param("stockingStandardId") Long stockingStandardId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.STOCKING_STANDARD_SPECIES)
  List<StockingStandardSpeciesProjection> findStockingStandardSpecies(
      @Param("stockingStandardId") Long stockingStandardId);
}
