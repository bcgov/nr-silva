import React, { useState, useEffect } from "react";
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
  Pagination
} from "@carbon/react";
import { OpeningHeaderType } from "../../../../types/TableHeader";
import { defaultSearchTableHeaders } from "./constants";
import { OpeningSearchFilterType } from "./definitions";
import CodeDescriptionDto from "../../../../types/CodeDescriptionType";
import { fetchCategories, fetchOpeningsOrgUnits, searchOpenings } from "../../../../services/OpeningSearchService";
import TableSkeleton from "../../../TableSkeleton";
import OpeningTableRow from "../../../OpeningTableRow";
import OpeningSearchBar from "./OpeningSearchBar";
import OpeningsMap from "../../../OpeningsMap";
import EmptySection from "../../../EmptySection";

import "./styles.scss";
import { PageSizesConfig } from "../../../../constants/tableConstants";
import { PaginationOnChangeType } from "../../../../types/GeneralTypes";

const OpeningSearch: React.FC = () => {
  const [searchTableHeaders, setSearchTableHeaders] = useState<OpeningHeaderType[]>(
    () => structuredClone(defaultSearchTableHeaders)
  );
  const [filters, setFilters] = useState<OpeningSearchFilterType>({});
  const [showMap, setShowMap] = useState<boolean>(false);
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [openingPolygonNotFound, setOpeningPolygonNotFound] = useState<boolean>(false);
  // 0 index
  const [currPageNumber, setCurrPageNumber] = useState<number>(0);
  const [currPageSize, setCurrPageSize] = useState<number>(() => PageSizesConfig[0]);

  /**
 * Toggles the selection of an opening ID.
 * If the ID is already selected, it is removed; otherwise, it is added.
 *
 * @param {number} id - The opening ID to toggle.
 */
  const handleRowSelection = (id: number) => {
    setSelectedOpeningIds((prev) =>
      prev.includes(id) ? prev.filter((openingId) => openingId !== id) : [...prev, id]
    );
  };

  /*
   * Data Queries
   */
  const categoryQuery = useQuery({
    queryKey: ['opening-search', 'categories'],
    queryFn: fetchCategories
  });

  const orgUnitQuery = useQuery({
    queryKey: ["opening-search", "org-units"],
    queryFn: fetchOpeningsOrgUnits,
    select: (data): CodeDescriptionDto[] => (
      data.map((orgUnit) => ({ code: orgUnit.orgUnitCode, description: orgUnit.orgUnitName })
      ))
  });

  /*
   * Search Mutation
   */
  const searchMutation = useMutation({
    mutationFn: (
      { page, perPage }: { page: number, perPage: number }
    ) => searchOpenings({
      ...filters,
      page,
      perPage
    })
  })

  /**
   * Handler for when a search action is triggered.
   */
  const handleSearch = () => {
    searchMutation.mutate({ page: currPageNumber, perPage: currPageSize });
  }


  // TODO: remove, debug only
  useEffect(() => {
    console.log('filter changed: ', filters)
  }, [filters])

  const handlePagination = (paginationObj: PaginationOnChangeType) => {
    console.log('triggered')
    // Convert to 0 based index
    const nextPageNum = paginationObj.page - 1;
    const nextPageSize = paginationObj.pageSize;

    setCurrPageNumber(nextPageNum);
    setCurrPageSize(nextPageSize);

    console.log(nextPageNum, nextPageSize)

    searchMutation.mutate({ page: nextPageNum, perPage: nextPageSize });
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
        totalResults={searchMutation.data?.totalItems}
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
          searchMutation.data?.totalItems === undefined
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
              rowCount={currPageSize}
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
                      searchMutation.data?.data.map((row) => (
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
                {
                  searchMutation.data?.totalItems > 0
                    ? (
                      <Pagination
                        className="default-pagination-white"
                        page={currPageNumber + 1}
                        pageSize={currPageSize}
                        pageSizes={PageSizesConfig}
                        itemsPerPageText=""
                        totalItems={searchMutation.data.totalItems}
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
