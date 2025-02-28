import React, { useState } from 'react';
import { Button, InlineNotification, Table, TableBody, TableHead, TableHeader, TableRow } from '@carbon/react';
import { Location } from '@carbon/icons-react';
import OpeningsMap from '../OpeningsMap';
import SectionTitle from '../SectionTitle';
import useBreakpoint from '../../hooks/UseBreakpoint';
import { useQuery } from '@tanstack/react-query';
import { fetchUserRecentOpenings } from '../../services/OpeningService';
import TableSkeleton from '../TableSkeleton';
import { recentOpeningsHeaders } from './constants';
import EmptySection from '../EmptySection';
import ComingSoonModal from '../ComingSoonModal';
import OpeningTableRow from '../OpeningTableRow';

import './styles.scss';

const RecentOpenings = () => {
  const [showMap, setShowMap] = useState<boolean>(false);
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [openingPolygonNotFound, setOpeningPolygonNotFound] = useState<boolean>(false);
  const [openingDetails, setOpeningDetails] = useState("");
  const breakpoint = useBreakpoint();

  const recentOpeningsQuery = useQuery({
    queryKey: ["opening", "recent"],
    queryFn: () => fetchUserRecentOpenings(),
    refetchOnMount: "always"
  });

  const toggleMap = () => {
    setShowMap(!showMap);
  };

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

  return (
    <div className="recent-openings-container">
      <div
        className="title-section"
      >
        <SectionTitle
          title="Recent openings"
          subtitle="Track the history of openings you have looked at and check spatial information by selecting the openings in the table below"
        />
        <Button
          className="map-button"
          renderIcon={Location}
          type="button"
          size={breakpoint === 'sm' ? 'sm' : 'lg'}
          onClick={toggleMap}
          disabled={!recentOpeningsQuery.data?.data.length}
        >
          {showMap ? 'Hide map' : 'Show map'}
        </Button>
      </div>
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
      {/* Table skeleton */}
      {
        recentOpeningsQuery.isLoading
          ? <TableSkeleton headers={recentOpeningsHeaders} showToolbar={false} showHeader={false} />
          : null
      }
      {/* Empty Table */}
      {
        !recentOpeningsQuery.isLoading && !recentOpeningsQuery.data?.data.length ? (
          <EmptySection
            pictogram="Magnify"
            title="There are no openings to show yet"
            description="Your recent openings will appear here once you generate one"
            fill="#0073E6"
          />
        )
          : null
      }
      {/* Loaded table content */}
      {
        !recentOpeningsQuery.isLoading && recentOpeningsQuery.data?.data.length ?
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
                  recentOpeningsQuery.data?.data.map((row) => (
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
      <ComingSoonModal
        openingDetails={openingDetails}
        setOpeningDetails={setOpeningDetails}
      />
    </div>
  );
};

export default RecentOpenings;
