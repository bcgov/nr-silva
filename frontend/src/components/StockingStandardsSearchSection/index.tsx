import { useEffect, useRef, useState } from "react";
import { Column, Checkbox, Grid, Button, Stack, InlineLoading, TableToolbarMenu, Table, TableRow, TableHead, TableBody, TableHeader, Pagination } from "@carbon/react";
import StockingStandardsSearchInput from "./StockingStandardsSearchInput";
import { StockingStandardsSearchParams } from "@/types/ApiType";
import { DEFAULT_PAGE_NUM, MAX_PAGINATION_PAGES, PageSizesConfig } from "@/constants/tableConstants";
import { CircleDash, Search } from "@carbon/icons-react";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { isAuthRefreshInProgress } from "@/constants/tanstackConfig";
import TableSkeleton from "@/components/TableSkeleton";
import { StockingStandardsHeaderKeyType, StockingStandardsHeaderType } from "@/types/TableHeader";
import EmptySection from "@/components/EmptySection";
import { PaginationOnChangeType } from "@/types/GeneralTypes";
import StockingStandardsSearchTableRow from "./StockingStandardsSearchTableRow";
import { readStockingStandardsSearchUrlParams, updateStockingStandardsSearchUrlParams, hasStockingStandardsSearchFilters } from "./utils";
import useScrollToSearchResults from "@/hooks/useScrollToSearchResults";
import { defaultStockingStandardsSearchTableHeaders } from "./constants";

import "./styles.scss";


