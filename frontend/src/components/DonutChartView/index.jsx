import { DonutChart } from "@carbon/charts-react";
import "@carbon/charts/styles.css";
import './DonutChartView.scss';

const DonutChartView = () => {
  const colors = {
    "0-5 months": "#A56EFF",
    "6-11 months": "#002D9C",
    "12-17 months": "#1192E8",
    "18 months": "#FA4D56",
  };
  const data = [
    {
      "group": "0-5 months",
      "value": 200
    },
    {
      "group": "6-11 months",
      "value": 200
    },
    {
      "group": "12-17 months",
      "value": 200
    },
    {
      "group": "18 months",
      "value": 200
    }
  ];

  const options = {
    title: "",
    resizable: true,
    donut: {
      center: {
        label: "Standard units"
      }
    },
    color: {
      scale: colors,
    },
    height: "24.5rem"
  };

  return (
    <DonutChart
      data={data}
      options={options}>
    </DonutChart>
  );
};

export default DonutChartView;