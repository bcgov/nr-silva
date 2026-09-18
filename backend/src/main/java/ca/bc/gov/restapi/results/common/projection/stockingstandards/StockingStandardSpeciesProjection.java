package ca.bc.gov.restapi.results.common.projection.stockingstandards;

import java.math.BigDecimal;

/** Species assigned to a stocking standard layer. */
public interface StockingStandardSpeciesProjection {
  Long getLayerId();

  String getSpeciesCode();

  String getSpeciesTypeCode();

  BigDecimal getMinHeight();

  String getRegenMilestoneInd();

  String getFreeGrowingMilestoneInd();
}
