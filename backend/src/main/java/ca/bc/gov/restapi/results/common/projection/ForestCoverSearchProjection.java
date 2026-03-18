package ca.bc.gov.restapi.results.common.projection;

import java.time.LocalDateTime;

/** Projection interface used for forest cover search native queries. */
public interface ForestCoverSearchProjection {

  Long getForestCoverId();

  String getPolygonId();

  String getStandardUnitId();

  /** Comma-separated damage agent codes aggregated across all layers. */
  String getDamageCodes();

  /**
   * {@code ||}-separated damage agent names aggregated across all layers (aligned with {@link
   * #getDamageCodes()}).
   */
  String getDamageNames();

  String getStockingTypeCode();

  String getStockingTypeName();

  String getStockingStatusCode();

  String getStockingStatusName();

  String getFileId();

  Long getOpeningId();

  String getOpeningCategoryCode();

  String getOpeningCategoryName();

  String getOrgUnitCode();

  String getOrgUnitName();

  LocalDateTime getUpdateTimestamp();

  LocalDateTime getRegenDueDate();

  LocalDateTime getFreeGrowingDueDate();

  Long getTotalCount();
}
