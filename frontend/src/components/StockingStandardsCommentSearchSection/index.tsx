import { useEffect, useRef, useState } from "react";
import { Column, Grid, Button, Stack, InlineLoading, Pagination } from "@carbon/react";
import { CircleDash, Search } from "@carbon/icons-react";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { StockingStandardsCommentSearchParams } from "@/types/ApiType";
import { DEFAULT_PAGE_NUM, PageSizesConfig } from "@/constants/tableConstants";
import { isAuthRefreshInProgress } from "@/constants/tanstackConfig";
import EmptySection from "@/components/EmptySection";
import { PaginationOnChangeType } from "@/types/GeneralTypes";
import useScrollToSearchResults from "@/hooks/useScrollToSearchResults";
import StockingStandardsCommentSearchInput from "./StockingStandardsCommentSearchInput";
import StockingStandardsCommentSearchCard from "./StockingStandardsCommentSearchCard";
import {
  readStockingStandardsCommentSearchUrlParams,
  updateStockingStandardsCommentSearchUrlParams,
  hasStockingStandardsCommentSearchFilters,
} from "./utils";
import { STOCKING_COMMENT_KEYWORD_MIN_LENGTH, STOCKING_COMMENT_KEYWORD_MAX_LENGTH } from "./constants";

import "./styles.scss";

