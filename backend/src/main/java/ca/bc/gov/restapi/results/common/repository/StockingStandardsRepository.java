package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.dto.StockingStandardsSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardsCommentSearchFilterDto;
import ca.bc.gov.restapi.results.common.projection.StockingStandardsCommentSearchProjection;
import ca.bc.gov.restapi.results.common.projection.StockingStandardsSearchProjection;
import java.util.List;

/** Database-specific stocking standards queries exposed to the common service. */
public interface StockingStandardsRepository {
  List<StockingStandardsSearchProjection> stockingStandardsSearch(
      StockingStandardsSearchFilterDto filters, long offset, long size);

  List<StockingStandardsCommentSearchProjection> searchStockingStandardsComments(
      StockingStandardsCommentSearchFilterDto filters, long offset, long size);

}
