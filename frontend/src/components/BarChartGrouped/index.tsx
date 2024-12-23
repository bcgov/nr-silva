// React imports
import React, { useState, useEffect, useRef } from "react";
import ReactDOMServer from "react-dom/server";

// Third-party library imports
import { GroupedBarChart, ScaleTypes } from "@carbon/charts-react";
import { 
  FilterableMultiSelect, 
  DatePicker, 
  DatePickerInput, 
  Grid, 
  Column 
} from "@carbon/react";
import { differenceInDays, addDays, startOfMonth, endOfMonth, format } from "date-fns";
import { useNavigate } from "react-router-dom";

// Styles
import "@carbon/charts/styles.css";
import "./BarChartGrouped.scss";

// Utility functions
import { fetchOpeningsPerYear } from "../../services/OpeningService";
import { fetchOrgUnits, status } from "../../services/search/openings";
import { TextValueData, sortItems } from "../../utils/multiSelectSortUtils";

// Types
import { OpeningPerYearChart } from "../../types/OpeningPerYearChart";

// Local components
import BarChartTooltip from "../BarChartTooltip";
import EmptySection from "../EmptySection";

interface MultiSelectEvent {
  selectedItems: TextValueData[];
}

interface BarChartGroupedEvent {
  detail: {
    datum: OpeningPerYearChart;
  }
}

const BarChartGrouped = (): JSX.Element => {
  const [windowWidth, setWindowWidth] = useState<number>(window.innerWidth);
  const [chartData, setChartData] = useState<OpeningPerYearChart[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);
  const [dateRange, setDateRange] = useState<Date[]>([]);
  const [orgUnitItems, setOrgUnitItems] = useState<TextValueData[]>([]);
  const [statusItems, setStatusItems] = useState<TextValueData[]>([]);
  const [selectedOrgUnits, setSelectedOrgUnits] = useState<TextValueData[]>([]);
  const [selectedStatusCodes, setSelectedStatusCodes] = useState<TextValueData[]>([]);
  const [searchParameters, setSearchParameters] = useState<string>("");
  const chartRef = useRef(null);
  const navigate = useNavigate();


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
    const tooltipContent = <BarChartTooltip datum={datum} />;
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
    setDateRange(dates);
    // Only apply dates if we have both selected
    if(dates.length === 2) {
      // If the difference between the dates is greater than 365 days, set the end date to 365 days after the start date
      if(differenceInDays(dates[1], dates[0]) > 365){
        dates[1] = addDays(dates[0], 365);
      }
      // Set the start and end date
      setStartDate(dates[0]);
      setEndDate(dates[1]);
    } else {
      setStartDate(null);
      setEndDate(null);
    }
  }

  const maxDate = () => formatDateToString(new Date());

  useEffect(() =>{

    const fetchChartData = async () => {
      try {

        const searchValues: string[] = [];

        setIsLoading(true);
        let formattedStartDate: string | null = null;
        let formattedEndDate: string | null = null;

        if (startDate) {
          formattedStartDate = formatDateToString(startDate);
          searchValues.push(`Start Date: ${formattedStartDate}`);
        }
        if (endDate) {
          formattedEndDate = formatDateToString(endDate);
          searchValues.push(`End Date: ${formattedEndDate}`);
        }

        const orgUnits = selectedOrgUnits?.map((orgUnit) => orgUnit.value);
        const statusCodes = selectedStatusCodes?.map((statusCode) => statusCode.value);


        if(orgUnits.length > 0) {
          searchValues.push(`Districts: ${orgUnits.join(", ")}`);
        }
        if(statusCodes.length > 0) {
          searchValues.push(`Status: ${statusCodes.join(", ")}`);
        }

        setSearchParameters(searchValues.join(", "));

        const data: OpeningPerYearChart[] = await fetchOpeningsPerYear({
          orgUnitCode: orgUnits,
          statusCode: statusCodes,
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

  },[selectedOrgUnits, selectedStatusCodes, startDate, endDate]);

  useEffect(() => {
    if(chartRef.current){
      const { chart }:GroupedBarChart = chartRef.current;
      if(chart){
      chart.services.events.addEventListener("bar-click", (event: BarChartGroupedEvent) => {
        const { datum } = event.detail;
        const searchDateStart = format(startOfMonth(new Date(datum.year, datum.month - 1)), "yyyy-MM-dd");
        const searchDateEnd = format(endOfMonth(new Date(datum.year, datum.month - 1)), "yyyy-MM-dd");
        navigate(`/silviculture-search?tab=openings&dateType=Update&startDate=${searchDateStart}&endDate=${searchDateEnd}`);
      });
      }
    }
  },[chartRef,isLoading]);

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
          maxDate={maxDate()}
          onChange={setDates}
          value={dateRange}
        >
          <DatePickerInput
            autoComplete="off"
            id="start-date-picker-input-id"
            placeholder="yyyy/MM/dd"
            size="md"
            labelText="Start Date"
          />
          <DatePickerInput
            autoComplete="off"
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
            {chartData.length === 0 && !searchParameters && <EmptySection
            pictogram="Touch"
            title="You don't have any openings to show yet"
            description="Select a filter to bring up the openings"
            fill="#0073E6"
          />}
          {chartData.length === 0 && searchParameters && <EmptySection
            pictogram="UserSearch"
            title="No results found"
            description={`Nothing found when searching for ${searchParameters}, try adjusting your filters to find what you want.`}
            fill="#0073E6"
          />}
            {chartData.length > 0 && <GroupedBarChart ref={chartRef} data={chartData} options={options} />}
          </div>
        )}
      </Column>
    </Grid>
  );
};

export default BarChartGrouped;
