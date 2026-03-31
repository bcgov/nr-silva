package ca.bc.gov.restapi.results.common.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Projection interface used for standard unit search native queries. */
public interface StandardUnitSearchProjection {

  Long getStockingStandardUnitId();

  Long getOpeningId();

  String getFileId();

  String getCutBlock();

  String getCuttingPermit();

  String getStandardsUnitId();

  BigDecimal getNetArea();

  Integer getRegenDelayYears();

  Integer getFreeGrowingEarlyYears();

  Integer getFreeGrowingLateYears();

  Integer getTotalLayer();

  Integer getTargetWellSpacedTrees();

  /** Comma-separated preferred species codes aggregated across layers. */
  String getPreferredSpeciesCodes();

  /**
   * {@code ||}-separated preferred species names (aligned with {@link
   * #getPreferredSpeciesCodes()}).
   */
  String getPreferredSpeciesNames();

  String getBgcZone();

  String getBgcSubZone();

  String getBgcVariant();

  String getBgcPhase();

  String getBecSiteSeries();

  String getBecSiteType();

  String getBecSeral();

  String getOrgUnitCode();

  String getOrgUnitName();

  String getClientNumber();

  String getClientLocation();

  LocalDateTime getUpdateTimestamp();

  Long getTotalCount();
}
