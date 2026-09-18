package ca.bc.gov.restapi.results.common.projection.stockingstandards;

/** BEC row assigned to a stocking standard. */
public interface StockingStandardBecProjection {
  String getBecZoneCode();

  String getBecSubzoneCode();

  String getBecVariant();

  String getBecPhase();

  String getBecSiteSeries();

  String getBecSiteType();

  String getBecSeral();
}
