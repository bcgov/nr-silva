import React from "react";
import './styles.scss'
import BarChartGrouped from "../BarChartGrouped";
import ChartContainer from "../ChartContainer";
import DonutChartView from "../DonutChartView";
import OpeningHistory from "../OpeningHistory";
import OpeningHistoryItems from "../../mock-data/OpeningHistoryItems";
import MyRecentActions from "../MyRecentActions";

const OpeningMetricsTab: React.FC = () => (
  <>
    <div className="container-fluid p-3">
      <div className="row gy-3">
        <div className="col-xxl-6">
          <ChartContainer
            title="Openings per years"
            description="Check openings and submissions per year"
          >
            <BarChartGrouped />
          </ChartContainer>
        </div>
        <div className="col-xxl-6">
          <ChartContainer title="Track Openings" description="Follow your favouirite openings">
            <OpeningHistory 
              histories={OpeningHistoryItems}
            />
          </ChartContainer>
        </div>
        <div className="col-xxl-6">
          <ChartContainer title="Free growing milestone declarations" description="Check opening standards unit for inspections purposes">
            <DonutChartView/>
          </ChartContainer>
        </div>
        <div className="col-xxl-6">
          <ChartContainer title="My recent actions" description="Check your recent requests and files">
            <MyRecentActions/>
          </ChartContainer>
        </div>
        
      </div>
    </div>
  </>
);

export default OpeningMetricsTab;
