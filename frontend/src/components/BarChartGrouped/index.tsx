import React, { useState, useEffect } from "react";
import { GroupedBarChart, ScaleTypes } from "@carbon/charts-react";
import { Dropdown, DatePicker, DatePickerInput } from "@carbon/react";
import "@carbon/charts/styles.css";
import "./BarChartGrouped.scss";
import { fetchOpeningsPerYear } from "../../services/OpeningService";

interface IDropdownItem {
  value: string,
  text: string
}

const BarChartGrouped = () => {
  const [windowWidth, setWindowWidth] = useState<number>(window.innerWidth);
  const [chartData, setChartData] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [orgUnitCode, setOrgUnitCode] = useState<string>('');
  const [statusCode, setStatusCode] = useState<string>('');
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);

  const handleResize = () => {
    setWindowWidth(window.innerWidth);
  };

  useEffect(() => {
    const fetchChartData = async () => {
      try {
        setIsLoading(true);
        let formattedStartDate: string | null = null;
        let formattedEndDate: string | null = null;
    
        if (startDate) {
          formattedStartDate = formatDateToString(startDate);
        }
        if (endDate) {
          formattedEndDate = formatDateToString(endDate);
        }
    
        const data = await fetchOpeningsPerYear({
          orgUnitCode,
          statusCode,
          entryDateStart: formattedStartDate,
          entryDateEnd: formattedEndDate
        });
        setChartData(data);
        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching chart data:", error);
        setIsLoading(false);
      }
    };

    fetchChartData();
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, [orgUnitCode, statusCode, startDate, endDate]);
  
  const formatDateToString = (dateToFormat: Date | null) => {
    if (!dateToFormat) return null;
    const year = dateToFormat.getFullYear();
    const month = String(dateToFormat.getMonth() + 1).padStart(2, "0");
    const day = String(dateToFormat.getDate()).padStart(2, "0");
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
        scaleType: ScaleTypes.LABELS,
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

  const setOrgUnitCodeSelected = ({selectedItem}:{selectedItem: IDropdownItem}) => {
    setOrgUnitCode(selectedItem.value);
  };

  const setStatusCodeSelected = ({selectedItem}:{selectedItem: IDropdownItem}) => {
    setStatusCode(selectedItem.value);
  };

  return (
    <div className="px-3">
      <div className="row gy-2 pb-3">
        <div className="col-md-4 p-0">
          <Dropdown
            id="district-dropdown"
            label={windowWidth <= 1584 ? "District" : "Filter by district"}
            titleText={windowWidth <= 1584 ? "District" : "Filter by district"}
            items={orgUnitItems}
            itemToString={(item: IDropdownItem) => item ? item.text : ''}
            onChange={setOrgUnitCodeSelected}
          />
        </div>
        <div className="col-md-4 p-0 px-md-1">
          <Dropdown
            id="status-dropdown"
            label={windowWidth <= 1584 ? "Status" : "Filter by status"}
            titleText={windowWidth <= 1584 ? "Status" : "Filter by status"}
            items={statusItems}
            itemToString={(item: IDropdownItem) => item ? item.text : ''}
            onChange={setStatusCodeSelected}
          />
        </div>
        <div className="col-2 px-md-1 d-none d-md-block">
          <DatePicker
            datePickerType="single"
            onChange={(date: Date) => setStartDate(date)}
          >
            <DatePickerInput
              id="start-date-picker-input-id"
              placeholder="yyyy-MM-dd"
              size="md"
              labelText="Start Date"
            />
          </DatePicker>
        </div>
        <div className="col-2 px-md-1 d-none d-md-block">
          <DatePicker
            datePickerType="single"
            onChange={(date: Date) => setEndDate(date)}
          >
            <DatePickerInput
              id="end-date-picker-input-id"
              placeholder="yyyy-MM-dd"
              size="md"
              labelText="End Date"
            />
          </DatePicker>
        </div>
      </div>
      {isLoading ? (
        <p>Loading...</p>
      ) : (
        <GroupedBarChart
          data={chartData}
          options={options}
        />
      )}
    </div>
  );
};

export default BarChartGrouped;
