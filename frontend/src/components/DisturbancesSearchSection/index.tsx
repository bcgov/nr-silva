import { useEffect, useRef, useState } from "react";
import { Column, Checkbox, Grid, Button, Stack, InlineLoading, TableToolbarMenu, Table, TableRow, TableHead, TableBody, TableHeader, Pagination } from "@carbon/react";
import { DisturbanceSearchParams } from "@/types/ApiType";
import { DEFAULT_PAGE_NUM, MAX_PAGINATION_PAGES, PageSizesConfig } from "@/constants/tableConstants";
import { MAP_KINDS } from "@/constants/mapKindConstants";
import { CircleDash, Search } from "@carbon/icons-react";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { isAuthRefreshInProgress } from "@/constants/tanstackConfig";
import TableSkeleton from "@/components/TableSkeleton";
import { DisturbanceHeaderKeyType, DisturbanceHeaderType } from "@/types/TableHeader";
import EmptySection from "@/components/EmptySection";
import { mapKinds, MapKindType } from "@/types/MapLayer";
import { PaginationOnChangeType } from "@/types/GeneralTypes";
import OpeningsMap from "../OpeningsMap";
import DisturbanceSearchTableRow from "./DisturbanceSearchTableRow";
import { readDisturbanceSearchUrlParams, updateDisturbanceSearchUrlParams, hasDisturbanceSearchFilters } from "./utils";
import useScrollToSearchResults from "@/hooks/useScrollToSearchResults";
import { defaultDisturbanceSearchTableHeaders } from "./constants";
import DisturbanceSearchInput from "./DisturbanceSearchInput";

