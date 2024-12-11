import React from "react";
import ChartTitle from "../ChartTitle";
import { OpeningPerYearChart } from "../../types/OpeningPerYearChart";
import { status } from "../../services/search/openings";
import './index.scss'

interface BarChartTooltipProps {
  datum: OpeningPerYearChart
}

const BarChartTooltip: React.FC<BarChartTooltipProps> = ({ datum }) => {

  const statusDescription = (statusCode: string) => (
    status.find((statusData) => statusData.value === statusCode)?.text ?? statusCode
  ).toLowerCase();

  return (
    <div>
      <ChartTitle title={`${datum.value} ${datum.group}`} subtitle="Click on the bar to see openings" />
      <ul className="bar--tooltip_status">
        {Object.keys(datum.statusCount).map((key) => (<li key={key}><p><b>{datum.statusCount[key]} {statusDescription(key)}</b></p></li>))}
      </ul>
    </div>
  );
};

export default BarChartTooltip;