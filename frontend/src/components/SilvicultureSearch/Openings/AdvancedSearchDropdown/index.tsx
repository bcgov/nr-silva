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
    <div className="advanced-search-dropdown">
      <FlexGrid className="container-fluid advanced-search-container p-32">
        <Row className="mb-3">
          <Column sm={4} className="group-1">
            <CheckboxGroup
              orientation="horizontal"
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

        <Row className="mb-3">
          <Column lg={8}>
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
              className="multi-select"
              titleText="Org Unit"
              items={orgUnitItems}
              itemToString={(item: TextValueData) => (item ? `${item.value} - ${item.text}` : "")}
              selectionFeedback="top-after-reopen"
              onChange={(e: any) => handleMultiSelectChange("orgUnit", e.selectedItems)}
              selectedItems={selectedOrgUnits}
              sortItems={sortItems}
            />
          </Column>
          <Column lg={8}>
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

        <Row className="mb-3">
          <Column lg={8}>
            <div className="d-flex flex-auto mt-2 gap-1">
              <div>
                <FormLabel>Client acronym</FormLabel>
                <Tooltip
                  align="bottom"
                  label="If you don't remember the client information you can go to client search."
                >
                  <button className="bx--tooltip__trigger" type="button">
                    <Icons.Information />
                    <Icons.Information />
                  </button>
                </Tooltip>
                <TextInput
                  id="text-input-1"
                  type="text"
                  labelText=""
                  className="mt-2"
                  value={filters.clientAcronym}
                  onChange={(e: any) =>
                    handleFilterChange({ clientAcronym: e.target.value })
                  }
                />
              </div>
              <div>
                <TextInput
                  id="client-location-code"
                  labelText="Client location code"
                  type="text"
                  className="mt-1"
                  value={filters.clientLocationCode}
                  onChange={(e: any) =>
                    handleFilterChange({ clientLocationCode: e.target.value })
                  }
                />
              </div>
            </div>
          </Column>

          <Column lg={8}>
            <div className="d-flex flex-auto mt-2 gap-1">
              <TextInput
                id="text-input-2"
                type="text"
                labelText="Cut block"
                value={filters.cutBlock}
                onChange={(e: any) =>
                  handleFilterChange({ cutBlock: e.target.value })
                }
              />
              <TextInput
                id="text-input-3"
                labelText="Cutting permit"
                type="text"
                value={filters.cuttingPermit}
                onChange={(e: any) =>
                  handleFilterChange({ cuttingPermit: e.target.value })
                }
              />
            </div>
          </Column>
        </Row>

        <Row className="mb-3">
          <Column lg={8}>
            <div className="d-flex flex-auto mt-2 gap-1">
              <TextInput
                id="timber-mark-input"
                type="number"
                labelText="Timber mark"
                value={filters.timberMark}
                onChange={(e: any) =>
                  handleFilterChange({ timberMark: e.target.value })
                }
              />
            </div>
          </Column>
          <Column>
            <div className="d-flex flex-auto mt-2 gap-1">
              <ComboBox
                id="date-type-dropdown"
                titleText="Date type"
                items={dateTypeItems}
                itemToString={(item: any) => (item ? item.text : "")}
                onChange={(e: any) =>
                  handleFilterChange({ dateType: e.selectedItem === null ? "" : e.selectedItem.value })
                }
                // selectedItem={
                //   filters.dateType
                //     ? dateTypeItems.find(
                //       (item: any) => item.value === filters.dateType
                //     )
                //     : ""
                // }
                label="Date type"
              />
              <DatePicker
                id="start-date-picker"
                datePickerType="single"
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
                readOnly={!filters.dateType}
              >
                <DatePickerInput
                  id="start-date-picker-input-id"
                  size="md"
                  labelText="Start Date"
                  placeholder={
                    filters.startDate
                      ? filters.startDate // Display the date in YYYY-MM-DD format
                      : "yyyy/MM/dd"
                  }
                  value={formatDateForDatePicker(filters.startDate)}
                />
              </DatePicker>
              <DatePicker
                id="end-date-picker"
                datePickerType="single"
                onChange={(dates: [Date]) => {
                  if (dates.length > 0) {
                    handleFilterChange({
                      endDate: dates[0].toISOString().slice(0, 10),
                    });
                  }
                }}
                onClose={(dates: [Date]) => {
                  if (dates.length > 0) {
                    handleFilterChange({
                      endDate: dates[0].toISOString().slice(0, 10),
                    });
                  }
                }}
                readOnly={!filters.dateType}
              >
                <DatePickerInput
                  id="end-date-picker-input-id"
                  size="md"
                  labelText="End Date"
                  placeholder={
                    filters.endDate
                      ? filters.endDate // Display the date in YYYY-MM-DD format
                      : "yyyy/MM/dd"
                  }
                  value={formatDateForDatePicker(filters.endDate)}
                />
              </DatePicker>
            </div>
          </Column>
        </Row>

        <Row className="mb-3">
          <Column>

          </Column>
          <Column>

          </Column>
        </Row>

        <Row className="">

          <Column lg={16}>
            <CheckboxGroup
              orientation="horizontal"
              legendText="Status"
            >
              <div className="d-flex flex-status-list">
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
              </div>
            </CheckboxGroup>
          </Column>
        </Row>
      </FlexGrid>
    </div >
  );
};

export default AdvancedSearchDropdown;
