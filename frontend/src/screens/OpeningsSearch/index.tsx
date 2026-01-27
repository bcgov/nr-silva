import { useEffect, useState, useRef } from "react";
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
  DataTable,
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
import { CircleDash, Search } from "@carbon/icons-react";
import OpeningTableRow from "@/components/OpeningTableRow";
import EmptySection from "@/components/EmptySection";
import { OpendingHeaderKeyType, OpeningHeaderType } from "@/types/TableHeader";
import { DEFAULT_PAGE_NUM, PageSizesConfig } from "@/constants/tableConstants";
import { PaginationOnChangeType } from "@/types/GeneralTypes";
import { isAuthRefreshInProgress } from "@/constants/tanstackConfig";
import OpeningsMap from "@/components/OpeningsMap";

import { defaultSearchTableHeaders } from "./constants";


import './styles.scss';

const OpeningsSearch = () => {

  const [searchParams, setSearchParams] = useState<OpeningSearchParamsType>();
  const hasAutoSearchedFromUrl = useRef(false);
  const [searchTableHeaders, setSearchTableHeaders] = useState<OpeningHeaderType[]>(() => structuredClone(defaultSearchTableHeaders));
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [currPageNumber, setCurrPageNumber] = useState<number>(DEFAULT_PAGE_NUM);
  const [currPageSize, setCurrPageSize] = useState<number>(() => PageSizesConfig[0]!);


  // On page load, read URL params and prefill search (one time)
  useEffect(() => {
    const urlParams = readOpeningSearchUrlParams();
    // Apply URL params if there are active filters OR if pagination params exist
    if (hasActiveFilters(urlParams) || urlParams.page !== undefined || urlParams.size !== undefined) {
      setSearchParams(urlParams);
      // Set pagination states from URL params
      if (urlParams.page !== undefined) {
        setCurrPageNumber(urlParams.page);
      }
      if (urlParams.size !== undefined) {
        setCurrPageSize(urlParams.size);
      }
      hasAutoSearchedFromUrl.current = true;
    }
  }, []);

  useEffect(() => {
    document.title = `Openings Search - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  const openingSearchQuery = useQuery({
    queryKey: ['search', 'openings', { ...searchParams, page: currPageNumber, size: currPageSize }],
    queryFn: () => openingSearch({ ...searchParams, page: currPageNumber, size: currPageSize }),
    enabled: false,
    refetchOnMount: true
  });

  // If the page was loaded with URL params, perform a one-time auto-search
  useEffect(() => {
    if (hasAutoSearchedFromUrl.current && hasActiveFilters(searchParams)) {
      hasAutoSearchedFromUrl.current = false; // reset to prevent re-triggering
      openingSearchQuery.refetch();
    }
  }, [searchParams, openingSearchQuery]);

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
    // Only search if params have active filters and not already fetching
    if (!hasActiveFilters(searchParams) && !openingSearchQuery.isFetching) {
      return;
    }
    // Update URL when search is triggered
    updateOpeningSearchUrlParams(searchParams);
    // Revalidate query - if params are already set, this re-runs the query
    openingSearchQuery.refetch();
  };

  /**
   * Reset all search params
   */
  const handleReset = () => {
    setSearchParams(undefined);
  };

  const handlePagination = (paginationObj: PaginationOnChangeType) => {
    // Convert to 0 based index
    const nextPageNum = paginationObj.page - 1;
    const nextPageSize = paginationObj.pageSize;
    console.log('nextPageNum', nextPageNum);
    setCurrPageNumber(nextPageNum);
    setCurrPageSize(nextPageSize);
    setSelectedOpeningIds([]);
    updateOpeningSearchUrlParams({
      ...searchParams,
      page: nextPageNum,
      size: nextPageSize,
    });

    openingSearchQuery.refetch();
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
              className="action-menu-button"
              iconDescription="Edit columns"
              menuOptionsClass="opening-search-action-menu-option"
              renderIcon={
                () => <Button
                  className="edit-col-button"
                  data-testid="edit-col-button"
                  type="button"
                  kind="tertiary"
                >
                  Edit columns
                </Button>
              }
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
              {openingSearchQuery.data?.content?.map((row) => (
                <OpeningTableRow
                  key={row.openingId}
                  headers={searchTableHeaders}
                  rowData={row}
                  showMap={true}
                  selectedRows={selectedOpeningIds}
                  handleRowSelection={handleRowSelection}
                />
              ))}
            </TableBody>
          </Table>
          {/* Display either pagination or empty message */}
          {openingSearchQuery.data?.page?.totalElements &&
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
          )}
        </>
      </Column>
    </Grid >
  );
};

export default OpeningsSearch;
