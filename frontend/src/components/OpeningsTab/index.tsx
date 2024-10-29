import React, { useEffect, useState } from 'react';
import { Button } from '@carbon/react';
import './styles.scss'
import { Location } from '@carbon/icons-react';
import OpeningsMap from '../OpeningsMap';
import OpeningScreenDataTable from '../OpeningScreenDataTable/index';
import { columns } from '../Dashboard/Opening/RecentOpeningsDataTable/testData';
import { fetchRecentOpenings } from '../../services/OpeningService';
import SectionTitle from '../SectionTitle';
import TableSkeleton from '../TableSkeleton';
import { InlineNotification } from '@carbon/react';
import { RecentOpening } from '../../types/RecentOpening';
import { useSelector } from 'react-redux';
import { RootState } from '../../store';
import { generateHtmlFile } from './layersGenerator';
import { getWmsLayersWhitelistUsers, WmsLayersWhitelistUser } from '../../services/SecretsService';
import { useUserRecentOpeningQuery } from '../../services/queries/search/openingQueries';
import RecentOpeningsDataTable from '../Dashboard/Opening/RecentOpeningsDataTable';
import { ITableHeader } from '../../types/TableHeader';

interface Props {
  showSpatial: boolean;
  setShowSpatial: Function;
}

const OpeningsTab: React.FC<Props> = ({ showSpatial, setShowSpatial }) => {
  const [loading, setLoading] = useState<boolean>(true);
  const [openingRows, setOpeningRows] = useState<RecentOpening[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loadId, setLoadId] = useState<number | null>(null);
  const [openingPolygonNotFound, setOpeningPolygonNotFound] = useState<boolean>(false);
  const [wmsUsersWhitelist, setWmsUsersWhitelist] = useState<WmsLayersWhitelistUser[]>([]);
  const userDetails = useSelector((state: RootState) => state.userDetails);
  const { data, isFetching } = useUserRecentOpeningQuery(10);
  const [headers, setHeaders] = useState<ITableHeader[]>(columns);

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
    setShowSpatial((prevShowSpatial :boolean) => !prevShowSpatial);
  };

  const onClickFn = () => {
    const allowed: string[] = wmsUsersWhitelist.map((user: WmsLayersWhitelistUser) => user.userName);
    const { userName } = userDetails.user;
    if (allowed.includes(userName)) {
      const newWindow = window.open();
      if (newWindow) {
        newWindow.document.body.innerHTML = generateHtmlFile();
      }
    }
  };

  const handleCheckboxChange = (columnKey: string) => {
    if(columnKey === "select-default"){
      //set to the deafult
      setHeaders(columns)
    }
    else if(columnKey === "select-all"){
      setHeaders((prevHeaders) =>
        prevHeaders.map((header) => ({
          ...header,
          selected: true, // Select all headers
        }))
      );
    }
    else{
      setHeaders((prevHeaders) =>
        prevHeaders.map((header) =>
          header.key === columnKey
            ? { ...header, selected: !header.selected }
            : header
        )
      );
    }
    
  };

  return (
    <>
      <div className="container-fluid">
        <div className="row px-0 py-4 p-sm-4">
          <SectionTitle
            title="Recent openings"
            subtitle="Track the history of openings you have looked at and check spatial information by selecting the openings in the table below"
            onClick={onClickFn}
          />
          <Button
            className="h-100 my-auto d-none d-sm-block"
            renderIcon={Location}
            type="button"
            onClick={() => toggleSpatial()}
          >
            {showSpatial ? 'Hide Spatial' : 'Show Spatial'}
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
          <RecentOpeningsDataTable
              rows={data?.data || []}
              headers={headers}
              defaultColumns={headers}
              handleCheckboxChange={handleCheckboxChange}
              setOpeningId={setLoadId}
              toggleSpatial={toggleSpatial}
              showSpatial={showSpatial}
              totalItems={(data?.perPage ?? 0) * (data?.totalPages ?? 0)}
            />
        )}
      </div>
    </>
  );
};

export default OpeningsTab;
