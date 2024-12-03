import React, { useRef, useEffect } from "react";
import './styles.scss';
import { FlexGrid, Row, Column } from "@carbon/react";
import SectionTitle from "../SectionTitle";
import BarChartGrouped from "../BarChartGrouped";
import ChartContainer from "../ChartContainer";
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
      <FlexGrid className="metricsContainer" narrow>
        <Row>
          <Column sm={4}>
            <SectionTitle title="Dashboard" subtitle="Manage and track silvicultural information about openings" />
          </Column>
        </Row>
        <Row>
          <Column sm={2}>
            <ChartContainer
              title="Openings submission trends"
              description="Check quantity and evolution of openings"
            >
              <BarChartGrouped />
            </ChartContainer>
          </Column>
          <Column sm={2}>
            {/* <div className="col-xxl-6" ref={trackOpeningRef}> Add ref here to scroll */}
              <ChartContainer title="Track Openings" description="Follow your favourite openings">
                <OpeningHistory />
              </ChartContainer>
            {/* </div> */}
          </Column>
        </Row>
        <Row>
          <Column sm={4}>
            <ChartContainer
              title="My recent actions"
              description="Check your recent requests and files"
            >
              <MyRecentActions />
            </ChartContainer>
          </Column>
        </Row>

      </FlexGrid>
    </>
  );
};

export default OpeningMetricsTab;
