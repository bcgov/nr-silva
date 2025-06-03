import React from "react";
import { SubmissionTrendChartObj } from "../OpeningSubmissionTrend/definitions";
import { OPENING_STATUS_LIST } from "../../constants";

import "./index.scss";

interface BarChartTooltipProps {
  datum: SubmissionTrendChartObj;
}

const BarChartTooltip: React.FC<BarChartTooltipProps> = ({ datum }) => {
  const statusDescription = (code: string) =>
    (
      OPENING_STATUS_LIST.find(
        (statusData) => statusData.code.toLowerCase() === code.toLowerCase()
      )?.description ?? code
    ).toLowerCase();

  return (
    <section className="bar-chart-tooltip" aria-label="Bar chart details">
      <header>
        <span className="title-bold">{datum.group}</span>{" "}
        <span className="title-code">{datum.value}</span>
      </header>

      <div className="helper-text">
        Click on the bar to see the list in detail
      </div>

      <ul className="status-list">
        {Object.keys(datum.statusCounts).map((key) => (
          <li key={key}>
            {`${datum.statusCounts[key]} ${statusDescription(key)}`}
          </li>
        ))}
      </ul>
    </section>
  );
};

export default BarChartTooltip;
