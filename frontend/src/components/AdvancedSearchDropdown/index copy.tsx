import React, { useState } from "react";
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

interface AdvancedSearchDropdownProps {
  onSearch: (filters: any) => void; // Function to handle search logic
  toggleShowFilters: () => void; // Function to be passed as a prop
}

const AdvancedSearchDropdown: React.FC<AdvancedSearchDropdownProps> = ({
  toggleShowFilters,
  onSearch,
}) => {
  const [filters, setFilters] = useState({
    startDate: null as Date | null,
    endDate: null as Date | null,
    orgUnit: null as string | null,
    category: null as string | null,
    clientAcronym: "",
    blockStatus: "",
    cutBlock: "",
    cuttingPermit: "",
    grossArea: "",
    timberMark: "",
    status: null as string | null,
    openingFilters: [] as string[], // Array to hold multiple checkbox selections
    blockStatuses: [] as string[], // Array to hold multiple block statuses
  });

  const items = [
    { text: "Option 1", value: "option1" },
    { text: "Option 2", value: "option2" },
    { text: "Option 3 - a disabled item", value: "option3", disabled: true },
    { text: "Option 4", value: "option4" },
    { text: "Option 5", value: "option5" },
    { text: "Option 6", value: "option6" },
    { text: "Option 7", value: "option7" },
    { text: "Option 8", value: "option8" },
  ];
  

  const handleFilterChange = (updatedFilters: Partial<typeof filters>) => {
    const newFilters = { ...filters, ...updatedFilters };
    setFilters(newFilters);
    onSearch(newFilters); // Send the updated filters to the parent component
  };

  const handleCheckboxChange = (value: string, group: string) => {
    const selectedGroup = filters[group as keyof typeof filters] as string[];
    const updatedGroup = selectedGroup.includes(value)
      ? selectedGroup.filter((item) => item !== value)
      : [...selectedGroup, value];

    handleFilterChange({ [group]: updatedGroup });
  };

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
                checked={filters.openingFilters.includes(
                  "Openings created by me"
                )}
                onChange={() =>
                  handleCheckboxChange(
                    "Openings created by me",
                    "openingFilters"
                  )
                }
              />
              <Checkbox
                labelText={`Submitted to FRPA section 108`}
                id="checkbox-label-2"
                checked={filters.openingFilters.includes(
                  "Submitted to FRPA section 108"
                )}
                onChange={() =>
                  handleCheckboxChange(
                    "Submitted to FRPA section 108",
                    "openingFilters"
                  )
                }
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
              onChange={(e: any) =>
                handleFilterChange({ orgUnit: e.selectedItem.value })
              }
              label="Enter or choose an org unit"
            />
          </div>
          <div className="col-6">
            <Dropdown
              id="category-dropdown"
              titleText="Category"
              items={items}
              itemToString={(item: any) => (item ? item.text : "")}
              onChange={(e: any) =>
                handleFilterChange({ category: e.selectedItem.value })
              }
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
            <TextInput
              id="text-input-1"
              type="text"
              className="mt-2"
              onChange={(e: any) =>
                handleFilterChange({ clientAcronym: e.target.value })
              }
            />
          </div>
          <div className="col-6">
            <FormLabel>Block</FormLabel>
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
                id="block-status-dropdown"
                items={items}
                itemToString={(item: any) => (item ? item.text : "")}
                onChange={(e: any) =>
                  handleFilterChange({ blockStatus: e.selectedItem.value })
                }
                label="Block Status"
              />
              <TextInput
                id="text-input-2"
                type="text"
                placeholder="Cut block"
                className="mx-1"
                onChange={(e: any) =>
                  handleFilterChange({ cutBlock: e.target.value })
                }
              />
              <TextInput
                id="text-input-3"
                placeholder="Cutting permit"
                type="text"
                onChange={(e: any) =>
                  handleFilterChange({ cuttingPermit: e.target.value })
                }
              />
            </div>
          </div>
        </div>

        <div className="row mb-3">
          <div className="col-6">
            <TextInput
              id="gross-area-input"
              type="number"
              className="mt-2"
              labelText="Gross area"
              onChange={(e: any) =>
                handleFilterChange({ grossArea: e.target.value })
              }
            />
          </div>
          <div className="col-6">
            <TextInput
              id="timber-mark-input"
              type="number"
              className="mt-2"
              labelText="Timber mark"
              onChange={(e: any) =>
                handleFilterChange({ timberMark: e.target.value })
              }
            />
          </div>
        </div>

        <div className="row">
          <div className="col-6">
            <div className="d-flex flex-auto">
              <Dropdown
                id="status-dropdown"
                titleText="Status"
                items={items}
                itemToString={(item: any) => (item ? item.text : "")}
                onChange={(e: any) =>
                  handleFilterChange({ status: e.selectedItem.value })
                }
                label="Status"
              />
              <DatePicker
                datePickerType="single"
                className="mx-2"
                onChange={(dates: [Date]) => {
                  handleFilterChange({ startDate: dates[0] });
                }}
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
                onChange={(dates: [Date]) => {
                  handleFilterChange({ endDate: dates[0] });
                }}
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
              legendText="Block Statuses"
            >
              <div className="d-flex">
                <Checkbox
                  labelText={`DFT - Draft`}
                  id="checkbox-label-dft"
                  checked={filters.blockStatuses.includes("DFT - Draft")}
                  onChange={() =>
                    handleCheckboxChange("DFT - Draft", "blockStatuses")
                  }
                />
                <Checkbox
                  labelText={`APP - Approved`}
                  id="checkbox-label-app"
                  checked={filters.blockStatuses.includes("APP - Approved")}
                  onChange={() =>
                    handleCheckboxChange("APP - Approved", "blockStatuses")
                  }
                />
              </div>
              <div className="d-flex">
                <Checkbox
                  labelText={`FG - Free growing`}
                  id="checkbox-label-fg"
                  checked={filters.blockStatuses.includes("FG - Free growing")}
                  onChange={() =>
                    handleCheckboxChange("FG - Free growing", "blockStatuses")
                  }
                />
                <Checkbox
                  labelText={`SUB - Submitted`}
                  id="checkbox-label-sub"
                  checked={filters.blockStatuses.includes("SUB - Submitted")}
                  onChange={() =>
                    handleCheckboxChange("SUB - Submitted", "blockStatuses")
                  }
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