const StockingStandardsSearchSection = () => {
  const [searchParams, setSearchParams] = useState<StockingStandardsSearchParams>();
  const [queryParams, setQueryParams] = useState<StockingStandardsSearchParams>();
  const [currPageNumber, setCurrPageNumber] = useState<number>(DEFAULT_PAGE_NUM);
  const [currPageSize, setCurrPageSize] = useState<number>(() => PageSizesConfig[0]!);
  const resultsRef = useRef<HTMLDivElement>(null);
  const shouldScrollRef = useRef(false);
  const emptyResultsRef = useRef<HTMLDivElement>(null);

  const [searchTableHeaders, setSearchTableHeaders] = useState<StockingStandardsHeaderType[]>(() => structuredClone(defaultStockingStandardsSearchTableHeaders));

  const stockingStandardsSearchQuery = useQuery({
    queryKey: ['search', 'stocking-standards', queryParams],
    queryFn: () => API.SearchEndpointService.stockingStandardsSearch(
      queryParams?.standardsRegimeId,
      queryParams?.preferredSpecies,
      queryParams?.orgUnits,
      queryParams?.clientNumbers,
      queryParams?.fspId,
      queryParams?.bgcZone,
      queryParams?.bgcSubZone,
      queryParams?.bgcVariant,
      queryParams?.bgcPhase,
      queryParams?.becSiteSeries,
      queryParams?.becSiteType,
      undefined, // becSeral — no input
      queryParams?.updateDateStart,
      queryParams?.updateDateEnd,
      queryParams?.page,
      queryParams?.size ?? 20,
      queryParams?.sort,
    ),
    enabled: !!queryParams && hasStockingStandardsSearchFilters(queryParams),
  });

  // On page load, read URL params and prefill search (one time)
  useEffect(() => {
    const urlParams = readStockingStandardsSearchUrlParams();
    if (hasStockingStandardsSearchFilters(urlParams)) {
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

  useScrollToSearchResults(resultsRef, emptyResultsRef, shouldScrollRef, stockingStandardsSearchQuery.isLoading, stockingStandardsSearchQuery.data, stockingStandardsSearchQuery.data?.page?.totalElements);

  const handleSearchFieldChange = (field: keyof StockingStandardsSearchParams, value: unknown) => {
    setSearchParams((prev) => ({
      ...prev,
      [field]: (value === '' || value === null) ? undefined : value,
    } as StockingStandardsSearchParams));
  };

  const handleSearch = () => {
    const paramsWithPagination: StockingStandardsSearchParams = {
      ...searchParams,
      page: DEFAULT_PAGE_NUM,
      size: PageSizesConfig[0]!,
    };

    if (!hasStockingStandardsSearchFilters(paramsWithPagination)) {
      return;
    }

    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    setSearchParams(paramsWithPagination);
    setQueryParams(paramsWithPagination);
    updateStockingStandardsSearchUrlParams(paramsWithPagination);
    shouldScrollRef.current = true;
  };

  const handleReset = () => {
    setSearchParams(undefined);
    setQueryParams(undefined);
    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    updateStockingStandardsSearchUrlParams(undefined);
  };

  const toggleColumn = (key: StockingStandardsHeaderKeyType) => {
    setSearchTableHeaders((prevHeaders) =>
      prevHeaders.map((header) =>
        header.key === key
          ? { ...header, selected: !header.selected }
          : header
      )
    );
  };

  const handlePagination = (paginationObj: PaginationOnChangeType) => {
    // Convert to 0 based index
    const nextPageNum = paginationObj.page - 1;
    const nextPageSize = paginationObj.pageSize;
    if (!queryParams) {
      return;
    }

    const paramsWithPagination: StockingStandardsSearchParams = {
      ...queryParams,
      page: nextPageNum,
      size: nextPageSize,
    };

    setCurrPageNumber(nextPageNum);
    setCurrPageSize(nextPageSize);
    setQueryParams(paramsWithPagination);
    updateStockingStandardsSearchUrlParams(paramsWithPagination);
  };

  return (
    <Grid className="default-grid stocking-standards-search-section">
      <Column sm={4} md={8} lg={16}>
        <StockingStandardsSearchInput searchParams={searchParams} queryParams={queryParams} handleSearchFieldChange={handleSearchFieldChange} />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Grid className="default-sub-grid">
          <Column sm={4} md={4} lg={6} max={4}>
            <Button className="default-button" onClick={handleReset} kind="secondary" renderIcon={CircleDash}>
              Clear all
            </Button>
          </Column>
          <Column sm={4} md={4} lg={6} max={4}>
            <Button className="default-button" onClick={handleSearch} renderIcon={Search}>
              Search
            </Button>
          </Column>
        </Grid>
      </Column>

      {
        hasStockingStandardsSearchFilters(queryParams) ? (
          <Column className="full-width-col" sm={4} md={8} lg={16}>
            <>
              <div className="search-table-banner" ref={resultsRef}>
                <Stack className="search-result-title-section" orientation="vertical">
                  <h5 className="search-result-title">Search results</h5>

                  <Stack className="search-result-sub-title-section" orientation="horizontal" gap={stockingStandardsSearchQuery.isLoading && !isAuthRefreshInProgress() ? 4 : 2}>
                    <p className="search-result-subtitle">Total search results:</p>
                    {
                      stockingStandardsSearchQuery.isLoading && !isAuthRefreshInProgress()
                        ? <InlineLoading />
                        : <p className="search-result-subtitle">{stockingStandardsSearchQuery.data?.page?.totalElements ?? 0}</p>
                    }
                  </Stack>
                </Stack>

                <TableToolbarMenu
                  className="edit-col-button"
                  iconDescription="Edit columns"
                  menuOptionsClass="default-search-action-menu-option"
                  renderIcon={() => <p>Edit columns</p>}
                >
                  <div className="default-search-action-menu-option-item">
                    <div className="helper-text">
                      Select columns you want to see
                    </div>
                    {
                      searchTableHeaders.map((header) => (
                        <Checkbox
                          key={header.key}
                          className="column-checkbox"
                          id={`${header.key}-checkbox`}
                          labelText={header.header}
                          checked={header.selected}
                          onChange={() => toggleColumn(header.key)}
                        />
                      ))
                    }
                  </div>
                </TableToolbarMenu>
              </div>

              {/* Table skeleton */}
              {
                (stockingStandardsSearchQuery.isLoading || isAuthRefreshInProgress())
                  ? (
                    <TableSkeleton
                      headers={searchTableHeaders}
                      showToolbar={false}
                      showHeader={false}
                    />
                  )
                  : null
              }
              {
                !stockingStandardsSearchQuery.isLoading && !isAuthRefreshInProgress()
                  ? (
                    <Table
                      className="opening-search-table default-zebra-table"
                      aria-label="Stocking standards search table"
                      useZebraStyles
                    >
                      <TableHead>
                        <TableRow>
                          {searchTableHeaders
                            .filter((header) => header.selected)
                            .map((header) => (
                              <TableHeader key={header.key}>
                                {header.header}
                              </TableHeader>
                            ))}
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {
                          stockingStandardsSearchQuery.data?.content?.map((row, index) => (
                            <StockingStandardsSearchTableRow
                              key={row.standardsRegimeId ?? index}
                              headers={searchTableHeaders}
                              rowData={row}
                            />
                          ))
                        }
                      </TableBody>
                    </Table>
                  )
                  : null
              }

              {/* Display either pagination or empty message */}
              {
                !stockingStandardsSearchQuery.isLoading && !isAuthRefreshInProgress()
                  ? (
                    stockingStandardsSearchQuery.data?.page?.totalElements &&
                      stockingStandardsSearchQuery.data?.page.totalElements > 0 ? (
                      <Pagination
                        className="default-pagination-white"
                        page={currPageNumber + 1}
                        pageSize={currPageSize}
                        pageSizes={PageSizesConfig}
                        totalItems={stockingStandardsSearchQuery.data?.page.totalElements}
                        onChange={handlePagination}
                        pagesUnknown={stockingStandardsSearchQuery.data?.page.totalElements > MAX_PAGINATION_PAGES * currPageSize}
                      />
                    ) : (
                      <div ref={emptyResultsRef}>
                        <EmptySection
                          pictogram="UserSearch"
                          title="No results"
                          description="Consider adjusting your search term(s) and try again."
                        />
                      </div>
                    )
                  )
                  : null
              }
            </>
          </Column>
        )
          : null
      }
    </Grid>
  );
};

export default StockingStandardsSearchSection;
