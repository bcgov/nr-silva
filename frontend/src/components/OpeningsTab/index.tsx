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

interface Props {
  showSpatial: boolean;
  setShowSpatial: (show: boolean) => void;
}

const OpeningsTab: React.FC<Props> = ({ showSpatial, setShowSpatial }) => {
  const [selectedOpeningIds,setSelectedOpeningIds] = useState<number[]>([]);
  const [openingPolygonNotFound, setOpeningPolygonNotFound] = useState<boolean>(false);
  const { data, isFetching } = useUserRecentOpeningQuery(10);

  const toggleSpatial = () => {
    setShowSpatial(!showSpatial);
  };

  return (
    <>
      <div className="container-fluid">
        <div className="row px-0 py-4 p-sm-4">
          <SectionTitle
            title="Recent openings"
            subtitle="Track the history of openings you have looked at and check spatial information by selecting the openings in the table below"
          />
          <Button
            className="h-100 my-auto d-none d-sm-block"
            renderIcon={Location}
            type="button"
            onClick={() => toggleSpatial()}
          >
            {showSpatial ? 'Hide map' : 'Show map'}
          </Button>
        </div>
        {showSpatial ? (
          <div className="row px-2">
            <div className="leaflet-container">
              <OpeningsMap
                openingId={null}
                openingIds={selectedOpeningIds}
                setOpeningPolygonNotFound={setOpeningPolygonNotFound}
              />
            </div>
          </div>
        ) : null}
      </div>

      <div className="container-fluid p-0 pb-5">
        {openingPolygonNotFound? (
          <InlineNotification
            title="Opening ID not found!"
            subtitle="Unable to find selected Opening Polygon!"
            kind="error"
            lowContrast
            className = "inline-notification"
          />
        ) : null }
        {isFetching ? (
          <TableSkeleton headers={headers} />
        ) : (
          <RecentOpeningsDataTable
            rows={data?.data || []}
            headers={headers}
            setOpeningIds={setSelectedOpeningIds}
            showSpatial={showSpatial}
            totalItems={(data?.perPage ?? 0) * (data?.totalPages ?? 0)}
          />
        )}
      </div>
    </>
  );
};

export default OpeningsTab;
