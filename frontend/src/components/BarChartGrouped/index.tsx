import React, { useState, useEffect } from "react";
import { GroupedBarChart, ScaleTypes } from "@carbon/charts-react";
import { Dropdown, DatePicker, DatePickerInput } from "@carbon/react";
import { fetchOpeningsPerYear } from "../../services/OpeningService";
import { OpeningPerYearChart } from "../../types/OpeningPerYearChart";
import "@carbon/charts/styles.css";
import "./BarChartGrouped.scss";
import { FlexGrid, Row, Column, } from "@carbon/react";
import { ComboBox } from "@carbon/react";

interface IDropdownItem {
  value: string;
  text: string;
}

/**
 * Renders an Bar Chart Grouped component.
 *
 * @returns {JSX.Element} The rendered BarChartGrouped component.
 */
function BarChartGrouped(): JSX.Element {
  const [windowWidth, setWindowWidth] = useState<number>(window.innerWidth);
  const [chartData, setChartData] = useState<OpeningPerYearChart[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [orgUnitCode, setOrgUnitCode] = useState<string | null>(null);
  const [statusCode, setStatusCode] = useState<string | null>(null);
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

        const data: OpeningPerYearChart[] = await fetchOpeningsPerYear({
          orgUnitCode,
          statusCode,
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
  }, [orgUnitCode, statusCode, startDate, endDate]);

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

  const orgUnitItems = [
    { value: "DCR", text: "DCR" },
    { value: "XYZ", text: "District 2" },
    // Add more options as needed
  ];

  const statusItems = [
    { value: "APP", text: "Approved" },
    { value: "NAN", text: "Not Approved" },
    // Add more options as needed
  ];

  const setOrgUnitCodeSelected = ({
    selectedItem,
  }: {
    selectedItem: IDropdownItem | null;
  }) => {
    setOrgUnitCode(selectedItem ? selectedItem.value : null);
  };

  const setStatusCodeSelected = ({
    selectedItem,
  }: {
    selectedItem: IDropdownItem | null;
  }) => {
    setStatusCode(selectedItem ? selectedItem.value : null);
  };

  return (
    <FlexGrid className="openingSubmissionTrends" condensed>
      <Row>
        <Column lg={4}>
          <ComboBox
            id="district-dropdown"
            titleText="District"
            items={orgUnitItems}
            itemToString={(item: IDropdownItem) => (item ? item.text : "")}
            onChange={setOrgUnitCodeSelected}
            label="District"
          /></Column>
        <Column lg={4}>
          <ComboBox
            id="status-dropdown"
            titleText="Status"
            items={statusItems}
            itemToString={(item: IDropdownItem) => (item ? item.text : "")}
            onChange={setStatusCodeSelected}
            label="Status"
          />
        </Column>
        <Column lg={8}>
          <DatePicker
            datePickerType="range"
            onChange={(dates: [Date]) => setStartDate(dates[0])}
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
        </Column>
      </Row>
      <Row>
        <Column sm={4}>
          {isLoading ? (
            <p>Loading...</p>
          ) : (
            <div className="bar-chart-container" data-testid="bar-chart">
              <GroupedBarChart data={chartData} options={options} />
            </div>
          )}
        </Column>
      </Row>
    </FlexGrid>
  );
};

export default BarChartGrouped;
