// React imports
import React, { useState, useEffect, useRef } from "react";

// Third-party library imports
import { GroupedBarChart } from "@carbon/charts-react";
import {
  FilterableMultiSelect,
  DatePicker,
  DatePickerInput,
  Column
} from "@carbon/react";
import { differenceInDays, addDays, startOfMonth, endOfMonth, format } from "date-fns";
import { useNavigate } from "react-router-dom";

// Styles
import "@carbon/charts/styles.css";
import "./styles.scss";

// Utility functions
import { fetchOpeningsOrgUnits, fetchUserSubmissionTrends } from "../../services/OpeningService";
import { status } from "../../services/search/openings";
import { TextValueData, sortItems } from "../../utils/multiSelectSortUtils";


// Local components
import EmptySection from "../EmptySection";
import ChartContainer from "../ChartContainer";
import { ChartOptions } from "./constants";
import { useQuery } from "@tanstack/react-query";
import { formatDateObjToString } from "../../utils/DateUtils";
import { SubmissionTrendChartObj } from "./definitions";

interface MultiSelectEvent {
  selectedItems: TextValueData[];
}

interface BarChartGroupedEvent {
  detail: {
    datum: OpeningPerYearChart;
  }
}

/**
 * Displays a chart that shows opening submission trend.
 */
const OpeningSubmissionTrend = (): JSX.Element => {
  const [chartData, setChartData] = useState<OpeningPerYearChart[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);
  const [dateRange, setDateRange] = useState<Date[]>([]);
  const [orgUnitItems, setOrgUnitItems] = useState<TextValueData[]>([]);
  const [selectedOrgUnits, setSelectedOrgUnits] = useState<TextValueData[]>([]);
  const [selectedStatusCodes, setSelectedStatusCodes] = useState<TextValueData[]>([]);
  const [searchParameters, setSearchParameters] = useState<string>("");
  const chartRef = useRef(null);
  const navigate = useNavigate();


  const orgUnitQuery = useQuery({
    queryKey: ["opening-search", "org-units"],
    queryFn: () => fetchOpeningsOrgUnits(),
    select: (data): TextValueData[] => (
      data.map((orgUnit) => ({ value: orgUnit.orgUnitCode, text: orgUnit.orgUnitName })
      ))
  });

  const submissionTrendQuery = useQuery({
    queryKey: ["users", "submission-trends"],
    queryFn: () => fetchUserSubmissionTrends({
      orgUnitCode: selectedOrgUnits?.map((orgUnit) => orgUnit.value),
      statusCode: selectedStatusCodes?.map((statusCode) => statusCode.value),
      entryDateStart: formatDateObjToString(startDate),
      entryDateEnd: formatDateObjToString(endDate)
    }),
    select: (data): SubmissionTrendChartObj[] => (
      data.map((item) => ({
        ...item,
        group: "Openings",
        key: `${item.monthName} ${item.year}`,
        value: item.amount
      })))
  });

  // useEffect(() => {

  //   const fetchOrgUnitsData = async () => {
  //     try {
  //       const data = await fetchOrgUnits();
  //       setOrgUnitItems(data.map((orgUnit) => ({ value: orgUnit.orgUnitCode, text: orgUnit.orgUnitName })));
  //     } catch (error) {
  //       console.error("Error fetching org units:", error);
  //     }
  //   };
  //   fetchOrgUnitsData();

  // }, []);

  const setDates = (dates: Date[]) => {
    setDateRange(dates);
    // Only apply dates if we have both selected
    if (dates.length === 2) {
      // If the difference between the dates is greater than 365 days, set the end date to 365 days after the start date
      if (differenceInDays(dates[1], dates[0]) > 365) {
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

  useEffect(() => {
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


        if (orgUnits.length > 0) {
          searchValues.push(`Districts: ${orgUnits.join(", ")}`);
        }
        if (statusCodes.length > 0) {
          searchValues.push(`Status: ${statusCodes.join(", ")}`);
        }

        setSearchParameters(searchValues.join(", "));

        // const data: OpeningPerYearChart[] = await fetchUserSubmissionTrends({
        //   orgUnitCode: orgUnits,
        //   statusCode: statusCodes,
        //   entryDateStart: formattedStartDate,
        //   entryDateEnd: formattedEndDate
        // });

        // setChartData(data);
        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching chart data:", error);
        setIsLoading(false);
      }
    };
    fetchChartData();
  }, [selectedOrgUnits, selectedStatusCodes, startDate, endDate]);

  useEffect(() => {
    if (chartRef.current) {
      const { chart }: GroupedBarChart = chartRef.current;
      if (chart) {
        chart.services.events.addEventListener("bar-click", (event: BarChartGroupedEvent) => {
          const { datum } = event.detail;
          const searchDateStart = format(startOfMonth(new Date(datum.year, datum.month - 1)), "yyyy-MM-dd");
          const searchDateEnd = format(endOfMonth(new Date(datum.year, datum.month - 1)), "yyyy-MM-dd");
          navigate(`/silviculture-search?tab=openings&dateType=Update&startDate=${searchDateStart}&endDate=${searchDateEnd}`);
        });
      }
    }
  }, [chartRef, isLoading]);

  return (
    <ChartContainer
      title="Openings submission trends"
      description="Check quantity and evolution of openings"
    >
      <Column sm={4} md={8} lg={16}>
        <div className="submission-trend-input-container">
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

          <FilterableMultiSelect
            label="Status"
            id="status-dropdown"
            titleText="Status"
            items={status}
            itemToString={(item: TextValueData) => (item ? `${item.value} - ${item.text}` : "")}
            selectionFeedback="top-after-reopen"
            onChange={(e: MultiSelectEvent) => setSelectedStatusCodes(e.selectedItems)}
            selectedItems={selectedStatusCodes}
            sortItems={sortItems}
            placeholder="Filter by status"
          />

          <DatePicker
            datePickerType="range"
            dateFormat="Y/m/d"
            allowInput={true}
            onChange={setDates}
            value={dateRange}
          >
            <DatePickerInput
              id="start-date-picker-input-id"
              placeholder="yyyy/MM/dd"
              size="md"
              labelText="Start Date"
            />
            <DatePickerInput
              id="end-date-picker-input-id"
              placeholder="yyyy/MM/dd"
              size="md"
              labelText="End Date"
            />
          </DatePicker>
        </div>
      </Column>

      <Column sm={4} md={8} lg={16} xlg={16}>
        {
          isLoading ? (
            <p>Loading...</p>
          ) : (
            <div className="bar-chart-container" data-testid="bar-chart">
              {
                chartData.length === 0 ? (
                  <EmptySection
                    pictogram={searchParameters ? 'UserSearch' : 'Touch'}
                    title={searchParameters ? 'No results found' : "You don't have any openings to show yet"}
                    description={
                      searchParameters
                        ? `Nothing found when searching for ${searchParameters}, try adjusting your filters to find what you want.`
                        : 'Select a filter to bring up the openings'
                    }
                    fill="#0073E6"
                  />
                )
                  : null
              }
              {
                chartData.length > 0 ? (
                  <GroupedBarChart ref={chartRef} data={chartData} options={ChartOptions} />
                ) : null
              }
            </div>
          )}
      </Column>

    </ChartContainer>
  );
};

export default OpeningSubmissionTrend;
