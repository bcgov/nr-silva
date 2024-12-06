import React from "react";
import { Button } from "@carbon/react";
import * as Icons from "@carbon/icons-react";
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
      <div>
        <Button
        hasIconOnly
        kind="ghost"
        renderIcon={Icons.MapBoundary}
        onClick={() => null}
        size="md"
      />
          {datum.value} {datum.group}</div>
      <div>
        <ul>
          {Object.keys(datum.statusCount).map((status, index) => (<li key={index}>{datum.statusCount[status]} {statusDescription(status)}</li>))}
        </ul>
      </div>
      <Button
        iconDescription="Go to the Openings search page"
        tooltipPosition="bottom"
        renderIcon={Icons.List}
        onClick={() => null}
        size="md"
      >
        Go to openings
      </Button>
    </div>
  );
};

export default BarChartTooltip;