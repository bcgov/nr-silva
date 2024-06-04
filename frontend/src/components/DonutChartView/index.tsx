import React, { useState, useEffect, ChangeEvent, useCallback } from "react";
import { DonutChart } from "@carbon/charts-react";
import { Dropdown, DatePicker, DatePickerInput, TextInput } from "@carbon/react";
import "./DonutChartView.scss";
import { fetchFreeGrowingMilestones } from "../../services/OpeningService";

interface IDonutChart {
  group: any;
  value: any;
}

interface IDropdownItem {
  value: string,
  text: string
}

const DonutChartView: React.FC = () => {
  const [windowWidth, setWindowWidth] = useState(window.innerWidth);
  const [chartData, setChartData] = useState<IDonutChart[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [orgUnitCode, setOrgUnitCode] = useState<string>("");
  const [clientNumber, setClientNumber] = useState<string>("");
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);

  const handleResize = () => {
    setWindowWidth(window.innerWidth);
  };

  useEffect(() => {
    fetchChartData();
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, [orgUnitCode, clientNumber, startDate, endDate]);

  const fetchChartData = async () => {
    try {
      setIsLoading(true);
      const formattedStartDate = formatDateToString(startDate);
      const formattedEndDate = formatDateToString(endDate);
      const data = await fetchFreeGrowingMilestones({
        orgUnitCode,
        clientNumber,
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

  const formatDateToString = (dateToFormat: Date | null) => {
    if (!dateToFormat) return null;
    const year = dateToFormat.getFullYear();
    const month = String(dateToFormat.getMonth() + 1).padStart(2, "0");
    const day = String(dateToFormat.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  const options = {
    title: "",
    resizable: true,
    donut: {
      center: {
        label: "Standard units"
      }
    },
    height: "18.5rem",
    toolbar:{
      enabled:false
    }
  };

  // Sample data for dropdowns
  const items: IDropdownItem[] = [
    { value: 'DCR', text: 'DCR' },
    { value: '2', text: 'Option 2' },
    { value: '3', text: 'Option 3' },
    { value: '4', text: 'Option 4' },
    // Add more options as needed
  ];

  const setOrgUnitCodeSelected = ({selectedItem}:{selectedItem: IDropdownItem}) => {
    setOrgUnitCode(selectedItem.value);
  };

  return (
    <div className="px-3">
      <div className="row gy-2 pb-3">
        <div className="col-md-4 p-0">
          <Dropdown
            id="orgUnitCode"
            label={windowWidth <= 1584 ? "District" : "Filter by district"}
            titleText={windowWidth <= 1584 ? "District" : "Filter by district"}
            items={items}
            itemToString={(item: IDropdownItem) => item ? item.text : ''}
            onChange={setOrgUnitCodeSelected}
          />
        </div>
        <div className="col-md-4 p-0 px-md-1">
          <TextInput
            labelText="Client Number"
            id="clientNumber"
            onChange={(event: ChangeEvent<HTMLInputElement>) => setClientNumber(event.target.value)}
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
        <DonutChart
          data={chartData}
          options={options}>
        </DonutChart>
      )}
    </div>
  );
};

export default DonutChartView;
