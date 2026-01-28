package ca.bc.gov.restapi.results.common.projection.opening.history;

import ca.bc.gov.restapi.results.common.projection.opening.OpeningStockingDetailsProjection;

public interface OpeningStockingHistoryDetailsProjection extends OpeningStockingDetailsProjection {
  String getAmendmentComment();
}