const DisturbancesSearchSection = () => {
  const [searchParams, setSearchParams] = useState<DisturbanceSearchParams>();
  const [queryParams, setQueryParams] = useState<DisturbanceSearchParams>();
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [currPageNumber, setCurrPageNumber] = useState<number>(DEFAULT_PAGE_NUM);
  const [currPageSize, setCurrPageSize] = useState<number>(() => PageSizesConfig[0]!);
  const [selectedDisturbanceIds, setSelectedDisturbanceIds] = useState<string[]>([]);
  const resultsRef = useRef<HTMLDivElement>(null);
  const shouldScrollRef = useRef(false);
  const disturbanceLayerConfig = mapKinds.find((kind) => kind.code === MAP_KINDS.activityTreatment)!;

  const [searchTableHeaders, setSearchTableHeaders] = useState<DisturbanceHeaderType[]>(() => structuredClone(defaultDisturbanceSearchTableHeaders));

  const disturbanceSearchQuery = useQuery({
    queryKey: ['search', 'disturbances', queryParams],
    queryFn: () => API.SearchEndpointService.disturbanceSearch(
      queryParams?.disturbances,
      queryParams?.silvSystems,
      queryParams?.variants,
      queryParams?.cutPhases,
      queryParams?.orgUnits,
      queryParams?.openingCategories,
      queryParams?.fileId,
      queryParams?.clientNumbers,
      queryParams?.openingStatuses,
      queryParams?.updateDateStart,
      queryParams?.updateDateEnd,
      queryParams?.page,
      queryParams?.size,
      queryParams?.sort,
    ),
    enabled: !!queryParams,
  });

  // On page load, read URL params and prefill search (one time)
  useEffect(() => {
    const urlParams = readDisturbanceSearchUrlParams();
    if (hasDisturbanceSearchFilters(urlParams) || urlParams.page !== undefined || urlParams.size !== undefined) {
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

  useScrollToSearchResults(resultsRef, shouldScrollRef, disturbanceSearchQuery.isLoading, disturbanceSearchQuery.data);

  const handleSearchFieldChange = (field: keyof DisturbanceSearchParams, value: unknown) => {
    setSearchParams((prev) => ({
      ...prev,
      [field]: (value === '' || value === null) ? undefined : value,
    }));
  };

  const handleSearch = () => {
    const paramsWithPagination: DisturbanceSearchParams = {
      ...searchParams,
      page: DEFAULT_PAGE_NUM,
      size: PageSizesConfig[0]!,
    };

    if (!hasDisturbanceSearchFilters(paramsWithPagination)) {
      return;
    }

    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    setSearchParams(paramsWithPagination);
    setQueryParams(paramsWithPagination);
    setSelectedOpeningIds([]);
    setSelectedDisturbanceIds([]);
    updateDisturbanceSearchUrlParams(paramsWithPagination);
    shouldScrollRef.current = true;
  };

  const handleReset = () => {
    setSearchParams(undefined);
    setQueryParams(undefined);
    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    setSelectedOpeningIds([]);
    setSelectedDisturbanceIds([]);
    updateDisturbanceSearchUrlParams(undefined);
  };

  const toggleColumn = (key: DisturbanceHeaderKeyType) => {
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

    const paramsWithPagination: DisturbanceSearchParams = {
      ...queryParams,
      page: nextPageNum,
      size: nextPageSize,
    };

    setCurrPageNumber(nextPageNum);
    setCurrPageSize(nextPageSize);
    setSelectedOpeningIds([]);
    setQueryParams(paramsWithPagination);
    updateDisturbanceSearchUrlParams(paramsWithPagination);
  };

  const handleRowSelection = (openingId: number, compoundId: string) => {
    if (selectedDisturbanceIds.includes(compoundId)) {
      setSelectedDisturbanceIds([]);
      setSelectedOpeningIds([]);
      return;
    }
    setSelectedDisturbanceIds([compoundId]);
    setSelectedOpeningIds([openingId]);
  };

  return (
    <Grid className="default-grid activity-search-section-grid">
      <Column sm={4} md={8} lg={16}>
        <DisturbanceSearchInput searchParams={searchParams} handleSearchFieldChange={handleSearchFieldChange} />
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
        hasDisturbanceSearchFilters(queryParams) ? (
          <Column className="full-width-col" sm={4} md={8} lg={16}>
            <>
              {
                selectedDisturbanceIds.length > 0
                  ? (
                    <OpeningsMap
                      openingIds={selectedOpeningIds}
                      setOpeningPolygonNotFound={() => { }}
                      mapHeight={480}
                      layerFilter={true}
                      kind={[disturbanceLayerConfig.code] as MapKindType[]}
                      isActivitiesMap
                      selectedSilvicultureActivityIds={[]}
                      selectedDisturbanceIds={selectedDisturbanceIds}
                    />
                  )
                  : null
              }
              <div className="search-table-banner" ref={resultsRef}>
                <Stack className="search-result-title-section" orientation="vertical">
                  <h5 className="search-result-title">Search results</h5>

                  <Stack className="search-result-sub-title-section" orientation="horizontal" gap={disturbanceSearchQuery.isLoading && !isAuthRefreshInProgress() ? 4 : 2}>
                    <p className="search-result-subtitle">Total search results:</p>
                    {
                      disturbanceSearchQuery.isLoading && !isAuthRefreshInProgress()
                        ? <InlineLoading />
                        : <p className="search-result-subtitle">{disturbanceSearchQuery.data?.page?.totalElements ?? 0}</p>
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
                (disturbanceSearchQuery.isLoading || isAuthRefreshInProgress())
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
                !disturbanceSearchQuery.isLoading && !isAuthRefreshInProgress()
                  ? (
                    <Table
                      className="opening-search-table default-zebra-table"
                      aria-label="Disturbance search table"
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
                          disturbanceSearchQuery.data?.content?.map((row) => (
                            <DisturbanceSearchTableRow
                              key={row.activityId}
                              headers={searchTableHeaders}
                              rowData={row}
                              showMap={true}
                              selectedRows={selectedDisturbanceIds.map((id) => Number(id.split('-')[0]))}
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
                !disturbanceSearchQuery.isLoading && !isAuthRefreshInProgress()
                  ? (
                    disturbanceSearchQuery.data?.page?.totalElements &&
                      disturbanceSearchQuery.data?.page.totalElements > 0 ? (
                      <Pagination
                        className="default-pagination-white"
                        page={currPageNumber + 1}
                        pageSize={currPageSize}
                        pageSizes={PageSizesConfig}
                        totalItems={disturbanceSearchQuery.data?.page.totalElements}
                        onChange={handlePagination}
                        pagesUnknown={disturbanceSearchQuery.data?.page.totalElements > MAX_PAGINATION_PAGES * currPageSize}
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

export default DisturbancesSearchSection;

