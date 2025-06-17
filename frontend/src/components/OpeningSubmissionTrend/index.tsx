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
  Loading,
} from "@carbon/react";
import { startOfMonth, endOfMonth, format } from "date-fns";
import { useNavigate } from "react-router-dom";
import API from "@/services/API";

// Utility functions
import { getMonthAbbreviation } from "@/utils/DateUtils";
import { ComboBoxEvent } from "@/types/CarbonTypes";
import {
  extractCodesFromCodeDescriptionArr,
  codeDescriptionToDisplayText,
  MultiSelectEvent
} from "@/utils/multiSelectUtils";
import { buildQueryString } from "@/utils/UrlUtils";
import { Chart } from "@carbon/charts";
import { CodeDescriptionDto } from "@/services/OpenApi";
import { OPENING_STATUS_LIST } from "@/constants";

// Local components
import { SilvicultureSearchParams } from '../SilvicultureSearch/definitions';
import EmptySection from "../EmptySection";
import ChartContainer from "../ChartContainer";
import { DefaultChartOptions } from "./constants";
import { SubmissionTrendChartObj } from "./definitions";
import { generateYearList, getYearBoundaryDate } from "./utils";

// Styles
import "@carbon/charts/styles.css";
import "./styles.scss";

interface BarChartGroupedEvent {
  detail: {
    datum: SubmissionTrendChartObj;
  };
}

/**
 * Displays a chart that shows opening submission trend.
 */
