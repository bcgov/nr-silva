package ca.bc.gov.restapi.results.oracle.entity.opening.history;

import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingDetailsProjection;

public interface OpeningStockingHistoryDetailsProjection extends OpeningStockingDetailsProjection {
  String getAmendmentComment();
}
