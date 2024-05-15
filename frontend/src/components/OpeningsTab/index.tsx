import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Button } from "@carbon/react";
import './styles.scss'
import { ViewFilled } from '@carbon/icons-react';
import OpeningsMap from "../OpeningsMap";
import OpeningScreenDataTable from "../OpeningScreenDataTable/index";
import { headers } from "../OpeningScreenDataTable/testData";
import { fetchRecentOpenings } from "../../services/OpeningService";
import SectionTitle from "../SectionTitle";
import TableSkeleton from "../TableSkeleton";
import { InlineNotification } from '@carbon/react';

const OpeningsTab: React.FC = () => {
  const [showSpatial, setShowSpatial] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(true);
  const [openingRows, setOpeningRows] = useState<any[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loadId, setLoadId] = useState<number | null>(null);
  const [openingPolygonNotFound, setOpeningPolygonNotFound] = useState<boolean>(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const rows = await fetchRecentOpenings();
        setOpeningRows(rows);
        setLoading(false);
        setError(null);
      } catch (error) {
        console.error('Error fetching recent openings:', error);
        setLoading(false);
        setError('Failed to fetch recent openings');
      }
    };

    fetchData();
  }, []);

  useEffect(() => {
    console.log('Loading OpeningsTab components!');
  }, [loadId, openingPolygonNotFound]);

  const toggleSpatial = () => {
    setShowSpatial(!showSpatial)
  }

  const userDetails = useSelector((state: any) => state.userDetails)
  const goToActivity = () => {
    console.log("clicked a row")
  }
  const { user } = userDetails

  return (
    <>
      <div>
        <div className="container-fluid">
          <div className="row px-0 py-4 p-sm-4">
            <SectionTitle title="Recent openings" subtitle="Track your recent openings and select to check spatial activity" />
            <Button className="h-100 my-auto d-none d-sm-block" renderIcon={ViewFilled} type="button" onClick={toggleSpatial}>
              {showSpatial ? 'Hide Spatial' : 'Show Spatial'}
            </Button>
          </div>
          {showSpatial ? (
            <div className="row px-2">
              <div className="leaflet-container">
                <OpeningsMap
                  selectedBasemap={{
                    id: 1,
                    name: "Google Maps Satelite",
                    attribution: '&copy; Google Maps',
                    url: "https://www.google.ca/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}",
                  }}
                  openingId={loadId}
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
          {loading ? (
            <TableSkeleton headers={headers} />
          ) : (
            <OpeningScreenDataTable
              headers={headers}
              rows={openingRows}
              error={error}
              setOpeningIds={setLoadId}
            />
          )}
        </div>
      </div>
    </>
  );
};

export default OpeningsTab;