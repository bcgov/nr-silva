import { useEffect, useState } from "react";
import { Column, Checkbox, Grid, Button, Stack, InlineLoading, TableToolbarMenu, Table, TableRow, TableHead, TableBody, TableHeader, Pagination } from "@carbon/react";
import StandardsUnitSearchInput from "./StandardsUnitSearchInput";
import { StandardsUnitSearchParams } from "@/types/ApiType";
import { DEFAULT_PAGE_NUM, MAX_PAGINATION_PAGES, PageSizesConfig } from "@/constants/tableConstants";
import { MAP_KINDS } from "@/constants/mapKindConstants";
import { CircleDash, Search } from "@carbon/icons-react";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { isAuthRefreshInProgress } from "@/constants/tanstackConfig";
import TableSkeleton from "@/components/TableSkeleton";
import { StandardsUnitHeaderKeyType, StandardsUnitHeaderType } from "@/types/TableHeader";
import EmptySection from "@/components/EmptySection";
import { MapKindType } from "@/types/MapLayer";
import { PaginationOnChangeType } from "@/types/GeneralTypes";
import OpeningsMap from "../OpeningsMap";
import StandardsUnitSearchTableRow from "./StandardsUnitSearchTableRow";
import { readStandardsUnitSearchUrlParams, updateStandardsUnitSearchUrlParams, hasStandardsUnitSearchFilters } from "./utils";
import { defaultStandardsUnitSearchTableHeaders } from "./constants";

import "./styles.scss";


