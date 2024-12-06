import React from "react";
import { OpeningPerYearChart } from "../../types/OpeningPerYearChart";
import { status } from "../../services/search/openings";

interface BarChartTooltipProps {
  data: OpeningPerYearChart[];
  defaultHTML: string;
  datum: OpeningPerYearChart
}

const BarChartTooltip: React.FC<BarChartTooltipProps> = ({ datum }) => {

  const statusDescription = (statusCode: string) => (
    status.find((statusData) => statusData.value === statusCode)?.text ?? statusCode
  ).toLowerCase();

  return (
    <div>
      <h4><b>{datum.value} {datum.group}</b></h4>
      <ul>
        {Object.keys(datum.statusCount).map((key, index) => (<li key={index}><p><b>{datum.statusCount[key]} {statusDescription(key)}</b></p></li>))}
      </ul>
    </div>
  );
};

export default BarChartTooltip;