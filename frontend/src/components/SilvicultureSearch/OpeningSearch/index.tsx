import React, { useState, useEffect, useRef } from "react";
import { useQuery } from "@tanstack/react-query";
import {
  Grid,
  Column,
  Table,
  TableHead,
  TableRow,
  TableHeader,
  TableBody,
  InlineNotification,
  Pagination,
} from "@carbon/react";
import { OpeningHeaderType } from "@/types/TableHeader";
import { defaultSearchTableHeaders } from "./constants";
import { OpeningSearchFilterType } from "./definitions";
import { fetchCategories, fetchOpeningsOrgUnits, searchOpenings } from "@/services/OpeningSearchService";
import TableSkeleton from "../../TableSkeleton";
import OpeningTableRow from "../../OpeningTableRow";
import OpeningSearchBar from "./OpeningSearchBar";
import OpeningsMap from "../../OpeningsMap";
import EmptySection from "../../EmptySection";
import { DEFAULT_PAGE_NUM, PageSizesConfig } from "@/constants/tableConstants";
import { PaginationOnChangeType } from "@/types/GeneralTypes";
import useSilvicultureSearchParams from "../hooks";
import { SilvicultureSearchParams } from "../definitions";
import CodeDescriptionDto from "@/types/CodeDescriptionType";
import { DATE_TYPE_LIST, OPENING_STATUS_LIST } from "@/constants";
import { DATE_TYPES } from "@/types/DateTypes";
import { hasAnyActiveFilters } from "./utils";

import "./styles.scss";

