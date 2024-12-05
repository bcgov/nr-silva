import React, { useState, useEffect } from "react";
import { GroupedBarChart, ScaleTypes } from "@carbon/charts-react";
import { FilterableMultiSelect, DatePicker, DatePickerInput } from "@carbon/react";
import { fetchOpeningsPerYear } from "../../services/OpeningService";
import { OpeningPerYearChart } from "../../types/OpeningPerYearChart";
import "@carbon/charts/styles.css";
import "./BarChartGrouped.scss";
import {  fetchOrgUnits} from "../../services/search/openings";
import { TextValueData, sortItems } from "../../utils/multiSelectSortUtils"

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

    setStatusItems([
      {value:'AMG', text: 'Amalgamate'},
      {value:'AMD', text: 'Amended'},
      {value:'APP', text: 'Approved'},
      {value:'DFT', text: 'Draft'},
      {value:'FG', text: 'Free Growing'},
      {value:'RMD', text: 'Removed'},
      {value:'RET', text: 'Retired'},
      {value:'SUB', text: 'Submitted'}
    ]);
    fetchOrgUnitsData();

  },[]);

  const formatDateToString = (dateToFormat: Date) => {
    if (!dateToFormat) return null;
    const year = dateToFormat.getFullYear();
    const month = String(dateToFormat.getMonth() + 1).padStart(2, "0");
    const day = String(dateToFormat.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  const colors = {
    Openings: "#1192E8",
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
        color: "#d3d3d3",
        strokeDashArray: "2,2",
      },
      y: {
        enabled: true,
        color: "#d3d3d3",
        strokeDashArray: "2,2",
      },
    },
    toolbar: {
      enabled: false,
      numberOfIcons: 2,
      controls: [
        {
          type: "Make fullscreen",
        },
        {
          type: "Make fullscreen",
        },
      ],
    },
  };

  useEffect(() =>{

    console.log(`For search`, selectedOrgUnits, selectedStatusCodes, startDate, endDate);

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
    <div className="px-3">
      <div className="row gy-2 gx-1 pb-3">
        <div className="col-md-3">
          <FilterableMultiSelect
            label="District"
            id="district-dropdown"
            titleText="District"
            items={orgUnitItems}
            itemToString={(item: TextValueData) => (item ? item.text : "")}
            selectionFeedback="top-after-reopen"
            onChange={(e: MultiSelectEvent) => setSelectedOrgUnits(e.selectedItems)}
            selectedItems={selectedOrgUnits}
            sortItems={sortItems}
            
          />
        </div>
        <div className="col-md-3">
          <FilterableMultiSelect
            label="Status"
            id="status-dropdown"
            titleText="Status"
            items={statusItems}
            itemToString={(item: TextValueData) => (item ? item.text : "")}
            selectionFeedback="top-after-reopen"
            onChange={(e: MultiSelectEvent) => setSelectedStatusCodes(e.selectedItems)}
            selectedItems={selectedStatusCodes}
            sortItems={sortItems}
            
          />
        </div>
        <div className="col-md-2 col-xxl-3 d-none d-md-block">
          <DatePicker
            datePickerType="single"
            onChange={(dates: [Date]) => setStartDate(dates[0])}
          >
            <DatePickerInput
              id="start-date-picker-input-id"
              placeholder="yyyy/MM/dd"
              size="md"
              labelText="Start Date"
            />
          </DatePicker>
        </div>
        <div className="col-md-2 col-xxl-3 d-none d-md-block">
          <DatePicker
            datePickerType="single"
            onChange={(dates: [Date]) => setEndDate(dates[0])}
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
        <div className="bar-chart-container" data-testid="bar-chart">
          <GroupedBarChart data={chartData} options={options} />
        </div>
      )}
    </div>
  );
};

export default BarChartGrouped;
