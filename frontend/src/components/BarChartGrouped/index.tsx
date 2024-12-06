import React, { useState, useEffect } from "react";
import ReactDOMServer from "react-dom/server";
import { GroupedBarChart, ScaleTypes } from "@carbon/charts-react";
import { 
  FilterableMultiSelect, 
  DatePicker, 
  DatePickerInput, 
  Grid, 
  Column
} from '@carbon/react';
import { fetchOpeningsPerYear } from "../../services/OpeningService";
import { OpeningPerYearChart } from "../../types/OpeningPerYearChart";
import "@carbon/charts/styles.css";
import "./BarChartGrouped.scss";
import { fetchOrgUnits, status } from "../../services/search/openings";
import { TextValueData, sortItems } from "../../utils/multiSelectSortUtils"
import { differenceInDays, addDays } from "date-fns";
import BarChartTooltip from "../BarChartTooltip";

interface MultiSelectEvent {
  selectedItems: TextValueData[];
}

const BarChartGrouped = (): JSX.Element => {
  const [windowWidth, setWindowWidth] = useState<number>(window.innerWidth);
  const [chartData, setChartData] = useState<OpeningPerYearChart[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);

  const [orgUnitItems, setOrgUnitItems] = useState<TextValueData[]>([]);
  const [statusItems, setStatusItems] = useState<TextValueData[]>([]);
  const [selectedOrgUnits, setSelectedOrgUnits] = useState<TextValueData[]>([]);
  const [selectedStatusCodes, setSelectedStatusCodes] = useState<TextValueData[]>([]);


  const handleResize = () => {
    setWindowWidth(window.innerWidth);
  };

  useEffect(() => {

    const fetchOrgUnitsData = async () => {
      try {
        const data = await fetchOrgUnits();
        setOrgUnitItems(data.map((orgUnit) => ({ value: orgUnit.orgUnitCode, text: orgUnit.orgUnitName })));
      } catch (error) {
        console.error("Error fetching org units:", error);
      }
    };
    setStatusItems(status);
    fetchOrgUnitsData();

  },[]);

  const formatDateToString = (dateToFormat: Date) => {
    if (!dateToFormat) return null;
    const year = dateToFormat.getFullYear();
    const month = String(dateToFormat.getMonth() + 1).padStart(2, "0");
    const day = String(dateToFormat.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  const tooltip = (data:OpeningPerYearChart[], defaultHTML: string, datum: OpeningPerYearChart) => {
    const tooltipContent = <BarChartTooltip data={data} defaultHTML={defaultHTML} datum={datum} />;
    return ReactDOMServer.renderToString(tooltipContent);
  }

  const colors = { Openings: "#1192E8" };

  const options = {
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
    tooltip:{
      enabled: true,
      customHTML: tooltip
    }
  };


  const setDates = (dates: Date[]) => {
    // Only apply dates if we have both selected
    if(dates.length === 2) {
      // If the difference between the dates is greater than 365 days, set the end date to 365 days after the start date
      if(differenceInDays(dates[1], dates[0]) > 365){
        dates[1] = addDays(dates[0], 365);
      }
      // Set the start and end date
      setStartDate(dates[0]);
      setEndDate(dates[1]);
    }
  }

  useEffect(() =>{

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

        const orgUnits = selectedOrgUnits?.map((orgUnit) => orgUnit.value);
        const statusCodes = selectedStatusCodes?.map((statusCode) => statusCode.value);
    
        const data: OpeningPerYearChart[] = await fetchOpeningsPerYear({
          orgUnitCode: orgUnits,
          statusCode: statusCodes,
          entryDateStart: formattedStartDate,
          entryDateEnd: formattedEndDate,
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

  },[selectedOrgUnits, selectedStatusCodes, startDate, endDate])

  return (
    <Grid condensed>
      <Column sm={4} md={3} lg={5}>
        <FilterableMultiSelect
          label="District"
          id="district-dropdown"
          titleText="District"
          items={orgUnitItems}
          itemToString={(item: TextValueData) => (item ? `${item.value} - ${item.text}` : "")}
          selectionFeedback="top-after-reopen"
          onChange={(e: MultiSelectEvent) => setSelectedOrgUnits(e.selectedItems)}
          selectedItems={selectedOrgUnits}
          sortItems={sortItems}
          placeholder="Filter by district"
        />
      </Column>
      <Column sm={4} md={3} lg={5}>
        <FilterableMultiSelect
          label="Status"
          id="status-dropdown"
          titleText="Status"
          items={statusItems}
          itemToString={(item: TextValueData) => (item ? `${item.value} - ${item.text}` : "")}
          selectionFeedback="top-after-reopen"
          onChange={(e: MultiSelectEvent) => setSelectedStatusCodes(e.selectedItems)}
          selectedItems={selectedStatusCodes}
          sortItems={sortItems}
          placeholder="Filter by status"
        />
      </Column>
      <Column sm={4} md={2} lg={6}>
        <DatePicker
          datePickerType="range"
            dateFormat="Y/m/d"
            allowInput={true}
            maxDate={new Date()}
            onChange={setDates}
        >
          <DatePickerInput
            autocomplete="off"
            id="start-date-picker-input-id"
            placeholder="yyyy/MM/dd"
            size="md"
            labelText="Start Date"
          />
          <DatePickerInput
            autocomplete="off"
            id="end-date-picker-input-id"
            placeholder="yyyy/MM/dd"
            size="md"
            labelText="End Date"
          />
        </DatePicker>
      </Column>
      <Column sm={4} md={8} lg={16} xlg={16}>
        {isLoading ? (
          <p>Loading...</p>
        ) : (
          <div className="bar-chart-container" data-testid="bar-chart">
            <GroupedBarChart data={chartData} options={options} />
          </div>
        )}
      </Column>
    </Grid>
  );
};

export default BarChartGrouped;