const StandardsUnitSearchSection = () => {
  const [searchParams, setSearchParams] = useState<StandardsUnitSearchParams>();
  const [queryParams, setQueryParams] = useState<StandardsUnitSearchParams>();
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [currPageNumber, setCurrPageNumber] = useState<number>(DEFAULT_PAGE_NUM);
  const [currPageSize, setCurrPageSize] = useState<number>(() => PageSizesConfig[0]!);
  const [selectedStandardsUnitIds, setSelectedStandardsUnitIds] = useState<string[]>([]);

  const standardsUnitKinds = [MAP_KINDS.standardsUnit] as MapKindType[];

  const [searchTableHeaders, setSearchTableHeaders] = useState<StandardsUnitHeaderType[]>(() => structuredClone(defaultStandardsUnitSearchTableHeaders));

  const standardsUnitSearchQuery = useQuery({
    queryKey: ['search', 'standards-unit', queryParams],
    queryFn: () => API.SearchEndpointService.standardsUnitSearch(
      queryParams?.standardsRegimeId,
      queryParams?.preferredSpecies,
      queryParams?.orgUnits,
      queryParams?.clientNumbers,
      queryParams?.bgcZone,
      queryParams?.bgcSubZone,
      queryParams?.bgcVariant,
      queryParams?.bgcPhase,
      queryParams?.becSiteSeries,
      queryParams?.becSiteType,
      queryParams?.becSeral,
      queryParams?.updateDateStart,
      queryParams?.updateDateEnd,
      queryParams?.page,
      queryParams?.size ?? 20,
      queryParams?.sort,
    ),
    enabled: !!queryParams,
  });

  // On page load, read URL params and prefill search (one time)
  useEffect(() => {
    const urlParams = readStandardsUnitSearchUrlParams();
    if (hasStandardsUnitSearchFilters(urlParams) || urlParams.page !== undefined || urlParams.size !== undefined) {
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

  const handleSearchFieldChange = (field: keyof StandardsUnitSearchParams, value: unknown) => {
    setSearchParams((prev) => ({
      ...prev,
      [field]: (value === '' || value === null) ? undefined : value,
    } as StandardsUnitSearchParams));
  };

  const handleSearch = () => {
    const paramsWithPagination: StandardsUnitSearchParams = {
      ...searchParams,
      page: DEFAULT_PAGE_NUM,
      size: PageSizesConfig[0]!,
    };

    if (!hasStandardsUnitSearchFilters(paramsWithPagination)) {
      return;
    }

    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    setSearchParams(paramsWithPagination);
    setQueryParams(paramsWithPagination);
    setSelectedOpeningIds([]);
    setSelectedStandardsUnitIds([]);
    updateStandardsUnitSearchUrlParams(paramsWithPagination);
  };

  const handleReset = () => {
    setSearchParams(undefined);
    setQueryParams(undefined);
    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    setSelectedOpeningIds([]);
    setSelectedStandardsUnitIds([]);
    updateStandardsUnitSearchUrlParams(undefined);
  };

  const toggleColumn = (key: StandardsUnitHeaderKeyType) => {
    if (key !== "openingId" && key !== "actions") {
      setSearchTableHeaders((prevHeaders) =>
        prevHeaders.map((header) =>
          header.key === key
            ? { ...header, selected: !header.selected }
            : header
        )
      );
    }
  };

  const handlePagination = (paginationObj: PaginationOnChangeType) => {
    // Convert to 0 based index
    const nextPageNum = paginationObj.page - 1;
    const nextPageSize = paginationObj.pageSize;
    if (!queryParams) {
      return;
    }

    const paramsWithPagination: StandardsUnitSearchParams = {
      ...queryParams,
      page: nextPageNum,
      size: nextPageSize,
    };

    setCurrPageNumber(nextPageNum);
    setCurrPageSize(nextPageSize);
    setSelectedOpeningIds([]);
    setQueryParams(paramsWithPagination);
    updateStandardsUnitSearchUrlParams(paramsWithPagination);
  };

  const handleRowSelection = (openingId: number, compoundId: string) => {
    if (selectedStandardsUnitIds.includes(compoundId)) {
      setSelectedStandardsUnitIds([]);
      setSelectedOpeningIds([]);
      return;
    }
    setSelectedStandardsUnitIds([compoundId]);
    setSelectedOpeningIds([openingId]);
  };

  return (
    <Grid className="default-grid standards-unit-search-section">
      <Column sm={4} md={8} lg={16}>
        <StandardsUnitSearchInput searchParams={searchParams} handleSearchFieldChange={handleSearchFieldChange} />
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
        hasStandardsUnitSearchFilters(queryParams) ? (
          <Column className="full-width-col" sm={4} md={8} lg={16}>
            <>
              {
                selectedStandardsUnitIds.length > 0
                  ? (
                    <OpeningsMap
                      openingIds={selectedOpeningIds}
                      setOpeningPolygonNotFound={() => { }}
                      mapHeight={480}
                      layerFilter={true}
                      kind={standardsUnitKinds}
                      isStandardsUnitMap
                      selectedStandardsUnitIds={selectedStandardsUnitIds}
                      selectedForestCoverIds={[]}
                      selectedSilvicultureActivityIds={[]}
                      selectedDisturbanceIds={[]}
                    />
                  )
                  : null
              }
              <div className="search-table-banner">
                <Stack className="search-result-title-section" orientation="vertical">
                  <h5 className="search-result-title">Search results</h5>

                  <Stack className="search-result-sub-title-section" orientation="horizontal" gap={standardsUnitSearchQuery.isLoading && !isAuthRefreshInProgress() ? 4 : 2}>
                    <p className="search-result-subtitle">Total search results:</p>
                    {
                      standardsUnitSearchQuery.isLoading && !isAuthRefreshInProgress()
                        ? <InlineLoading />
                        : <p className="search-result-subtitle">{standardsUnitSearchQuery.data?.page?.totalElements ?? 0}</p>
                    }
                  </Stack>
                </Stack>

                <TableToolbarMenu
                  className="edit-col-button"
                  iconDescription="Edit columns"
                  menuOptionsClass="opening-search-action-menu-option"
                  renderIcon={() => <p>Edit columns</p>}
                >
                  <div className="opening-search-action-menu-option-item">
                    <div className="helper-text">
                      Select columns you want to see
                    </div>
                    {
                      searchTableHeaders.map((header) =>
                        header.key !== "actions" ? (
                          <Checkbox
                            key={header.key}
                            className="column-checkbox"
                            id={`${header.key}-checkbox`}
                            labelText={header.header}
                            checked={header.selected}
                            onChange={() => toggleColumn(header.key)}
                            readOnly={header.key === "openingId"}
                          />
                        ) : null
                      )
                    }
                  </div>
                </TableToolbarMenu>
              </div>

              {/* Table skeleton */}
              {
                (standardsUnitSearchQuery.isLoading || isAuthRefreshInProgress())
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
                !standardsUnitSearchQuery.isLoading && !isAuthRefreshInProgress()
                  ? (
                    <Table
                      className="opening-search-table default-zebra-table"
                      aria-label="Standards unit search table"
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
                          standardsUnitSearchQuery.data?.content?.map((row) => (
                            <StandardsUnitSearchTableRow
                              key={row.stockingStandardUnitId}
                              headers={searchTableHeaders}
                              rowData={row}
                              showMap={true}
                              selectedRows={selectedStandardsUnitIds.map((id) => Number(id))}
                              handleRowSelection={handleRowSelection}
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
                !standardsUnitSearchQuery.isLoading && !isAuthRefreshInProgress()
                  ? (
                    standardsUnitSearchQuery.data?.page?.totalElements &&
                      standardsUnitSearchQuery.data?.page.totalElements > 0 ? (
                      <Pagination
                        className="default-pagination-white"
                        page={currPageNumber + 1}
                        pageSize={currPageSize}
                        pageSizes={PageSizesConfig}
                        totalItems={standardsUnitSearchQuery.data?.page.totalElements}
                        onChange={handlePagination}
                        pagesUnknown={standardsUnitSearchQuery.data?.page.totalElements > MAX_PAGINATION_PAGES * currPageSize}
                      />
                    ) : (
                      <EmptySection
                        pictogram="UserSearch"
                        title="No results"
                        description="Consider adjusting your search term(s) and try again."
                      />
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

export default StandardsUnitSearchSection;

