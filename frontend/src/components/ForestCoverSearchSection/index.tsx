import { useEffect, useState } from "react";
import { Column, Checkbox, Grid, Button, Stack, InlineLoading, TableToolbarMenu, Table, TableRow, TableHead, TableBody, TableHeader, Pagination } from "@carbon/react";
import ForestCoverSearchInput from "./ForestCoverSearchInput";
import { ForestCoverSearchParams } from "@/types/ApiType";
import { DEFAULT_PAGE_NUM, MAX_PAGINATION_PAGES, PageSizesConfig } from "@/constants/tableConstants";
import { MAP_KINDS } from "@/constants/mapKindConstants";
import { CircleDash, Search } from "@carbon/icons-react";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { isAuthRefreshInProgress } from "@/constants/tanstackConfig";
import TableSkeleton from "@/components/TableSkeleton";
import { ForestCoverHeaderKeyType, ForestCoverHeaderType } from "@/types/TableHeader";
import EmptySection from "@/components/EmptySection";
import { MapKindType } from "@/types/MapLayer";
import { PaginationOnChangeType } from "@/types/GeneralTypes";
import OpeningsMap from "../OpeningsMap";
import ForestCoverSearchTableRow from "./ForestCoverSearchTableRow";
import { readForestCoverSearchUrlParams, updateForestCoverSearchUrlParams, hasForestCoverSearchFilters } from "./utils";
import { defaultForestCoverSearchTableHeaders } from "./constants";

