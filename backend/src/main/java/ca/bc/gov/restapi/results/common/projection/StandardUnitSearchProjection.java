package ca.bc.gov.restapi.results.common.projection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Projection interface used for standard unit search native queries. */
public interface StandardUnitSearchProjection {

  Long getStockingStandardUnitId();

  Long getOpeningId();

  String getFileId();

  String getCutBlock();

  String getCuttingPermit();

  String getStandardsUnitId();

  Long getStandardsRegimeId();

  LocalDateTime getStandardsRegimeExpiryDate();

  BigDecimal getNetArea();

  LocalDate getRegenDueDate();

  LocalDate getFreeGrowingDueDate();

  Integer getTotalLayer();

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
