import React, { useEffect, useState, useRef } from "react";
import {
  Checkbox,
  CheckboxGroup,
  TextInput,
  DatePicker,
  DatePickerInput,
  Loading,
  FlexGrid,
  FilterableMultiSelect,
  Row,
  Column
  , ComboBox
} from "@carbon/react";
import "./AdvancedSearchDropdown.scss";
import { useOpeningFiltersQuery } from "../../../../services/queries/search/openingQueries";
import { useOpeningsSearch } from "../../../../contexts/search/OpeningsSearch";
import { TextValueData, sortItems } from "../../../../utils/multiSelectSortUtils";
import { formatDateForDatePicker } from "../../../../utils/DateUtils";
import { AutocompleteProvider } from "../../../../contexts/AutocompleteProvider";
import AutocompleteClientLocation, { skipConditions, fetchValues, AutocompleteComponentRefProps } from "../../../AutocompleteClientLocation";

interface AdvancedSearchDropdownProps {
  toggleShowFilters: () => void; // Function to be passed as a prop
}

const AdvancedSearchDropdown: React.FC<AdvancedSearchDropdownProps> = () => {
  const { filters, setFilters, setIndividualClearFieldFunctions } = useOpeningsSearch();
  const { data, isLoading, isError } = useOpeningFiltersQuery();

  // Initialize selected items for OrgUnit MultiSelect based on existing filters
  const [selectedOrgUnits, setSelectedOrgUnits] = useState<any[]>([]);
  const [selectedCategories, setSelectedCategories] = useState<any[]>([]);
  const autoCompleteRef = useRef<AutocompleteComponentRefProps>(null);

  useEffect(() => {
    //console.log("Use Effect in child is being called.", filters);
    // Split filters.orgUnit into array and format as needed for selectedItems
    if (filters.orgUnit) {
      const orgUnitsArray = filters.orgUnit.map((orgUnit: string) => ({
        text: data?.orgUnits?.find((item: any) => item.orgUnitCode === orgUnit)?.orgUnitName || orgUnit,
        value: orgUnit
      }));
      setSelectedOrgUnits(orgUnitsArray);
    } else {
      setSelectedOrgUnits([]);
    }
    // Split filters.category into array and format as needed for selectedItems
    if (filters.category) {
      const categoriesArray = filters.category.map((category: string) => ({
        text: data?.categories?.find((item: any) => item.code === category)?.description || category,
        value: category
      }));
      setSelectedCategories(categoriesArray);
    } else {
      setSelectedCategories([]);
    }
  }, [filters.orgUnit, filters.category]);

  useEffect(() => {

    // In here, we're defining the function that will be called when the user clicks on the "Clear" button
    // The idea is to keep the autocomplete component clear of any ties to the opening search context
    setIndividualClearFieldFunctions((previousIndividualFilters) => ({
      ...previousIndividualFilters,
      clientLocationCode: () => autoCompleteRef.current?.reset()
    }));
  }, []);

  const handleFilterChange = (updatedFilters: Partial<typeof filters>) => {
    if (updatedFilters.dateType === "") {
      updatedFilters.startDate = null as Date | null;
      updatedFilters.endDate = null as Date | null;
    }
    setFilters({ ...filters, ...updatedFilters });
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
      value: item.code
    })) || [];

  const orgUnitItems =
    data.orgUnits?.map((item: any) => ({
      text: item.orgUnitName,
      value: item.orgUnitCode
    })) || [];

  const dateTypeItems =
    data.dateTypes?.map((item: any) => ({
      text: item.label,
      value: item.value
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
          <FilterableMultiSelect
            label="Enter or choose an org unit"
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
        <Column sm={2}>
          <FilterableMultiSelect
            label="Enter or choose a category"
            id="category-multiselect"
            className="multi-select"
            titleText="Category"
            items={categoryItems}
            itemToString={(item: any) => (item ? `${item.value} - ${item.text}` : "")}
            selectionFeedback="top-after-reopen"
            onChange={(e: any) => handleMultiSelectChange("category", e.selectedItems)}
            selectedItems={selectedCategories}
            sortItems={sortItems}
          />
        </Column>
      </Row>
      <Row>
        <Column sm={2} className="clientLocationCol">
          <AutocompleteProvider fetchOptions={fetchValues} skipConditions={skipConditions}>
            <AutocompleteClientLocation
              setValue={(value: string | null) => handleFilterChange({ clientLocationCode: value })}
              ref={autoCompleteRef}
            />
          </AutocompleteProvider>
        </Column>
        <Column sm={1}>
          <TextInput
            id="text-input-2"
            className="cutBlock"
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
            className="cuttingPermit"
            labelText="Cutting permit"
            type="text"
            value={filters.cuttingPermit}
            onChange={(e: any) =>
              handleFilterChange({ cuttingPermit: e.target.value })
            }
          />
        </Column>
      </Row>

      <Row>
        <Column sm={2} className="timeberMarkCol">
          <TextInput
            id="timber-mark-input"
            labelText="Timber mark"
            value={filters.timberMark}
            onChange={(e: any) =>
              handleFilterChange({ timberMark: e.target.value })
            }
          />
        </Column>
        <Column lg={4} className="dateTypeCol">
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
            onChange={(dates: [Date, Date]) => {
              if (dates.length > 0) {
                handleFilterChange({
                  startDate: dates[0].toISOString().slice(0, 10),
                  endDate: dates[1] ? dates[1].toISOString().slice(0, 10) : null
                });
              }
            }}
            onClose={(dates: [Date, Date]) => {
              if (dates.length > 0) {
                handleFilterChange({
                  startDate: dates[0].toISOString().slice(0, 10),
                  endDate: dates[1] ? dates[1].toISOString().slice(0, 10) : null
                });
              }
            }}
            disabled={!filters.dateType}
          >
            <DatePickerInput
              labelText="Start Date"
              placeholder="yyyy/MM/dd"
              disabled={!filters.dateType}
              // enabled={filters.dateType}
              value={formatDateForDatePicker(filters.startDate)}
            />
            <DatePickerInput
              labelText="End Date"
              placeholder="yyyy/MM/dd"
              disabled={!filters.dateType}
              value={formatDateForDatePicker(filters.endDate)}
            />
          </DatePicker>
        </Column>
      </Row>
      <Row>
        <Column sm={4}>
          <CheckboxGroup
            orientation="horizontal"
            legendText="Status"
          >
            <Checkbox
              labelText={`AMG - Amalgamate`}
              id="checkbox-label-amg"
              checked={filters.status.includes("AMG")}
              onChange={() => handleCheckboxChange("AMG", "status")}
            />
            <Checkbox
              labelText={`AMD - Amended`}
              id="checkbox-label-amd"
              checked={filters.status.includes("AMD")}
              onChange={() => handleCheckboxChange("AMD", "status")}
            />
            <Checkbox
              labelText={`APP - Approved`}
              id="checkbox-label-app"
              checked={filters.status.includes("APP")}
              onChange={() => handleCheckboxChange("APP", "status")}
            />
            <Checkbox
              labelText={`DFT - Draft`}
              id="checkbox-label-dft"
              checked={filters.status.includes("DFT")}
              onChange={() => handleCheckboxChange("DFT", "status")}
            />
            <Checkbox
              labelText={`FG - Free Growing`}
              id="checkbox-label-fg"
              checked={filters.status.includes("FG")}
              onChange={() => handleCheckboxChange("FG", "status")}
            />
            <Checkbox
              labelText={`RMD - Removed`}
              id="checkbox-label-rmd"
              checked={filters.status.includes("RMD")}
              onChange={() => handleCheckboxChange("RMD", "status")}
            />
            <Checkbox
              labelText={`RET - Retired`}
              id="checkbox-label-ret"
              checked={filters.status.includes("RET")}
              onChange={() => handleCheckboxChange("RET", "status")}
            />
            <Checkbox
              labelText={`SUB - Submitted`}
              id="checkbox-label-sub"
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
