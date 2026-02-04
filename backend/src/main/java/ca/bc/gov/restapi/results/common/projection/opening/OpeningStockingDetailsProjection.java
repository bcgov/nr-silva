package ca.bc.gov.restapi.results.common.projection.opening;

public interface OpeningStockingDetailsProjection {

  String getStockingStandardUnit();

  Long getSsuid();

  Long getSrid();

  Boolean getDefaultMof();

  Boolean getManualEntry();

  Long getFspId();

  Float getNetArea();

  Float getSoilDisturbancePercent();

  String getBecZoneCode();

  String getBecSubzoneCode();

  String getBecVariant();

  String getBecPhase();

  String getBecSiteSeries();

  String getBecSiteType();

  String getBecSeral();

  Long getRegenDelay();

  Long getFreeGrowingLate();

  Long getFreeGrowingEarly();

  String getAdditionalStandards();
}
