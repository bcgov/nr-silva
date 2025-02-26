import React from "react";
import ChartTitle from "../ChartTitle";
import { SubmissionTrendChartObj } from "../OpeningSubmissionTrend/definitions";
import { OPENING_STATUS_LIST } from "../../constants";

import './index.scss'

interface BarChartTooltipProps {
  datum: SubmissionTrendChartObj
}

const BarChartTooltip: React.FC<BarChartTooltipProps> = ({ datum }) => {

  const statusDescription = (code: string) => (
    OPENING_STATUS_LIST.find((statusData) => statusData.code.toLowerCase() === code.toLowerCase())?.description ?? code
  ).toLowerCase();

  return (
    <div>
      <ChartTitle title={`${datum.value} ${datum.group}`} subtitle="Click on the bar to see openings" />
      <ul className="bar--tooltip_status">
        {Object.keys(datum.statusCounts).map((key) => (<li key={key}><p><b>{datum.statusCounts[key]} {statusDescription(key)}</b></p></li>))}
      </ul>
    </div>
  );
};

export default BarChartTooltip;
