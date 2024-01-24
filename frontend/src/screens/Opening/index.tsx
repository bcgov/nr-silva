import React, {useState} from "react";
import { useSelector } from "react-redux";
import { Button } from "@carbon/react";
import StandardCard from "../../components/StandardCard";
import FavouriteCard from "../../components/FavouriteCard";
import PageTitle from "../../components/PageTitle";
import RecentOpeningsTable from "../../components/RecentOpeningsTable";
import { recentOpeningItems, recentOpeningItems1, recentOpeningsHeader } from "../../mock-data/constants";
import './Opening.scss'
import { ViewFilled } from '@carbon/icons-react';
import OpeningsMap from "../../components/OpeningsMap";
import OpeningScreenDataTable from "../../components/OpeningScreenDataTable/index";
import { headers, rows } from "../../components/OpeningScreenDataTable/testData";
import SectionTitle from "../../components/SectionTitle";


const Opening: React.FC = () => {
  const [showSpatial, setShowSpatial] = useState<boolean>(false);

  const toggleSpatial = () => {
    setShowSpatial(!showSpatial)
  }

  const userDetails = useSelector((state:any) => state.userDetails)
  const goToActivity = () => {
    console.log("clicked a row")
  }
  const { user } = userDetails
    return (
      <>
      <div className="container-fluid">
        <div className="row px-0 py-4 p-md-4">
          <PageTitle title="Openings" subtitle="Create, manage or check opening information " />
        </div>
      </div>

      <div className="favourite-activities">
        <div className="row">
          <div className="col-md-6 col-lg-6 col-xl-6 col-xxl-3">
            <FavouriteCard index={1} title={'Search opening'} link={'/'} icon={'SearchLocate'} description={'Search for a opening to update or consult using advance filters such as activities, standards, codes or links'}/>
          </div>
          <div className="col-md-6 col-lg-6 col-xl-6 col-xxl-3">
            <FavouriteCard index={1} title={'Create a new opening'} link={'/'} icon={'MapBoundary'} description={'Register a opening for tracking silviculture treatments'}/>
          </div>
          <div className="col-md-6 col-lg-6 col-xl-6 col-xxl-3">
            <FavouriteCard index={1} title={'Reports'} link={'/'} icon={'ReportData'} description={'Consult and manage opening reports'}/>
          </div>
          <div className="col-md-6 col-lg-6 col-xl-6 col-xxl-3">
            <FavouriteCard index={1} title={'Upcoming activities'} link={'/'} icon={'Activity'} description={'Track opening activities and milestones'}/>
          </div>
        </div>
      </div>

      <div className="container-fluid">
        <div className="row px-0 py-4 p-md-4">
            <SectionTitle title="Recent openings" subtitle="Track your recent openings and select to check spatial activity" />
            <Button className="h-100 my-auto d-none d-md-block" renderIcon={ViewFilled} type="button" onClick={toggleSpatial}>
              {showSpatial?'Hide Spatial':'Show Spatial'}
            </Button>
        </div>
        {showSpatial?(
          <div className="row px-2">
            <div className="leaflet-container">
              <OpeningsMap selectedBasemap={{
                id: 1,
                name: "Google Maps Satelite",
                attribution: '&copy; Google Maps',
                url: "https://www.google.cn/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}",
              }}/>
            </div>
          </div>
        ):null}
      </div>
      

      <div className="container-fluid p-0">
        <OpeningScreenDataTable
        headers={headers}
        rows={rows}
        />
      </div>
      </>
    );
  };

export default Opening;