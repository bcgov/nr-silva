import React, {useState} from "react";
import { useSelector } from "react-redux";
import { Button } from "@carbon/react";
import './styles.scss'
import { ViewFilled } from '@carbon/icons-react';
import OpeningsMap from "../OpeningsMap";
import OpeningScreenDataTable from "../OpeningScreenDataTable/index";
import { headers, rows } from "../OpeningScreenDataTable/testData";
import SectionTitle from "../SectionTitle";


const OpeningsTab: React.FC = () => {
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
        <div>
          <div className="container-fluid">
            <div className="row px-0 py-4 p-sm-4">
                <SectionTitle title="Recent openings" subtitle="Track your recent openings and select to check spatial activity" />
                <Button className="h-100 my-auto d-none d-sm-block" renderIcon={ViewFilled} type="button" onClick={toggleSpatial}>
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
                    url: "https://www.google.ca/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}",
                  }}/>
                </div>
              </div>
            ):null}
          </div>
          

          <div className="container-fluid p-0 pb-5">
            <OpeningScreenDataTable
            headers={headers}
            rows={rows}
            />
          </div>
        </div>
      </>
    );
  };

export default OpeningsTab;
