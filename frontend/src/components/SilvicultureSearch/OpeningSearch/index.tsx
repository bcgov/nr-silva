import React, { useState, useEffect, useRef } from "react";
import { useMutation, useQuery } from "@tanstack/react-query";
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
  Modal
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
import { PageSizesConfig } from "@/constants/tableConstants";
import { PaginationOnChangeType } from "@/types/GeneralTypes";
import { OpeningSearchResponseDto } from "@/types/OpeningTypes";
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
  const [currPageNumber, setCurrPageNumber] = useState<number>(0);
  const [currPageSize, setCurrPageSize] = useState<number>(() => PageSizesConfig[0]);
  const [clickedOpening, setClickedOpening] = useState<OpeningSearchResponseDto>();
  const [isComingSoonOpen, setIsComingSoonOpen] = useState<boolean>(false);

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

  /*
   * Search Mutation
   */
  const searchMutation = useMutation({
    mutationFn: (
      { page, size }: { page: number, size: number }
    ) => searchOpenings({
      ...filters,
      page,
      size
    })
  })

  /**
   * Handler for when a search action is triggered.
   */
  const handleSearch = () => {
    searchMutation.mutate({ page: currPageNumber, size: currPageSize });
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

    setFilters((prev) => ({ ...prev, ...nextFilters }));

    if (hasAnyActiveFilters(nextFilters)) {
      handleSearch();
    }
  }, [orgUnitQuery.isFetched, initialParamsRef.current]);

  const handlePagination = (paginationObj: PaginationOnChangeType) => {
    // Convert to 0 based index
    const nextPageNum = paginationObj.page - 1;
    const nextPageSize = paginationObj.pageSize;

    setCurrPageNumber(nextPageNum);
    setCurrPageSize(nextPageSize);

    searchMutation.mutate({ page: nextPageNum, size: nextPageSize });
  }

  const handleComingSoon = (rowData: OpeningSearchResponseDto) => {
    setIsComingSoonOpen(true);
    setClickedOpening(rowData);
  }

  return (
    <Grid className="opening-search-grid">
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
        totalResults={searchMutation.data?.page.totalElements}
      />

      {/* Map Section */}
      <Column className="opening-search-table-col subgrid-full-width-no-row-gap-col" sm={4} md={8} lg={16}>
        {
          showMap
            ? (
              <OpeningsMap
                openingId={null}
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
          !searchMutation.isPending && searchMutation.data?.page.totalElements === undefined
            ? (
              <EmptySection
                className="initial-empty-section"
                pictogram="Summit"
                title={
                  searchMutation.isError ? "Something went wrong!" : "Nothing to show yet!"
                }
                description={
                  searchMutation.isError
                    ? "Error occured while searching for results."
                    : "Enter at least one criteria to start the search. The list will display here."
                }
              />
            )
            : null
        }

        {/* Table skeleton */}
        {
          searchMutation.isPending
            ? <TableSkeleton
              headers={searchTableHeaders}
              showToolbar={false}
              showHeader={false}
            />
            : null
        }

        {/* Loaded Table section */}
        {
          searchMutation.isSuccess
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
                      searchMutation.data?.content.map((row) => (
                        <OpeningTableRow
                          key={row.openingId}
                          headers={searchTableHeaders}
                          rowData={row}
                          showMap={showMap}
                          selectedRows={selectedOpeningIds}
                          handleRowSelection={handleRowSelection}
                          enableClick
                          handleComingSoon={handleComingSoon}
                        />
                      ))
                    }
                  </TableBody>
                </Table>
                {/* Display either pagination or empty message */}
                {
                  searchMutation.data?.page.totalElements > 0
                    ? (
                      <Pagination
                        className="default-pagination-white"
                        page={currPageNumber + 1}
                        pageSize={currPageSize}
                        pageSizes={PageSizesConfig}
                        totalItems={searchMutation.data.page.totalElements}
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
      <Modal
        open={isComingSoonOpen}
        onRequestClose={() => setIsComingSoonOpen(false)}
        passiveModal
        modalHeading="Coming Soon"
        modalLabel={`Opening ID: ${clickedOpening?.openingId}`}
      >
        <p className="modal-subtext">An opening details page is in development.</p> <br />
        <p className="modal-subtext">
          {` Until it's available, search for ${clickedOpening?.openingId} in RESULTS to view the opening details.`}
        </p>
      </Modal>
    </Grid>
  )
};

export default OpeningSearch;