const ForestCoverSearchSection = () => {
  const [searchParams, setSearchParams] = useState<ForestCoverSearchParams>();
  const [queryParams, setQueryParams] = useState<ForestCoverSearchParams>();
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [currPageNumber, setCurrPageNumber] = useState<number>(DEFAULT_PAGE_NUM);
  const [currPageSize, setCurrPageSize] = useState<number>(() => PageSizesConfig[0]!);
  const [selectedForestCoverIds, setSelectedForestCoverIds] = useState<string[]>([]);

  const forestCoverKinds = [
    MAP_KINDS.forestCoverInventory,
    MAP_KINDS.forestCoverReserve,
    MAP_KINDS.forestCoverSilviculture,
  ] as MapKindType[];

  const [searchTableHeaders, setSearchTableHeaders] = useState<ForestCoverHeaderType[]>(() => structuredClone(defaultForestCoverSearchTableHeaders));

  const forestCoverSearchQuery = useQuery({
    queryKey: ['search', 'forest-cover', queryParams],
    queryFn: () => API.SearchEndpointService.forestCoverSearch(
      queryParams?.stockingStatuses,
      queryParams?.stockingTypes,
      queryParams?.damageAgents,
      queryParams?.openingStatuses,
      queryParams?.fileId,
      queryParams?.orgUnits,
      queryParams?.openingCategories,
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
    const urlParams = readForestCoverSearchUrlParams();
    if (hasForestCoverSearchFilters(urlParams) || urlParams.page !== undefined || urlParams.size !== undefined) {
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

  const handleSearchFieldChange = (field: keyof ForestCoverSearchParams, value: unknown) => {
    setSearchParams((prev) => ({
      ...prev,
      [field]: (value === '' || value === null) ? undefined : value,
    } as ForestCoverSearchParams));
  };

  const handleSearch = () => {
    const paramsWithPagination: ForestCoverSearchParams = {
      ...searchParams,
      page: DEFAULT_PAGE_NUM,
      size: PageSizesConfig[0]!,
    };

    if (!hasForestCoverSearchFilters(paramsWithPagination)) {
      return;
    }

    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    setSearchParams(paramsWithPagination);
    setQueryParams(paramsWithPagination);
    setSelectedOpeningIds([]);
    setSelectedForestCoverIds([]);
    updateForestCoverSearchUrlParams(paramsWithPagination);
  };

  const handleReset = () => {
    setSearchParams(undefined);
    setQueryParams(undefined);
    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    setSelectedOpeningIds([]);
    setSelectedForestCoverIds([]);
    updateForestCoverSearchUrlParams(undefined);
  };

  const toggleColumn = (key: ForestCoverHeaderKeyType) => {
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

    const paramsWithPagination: ForestCoverSearchParams = {
      ...queryParams,
      page: nextPageNum,
      size: nextPageSize,
    };

    setCurrPageNumber(nextPageNum);
    setCurrPageSize(nextPageSize);
    setSelectedOpeningIds([]);
    setQueryParams(paramsWithPagination);
    updateForestCoverSearchUrlParams(paramsWithPagination);
  };

  const handleRowSelection = (openingId: number, compoundId: string) => {
    if (selectedForestCoverIds.includes(compoundId)) {
      setSelectedForestCoverIds([]);
      setSelectedOpeningIds([]);
      return;
    }
    setSelectedForestCoverIds([compoundId]);
    setSelectedOpeningIds([openingId]);
  };

  return (
    <Grid className="default-grid forest-cover-search-section">
      <Column sm={4} md={8} lg={16}>
        <ForestCoverSearchInput searchParams={searchParams} handleSearchFieldChange={handleSearchFieldChange} />
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
        hasForestCoverSearchFilters(queryParams) ? (
          <Column className="full-width-col" sm={4} md={8} lg={16}>
            <>
              {
                selectedForestCoverIds.length > 0
                  ? (
                    <OpeningsMap
                      openingIds={selectedOpeningIds}
                      setOpeningPolygonNotFound={() => { }}
                      mapHeight={480}
                      layerFilter={true}
                      kind={forestCoverKinds}
                      isForestCoverMap
                      selectedForestCoverIds={selectedForestCoverIds}
                      selectedSilvicultureActivityIds={[]}
                      selectedDisturbanceIds={[]}
                    />
                  )
                  : null
              }
              <div className="search-table-banner">
                <Stack className="search-result-title-section" orientation="vertical">
                  <h5 className="search-result-title">Search results</h5>

                  <Stack className="search-result-sub-title-section" orientation="horizontal" gap={forestCoverSearchQuery.isLoading && !isAuthRefreshInProgress() ? 4 : 2}>
                    <p className="search-result-subtitle">Total search results:</p>
                    {
                      forestCoverSearchQuery.isLoading && !isAuthRefreshInProgress()
                        ? <InlineLoading />
                        : <p className="search-result-subtitle">{forestCoverSearchQuery.data?.page?.totalElements ?? 0}</p>
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
                (forestCoverSearchQuery.isLoading || isAuthRefreshInProgress())
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
                !forestCoverSearchQuery.isLoading && !isAuthRefreshInProgress()
                  ? (
                    <Table
                      className="opening-search-table default-zebra-table"
                      aria-label="Forest cover search table"
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
                          forestCoverSearchQuery.data?.content?.map((row) => (
                            <ForestCoverSearchTableRow
                              key={row.forestCoverId}
                              headers={searchTableHeaders}
                              rowData={row}
                              showMap={true}
                              selectedRows={selectedForestCoverIds.map((id) => Number(id.split('-')[0]))}
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
                !forestCoverSearchQuery.isLoading && !isAuthRefreshInProgress()
                  ? (
                    forestCoverSearchQuery.data?.page?.totalElements &&
                      forestCoverSearchQuery.data?.page.totalElements > 0 ? (
                      <Pagination
                        className="default-pagination-white"
                        page={currPageNumber + 1}
                        pageSize={currPageSize}
                        pageSizes={PageSizesConfig}
                        totalItems={forestCoverSearchQuery.data?.page.totalElements}
                        onChange={handlePagination}
                        pagesUnknown={forestCoverSearchQuery.data?.page.totalElements > MAX_PAGINATION_PAGES * currPageSize}
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

export default ForestCoverSearchSection;
