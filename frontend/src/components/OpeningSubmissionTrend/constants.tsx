import ReactDOMServer from "react-dom/server";
import { OpeningPerYearChart } from "../../types/OpeningPerYearChart";
import BarChartTooltip from "../BarChartTooltip";
import { ScaleTypes } from "@carbon/charts";


const tooltip = (data: OpeningPerYearChart[], defaultHTML: string, datum: OpeningPerYearChart) => {
  const tooltipContent = <BarChartTooltip datum={datum} />;
  return ReactDOMServer.renderToString(tooltipContent);
}

const colors = { Openings: "#1192E8" };

export const ChartOptions = {
  axes: {
    left: { mapsTo: "value" },
    bottom: {
      scaleType: ScaleTypes.LABELS,
      mapsTo: "key"
    }
  },
  color: {
    scale: colors
  },
  height: "18.5rem",
  grid: {
    x: {
      enabled: false,
      color: "#d3d3d3",
      strokeDashArray: "2,2"
    },
    y: {
      enabled: true,
      color: "#d3d3d3",
      strokeDashArray: "2,2"
    }
  },
  toolbar: {
    enabled: false,
    numberOfIcons: 2,
    controls: [
      {
        type: "Make fullscreen"
      },
      {
        type: "Make fullscreen"
      }
    ]
  },
  tooltip: {
    enabled: true,
    customHTML: tooltip
  },
  legend: {
    enabled: false
  }
};
