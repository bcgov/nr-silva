import React, { useEffect, useState } from "react";
import {
  Checkbox,
  CheckboxGroup,
  Dropdown,
  TextInput,
  FormLabel,
  Tooltip,
  DatePicker,
  DatePickerInput,
  Loading,
  FlexGrid,
  Row,
  Column,
} from "@carbon/react";
import "./AdvancedSearchDropdown.scss";
import * as Icons from "@carbon/icons-react";
import { useOpeningFiltersQuery } from "../../../../services/queries/search/openingQueries";
import { useOpeningsSearch } from "../../../../contexts/search/OpeningsSearch";
import { TextValueData, sortItems } from "../../../../utils/multiSelectSortUtils";
import { formatDateForDatePicker } from "../../../../utils/DateUtils";
import { ComboBox } from "@carbon/react";

interface AdvancedSearchDropdownProps {
  toggleShowFilters: () => void; // Function to be passed as a prop
}

const AdvancedSearchDropdown: React.FC<AdvancedSearchDropdownProps> = () => {
  const { filters, setFilters, clearFilters } = useOpeningsSearch();
  const { data, isLoading, isError } = useOpeningFiltersQuery();

  // Initialize selected items for OrgUnit MultiSelect based on existing filters
  const [selectedOrgUnits, setSelectedOrgUnits] = useState<any[]>([]);
  const [selectedCategories, setSelectedCategories] = useState<any[]>([]);

  useEffect(() => {
    console.log("Use Effect in child is being called.", filters);
    // Split filters.orgUnit into array and format as needed for selectedItems
    if (filters.orgUnit) {
      const orgUnitsArray = filters.orgUnit.map((orgUnit: string) => ({
        text: data?.orgUnits?.find((item: any) => item.orgUnitCode === orgUnit)?.orgUnitName || orgUnit,
        value: orgUnit,
      }));
      setSelectedOrgUnits(orgUnitsArray);
    } else {
      setSelectedOrgUnits([]);
    }
    // Split filters.category into array and format as needed for selectedItems
    if (filters.category) {
      const categoriesArray = filters.category.map((category: string) => ({
        text: data?.categories?.find((item: any) => item.code === category)?.description || category,
        value: category,
      }));
      setSelectedCategories(categoriesArray);
    } else {
      setSelectedCategories([]);
    }
  }, [filters.orgUnit, filters.category]);

  // const handleFilterChange = (updatedFilters: Partial<typeof filters>) => {
  //   const newFilters = { ...filters, ...updatedFilters };
  //   setFilters(newFilters);
  // };
  const handleFilterChange = (updatedFilters: Partial<typeof filters>) => {
    const newFilters = { ...filters, ...updatedFilters };
    setFilters(newFilters);
  };


  const handleMultiSelectChange = (group: string, selectedItems: any) => {
    const updatedGroup = selectedItems.map((item: any) => item.value);
    if (group === "orgUnit")
      setSelectedOrgUnits(selectedItems);
    if (group === "category")
      setSelectedCategories(selectedItems);
    handleFilterChange({ [group]: updatedGroup });
  }

  const handleCheckboxChange = (value: string, group: string) => {
    const selectedGroup = filters[group as keyof typeof filters] as string[];
    const updatedGroup = selectedGroup.includes(value)
      ? selectedGroup.filter((item) => item !== value)
      : [...selectedGroup, value];

    handleFilterChange({ [group]: updatedGroup });
  };

  if (isLoading) {
    return <Loading withOverlay={true} />;
  }

  if (isError) {
    return (
      <div>
        <p>There was an error while loading the advanced filters.</p>
      </div>
    );
  }

  const categoryItems =
    data.categories?.map((item: any) => ({
      text: item.description,
      value: item.code,
    })) || [];

  const orgUnitItems =
    data.orgUnits?.map((item: any) => ({
      text: item.orgUnitName,
      value: item.orgUnitCode,
    })) || [];

  const dateTypeItems =
    data.dateTypes?.map((item: any) => ({
      text: item.label,
      value: item.value,
    })) || [];

  const blockStatusItems =
    data.blockStatuses?.map((item: any) => ({
      text: item.label,
      value: item.value,
    })) || [];

  return (
    <FlexGrid className="advanced-search-dropdown" condensed >
      <Row>
        <Column sm={4}>
          <CheckboxGroup
            orientation="horizontal"
            legendText="Opening Filters"
            className="horizontal-checkbox-group"
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
              labelText={`FRPA section 108`}
              id="checkbox-label-2"
              checked={filters.openingFilters.includes(
                "FRPA section 108"
              )}
              onChange={() =>
                handleCheckboxChange(
                  "FRPA section 108",
                  "openingFilters"
                )
              }
            />
          </CheckboxGroup>
        </Column>
      </Row>
      <Row>
        <Column sm={2} className="orgUnitCol">
          <Dropdown
            label="Enter or choose an org unit"
            selectedItem={
              filters.orgUnit
                ? orgUnitItems.find(
                  (item: any) => item.value === filters.orgUnit
                )
                : ""
            }
            id="orgunit-multiselect"
            className=""
            titleText="Org Unit"
            items={orgUnitItems}
            itemToString={(item: TextValueData) => (item ? `${item.value} - ${item.text}` : "")}
            selectionFeedback="top-after-reopen"
            onChange={(e: any) => handleMultiSelectChange("orgUnit", e.selectedItems)}
            selectedItems={selectedOrgUnits}
            sortItems={sortItems}
          />
        </Column>
        <Column sm={2}>
          <Dropdown
            id="category-dropdown"
            titleText="Category"
            items={categoryItems}
            itemToString={(item: any) => (item ? item.text : "")}
            onChange={(e: any) =>
              handleFilterChange({ category: e.selectedItem.value })
            }
            label="Enter or choose a category"
            selectedItem={
              filters.category
                ? categoryItems.find(
                  (item: any) => item.value === filters.category
                )
                : ""
            }
            selectionFeedback="top-after-reopen"
            selectedItems={selectedCategories}
            sortItems={sortItems}
          />
        </Column>
      </Row>
      <Row>
        <Column sm={1}>
          <TextInput
            id="client-acronym-code"
            type="text"
            labelText={
              <>
                Client acronym
                <Tooltip
                  message="If you don't remember the client information you can go to client search."
                  position="right"
                >
                  <button id="tooltip" type="button">
                    <Icons.Information />
                  </button>
                </Tooltip>
              </>
            }
            className=""
            value={filters.clientAcronym}
            onChange={(e: any) =>
              handleFilterChange({ clientAcronym: e.target.value })
            }
          />
        </Column>
        <Column lg={4} className="clientLocationCodeCol">
          <TextInput
            id="client-location-code"
            labelText="Client location code"
            type="text"
            className=""
            value={filters.clientLocationCode}
            onChange={(e: any) =>
              handleFilterChange({ clientLocationCode: e.target.value })
            }
          />
        </Column>
        <Column sm={1}>
          <TextInput
            id="text-input-2"
            type="text"
            labelText="Cut block"
            value={filters.cutBlock}
            onChange={(e: any) =>
              handleFilterChange({ cutBlock: e.target.value })
            }
          />
        </Column>
        <Column sm={1}>
          <TextInput
            id="text-input-3"
            labelText="Cutting permit"
            type="text"
            value={filters.cuttingPermit}
            onChange={(e: any) =>
              handleFilterChange({ cuttingPermit: e.target.value })
            }
          /></Column>
      </Row>
      <Row>
        <Column md={4} className="timeberMarkCol">
          <TextInput
            id="timber-mark-input"
            type="number"
            labelText="Timber mark"
            value={filters.timberMark}
            onChange={(e: any) =>
              handleFilterChange({ timberMark: e.target.value })
            }
          />
        </Column>
        <Column lg={2} className="dateTypeCol">
          <ComboBox
            titleText="Date type"
            items={dateTypeItems}
            itemToString={(item: any) => (item ? item.text : "")}
            onChange={(e: any) =>
              handleFilterChange({ dateType: e.selectedItem === null ? "" : e.selectedItem.value })
            }
            label="Date type"
          />
        </Column>
        <Column lg={4} className="startEndDateCol">
          <DatePicker 
              datePickerType="range"
              onChange={(dates: any) => {
                if (dates.length === 2) {
                  handleFilterChange({
                    startDate: dates[0].toISOString().slice(0, 10),
                    endDate: dates[1].toISOString().slice(0, 10),
                  });
                }
              }}
              >
            <DatePickerInput id="date-picker-input-id-start" 
              placeholder="mm/dd/yyyy" 
              labelText="Start date" 
              disabled={!filters.dateType}
              size="md" />
            <DatePickerInput id="date-picker-input-id-finish" 
              placeholder="mm/dd/yyyy" 
              labelText="End date" 
              disabled={!filters.dateType}
              size="md" />
          </DatePicker>
          {/* <DatePicker
            datePickerType="range"
            onChange={(dates: [Date]) => {
              if (dates.length > 0) {
                handleFilterChange({
                  startDate: dates[0].toISOString().slice(0, 10),
                });
              }
            }}
            onClose={(dates: [Date]) => {
              if (dates.length > 0) {
                handleFilterChange({
                  startDate: dates[0].toISOString().slice(0, 10),
                });
              }
            }}
            disabled={!filters.dateType}
            enabled={filters.dateType}
            readOnly={!filters.dateType}
          >
            <DatePickerInput
              labelText="Start Date"
              size="sm"
              placeholder={
                filters.startDate
                  ? filters.startDate // Display the date in YYYY-MM-DD format
                  : "yyyy/MM/dd"
              }
              disabled={!filters.dateType}
              enabled={filters.dateType}
              value={formatDateForDatePicker(filters.startDate)}
            />
            <DatePickerInput
              size="sm"
              labelText="End Date"
              placeholder={
                filters.endDate
                  ? filters.endDate // Display the date in YYYY-MM-DD format
                  : "yyyy/MM/dd"
              }
              disabled={!filters.dateType}
              enabled={filters.dateType}
              readOnly={!filters.dateType}
              value={formatDateForDatePicker(filters.endDate)}
            />
          </DatePicker> */}
        </Column>
      </Row>
      <Row>
        <Column sm={4}>
          <CheckboxGroup
            orientation="horizontal"
            legendText="Status"
          >
            <Checkbox
              labelText={`DFT - Draft`}
              id="checkbox-label-dft"
              checked={filters.status.includes("DFT")}
              onChange={() => handleCheckboxChange("DFT", "status")}
            />
            <Checkbox
              labelText={`APP - Approved`}
              id="checkbox-label-app"
              checked={filters.status.includes("APP")}
              onChange={() => handleCheckboxChange("APP", "status")}
            />
            <Checkbox
              labelText={`FG - Free Growing`}
              id="checkbox-label-rjc"
              checked={filters.status.includes("FG")}
              onChange={() => handleCheckboxChange("FG", "status")}
            />
            <Checkbox
              labelText={`SUB - Submitted`}
              id="checkbox-label-cnl"
              checked={filters.status.includes("SUB")}
              onChange={() => handleCheckboxChange("SUB", "status")}
            />
          </CheckboxGroup>
        </Column>
      </Row>
    </FlexGrid>
  );
};

export default AdvancedSearchDropdown;