const OpeningSearch: React.FC = () => {
  const searchParams = useSilvicultureSearchParams();
  const initialParamsRef = useRef<SilvicultureSearchParams | null>(null);

  if (searchParams && !initialParamsRef.current && searchParams?.tab === 'openings') {
    initialParamsRef.current = searchParams;
  }

  const [filters, setFilters] = useState<OpeningSearchFilterType>({});
  const [searchTableHeaders, setSearchTableHeaders] = useState<OpeningHeaderType[]>(
    () => structuredClone(defaultSearchTableHeaders)
  );
  const [showMap, setShowMap] = useState<boolean>(false);
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [openingPolygonNotFound, setOpeningPolygonNotFound] = useState<boolean>(false);
  // 0 index
  const [currPageNumber, setCurrPageNumber] = useState<number>(DEFAULT_PAGE_NUM);
  const [currPageSize, setCurrPageSize] = useState<number>(() => PageSizesConfig[0]);
  const [isSearchFilterEmpty, setIsSearchFilterEmpty] = useState<boolean>(false);

  const resetPagination = () => {
    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]);
  }

  /**
   * Toggles the selection of an opening ID.
   * If the ID is already selected, it is removed; otherwise, it is added.
   * It will also open a coming soon modal.
   *
   * @param {number} id - The opening ID to toggle.
   */
  /* v8 ignore next 5 */
  const handleRowSelection = (id: number) => {
    setSelectedOpeningIds((prev) =>
      prev.includes(id) ? prev.filter((openingId) => openingId !== id) : [...prev, id]
    );
  };

  /*
   * Data Queries
   */
  const categoryQuery = useQuery({
    queryKey: ['codes', 'categories'],
    queryFn: fetchCategories
  });

  const orgUnitQuery = useQuery({
    queryKey: ["codes", "org-units"],
    queryFn: fetchOpeningsOrgUnits,
    // A refetch is needed here to ensure filters are properly applied from url params
    refetchOnMount: 'always'
  });

  const [enableSearch, setEnableSearch] = useState<boolean>(false);

  /**
   * Search Query
   */
  const searchQuery = useQuery({
    queryKey: ['openings', 'search', {
      ...filters, page: currPageNumber,
      size: currPageSize
    }],
    queryFn: () => searchOpenings({
      ...filters,
      page: currPageNumber,
      size: currPageSize
    }),
    enabled: enableSearch,
    gcTime: 0,
    staleTime: 0
  })

  useEffect(() => {
    if (searchQuery.status !== 'pending') {
      setEnableSearch(false);
    }
  }, [searchQuery.status])

  /**
   * Handler for when a search action is triggered.
   */
  const handleSearch = () => {
    if (hasAnyActiveFilters(filters)) {
      setIsSearchFilterEmpty(false);
      searchQuery.refetch();
    }
    else {
      setIsSearchFilterEmpty(true);
    }
  }

  /**
   * Handles url params, triggers a search if there's any param filters present.
   */
  /* v8 ignore next 40 */
  useEffect(() => {
    if (!initialParamsRef.current) return;

    const orgUnitsFromParams =
      orgUnitQuery.data && initialParamsRef.current.orgUnit
        ? initialParamsRef.current.orgUnit
          .map((code) => orgUnitQuery.data?.find((item) => item.code === code))
          .filter(Boolean) as CodeDescriptionDto[]
        : [];

    const statusFromParams =
      initialParamsRef.current.status && OPENING_STATUS_LIST.length > 0
        ? initialParamsRef.current.status
          .map((code) =>
            OPENING_STATUS_LIST.find((item) => item.code === code)
          )
          .filter(Boolean) as CodeDescriptionDto[]
        : [];

    const dateTypeCode = initialParamsRef.current?.dateType;
    let dateType: CodeDescriptionDto<DATE_TYPES> | undefined = undefined;

    if (dateTypeCode) {
      dateType = DATE_TYPE_LIST.find((d) => d.code === dateTypeCode);
    }

    const nextFilters = {
      updateDateStart: initialParamsRef.current?.dateStart,
      updateDateEnd: initialParamsRef.current?.dateEnd,
      orgUnit: orgUnitsFromParams,
      statusList: statusFromParams,
      dateType
    };

    if (hasAnyActiveFilters(nextFilters)) {
      setFilters(nextFilters);
      setEnableSearch(true);
    }
  }, [orgUnitQuery.isSuccess, initialParamsRef.current]);

  const handlePagination = (paginationObj: PaginationOnChangeType) => {
    // Convert to 0 based index
    const nextPageNum = paginationObj.page - 1;
    const nextPageSize = paginationObj.pageSize;

    setCurrPageNumber(nextPageNum);
    setCurrPageSize(nextPageSize);
    setEnableSearch(true);
    searchQuery.refetch();
  }

  return (
    <Grid className="opening-search-grid">
      {
        isSearchFilterEmpty
          ? (
            <Column className="opening-search-table-col" sm={4} md={8} lg={16}>
              <InlineNotification
                title="Missing at least one criteria to search"
                subtitle="Please, enter at least one criteria to start the search."
                kind="error"
                lowContrast
              />
            </Column>
          )
          : null
      }
      {/* Search bar Section */}
      <OpeningSearchBar
        showMap={showMap}
        setShowMap={setShowMap}
        headers={searchTableHeaders}
        setHeaders={setSearchTableHeaders}
        filters={filters}
        setFilters={setFilters}
        categories={categoryQuery.data ?? []}
        orgUnits={orgUnitQuery.data ?? []}
        handleSearch={handleSearch}
        totalResults={searchQuery.data?.page.totalElements}
        setEnableSearch={setEnableSearch}
        resetPagination={resetPagination}
      />

      {/* Map Section */}
      <Column className="opening-search-table-col subgrid-full-width-no-row-gap-col" sm={4} md={8} lg={16}>
        {
          showMap
            ? (
              <OpeningsMap
                openingIds={selectedOpeningIds}
                setOpeningPolygonNotFound={setOpeningPolygonNotFound}
                mapHeight={280}
              />
            )
            : null
        }
        {
          openingPolygonNotFound
            ? (
              <InlineNotification
                title="Opening ID not found!"
                subtitle="Unable to find selected Opening Polygon!"
                kind="error"
                lowContrast
                className="inline-notification"
              />
            )
            : null
        }

        {/* Adaptive initial empty display and error display */}
        {
          !searchQuery.isFetching && searchQuery.data?.page.totalElements === undefined
            ? (
              <EmptySection
                className="initial-empty-section"
                pictogram="Summit"
                title={
                  searchQuery.isError ? "Something went wrong!" : "Nothing to show yet!"
                }
                description={
                  searchQuery.isError
                    ? "Error occured while searching for results."
                    : "Enter at least one criteria to start the search. The list will display here."
                }
              />
            )
            : null
        }

        {/* Table skeleton */}
        {
          searchQuery.isLoading
            ? <TableSkeleton
              headers={searchTableHeaders}
              showToolbar={false}
              showHeader={false}
            />
            : null
        }

        {/* Loaded Table section */}
        {
          searchQuery.isFetched
            ? (
              <>
                <Table
                  className="opening-search-table default-zebra-table"
                  aria-label="Opening search table"
                  useZebraStyles
                >
                  <TableHead>
                    <TableRow>
                      {
                        searchTableHeaders
                          .filter((header) => header.selected)
                          .map((header) => (
                            <TableHeader key={header.key}>{header.header}</TableHeader>
                          ))
                      }
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {
                      searchQuery.data?.content.map((row) => (
                        <OpeningTableRow
                          key={row.openingId}
                          headers={searchTableHeaders}
                          rowData={row}
                          showMap={showMap}
                          selectedRows={selectedOpeningIds}
                          handleRowSelection={handleRowSelection}
                        />
                      ))
                    }
                  </TableBody>
                </Table>
                {/* Display either pagination or empty message */}
                {
                  searchQuery.data?.page.totalElements && searchQuery.data?.page.totalElements > 0
                    ? (
                      <Pagination
                        className="default-pagination-white"
                        page={currPageNumber + 1}
                        pageSize={currPageSize}
                        pageSizes={PageSizesConfig}
                        totalItems={searchQuery.data?.page.totalElements}
                        onChange={handlePagination}
                      />
                    )
                    : (
                      <EmptySection
                        pictogram="UserSearch"
                        title="No results"
                        description="Consider adjusting your search term(s) and try again."
                      />
                    )
                }
              </>
            )
            : null
        }
      </Column>
    </Grid>
  )
};

export default OpeningSearch;
