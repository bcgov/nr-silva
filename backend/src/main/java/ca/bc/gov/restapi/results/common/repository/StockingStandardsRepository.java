package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.dto.StockingStandardsSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardsCommentSearchFilterDto;
import ca.bc.gov.restapi.results.common.projection.StockingStandardsCommentSearchProjection;
import ca.bc.gov.restapi.results.common.projection.StockingStandardsSearchProjection;
import java.util.List;

public interface StockingStandardsRepository {
  List<StockingStandardsSearchProjection> stockingStandardsSearch(
      StockingStandardsSearchFilterDto filters, long offset, long size);

  List<StockingStandardsCommentSearchProjection> stockingStandardsCommentSearch(
      StockingStandardsCommentSearchFilterDto filters, long offset, long size);
}
