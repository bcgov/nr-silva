import React, {useState} from "react";
import { useSelector } from "react-redux";
import { Button } from "@carbon/react";
import FavouriteCard from "../../components/FavouriteCard";
import PageTitle from "../../components/PageTitle";
import './Opening.scss'
import { ViewFilled } from '@carbon/icons-react';
import OpeningsMap from "../../components/OpeningsMap";
import OpeningScreenDataTable from "../../components/OpeningScreenDataTable/index";
import { headers, rows } from "../../components/ActionsTable/testData";
import SectionTitle from "../../components/SectionTitle";
import BarChartGrouped from "../../components/BarChartGrouped";
import ChartContainer from "../../components/ChartContainer";
import { DonutChart } from "@carbon/charts-react";
import DonutChartView from "../../components/DonutChartView";
import OpeningHistory from "../../components/OpeningHistory";
import OpeningHistoryItems from "../../mock-data/OpeningHistoryItems";
import ActionsTable from "../../components/ActionsTable";


const OpeningMetrics: React.FC = () => {
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
        <div className="row px-0 py-4 p-sm-4">
          <PageTitle title="Openings Metrics" subtitle="Create, manage or check opening information " />
        </div>
      </div>

      <div className="favourite-activities">
        <div className="row gy-0 gy-sm-4 ">
          <div className="col-sm-6 col-md-6 col-lg-6 col-xl-6 col-xxl-3">
            <FavouriteCard index={1} title={'Search opening'} link={'/'} icon={'SearchLocate'} description={'Search for a opening to update or consult using advance filters such as activities, standards, codes or links'}/>
          </div>
          <div className="col-sm-6 col-md-6 col-lg-6 col-xl-6 col-xxl-3">
            <FavouriteCard index={1} title={'Create a new opening'} link={'/'} icon={'MapBoundary'} description={'Register a opening for tracking silviculture treatments'}/>
          </div>
          <div className="col-sm-6 col-md-6 col-lg-6 col-xl-6 col-xxl-3">
            <FavouriteCard index={1} title={'Reports'} link={'/'} icon={'ReportData'} description={'Consult and manage opening reports'}/>
          </div>
          <div className="col-sm-6 col-md-6 col-lg-6 col-xl-6 col-xxl-3">
            <FavouriteCard index={1} title={'Upcoming activities'} link={'/'} icon={'Activity'} description={'Track opening activities and milestones'}/>
          </div>
        </div>
      </div>
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

export default OpeningMetrics;
