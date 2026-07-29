import React, { useState } from 'react';
import { Button, InlineNotification, Table, TableBody, TableHead, TableHeader, TableRow, Pagination } from '@carbon/react';
import { Location } from '@carbon/icons-react';
import { isAuthRefreshInProgress } from '@/constants/tanstackConfig';
import { useQuery } from '@tanstack/react-query';
import API from '@/services/API';
import { DEFAULT_PAGE_NUM, MAX_PAGINATION_PAGES, PageSizesConfig } from '@/constants/tableConstants';
import { PaginationOnChangeType } from '@/types/GeneralTypes';

import OpeningsMap from '../OpeningsMap';
import SectionTitle from '../SectionTitle';
import useBreakpoint from '@/hooks/UseBreakpoint';
import TableSkeleton from '../TableSkeleton';
import { recentOpeningsHeaders } from '../RecentOpenings/constants';
import EmptySection from '../EmptySection';
import OpeningTableRow from '../OpeningTableRow';

import './styles.scss';

type MyOpeningsProps = {
  defaultMapOpen?: boolean;
}

const MyOpenings = ({ defaultMapOpen = false }: MyOpeningsProps) => {
  const [showMap, setShowMap] = useState<boolean>(defaultMapOpen);
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [openingPolygonNotFound, setOpeningPolygonNotFound] =
    useState<boolean>(false);
  const [faultyOpeningPolygonId, setFaultyOpeningPolygonId] = useState<
    number | null
  >(null);
  const [currPageNumber, setCurrPageNumber] = useState<number>(DEFAULT_PAGE_NUM);
  const [currPageSize, setCurrPageSize] = useState<number>(() => PageSizesConfig[0]!);

  const breakpoint = useBreakpoint();

  const myOpeningsQuery = useQuery({
    queryKey: ["openings", "user-created", currPageNumber, currPageSize],
    queryFn: () => API.OpeningEndpointService.getUserCreatedOpenings(currPageNumber, currPageSize),
    refetchOnMount: "always",
  });

  const toggleMap = () => {
    setShowMap(!showMap);
  };

  const handleMapError = (value: boolean, openingId: number | null) => {
    setOpeningPolygonNotFound(value);
    setFaultyOpeningPolygonId(openingId);
  };

  /**
   * Toggles the selection of an opening ID.
   * If the ID is already selected, it is removed; otherwise, it is added.
   *
   * @param {number} id - The opening ID to toggle.
   */
  const handleRowSelection = (id: number) => {
    setSelectedOpeningIds((prev) =>
      prev.includes(id)
        ? prev.filter((openingId) => openingId !== id)
        : [...prev, id]
    );
  };

  const handlePagination = (paginationObj: PaginationOnChangeType) => {
    // Convert to 0 based index
    const nextPageNum = paginationObj.page - 1;
    const nextPageSize = paginationObj.pageSize;

    setCurrPageNumber(nextPageNum);
    setCurrPageSize(nextPageSize);
    setSelectedOpeningIds([]);
  };

  return (
    <div className="my-openings-container">
      <div className="title-section">
        <SectionTitle
          title="My openings"
          subtitle="View all openings you have created and check spatial information by selecting the openings in the table below"
        />
        <Button
          className="map-button"
          data-testid="toggle-map-button"
          renderIcon={Location}
          type="button"
          size={breakpoint === "sm" ? "sm" : "lg"}
          onClick={toggleMap}
          disabled={!myOpeningsQuery.data?.content?.length}
        >
          {showMap ? "Hide map" : "Show map"}
        </Button>
      </div>
      {
        openingPolygonNotFound ? (
          <InlineNotification
            title={`Opening ID ${faultyOpeningPolygonId} map geometry not found`}
            subtitle="No map data available for this opening ID"
            statusIconDescription={`Opening ID ${faultyOpeningPolygonId} map geometry not found`}
            kind="error"
            lowContrast
            className="inline-notification"
            hideCloseButton
            role="alert"
          />
        )
          : null
      }
      {
        showMap && myOpeningsQuery.data?.content?.length
          ? (
            <OpeningsMap
              openingIds={selectedOpeningIds}
              setOpeningPolygonNotFound={handleMapError}
              mapHeight={480}
            />
          )
          : null
      }

      {/* Table skeleton */}
      {
        (myOpeningsQuery.isLoading || isAuthRefreshInProgress()) ? (
          <TableSkeleton
            headers={recentOpeningsHeaders}
            showToolbar={false}
            showHeader={false}
          />
        ) : null
      }
      {/* Empty Table */}
      {
        (
          !myOpeningsQuery.isLoading &&
          !isAuthRefreshInProgress() &&
          !myOpeningsQuery.data?.content?.length
        )
          ? (
            <EmptySection
              pictogram="Magnify"
              title="There are no openings to show yet"
              description="Your created openings will appear here once you create one"
            />
          ) : null
      }
      {/* Loaded table content */}
      {
        !myOpeningsQuery.isLoading && myOpeningsQuery.data?.content?.length && !isAuthRefreshInProgress() ?
          (
            <Table
              className="my-openings-table default-zebra-table"
              aria-label="My openings table"
              useZebraStyles
            >
              <TableHead>
                <TableRow>
                  {
                    recentOpeningsHeaders.map((header) => (
                      <TableHeader key={header.key}>{header.header}</TableHeader>
                    ))
                  }
                </TableRow>
              </TableHead>
              <TableBody>
                {
                  myOpeningsQuery.data?.content.map((row) => (
                    <OpeningTableRow
                      headers={recentOpeningsHeaders}
                      key={row.openingId}
                      rowData={row}
                      showMap={showMap}
                      selectedRows={selectedOpeningIds}
                      handleRowSelection={handleRowSelection}
                    />
                  ))
                }
              </TableBody>
            </Table>
          )
          : null
      }

      {/* Pagination */}
      {
        !myOpeningsQuery.isLoading && !isAuthRefreshInProgress() && myOpeningsQuery.data?.page
          ? (
            <Pagination
              className="default-pagination-white"
              page={currPageNumber + 1}
              pageSize={currPageSize}
              pageSizes={PageSizesConfig}
              totalItems={myOpeningsQuery.data?.page.totalElements ?? 0}
              onChange={handlePagination}
              pagesUnknown={myOpeningsQuery.data?.page.totalElements ? myOpeningsQuery.data.page.totalElements > MAX_PAGINATION_PAGES * currPageSize : false}
            />
          )
          : null
      }
    </div>
  );
};

export default MyOpenings;
