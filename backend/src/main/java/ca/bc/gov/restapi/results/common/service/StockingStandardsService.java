package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.StockingStandardsSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.StockingStandardsSearchResponseDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardsCommentSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardsCommentSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StockingStandardsService {
  Page<StockingStandardsSearchResponseDto> searchStockingStandards(
      StockingStandardsSearchFilterDto filters, Pageable pagination);

  Page<StockingStandardsCommentSearchResponseDto> stockingStandardsCommentSearch(
      StockingStandardsCommentSearchFilterDto filters, Pageable pagination);
}
