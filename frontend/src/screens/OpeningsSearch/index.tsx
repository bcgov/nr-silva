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
import { usePreference } from "@/contexts/PreferenceProvider";
import { OpeningHeaderType } from "@/types/TableHeader";
import { PageSizesConfig } from "../../constants/tableConstants";
import { PaginationOnChangeType } from "../../types/GeneralTypes";

import './styles.scss';

const OpeningsSearch = () => {

  const [searchParams, setSearchParams] = useState<OpeningSearchParamsType>();
  const hasAutoSearchedFromUrl = useRef(false);
  const { userPreference, updatePreferences } = usePreference();
  const [searchTableHeaders, setSearchTableHeaders] = useState<
    OpeningHeaderType[]
  >(() => structuredClone(userPreference.visibleColumns.silvicultureSearch) || []);
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);

  // On page load, read URL params and prefill search (one time)
  useEffect(() => {
    const urlParams = readOpeningSearchUrlParams();
    if (hasActiveFilters(urlParams)) {
      setSearchParams(urlParams);
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
    queryKey: ['search', 'openings', searchParams],
    queryFn: () => openingSearch(searchParams),
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

    setSearchParams((prev) => ({
      ...prev,
      page: nextPageNum,
      size: nextPageSize,
    }));

    handleSearch();
  };

  const handleRowSelection = (id: number) => {
    setSelectedOpeningIds((prev) =>
      prev.includes(id)
        ? prev.filter((openingId) => openingId !== id)
        : [...prev, id]
    );
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
          <div className="search-table-banner">
            <Stack className="search-result-title-section" orientation="vertical">
              <h5 className="search-result-title">Search results</h5>

              <Stack className="search-result-sub-title-section" orientation="horizontal" gap={2}>
                <p className="search-result-subtitle">Total search results:</p>
                {
                  openingSearchQuery.isLoading
                    ? <InlineLoading />
                    : <p className="search-result-subtitle">{openingSearchQuery.data?.page?.totalElements ?? 0}</p>
                }
              </Stack>
            </Stack>
            <Button
              className="edit-col-button"
              data-testid="edit-col-button"
              type="button"
              kind="tertiary"
            >
              Edit columns
            </Button>
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
                  showMap={false}
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
              page={searchParams?.page ?? 0 + 1}
              pageSize={searchParams?.size ?? PageSizesConfig[0]}
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
