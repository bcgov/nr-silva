import React from 'react';
import { DonutChart } from "@carbon/charts-react";
import { useState } from "react";
import { Dropdown, DatePicker, DatePickerInput } from "@carbon/react";
import "@carbon/charts/styles.css";
import './DonutChartView.scss';

const DonutChartView = () => {
  const [windowWidth, setWindowWidth] = useState(window.innerWidth);
  const handleResize = ()=>{
    setWindowWidth(window.innerWidth);
  }
  window.addEventListener('resize', handleResize);

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
    height: "18.5rem",
    toolbar:{
      enabled:false
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
    <div className="px-3">
      <div className="row gy-2 pb-3">
        <div className="col-md-4 p-0">
          <Dropdown
            id="default3"
            label={windowWidth<=1584?"District":"Filter by district"}
            items={items}
            itemToString={item => item ? item.text : ''}
            titleText={windowWidth<=1584?"District":"Filter by district"}
          />
        </div>
        <div className="col-md-4 p-0 px-md-1">
          <Dropdown
            id="default4"
            label={windowWidth<=1584?"Client Number":"Filter by client number"}
            items={items}
            itemToString={item => item ? item.text : ''}
            titleText={windowWidth<=1584?"Client Number":"Filter by client number"}
          />
        </div>
        <div className="col-4 p-0 d-none d-md-block">
          <DatePicker datePickerType="single">
            <DatePickerInput
              id="date-picker-input-id"
              placeholder="mm/dd/yyyy"
              size="md"
              labelText=""
            />
          </DatePicker>
        </div>
      </div>
      <DonutChart
        data={data}
        options={options}>
      </DonutChart>
    </div>
  );
};

export default DonutChartView;