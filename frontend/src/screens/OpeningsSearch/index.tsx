import { useEffect, useState } from "react";
import PageTitle from "@/components/PageTitle";
import {
  Grid,
  Column,
  Button,
  Table,
  TableHead,
  TableRow,
  TableBody,
  Pagination,
  TableHeader,
  InlineLoading,
  Stack,
  TableToolbarMenu,
  Checkbox,
} from "@carbon/react";

import FavOpeningSection from "@/components/FavouriteOpenings/FavOpeningSection";
import { useQuery } from "@tanstack/react-query";
import { OpeningSearchParamsType } from "@/types/OpeningTypes";
import { openingSearch } from "@/services/OpeningSearchService";
import {
  readOpeningSearchUrlParams,
  updateOpeningSearchUrlParams,
  hasActiveFilters,
} from "@/utils/OpeningSearchParamsUtils";
import OpeningsSearchInput from "@/components/OpeningsSearchInput";
import { CircleDash, Edit, Search } from "@carbon/icons-react";
import OpeningTableRow from "@/components/OpeningTableRow";
import EmptySection from "@/components/EmptySection";
import { OpendingHeaderKeyType, OpeningHeaderType } from "@/types/TableHeader";
import { DEFAULT_PAGE_NUM, PageSizesConfig } from "@/constants/tableConstants";
import { PaginationOnChangeType } from "@/types/GeneralTypes";
import { isAuthRefreshInProgress } from "@/constants/tanstackConfig";
import OpeningsMap from "@/components/OpeningsMap";
import TableSkeleton from "@/components/TableSkeleton";

import { defaultSearchTableHeaders } from "./constants";

import './styles.scss';

const OpeningsSearch = () => {

  const [searchParams, setSearchParams] = useState<OpeningSearchParamsType>();
  const [queryParams, setQueryParams] = useState<OpeningSearchParamsType>();
  const [searchTableHeaders, setSearchTableHeaders] = useState<OpeningHeaderType[]>(() => structuredClone(defaultSearchTableHeaders));
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [currPageNumber, setCurrPageNumber] = useState<number>(DEFAULT_PAGE_NUM);
  const [currPageSize, setCurrPageSize] = useState<number>(() => PageSizesConfig[0]!);


  // On page load, read URL params and prefill search (one time)
  useEffect(() => {
    const urlParams = readOpeningSearchUrlParams();
    // Apply URL params if there are active filters OR if pagination params exist
    if (hasActiveFilters(urlParams) || urlParams.page !== undefined || urlParams.size !== undefined) {
      const nextPage = urlParams.page ?? DEFAULT_PAGE_NUM;
      const nextSize = urlParams.size ?? PageSizesConfig[0]!;
      const paramsWithPagination = {
        ...urlParams,
        page: nextPage,
        size: nextSize,
      } as OpeningSearchParamsType;

      setSearchParams(paramsWithPagination);
      setCurrPageNumber(nextPage);
      setCurrPageSize(nextSize);
      setQueryParams(paramsWithPagination);
    }
  }, []);

  useEffect(() => {
    document.title = `Openings Search - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  const openingSearchQuery = useQuery({
    queryKey: ['search', 'openings', queryParams],
    queryFn: () => openingSearch(queryParams),
    enabled: !!queryParams,
  });

  /**
   * Handler to update a single field in searchParams
   */
  const handleSearchFieldChange = (field: keyof OpeningSearchParamsType, value: unknown) => {
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
    } as OpeningSearchParamsType;

    if (!hasActiveFilters(paramsWithPagination)) {
      return;
    }
    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    setSearchParams(paramsWithPagination);
    setQueryParams(paramsWithPagination);
    setSelectedOpeningIds([]);
    updateOpeningSearchUrlParams(paramsWithPagination);
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
    updateOpeningSearchUrlParams(undefined);
  };

  const handlePagination = (paginationObj: PaginationOnChangeType) => {
    // Convert to 0 based index
    const nextPageNum = paginationObj.page - 1;
    const nextPageSize = paginationObj.pageSize;
    if (!queryParams) {
      return;
    }

    const paramsWithPagination: OpeningSearchParamsType = {
      ...queryParams,
      page: nextPageNum,
      size: nextPageSize,
    };

    setCurrPageNumber(nextPageNum);
    setCurrPageSize(nextPageSize);
    setSelectedOpeningIds([]);
    setQueryParams(paramsWithPagination);
    updateOpeningSearchUrlParams(paramsWithPagination);
  };

  const handleRowSelection = (id: number) => {
    setSelectedOpeningIds((prev) =>
      prev.includes(id)
        ? prev.filter((openingId) => openingId !== id)
        : [...prev, id]
    );
  };

  const toggleColumn = (key: OpendingHeaderKeyType) => {
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

  return (
    <Grid className="default-grid openings-search-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle title="Openings Search" />
      </Column>

      <Column sm={4} md={8} lg={16} className="bookmark-col">
        <FavOpeningSection />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <OpeningsSearchInput searchParams={searchParams} onSearchParamsChange={handleSearchFieldChange} />
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
        hasActiveFilters(queryParams) ? (
          <Column className="full-width-col" sm={4} md={8} lg={16}>
            <>
              {
                selectedOpeningIds.length > 0
                  ? (
                    <OpeningsMap
                      openingIds={selectedOpeningIds}
                      setOpeningPolygonNotFound={() => { }}
                      mapHeight={480}
                    />
                  )
                  : null
              }
              <div className="search-table-banner">
                <Stack className="search-result-title-section" orientation="vertical">
                  <h5 className="search-result-title">Search results</h5>

                  <Stack className="search-result-sub-title-section" orientation="horizontal" gap={openingSearchQuery.isLoading && !isAuthRefreshInProgress() ? 4 : 2}>
                    <p className="search-result-subtitle">Total search results:</p>
                    {
                      openingSearchQuery.isLoading && !isAuthRefreshInProgress()
                        ? <InlineLoading />
                        : <p className="search-result-subtitle">{openingSearchQuery.data?.page?.totalElements ?? 0}</p>
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
                (openingSearchQuery.isLoading || isAuthRefreshInProgress())
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
                !openingSearchQuery.isLoading && !isAuthRefreshInProgress()
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
                          !openingSearchQuery.isLoading ? (
                            openingSearchQuery.data?.content?.map((row) => (
                              <OpeningTableRow
                                key={row.openingId}
                                headers={searchTableHeaders}
                                rowData={row}
                                showMap={true}
                                selectedRows={selectedOpeningIds}
                                handleRowSelection={handleRowSelection}
                              />
                            ))
                          )
                            : null
                        }
                      </TableBody>
                    </Table>
                  )
                  : null
              }

              {/* Display either pagination or empty message */}
              {
                !openingSearchQuery.isLoading && !isAuthRefreshInProgress()
                  ? (
                    openingSearchQuery.data?.page?.totalElements &&
                      openingSearchQuery.data?.page.totalElements > 0 ? (
                      <Pagination
                        className="default-pagination-white"
                        page={currPageNumber + 1}
                        pageSize={currPageSize}
                        pageSizes={PageSizesConfig}
                        totalItems={openingSearchQuery.data?.page.totalElements}
                        onChange={handlePagination}
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


    </Grid >
  );
};

export default OpeningsSearch;
