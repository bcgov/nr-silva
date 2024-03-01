import React, {useState} from "react";
import { useSelector } from "react-redux";
import FavouriteCard from "../FavouriteCard";
import PageTitle from "../PageTitle";
import './styles.scss'
import BarChartGrouped from "../BarChartGrouped";
import ChartContainer from "../ChartContainer";
import DonutChartView from "../DonutChartView";
import OpeningHistory from "../OpeningHistory";
import OpeningHistoryItems from "../../mock-data/OpeningHistoryItems";
import ActionsTable from "../ActionsTable";


const OpeningMetricsTab: React.FC = () => {
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
      <div className="container-fluid p-3">
        <div className="row gy-3">
          <div className="col-md-6">
            <ChartContainer title="Openings per year" description="Check openings and submissions per year">
              <BarChartGrouped/>
            </ChartContainer>
          </div>
          <div className="col-md-6">
            <ChartContainer title="Track Openings" description="Follow your favouirite openings">
              <OpeningHistory 
                histories={OpeningHistoryItems}
              />
            </ChartContainer>
          </div>
          <div className="col-md-6">
            <ChartContainer title="Free grow milestone declarations" description="Check opening standards unit for inspections purposes">
              <DonutChartView/>
            </ChartContainer>
          </div>
          <div className="col-md-6">
            <ChartContainer title="Free grow milestone declarations" description="Check opening standards unit for inspections purposes">
              <ActionsTable />
            </ChartContainer>
          </div>
          
        </div>
      </div>
      </>
    );
  };

export default OpeningMetricsTab;