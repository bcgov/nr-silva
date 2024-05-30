import React, { useState, useEffect } from "react";
import { GroupedBarChart } from "@carbon/charts-react";
import { Dropdown, DatePicker, DatePickerInput } from "@carbon/react";
import "@carbon/charts/styles.css";
import "./BarChartGrouped.scss";
import { fetchOpeningsPerYear } from "../../services/OpeningService";

const BarChartGrouped = () => {
  const [windowWidth, setWindowWidth] = useState(window.innerWidth);
  const [chartData, setChartData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [orgUnitCode, setOrgUnitCode] = useState(null);
  const [statusCode, setStatusCode] = useState(null);
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);

  const handleResize = () => {
    setWindowWidth(window.innerWidth);
  };

  useEffect(() => {
    fetchChartData();
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, [orgUnitCode, statusCode, startDate, endDate]);
  
  const fetchChartData = async () => {
    try {
      setIsLoading(true);
      let formattedStartDate = startDate;
      let formattedEndDate = endDate;
  
      if (startDate) {
        formattedStartDate = formatDateToString(startDate);
      }
      if (endDate) {
        formattedEndDate = formatDateToString(endDate);
      }
  
      const data = await fetchOpeningsPerYear(orgUnitCode, statusCode, formattedStartDate, formattedEndDate);
      setChartData(data);
      setIsLoading(false);
    } catch (error) {
      console.error("Error fetching chart data:", error);
      setIsLoading(false);
    }
  };
  

  const formatDateToString = (dates) => {
    const date = dates[0]
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  const colors = {
    "Openings": "#1192E8",
  };

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
    height: "18.5rem",
    grid: {
      x: {
        enabled: false,
        color: '#d3d3d3',
        strokeDashArray: '2,2'
      },
      y: {
        enabled: true,
        color: '#d3d3d3',
        strokeDashArray: '2,2'
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
        },
      ]
    }
  };

  const orgUnitItems = [
    { value: 'DCR', text: 'DCR' },
    { value: 'XYZ', text: 'District 2' },
    // Add more options as needed
  ];

  const statusItems = [
    { value: 'APP', text: 'Approved' },
    { value: 'NAN', text: 'Not Approved' },
    // Add more options as needed
  ];

  return (
    <div >
      <div className="row gy-2 gx-1 p-3 pt-0">
        <div className="col-md-3">
          <Dropdown
            id="district-dropdown"
            titleText="District"
            items={orgUnitItems}
            itemToString={item => item ? item.text : ''}
            onChange={({ selectedItem }) => setOrgUnitCode(selectedItem.value)}
          />
        </div>
        <div className="col-md-3">
          <Dropdown
            id="status-dropdown"
            titleText = "Status"
            items={statusItems}
            itemToString={item => item ? item.text : ''}
            onChange={({ selectedItem }) => setStatusCode(selectedItem.value)}
          />
        </div>
        <div className="col-md-3 d-none d-md-block">
          <DatePicker
            datePickerType="single"
            onChange={date => setStartDate(date)}
          >
            <DatePickerInput
              id="start-date-picker-input-id"
              placeholder="yyyy/MM/dd"
              size="md"
              labelText="Start Date"
            />
          </DatePicker>
        </div>
        <div className="col-md-3 d-none d-md-block ">
          <DatePicker
            datePickerType="single"
            onChange={date => setEndDate(date)}
            className="h-66"
          >
            <DatePickerInput
              id="end-date-picker-input-id"
              placeholder="yyyy/MM/dd"
              size="md"
              labelText="End Date"
            />
          </DatePicker>
        </div>
      </div>
      {isLoading ? (
        <p>Loading...</p>
      ) : (
        <div className="p-3">
          <GroupedBarChart
          data={chartData}
          options={options}
        />
        </div>
      )}
    </div>
  );
};

export default BarChartGrouped;
