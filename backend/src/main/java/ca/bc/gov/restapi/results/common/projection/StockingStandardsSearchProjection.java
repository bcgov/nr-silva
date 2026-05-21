package ca.bc.gov.restapi.results.common.projection;

import java.time.LocalDateTime;

/** Projection interface for stocking standards (standards regime) search native queries. */
public interface StockingStandardsSearchProjection {

  Long getStandardsRegimeId();

  String getStandardsRegimeName();

  LocalDateTime getExpiryDate();

  String getStandardsObjective();

  /** Comma-separated preferred species codes aggregated across regime layers. */
  String getPreferredSpeciesCodes();

  /**
   * {@code ||}-separated preferred species names (aligned with {@link
   * #getPreferredSpeciesCodes()}).
   */
  String getPreferredSpeciesNames();

  /** Comma-separated FSP IDs associated with this regime. */
  String getFspIds();

  String getBgcZone();

  String getBgcSubZone();

  String getBgcVariant();

  String getBgcPhase();

  String getBecSiteSeries();

  String getBecSiteType();

  String getBecSeral();

  /** Comma-separated org unit codes associated with this regime. */
  String getOrgUnitCodes();

  /** {@code ||}-separated org unit names (aligned with {@link #getOrgUnitCodes()}). */
  String getOrgUnitNames();

  /** Comma-separated client numbers associated with this regime. */
  String getClientNumbers();

  LocalDateTime getUpdateTimestamp();

  Long getTotalCount();
}