const OpeningSubmissionTrend = () => {
  const yearOptions = generateYearList();
  const [selectedYear, setSelectedYear] = useState<number>(yearOptions[0]!);

  const [selectedOrgUnits, setSelectedOrgUnits] = useState<CodeDescriptionDto[]>([]);
  const [selectedStatusCodes, setSelectedStatusCodes] = useState<CodeDescriptionDto[]>([]);
  const chartInstanceRef = useRef<Chart | null>(null);
  const navigate = useNavigate();

  const orgUnitQuery = useQuery({
    queryKey: ["codes", "org-units"],
    queryFn: API.CodesEndpointService.getOpeningOrgUnits,
  });

  const submissionTrendQuery = useQuery({
    queryKey: [
      "users",
      "submission-trends",
      {
        entryStartDate: getYearBoundaryDate(selectedYear, true),
        entryEndDate: getYearBoundaryDate(selectedYear, false),
        statusCode: extractCodesFromCodeDescriptionArr(selectedStatusCodes),
        orgUnitCode: extractCodesFromCodeDescriptionArr(selectedOrgUnits),
      }
    ],
    queryFn: async () => {
      const result = await API.UserActionsEndpointService.getOpeningsSubmissionTrends(
        extractCodesFromCodeDescriptionArr(selectedOrgUnits),
        extractCodesFromCodeDescriptionArr(selectedStatusCodes),
        getYearBoundaryDate(selectedYear, true) ?? undefined,
        getYearBoundaryDate(selectedYear, false) ?? undefined
      );

      // Handles 204 no content
      return result.length ? result : null;
    },
    select: (data): SubmissionTrendChartObj[] | undefined =>
      data
        ? data.map((item) => ({
          ...item,
          group: "Openings",
          key: `${getMonthAbbreviation(item.month)} ${item.year}`,
          value: item.amount,
        }))
        : undefined,
    refetchOnMount: true,
  });

  const handleYearSelection = (e: ComboBoxEvent<number>) => {
    if (e.selectedItem) {
      setSelectedYear(e.selectedItem)
    } else {
      setSelectedYear(yearOptions[0]!)
    }
  }

  const handleOrgUnitChange = (e: MultiSelectEvent) => {
    setSelectedOrgUnits(e.selectedItems);
  };

  const handleStatusChange = (e: MultiSelectEvent) => {
    setSelectedStatusCodes(e.selectedItems);
  };

  useEffect(() => {
    submissionTrendQuery.refetch();
  }, [selectedOrgUnits, selectedStatusCodes, selectedYear]);

  /**
   * Handles the reference to the GroupedBarChart component and dynamically attaches an event listener for bar clicks.
   *
   * ### Why use this instead of `useEffect`?
   * - `useEffect` does not reliably capture the `chart` instance because `GroupedBarChart` initializes asynchronously.
   * - Directly accessing the chart from the ref (`chartRef.current`) inside `useEffect` may return `null` or an outdated instance.
   * - This function ensures the chart instance is properly assigned and event listeners are correctly managed when the component mounts.
   *
   * ### What does this do?
   * - Stores the chart instance in `chartInstanceRef` for future reference.
   * - Attaches a "bar-click" event listener to navigate based on the clicked bar's data.
   * - Ensures old event listeners are removed before adding a new one to prevent duplicates.
   *
   * @param {GroupedBarChart | null} chartComponent - The GroupedBarChart component instance.
   */
  /* v8 ignore next 38 */
  const handleChartRef = (chartComponent: GroupedBarChart | null) => {
    if (!chartComponent) return;

    const { chart } = chartComponent;
    if (!chart) return;

    chartInstanceRef.current = chart;

    const handleBarClick = (event: BarChartGroupedEvent) => {

      const { datum } = event.detail;

      const dateStart = format(
        startOfMonth(new Date(datum.year, datum.month - 1)),
        "yyyy-MM-dd"
      );
      const dateEnd = format(
        endOfMonth(new Date(datum.year, datum.month - 1)),
        "yyyy-MM-dd"
      );

      const queryParams: SilvicultureSearchParams = {
        tab: "openings",
        dateType: "update",
        dateStart,
        dateEnd,
        orgUnit: extractCodesFromCodeDescriptionArr(selectedOrgUnits),
        status: extractCodesFromCodeDescriptionArr(selectedStatusCodes),
      };

      navigate(`/silviculture-search?${buildQueryString(queryParams)}`);
    };
    // Ensure existing listeners are removed before adding a new one
    chart.services.events.removeEventListener("bar-click", handleBarClick);
    chart.services.events.addEventListener("bar-click", handleBarClick);
    return () => {
      chart.services.events.removeEventListener("bar-click", handleBarClick);
    };
  };

  return (
    <ChartContainer
      className="submission-trend-container"
      title="Opening submission per year"
      description="Check quantity and evolution of openings"
    >
      <Column sm={4} md={8} lg={16}>
        <div className="submission-trend-input-container">
          {
            orgUnitQuery.isLoading ? (
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
                  itemToString={codeDescriptionToDisplayText}
                  selectionFeedback="top-after-reopen"
                  onChange={handleOrgUnitChange}
                  disabled={submissionTrendQuery.isFetching}
                />

                <FilterableMultiSelect
                  id="status-dropdown"
                  titleText="Status"
                  items={OPENING_STATUS_LIST}
                  itemToString={codeDescriptionToDisplayText}
                  selectionFeedback="top-after-reopen"
                  onChange={handleStatusChange}
                  disabled={submissionTrendQuery.isFetching}
                />

                <ComboBox
                  className="trend-year-selection-combobox"
                  id="trend-year-selection"
                  onChange={handleYearSelection}
                  items={yearOptions}
                  titleText="Opening submission year"
                  disabled={submissionTrendQuery.isFetching}
                  selectedItem={selectedYear}
                />
              </>
            )}
        </div>
      </Column>

      <Column className="trend-loading-col" sm={4} md={8} lg={16} xlg={16}>
        {submissionTrendQuery.isFetching ? (
          <Loading className="trend-loading-spinner" withOverlay={false} />
        ) : null}
        {!submissionTrendQuery.isFetching && !submissionTrendQuery.data ? (
          <EmptySection
            pictogram="UserSearch"
            title="No results found"
            description="No results found with the current filters. Try adjusting them to refine your search."
          />
        ) : null}
        {!submissionTrendQuery.isFetching && submissionTrendQuery.data ? (
          <GroupedBarChart
            ref={handleChartRef}
            data={submissionTrendQuery.data}
            options={DefaultChartOptions}
          />
        ) : null}
      </Column>
    </ChartContainer>
  );
};

export default OpeningSubmissionTrend;
