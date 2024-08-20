import React, {useState} from "react";
import {
  Checkbox,
  CheckboxGroup,
  Dropdown,
  TextInput,
  FormLabel,
  Tooltip,
  DatePicker,
  DatePickerInput,
} from "@carbon/react";
import "./AdvancedSearchDropdown.scss";
import * as Icons from "@carbon/icons-react";
import Icon from "../Icon";
import { NumberInput } from "@carbon/react";

interface AdvancedSearchDropdownProps {
  toggleShowFilters: () => void; // Function to be passed as a prop
}
const AdvancedSearchDropdown: React.FC<AdvancedSearchDropdownProps> = ({ toggleShowFilters }) => {
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);
  const items = [
    {
      text: "Option 1",
    },
    {
      text: "Option 2",
    },
    {
      text: "Option 3 - a disabled item",
      disabled: true,
    },
    {
      text: "Option 4",
    },
    {
      text: "Option 5",
    },
    {
      text: "Option 6",
    },
    {
      text: "Option 7",
    },
    {
      text: "Option 8",
    },
  ];
  return (
    <div className="advanced-search-dropdown">
      <div className="container-fluid advanced-search-container p-32">
        <div className="row pb-32">
          <div className="group-1">
            <CheckboxGroup
              orientation="horizontal"
              className="horizontal-checkbox-group"
              legendText="Opening Filters"
            >
              <Checkbox
                labelText={`Openings created by me`}
                id="checkbox-label-1"
              />
              <Checkbox
                labelText={`Submitted to FRPA section 108`}
                id="checkbox-label-2"
              />
            </CheckboxGroup>
          </div>
        </div>

        <div className="row mb-3">
          <div className="col-6">
            <Dropdown
              id="orguni-dropdown"
              titleText="Org unit"
              items={items}
              itemToString={(item: any) => (item ? item.text : "")}
              onChange={() => toggleShowFilters()}
              label="Enter or choose an org unit"
            />
          </div>
          <div className="col-6">
            <Dropdown
              id="category-dropdown"
              titleText="Category"
              items={items}
              itemToString={(item: any) => (item ? item.text : "")}
              onChange={() => console.log("item selected")}
              label="Enter or choose a category"
            />
          </div>
        </div>

        <div className="row mb-3">
          <div className="col-6">
            <FormLabel>Client acronym</FormLabel>
            <Tooltip
              align="bottom"
              label="If you don't remember the client information you can go to client search."
            >
              <button className="bx--tooltip__trigger" type="button">
                <Icons.Information />
              </button>
            </Tooltip>
            <TextInput id="text-input-1" type="text" className="mt-2" />
          </div>
          <div className="col-6">
            <FormLabel>Form label with Tooltip</FormLabel>
            <Tooltip
              align="bottom"
              label="If you don't remember the client information you can go to client search."
            >
              <button className="bx--tooltip__trigger" type="button">
                <Icons.Information />
              </button>
            </Tooltip>
            <div className="d-flex flex-auto mt-2">
              <Dropdown
                id="category-dropdown"
                items={items}
                itemToString={(item: any) => (item ? item.text : "")}
                onChange={() => console.log("item selected")}
                label="Block Status"
              />
              <TextInput
                id="text-input-1"
                type="text"
                placeholder="Cut block"
                className="mx-1"
              />
              <TextInput
                id="text-input-1"
                placeholder="Cutting permit"
                type="text"
              />
            </div>
          </div>
        </div>

        <div className="row mb-3">
          <div className="col-6">
            <TextInput
              id="text-input-1"
              type="number"
              className="mt-2"
              labelText="Gross area"
            />
          </div>
          <div className="col-6">
            <TextInput
              id="text-input-1"
              type="number"
              className="mt-2"
              labelText="Timber mark"
            />
          </div>
        </div>

        <div className="row ">
          <div className="col-6">
            <div className="d-flex flex-auto">
              <Dropdown
                id="status-dropdown"
                titleText="Status"
                items={items}
                itemToString={(item: any) => (item ? item.text : "")}
                onChange={() => console.log("this is it")}
                label="Status"
              />
              <DatePicker
                datePickerType="single"
                className="mx-2"
                onChange={(dates: [Date]) => setStartDate(dates[0])}
              >
                <DatePickerInput
                  id="start-date-picker-input-id"
                  placeholder="yyyy/MM/dd"
                  size="md"
                  labelText="Start Date"
                />
              </DatePicker>
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
          <div className="col-6">
            <CheckboxGroup
              orientation="horizontal"
              className="horizontal-checkbox-group"
              legendText="Opening Filters"
            >
              <div className="d-flex">
                <Checkbox labelText={`DFT - Draft`} id="checkbox-label-dft" />
                <Checkbox
                  labelText={`APP - Approved`}
                  id="checkbox-label-app"
                />
              </div>
              <div className="d-flex">
                <Checkbox
                  labelText={`FG - Free growing`}
                  id="checkbox-label-fg"
                />
                <Checkbox
                  labelText={`SUB - Submitted`}
                  id="checkbox-label-sub"
                />
              </div>
            </CheckboxGroup>
          </div>
        </div>

      </div>
    </div>
  );
};

export default AdvancedSearchDropdown;
