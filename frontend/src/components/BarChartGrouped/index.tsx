// components/BarChartGrouped.tsx
import React, { useState, useEffect } from "react";
import { GroupedBarChart, ScaleTypes } from "@carbon/charts-react";
import { Dropdown, DatePicker, DatePickerInput } from "@carbon/react";
import { useDistrictListQuery, useFetchOpeningsPerYear } from "../../services/queries/dashboard/dashboardQueries";
import { IOpeningPerYear } from "../../types/IOpeningPerYear";

import "@carbon/charts/styles.css";
import "./BarChartGrouped.scss";

function BarChartGrouped(): JSX.Element {
  const [orgUnitCode, setOrgUnitCode] = useState<string | null>(null);
  const [statusCode, setStatusCode] = useState<string | null>(null);
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);
  
  const formatDateToString = (date: Date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  const formattedStartDate = startDate ? formatDateToString(startDate) : null;
  const formattedEndDate = endDate ? formatDateToString(endDate) : null;

  const queryProps: IOpeningPerYear = {
    orgUnitCode,
    statusCode,
    entryDateStart: formattedStartDate,
    entryDateEnd: formattedEndDate,
  };

  // Fetch the openings submission trends data
  const { data: chartData = [], isLoading } = useFetchOpeningsPerYear(queryProps);
  // Fetch the org units (district list) data
  const { data: orgunitsData = [], isLoading: isOrgUnitsLoading } = useDistrictListQuery();

   // Map the orgunitsData to create orgUnitItems for the Dropdown
   const orgUnitItems = orgunitsData?.map((item: any) => ({
    text: item.orgUnitCode,
    value: item.orgUnitCode,
  })) || [];

  const statusItems = [
    { value: "APP", text: "Approved" },
    { value: "NAN", text: "Not Approved" },
  ];

  const setOrgUnitCodeSelected = ({ selectedItem }: { selectedItem: { value: string } }) => {
    setOrgUnitCode(selectedItem.value);
  };

  const setStatusCodeSelected = ({ selectedItem }: { selectedItem: { value: string } }) => {
    setStatusCode(selectedItem.value);
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
    color: { scale: { Openings: "#1192E8" } },
    height: "18.5rem",
    grid: {
      x: { enabled: false, color: "#d3d3d3", strokeDashArray: "2,2" },
      y: { enabled: true, color: "#d3d3d3", strokeDashArray: "2,2" },
    },
    toolbar: { enabled: false },
  };

  return (
    <div className="px-3">
      <div className="row gy-2 gx-1 pb-3">
        <div className="col-md-3">
          <Dropdown
            id="district-dropdown"
            titleText="District"
            items={orgUnitItems}
            itemToString={(item:any) => (item ? item.text : "")}
            onChange={setOrgUnitCodeSelected}
            label="District"
          />
        </div>
        <div className="col-md-3">
          <Dropdown
            id="status-dropdown"
            titleText="Status"
            items={statusItems}
            itemToString={(item:any) => (item ? item.text : "")}
            onChange={setStatusCodeSelected}
            label="Status"
          />
        </div>
        <div className="col-md-2 col-xxl-3 d-none d-md-block">
          <DatePicker datePickerType="single" onChange={(dates:any) => setStartDate(dates[0])}>
            <DatePickerInput id="start-date-picker-input-id" placeholder="yyyy/MM/dd" size="md" labelText="Start Date" />
          </DatePicker>
        </div>
        <div className="col-md-2 col-xxl-3 d-none d-md-block">
          <DatePicker datePickerType="single" onChange={(dates:any) => setEndDate(dates[0])}>
            <DatePickerInput id="end-date-picker-input-id" placeholder="yyyy/MM/dd" size="md" labelText="End Date" />
          </DatePicker>
        </div>
      </div>
      {isLoading ? (
        <p>Loading...</p>
      ) : (
        <div className="bar-chart-container" data-testid="bar-chart">
          <GroupedBarChart data={chartData} options={options} />
        </div>
      )}
    </div>
  );
}

export default BarChartGrouped;
