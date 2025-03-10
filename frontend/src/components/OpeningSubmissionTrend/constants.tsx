import ReactDOMServer from "react-dom/server";

import BarChartTooltip from "../BarChartTooltip";
import { BarChartOptions, ScaleTypes } from "@carbon/charts";
import { SubmissionTrendChartObj } from "./definitions";


const tooltip = (
  _data: SubmissionTrendChartObj[],
  _defaultHTML: string,
  datum: SubmissionTrendChartObj
) => {
  const tooltipContent = <BarChartTooltip datum={datum} />;
  return ReactDOMServer.renderToString(tooltipContent);
}

const colors = { Openings: "#1192E8" };

export const DefaultChartOptions: BarChartOptions = {
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
    },
    y: {
      enabled: true,
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