const StockingStandardsCommentSearchSection = () => {
  const [searchParams, setSearchParams] = useState<StockingStandardsCommentSearchParams>();
  const [queryParams, setQueryParams] = useState<StockingStandardsCommentSearchParams>();
  const [showValidation, setShowValidation] = useState(false);
  const [currPageNumber, setCurrPageNumber] = useState<number>(DEFAULT_PAGE_NUM);
  const [currPageSize, setCurrPageSize] = useState<number>(() => PageSizesConfig[0]!);
  const resultsRef = useRef<HTMLDivElement>(null);
  const shouldScrollRef = useRef(false);
  const emptyResultsRef = useRef<HTMLDivElement>(null);

  const isSearchTermValid = (params?: StockingStandardsCommentSearchParams) =>
    !!params?.searchTerm &&
    params.searchTerm.length >= STOCKING_COMMENT_KEYWORD_MIN_LENGTH &&
    params.searchTerm.length <= STOCKING_COMMENT_KEYWORD_MAX_LENGTH;

  const stockingCommentSearchQuery = useQuery({
    queryKey: ['search', 'stocking-standards-comments', queryParams],
    queryFn: () =>
      API.SearchEndpointService.searchStockingStandardsComments(
        queryParams!.searchTerm!,
        queryParams?.commentLocations as Parameters<typeof API.SearchEndpointService.searchStockingStandardsComments>[1] ?? undefined,
        queryParams?.clientNumbers,
        queryParams?.orgUnits,
        queryParams?.updateDateStart,
        queryParams?.updateDateEnd,
        queryParams?.page,
        queryParams?.size ?? PageSizesConfig[0]!,
        queryParams?.sort,
      ),
    enabled: !!queryParams && isSearchTermValid(queryParams),
  });

  // On page load, read URL params and prefill search (one time)
  useEffect(() => {
    const urlParams = readStockingStandardsCommentSearchUrlParams();
    if (hasStockingStandardsCommentSearchFilters(urlParams) || urlParams.page !== undefined || urlParams.size !== undefined) {
      const nextPage = urlParams.page ?? DEFAULT_PAGE_NUM;
      const nextSize = urlParams.size ?? PageSizesConfig[0]!;
      const paramsWithPagination = {
        ...urlParams,
        page: nextPage,
        size: nextSize,
      };

      setSearchParams(paramsWithPagination);
      setCurrPageNumber(nextPage);
      setCurrPageSize(nextSize);
      setQueryParams(paramsWithPagination);
    }
  }, []);

  useScrollToSearchResults(
    resultsRef,
    emptyResultsRef,
    shouldScrollRef,
    stockingCommentSearchQuery.isLoading,
    stockingCommentSearchQuery.data,
    stockingCommentSearchQuery.data?.page?.totalElements,
  );

  const handleSearchFieldChange = (field: keyof StockingStandardsCommentSearchParams, value: unknown) => {
    setSearchParams((prev) => ({
      ...prev,
      [field]: value === '' || value === null ? undefined : value,
    }));
  };

  const handleSearch = () => {
    setShowValidation(true);
    const paramsWithPagination: StockingStandardsCommentSearchParams = {
      ...searchParams,
      page: DEFAULT_PAGE_NUM,
      size: PageSizesConfig[0]!,
    };

    if (!isSearchTermValid(paramsWithPagination)) {
      return;
    }

    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    setSearchParams(paramsWithPagination);
    setQueryParams(paramsWithPagination);
    updateStockingStandardsCommentSearchUrlParams(paramsWithPagination);
    shouldScrollRef.current = true;
  };

  const handleReset = () => {
    setSearchParams(undefined);
    setQueryParams(undefined);
    setShowValidation(false);
    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    updateStockingStandardsCommentSearchUrlParams(undefined);
  };

  const handlePagination = (paginationObj: PaginationOnChangeType) => {
    const nextPageNum = paginationObj.page - 1;
    const nextPageSize = paginationObj.pageSize;
    if (!queryParams) return;

    const paramsWithPagination: StockingStandardsCommentSearchParams = {
      ...queryParams,
      page: nextPageNum,
      size: nextPageSize,
    };

    setCurrPageNumber(nextPageNum);
    setCurrPageSize(nextPageSize);
    setQueryParams(paramsWithPagination);
    updateStockingStandardsCommentSearchUrlParams(paramsWithPagination);
  };

  return (
    <Grid className="default-grid stocking-standards-comment-search-section">
      <Column sm={4} md={8} lg={16}>
        <StockingStandardsCommentSearchInput
          searchParams={searchParams}
          handleSearchFieldChange={handleSearchFieldChange}
          showValidation={showValidation}
          onSearch={handleSearch}
        />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Grid className="default-sub-grid">
          <Column sm={4} md={4} lg={6} max={4}>
            <Button
              className="default-button"
              onClick={handleReset}
              kind="secondary"
              renderIcon={CircleDash}
            >
              Clear all
            </Button>
          </Column>
          <Column sm={4} md={4} lg={6} max={4}>
            <Button
              className="default-button"
              onClick={handleSearch}
              renderIcon={Search}
            >
              Search
            </Button>
          </Column>
        </Grid>
      </Column>

      {isSearchTermValid(queryParams) ? (
        <Column className="full-width-col" sm={4} md={8} lg={16}>
          <div className="search-table-banner" ref={resultsRef}>
            <Stack className="search-result-title-section" orientation="vertical">
              <h5 className="search-result-title">Search results</h5>
              <Stack
                className="search-result-sub-title-section"
                orientation="horizontal"
                gap={stockingCommentSearchQuery.isLoading || isAuthRefreshInProgress() ? 4 : 2}
              >
                <p className="search-result-subtitle">Total search results:</p>
                {stockingCommentSearchQuery.isLoading || isAuthRefreshInProgress() ? (
                  <InlineLoading />
                ) : (
                  <p className="search-result-subtitle">
                    {stockingCommentSearchQuery.data?.page?.totalElements ?? 0}
                  </p>
                )}
              </Stack>
            </Stack>
          </div>

          {!stockingCommentSearchQuery.isLoading && !isAuthRefreshInProgress() ? (
            stockingCommentSearchQuery.data?.page?.totalElements &&
              stockingCommentSearchQuery.data.page.totalElements > 0 ? (
              <>
                {stockingCommentSearchQuery.data?.content?.map((comment, idx) => (
                  <StockingStandardsCommentSearchCard
                    key={`${comment.standardsRegimeId}-${idx}`}
                    commentDto={comment}
                    index={idx}
                  />
                ))}
                <Pagination
                  className="default-pagination-white"
                  page={currPageNumber + 1}
                  pageSize={currPageSize}
                  pageSizes={PageSizesConfig}
                  totalItems={stockingCommentSearchQuery.data?.page?.totalElements ?? 0}
                  onChange={handlePagination}
                />
              </>
            ) : (
              <div ref={emptyResultsRef}>
                <EmptySection
                  title="No results found"
                  description="Try adjusting your search criteria."
                  icon="Search"
                />
              </div>
            )
          ) : null}
        </Column>
      ) : null}
    </Grid>
  );
};

export default StockingStandardsCommentSearchSection;
