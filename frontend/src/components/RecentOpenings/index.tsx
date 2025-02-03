import React, { useState } from 'react';
import { Button, InlineNotification } from '@carbon/react';
import './styles.scss';
import { Location } from '@carbon/icons-react';
import OpeningsMap from '../OpeningsMap';
import RecentOpeningsDataTable from '../Dashboard/Opening/RecentOpeningsDataTable';
import { useUserRecentOpeningQuery } from '../../services/queries/search/openingQueries';
import SectionTitle from '../SectionTitle';
import TableSkeleton from '../TableSkeleton';
import { recentOpeningsColumns as headers } from '../../constants/tableConstants';
import useBreakpoint from '../../hooks/UseBreakpoint';

const RecentOpenings = () => {
  const [showMap, setShowMap] = useState<boolean>(false);
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [openingPolygonNotFound, setOpeningPolygonNotFound] = useState<boolean>(false);
  const { data, isFetching } = useUserRecentOpeningQuery();
  const breakpoint = useBreakpoint();

  const toggleMap = () => {
    setShowMap(!showMap);
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
            />
          )
          : null
      }
      <div className="container-fluid p-0 pb-5">
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
        {
          isFetching ?
            (
              <TableSkeleton headers={headers} />
            )
            : (
              <RecentOpeningsDataTable
                rows={data?.data || []}
                headers={headers}
                setOpeningIds={setSelectedOpeningIds}
                showSpatial={showMap}
                totalItems={(data?.perPage ?? 0) * (data?.totalPages ?? 0)}
              />
            )
        }
      </div>
    </div>
  );
};

export default RecentOpenings;
