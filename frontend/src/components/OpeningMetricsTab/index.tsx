import React, { useRef, useEffect } from "react";
import './styles.scss';
import SectionTitle from "../SectionTitle";
import BarChartGrouped from "../BarChartGrouped";
import ChartContainer from "../ChartContainer";
import DoughnutChartView from "../DoughnutChartView";
import OpeningHistory from "../OpeningHistory";
import MyRecentActions from "../MyRecentActions";

const OpeningMetricsTab: React.FC = () => {
  const trackOpeningRef = useRef<HTMLDivElement>(null);
  
  // Optional: Scroll to "Track Openings" when this component mounts
  useEffect(() => {
    
    const params = new URLSearchParams(window.location.search);
    const scrollToSection = params.get('scrollTo');

    if (scrollToSection === 'trackOpenings' && trackOpeningRef.current) {
      trackOpeningRef.current.scrollIntoView({ behavior: "smooth" });
    }

  }, []);

  return (
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
          <div className="col-xxl-6" ref={trackOpeningRef}> {/* Add ref here to scroll */}
            <ChartContainer title="Track Openings" description="Follow your favourite openings">              
              <OpeningHistory />
            </ChartContainer>
          </div>
          <div className="col-xxl-6">
            <ChartContainer
              title="Free growing milestone declarations"
              description="Check opening standards unit for inspections purposes"
            >
              <DoughnutChartView />
            </ChartContainer>
          </div>
          <div className="col-xxl-6">
            <ChartContainer
              title="My recent actions"
              description="Check your recent requests and files"
            >
              <MyRecentActions />
            </ChartContainer>
          </div>
        </div>
      </div>
    </>
  );
};

export default OpeningMetricsTab;
