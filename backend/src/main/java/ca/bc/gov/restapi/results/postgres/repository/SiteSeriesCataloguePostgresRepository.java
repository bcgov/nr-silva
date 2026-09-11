package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.SiteSeriesCatalogueEntity;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface SiteSeriesCataloguePostgresRepository
    extends JpaRepository<SiteSeriesCatalogueEntity, Long> {

  /**
   * Finds site series catalogue rows matching a full BEC combination (zone, subzone, variant,
   * phase, site series), used to validate that a submitted BEC combo is a real BC BEC unit.
   *
   * @param bgcZoneCode BGC zone code
   * @param bgcSubzoneCode BGC subzone code
   * @param variant BGC variant (nullable)
   * @param phase BGC phase (nullable)
   * @param siteSeries site series number
   * @param siteSeriesPhase site-series phase (nullable)
   * @return matching rows, if any
   */
  @Query(
      "SELECT ssc FROM SiteSeriesCatalogueEntity ssc"
          + " JOIN BiogeoclimaticCatalogueEntity bc ON bc.id = ssc.biogeoclimaticCatalogueId"
          + " WHERE bc.becZoneCode = :bgcZoneCode"
          + " AND bc.subzone = :bgcSubzoneCode"
          + " AND (bc.variant = :variant OR (bc.variant IS NULL AND :variant IS NULL))"
          + " AND (bc.phase = :phase OR (bc.phase IS NULL AND :phase IS NULL))"
          + " AND ssc.siteSeries = :siteSeries"
          + " AND (ssc.siteSeriesPhase = :siteSeriesPhase"
          + " OR (ssc.siteSeriesPhase IS NULL AND :siteSeriesPhase IS NULL))")
  List<SiteSeriesCatalogueEntity> findMatchingBecCombo(
      @Param("bgcZoneCode") String bgcZoneCode,
      @Param("bgcSubzoneCode") String bgcSubzoneCode,
      @Param("variant") String variant,
      @Param("phase") String phase,
      @Param("siteSeries") String siteSeries,
      @Param("siteSeriesPhase") String siteSeriesPhase);
}
