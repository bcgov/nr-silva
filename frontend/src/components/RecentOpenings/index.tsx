import React, { useState } from 'react';
import { Button, InlineNotification, Table, TableBody, TableHead, TableHeader, TableRow } from '@carbon/react';
import { Location } from '@carbon/icons-react';
import OpeningsMap from '../OpeningsMap';
import SectionTitle from '../SectionTitle';
import useBreakpoint from '@/hooks/UseBreakpoint';
import { useQuery } from '@tanstack/react-query';
import API from '@/services/API';
import TableSkeleton from '../TableSkeleton';
import { recentOpeningsHeaders } from './constants';
import EmptySection from '../EmptySection';
import OpeningTableRow from '../OpeningTableRow';

import './styles.scss';

type RecentOpeningsProps = {
  defaultMapOpen?: boolean;
}

const RecentOpenings = ({ defaultMapOpen = false }: RecentOpeningsProps) => {
  const [showMap, setShowMap] = useState<boolean>(defaultMapOpen);
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [openingPolygonNotFound, setOpeningPolygonNotFound] =
    useState<boolean>(false);
  const [faultyOpeningPolygonId, setFaultyOpeningPolygonId] = useState<
    number | null
  >(null);

  const breakpoint = useBreakpoint();

  const recentOpeningsQuery = useQuery({
    queryKey: ["opening", "recent"],
    queryFn: () => API.UserRecentOpeningEndpointService.getUserRecentOpenings({}),
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

  return (
    <div className="recent-openings-container">
      <div className="title-section">
        <SectionTitle
          title="Recent openings"
          subtitle="Track the history of openings you have looked at and check spatial information by selecting the openings in the table below"
        />
        <Button
          className="map-button"
          data-testid="toggle-map-button"
          renderIcon={Location}
          type="button"
          size={breakpoint === "sm" ? "sm" : "lg"}
          onClick={toggleMap}
          disabled={!recentOpeningsQuery.data?.content?.length}
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
        showMap && recentOpeningsQuery.data?.content?.length
          ? (
            <OpeningsMap
              openingIds={selectedOpeningIds}
              setOpeningPolygonNotFound={handleMapError}
              mapHeight={280}
            />
          )
          : null
      }

      {/* Table skeleton */}
      {
        recentOpeningsQuery.isLoading ? (
          <TableSkeleton
            headers={recentOpeningsHeaders}
            showToolbar={false}
            showHeader={false}
          />
        ) : null
      }
      {/* Empty Table */}
      {
        !recentOpeningsQuery.isLoading &&
          !recentOpeningsQuery.data?.content?.length ? (
          <EmptySection
            pictogram="Magnify"
            title="There are no openings to show yet"
            description="Your recent openings will appear here once you generate one"
          />
        ) : null
      }
      {/* Loaded table content */}
      {
        !recentOpeningsQuery.isLoading && recentOpeningsQuery.data?.content?.length ?
          (
            <Table
              className="recent-openings-table default-zebra-table"
              aria-label="Recent openings table"
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
                  recentOpeningsQuery.data?.content.map((row) => (
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
    </div>
  );
};

export default RecentOpenings;
