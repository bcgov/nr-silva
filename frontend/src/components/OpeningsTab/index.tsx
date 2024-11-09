import React, { useEffect, useState } from 'react';
import { Button, InlineNotification } from '@carbon/react';
import './styles.scss'
import { Location } from '@carbon/icons-react';
import OpeningsMap from '../OpeningsMap';
import OpeningScreenDataTable from '../OpeningScreenDataTable/index';
import { headers } from '../OpeningScreenDataTable/testData';
import { fetchRecentOpenings } from '../../services/OpeningService';
import SectionTitle from '../SectionTitle';
import TableSkeleton from '../TableSkeleton';
import { RecentOpening } from '../../types/RecentOpening';
import { generateHtmlFile } from './layersGenerator';
import { getWmsLayersWhitelistUsers, WmsLayersWhitelistUser } from '../../services/SecretsService';
import { useGetAuth } from '../../contexts/AuthProvider';

interface Props {
  showSpatial: boolean;
  setShowSpatial: (show: boolean) => void;
}

const OpeningsTab: React.FC<Props> = ({ showSpatial, setShowSpatial }) => {
  const [loading, setLoading] = useState<boolean>(true);
  const [openingRows, setOpeningRows] = useState<RecentOpening[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loadId, setLoadId] = useState<number | null>(null);
  const [openingPolygonNotFound, setOpeningPolygonNotFound] = useState<boolean>(false);
  const [wmsUsersWhitelist, setWmsUsersWhitelist] = useState<WmsLayersWhitelistUser[]>([]);  
  const { user } = useGetAuth();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const rows: RecentOpening[] = await fetchRecentOpenings();
        setOpeningRows(rows);
        setLoading(false);
        setError(null);
      } catch (error) {
        console.error('Error fetching recent openings:', error);
        setLoading(false);
        setError('Failed to fetch recent openings');
      }
    };

    const fetchAllowedPeople = async () => {
      try {
        const usersList: WmsLayersWhitelistUser[] = await getWmsLayersWhitelistUsers();
        setWmsUsersWhitelist(usersList);
      } catch (error) {
        console.error('Error fetching recent openings:', error);
      }
    };

    fetchData();
    fetchAllowedPeople();
  }, []);

  useEffect(() => {}, [loadId, openingPolygonNotFound, wmsUsersWhitelist]);

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
            disabled
          >
            {showSpatial ? 'Hide map' : 'Show map'}
          </Button>
        </div>
        {showSpatial ? (
          <div className="row px-2">
            <div className="leaflet-container">
              <OpeningsMap
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
            setOpeningId={setLoadId}
            showSpatial={showSpatial}
          />
        )}
      </div>
    </>
  );
};

export default OpeningsTab;
