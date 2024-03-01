import { GroupedBarChart } from "@carbon/charts-react";
import "@carbon/charts/styles.css";

const BarChartGrouped = () => {
  const colors = {
    "Openings": "#1192E8",
  };

  const data = [
    {
      group: "Openings",
      key: "Mar",
      value: 70,
    },
    {
      group: "Openings",
      key: "Apr",
      value: 300,
    },
    {
      group: "Openings",
      key: "May",
      value: 200,
    },
    {
      group: "Openings",
      key: "Jun",
      value: 140,
    },
    {
      group: "Openings",
      key: "Jul",
      value: 180,
    },
    {
      group: "Openings",
      key: "Aug",
      value: 100,
    },
    {
      group: "Openings",
      key: "Sep",
      value: 40,
    },
    {
      group: "Openings",
      key: "Oct",
      value: 90,
    },
    {
      group: "Openings",
      key: "Nov",
      value: 160,
    },
    {
      group: "Openings",
      key: "Dec",
      value: 70,
    },
    {
      group: "Openings",
      key: "Jan",
      value: 125,
    },
    {
      group: "Openings",
      key: "Feb",
      value: 250,
    }
  ];

  const options = {
    title:"Openings",
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
    height: "23.5rem",
    toolbar: {
      enabled: true,
      numberOfIcons: 2,
      controls: [
        {
          type: "Make fullscreen"
        },
        {
          type: "Make fullscreen"
        },
      ]
    }
  };


  return (
    <GroupedBarChart
      data={data}
      options={options}
    ></GroupedBarChart>
  );
};

export default BarChartGrouped;
