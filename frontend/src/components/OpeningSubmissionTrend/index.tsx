// React imports
import React, { useState, useEffect, useRef } from "react";

// Third-party library imports
import { useQuery } from "@tanstack/react-query";
import { GroupedBarChart } from "@carbon/charts-react";
import {
  FilterableMultiSelect,
  Column,
  ComboBox,
  DropdownSkeleton,
  Loading
} from "@carbon/react";
import { startOfMonth, endOfMonth, format } from "date-fns";
import { useNavigate } from "react-router-dom";

// Utility functions
import { ComboBoxEvent } from "../../types/CarbonTypes";
import { fetchOpeningsOrgUnits, fetchUserSubmissionTrends } from "../../services/OpeningService";
import { status } from "../../services/search/openings";
import { extractValsFromTextValueArr, TextValueData, textValueToDisplayText } from "../../utils/multiSelectSortUtils";

// Local components
import EmptySection from "../EmptySection";
import ChartContainer from "../ChartContainer";
import { ChartOptions } from "./constants";
import { SubmissionTrendChartObj } from "./definitions";
import { generateYearList, getYearBoundaryDate } from "./utils";

// Styles
import "@carbon/charts/styles.css";
import "./styles.scss";

interface MultiSelectEvent {
  selectedItems: TextValueData[];
}

interface BarChartGroupedEvent {
  detail: {
    datum: SubmissionTrendChartObj;
  }
}

/**
 * Displays a chart that shows opening submission trend.
 */
const OpeningSubmissionTrend = (): JSX.Element => {
  const yearOptions = generateYearList();
  const [selectedYear, setSelectedYear] = useState<number>(yearOptions[0]);
  // const [startDate, setStartDate] = useState<Date | null>(null);
  // const [endDate, setEndDate] = useState<Date | null>(null);

  const [selectedOrgUnits, setSelectedOrgUnits] = useState<TextValueData[]>([]);
  const [selectedStatusCodes, setSelectedStatusCodes] = useState<TextValueData[]>([]);
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
    queryKey: [
      "users",
      "submission-trends",
      {
        entryStartDate: getYearBoundaryDate(selectedYear, true),
        entryEndDate: getYearBoundaryDate(selectedYear, false),
        statusCode: extractValsFromTextValueArr(selectedStatusCodes),
        orgUnitCode: extractValsFromTextValueArr(selectedOrgUnits)
      }
    ],
    queryFn: async () => {
      const data = await fetchUserSubmissionTrends({
        orgUnitCode: extractValsFromTextValueArr(selectedOrgUnits),
        statusCode: extractValsFromTextValueArr(selectedStatusCodes),
        entryDateStart: getYearBoundaryDate(selectedYear, true),
        entryDateEnd: getYearBoundaryDate(selectedYear, false)
      });

      return data.length ? data : undefined;
    },
    select: (data): SubmissionTrendChartObj[] | undefined => (
      data ?
        data.map((item) => ({
          ...item,
          group: "Openings",
          key: `${item.monthName} ${item.year}`,
          value: item.amount
        }))
        : undefined),
    refetchOnMount: true
  });

  console.log(submissionTrendQuery.data);


  const handleYearSelection = (e: ComboBoxEvent) => {
    setSelectedYear(e.selectedItem)
  }

  const handleOrgUnitChange = (e: MultiSelectEvent) => {
    setSelectedOrgUnits(e.selectedItems)
  };

  const handleStatusChange = (e: MultiSelectEvent) => {
    setSelectedStatusCodes(e.selectedItems)
  };

  useEffect(() => {
    submissionTrendQuery.refetch();
  }, [selectedOrgUnits, selectedStatusCodes, selectedYear]);



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
  }, [chartRef, submissionTrendQuery.status]);

  return (
    <ChartContainer
      className="submission-trend-container"
      title="Opening submission per year"
      description="Check quantity and evolution of openings"
    >
      <Column sm={4} md={8} lg={16}>
        <div className="submission-trend-input-container">
          {
            orgUnitQuery.isFetching ? (
              <>
                <DropdownSkeleton />
                <DropdownSkeleton />
                <DropdownSkeleton />
              </>
            ) : (
              <>
                <FilterableMultiSelect
                  id="district-dropdown"
                  titleText="District"
                  items={orgUnitQuery.data ?? []}
                  itemToString={textValueToDisplayText}
                  selectionFeedback="top-after-reopen"
                  onChange={handleOrgUnitChange}
                  disabled={submissionTrendQuery.isFetching}
                />

                <FilterableMultiSelect
                  id="status-dropdown"
                  titleText="Status"
                  items={status}
                  itemToString={textValueToDisplayText}
                  selectionFeedback="top-after-reopen"
                  onChange={handleStatusChange}
                  disabled={submissionTrendQuery.isFetching}
                />

                <ComboBox
                  id="trend-year-selection"
                  onChange={handleYearSelection}
                  items={yearOptions}
                  titleText="Opening submission year"
                  disabled={submissionTrendQuery.isFetching}
                  initialSelectedItem={selectedYear}
                />
              </>
            )
          }
        </div>
      </Column>

      <Column className="trend-loading-col" sm={4} md={8} lg={16} xlg={16}>
        {
          submissionTrendQuery.isFetching
            ? (
              <Loading className="trend-loading-spinner" withOverlay={false} />
            )
            : null
        }
        {
          !submissionTrendQuery.isFetching && !submissionTrendQuery.data ? (
            <EmptySection
              pictogram="UserSearch"
              title="No results found"
              description={`No results found with the current filters. Try adjusting them to refine your search.`}
              fill="#0073E6"
            />
          )
            : null
        }
        {
          !submissionTrendQuery.isFetching && submissionTrendQuery.data?.length ? (
            <GroupedBarChart ref={chartRef} data={submissionTrendQuery.data} options={ChartOptions} />
          ) : null
        }
      </Column>

    </ChartContainer>
  );
};

export default OpeningSubmissionTrend;
