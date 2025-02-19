import React from "react";
import ChartTitle from "../ChartTitle";
import { SubmissionTrendChartObj } from "../OpeningSubmissionTrend/definitions";
import { status } from "../../services/search/openings";
import './index.scss'

interface BarChartTooltipProps {
  datum: SubmissionTrendChartObj
}

const BarChartTooltip: React.FC<BarChartTooltipProps> = ({ datum }) => {

  const statusDescription = (statusCode: string) => (
    status.find((statusData) => statusData.value === statusCode)?.text ?? statusCode
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
