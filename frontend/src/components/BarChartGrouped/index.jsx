import React from "react";
import ReactDOM from "react-dom";
import { GroupedBarChart } from "@carbon/charts-react";
import "@carbon/charts/styles.css";

const BarChartGrouped = () => {
  const colors = {
    "Dataset 1": "red",
    "Dataset 2": "green",
  };

  const data = [
    {
      group: "Dataset 1",
      key: "Jul",
      value: 65000,
    },
    {
      group: "Dataset 1",
      key: "Aug",
      value: 51213,
    },
    {
      group: "Dataset 1",
      key: "Sept",
      value: 16932,
    },
    {
      group: "Dataset 1",
      key: "Oct",
      value: 17932,
    },
    {
      group: "Dataset 1",
      key: "Nov",
      value: 17932,
    },
    {
      group: "Dataset 1",
      key: "Dec",
      value: 17932,
    },
    {
      group: "Dataset 2",
      key: "Jul",
      value: 42678,
    },
    {
      group: "Dataset 2",
      key: "Aug",
      value: 29384,
    },
    {
      group: "Dataset 2",
      key: "Sept",
      value: 45678,
    },
    {
      group: "Dataset 2",
      key: "Oct",
      value: 28765,
    },
    {
      group: "Dataset 2",
      key: "Nov",
      value: 34890,
    },
    {
      group: "Dataset 2",
      key: "Dec",
      value: 21456,
    },
  ];

  const options = {
    title: "",
    axes: {
      left: {
        mapsTo: "value",
      },
      bottom: {
        scaleType: "labels",
        mapsTo: "key",
      },
    },
    color: {
      scale: colors,
    },
    height: "400px",
  };

  return (
    <GroupedBarChart
      data={data}
      options={options}
    ></GroupedBarChart>
  );
};

export default BarChartGrouped;
