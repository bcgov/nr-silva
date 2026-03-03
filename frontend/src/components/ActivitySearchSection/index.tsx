import { useEffect, useState } from "react";
import { Column, Checkbox, Grid, Button, Stack, InlineLoading, TableToolbarMenu, Table, TableRow, TableHead, TableBody, TableHeader, Pagination } from "@carbon/react";
import ActivitySearchInput from "@/components/ActivitySearchSection/ActivitySearchInput";
import { ActivitySearchParams } from "@/types/ApiType";
import { DEFAULT_PAGE_NUM, MAX_PAGINATION_PAGES, PageSizesConfig } from "@/constants/tableConstants";
import { CircleDash, Search } from "@carbon/icons-react";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { isAuthRefreshInProgress } from "@/constants/tanstackConfig";
import TableSkeleton from "@/components/TableSkeleton";
import { ActivityHeaderKeyType, ActivityHeaderType } from "@/types/TableHeader";
import EmptySection from "@/components/EmptySection";
import { PaginationOnChangeType } from "@/types/GeneralTypes";

import { readActivitySearchUrlParams, updateActivitySearchUrlParams, hasActivitySearchFilters } from "./utils";
import { defaultActivitySearchTableHeaders } from "./constants";
import ActivitySearchTableRow from "./ActivitySearchTableRow";
import OpeningsMap from "../OpeningsMap";
import { mapKinds, MapKindType } from "../../types/MapLayer";

const ActivitiesSearchSection = () => {
  const [searchParams, setSearchParams] = useState<ActivitySearchParams>();
  const [queryParams, setQueryParams] = useState<ActivitySearchParams>();
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [currPageNumber, setCurrPageNumber] = useState<number>(DEFAULT_PAGE_NUM);
  const [currPageSize, setCurrPageSize] = useState<number>(() => PageSizesConfig[0]!);
  const [availableSilvicultureActivityIds, setAvailableSilvicultureActivityIds] = useState<string[]>([]);
  const [selectedSilvicultureActivityIds, setSelectedSilvicultureActivityIds] = useState<string[]>([]);
  const activityLayerConfig = mapKinds.find((kind) => kind.code === 'WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW')!;

  const [searchTableHeaders, setSearchTableHeaders] = useState<ActivityHeaderType[]>(() => structuredClone(defaultActivitySearchTableHeaders));

  const activitySearchQuery = useQuery({
    queryKey: ['search', 'activities', queryParams],
    queryFn: () => API.SearchEndpointService.activitySearch(
      queryParams?.bases,
      queryParams?.techniques,
      queryParams?.methods,
      queryParams?.isComplete,
      queryParams?.objectives,
      queryParams?.fundingSources,
      queryParams?.orgUnits,
      queryParams?.openingCategories,
      queryParams?.fileId,
      queryParams?.clientNumbers,
      queryParams?.openingStatuses,
      queryParams?.intraAgencyNumber,
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
    const urlParams = readActivitySearchUrlParams();
    if (hasActivitySearchFilters(urlParams) || urlParams.page !== undefined || urlParams.size !== undefined) {
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

  const handleSearchFieldChange = (field: keyof ActivitySearchParams, value: unknown) => {
    setSearchParams((prev) => ({
      ...prev,
      [field]: (value === '' || value === null) ? undefined : value,
    }));
  };

  /**
   * Trigger search manually
   */
  const handleSearch = () => {
    const paramsWithPagination = {
      ...(searchParams ?? {}),
      page: DEFAULT_PAGE_NUM,
      size: PageSizesConfig[0]!,
    } as ActivitySearchParams;

    if (!hasActivitySearchFilters(paramsWithPagination)) {
      return;
    }

    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    setSearchParams(paramsWithPagination);
    setQueryParams(paramsWithPagination);
    setSelectedOpeningIds([]);
    updateActivitySearchUrlParams(paramsWithPagination);
  };

  /**
   * Reset all search params
   */
  const handleReset = () => {
    setSearchParams(undefined);
    setQueryParams(undefined);
    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    setSelectedOpeningIds([]);
    updateActivitySearchUrlParams(undefined);
  };

  const toggleColumn = (key: ActivityHeaderKeyType) => {
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

    const paramsWithPagination: ActivitySearchParams = {
      ...queryParams,
      page: nextPageNum,
      size: nextPageSize,
    };

    setCurrPageNumber(nextPageNum);
    setCurrPageSize(nextPageSize);
    setSelectedOpeningIds([]);
    setQueryParams(paramsWithPagination);
    updateActivitySearchUrlParams(paramsWithPagination);
  };

  const handleRowSelection = (openingId: number, compoundId: string) => {
    setSelectedSilvicultureActivityIds([compoundId]);
    setSelectedOpeningIds([openingId]);
  }

  return (
    <Grid className="default-grid activity-search-section-grid">
      <Column sm={4} md={8} lg={16}>
        <ActivitySearchInput searchParams={searchParams} handleSearchFieldChange={handleSearchFieldChange} />
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
        hasActivitySearchFilters(queryParams) ? (
          <Column className="full-width-col" sm={4} md={8} lg={16}>
            <>
              {
                selectedSilvicultureActivityIds.length > 0
                  ? (
                    <OpeningsMap
                      key={selectedSilvicultureActivityIds.join(",")}
                      openingIds={selectedOpeningIds}
                      setOpeningPolygonNotFound={() => { }}
                      mapHeight={480}
                      layerFilter={true}
                      kind={[activityLayerConfig.code] as MapKindType[]}
                      isActivitiesMap
                      setAvailableSilvicultureActivityIds={setAvailableSilvicultureActivityIds}
                      selectedSilvicultureActivityIds={selectedSilvicultureActivityIds}
                      selectedDisturbanceIds={[]}
                    />
                  )
                  : null
              }
              <div className="search-table-banner">
                <Stack className="search-result-title-section" orientation="vertical">
                  <h5 className="search-result-title">Search results</h5>

                  <Stack className="search-result-sub-title-section" orientation="horizontal" gap={activitySearchQuery.isLoading && !isAuthRefreshInProgress() ? 4 : 2}>
                    <p className="search-result-subtitle">Total search results:</p>
                    {
                      activitySearchQuery.isLoading && !isAuthRefreshInProgress()
                        ? <InlineLoading />
                        : <p className="search-result-subtitle">{activitySearchQuery.data?.page?.totalElements ?? 0}</p>
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
                (activitySearchQuery.isLoading || isAuthRefreshInProgress())
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
                !activitySearchQuery.isLoading && !isAuthRefreshInProgress()
                  ? (
                    <Table
                      className="opening-search-table default-zebra-table"
                      aria-label="Opening search table"
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
                          activitySearchQuery.data?.content?.map((row) => (
                            <ActivitySearchTableRow
                              key={row.activityId}
                              headers={searchTableHeaders}
                              rowData={row}
                              showMap={true}
                              selectedRows={selectedSilvicultureActivityIds.map((id) => Number(id.split('-')[0]))}
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
                !activitySearchQuery.isLoading && !isAuthRefreshInProgress()
                  ? (
                    activitySearchQuery.data?.page?.totalElements &&
                      activitySearchQuery.data?.page.totalElements > 0 ? (
                      <Pagination
                        className="default-pagination-white"
                        page={currPageNumber + 1}
                        pageSize={currPageSize}
                        pageSizes={PageSizesConfig}
                        totalItems={activitySearchQuery.data?.page.totalElements}
                        onChange={handlePagination}
                        pagesUnknown={activitySearchQuery.data?.page.totalElements > MAX_PAGINATION_PAGES * currPageSize}
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

export default ActivitiesSearchSection;
