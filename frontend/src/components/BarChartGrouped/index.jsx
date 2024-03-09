import { GroupedBarChart } from "@carbon/charts-react";
import { Dropdown, DatePicker, DatePickerInput } from "@carbon/react";
import "@carbon/charts/styles.css";
import { useState } from "react";

const BarChartGrouped = () => {
  const [windowWidth, setWindowWidth] = useState(window.innerWidth);
  const handleResize = ()=>{
    setWindowWidth(window.innerWidth);
  }
  window.addEventListener('resize', handleResize);

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
      enabled: false,
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

  //sample data
  const items = [
    { value: 1, text: 'Option 1' },
    { value: 2, text: 'Option 2' },
    { value: 3, text: 'Option 3' },
    { value: 4, text: 'Option 4' },
    // Add more options as needed
  ];


  return (
    <>
      <div className="row gy-2">
        <div className="col-md-4 p-0">
          <Dropdown id="default" label={windowWidth<=1584?"District":"Filter by district"} items={items} itemToString={item => item ? item.text : ''} />
        </div>
        <div className="col-md-4 p-0 px-md-1">
          <Dropdown id="default" label={windowWidth<=1584?"Status":"Filter by status"} items={items} itemToString={item => item ? item.text : ''} />
        </div>
        <div className="col-4 p-0 d-none d-md-block">
          <DatePicker datePickerType="single">
            <DatePickerInput
              id="date-picker-input-id"
              placeholder="mm/dd/yyyy"
              size="md"
            />
          </DatePicker>
        </div>
      </div>
      <GroupedBarChart
        data={data}
        options={options}
      >
      </GroupedBarChart>
    </>
  );
};

export default BarChartGrouped;
