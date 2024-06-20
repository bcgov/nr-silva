import React from "react";
import './styles.scss';
import SectionTitle from "../SectionTitle";
import BarChartGrouped from "../BarChartGrouped";
import ChartContainer from "../ChartContainer";
import DonutChartView from "../DonutChartView";
import OpeningHistory from "../OpeningHistory";
import OpeningHistoryItems from "../../mock-data/OpeningHistoryItems";
import MyRecentActions from "../MyRecentActions";

const OpeningMetricsTab: React.FC = () => (
  <>
    <div className="container-fluid tab-padding">
    <div className="title-container pb-32">
      <SectionTitle title="Dashboard" subtitle="Manage and track silvicultural information about openings" />
    </div>
      <div className="row gy-4">
        <div className="col-xxl-6">
          <ChartContainer
            title="Openings submission trends"
            description="Check quantity and evolution of openings"
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
