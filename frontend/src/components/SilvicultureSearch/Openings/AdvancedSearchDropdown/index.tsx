import React, { useState, useEffect, useRef } from "react";
import {
  Checkbox,
  CheckboxGroup,
  Dropdown,
  TextInput,
  DatePicker,
  DatePickerInput,
  Loading,
  FlexGrid,
  Row,
  Column,
  FilterableMultiSelect
} from "@carbon/react";
import "./AdvancedSearchDropdown.scss";
import { useOpeningFiltersQuery } from "../../../../services/queries/search/openingQueries";
import { useOpeningsSearch } from "../../../../contexts/search/OpeningsSearch";
import { TextValueData, sortItems } from "../../../../utils/multiSelectSortUtils";
import { formatDateForDatePicker } from "../../../../utils/DateUtils";
import { AutocompleteProvider } from "../../../../contexts/AutocompleteProvider";
import AutocompleteClientLocation, { skipConditions, fetchValues, AutocompleteComponentRefProps} from "../../../AutocompleteClientLocation";

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
  } else{
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
  },[]);

  const handleFilterChange = (updatedFilters: Partial<typeof filters>) => {
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

  return (
    <div className="advanced-search-dropdown">
      <FlexGrid className="container-fluid advanced-search-container p-32">
        <Row className="pb-32">
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
          <Column lg={8}>
            <FilterableMultiSelect
              label="Enter or choose a category"
              id="category-multiselect"
              className="multi-select"
              titleText="Category"
              items={categoryItems}
              itemToString={(item: any) => (item ? `${item.value} - ${item.text}` : "")}
              selectionFeedback="top-after-reopen"
              onChange={(e: any) => handleMultiSelectChange("category",e.selectedItems)}
              selectedItems={selectedCategories}
              sortItems={sortItems}
            />
          </Column>
        </Row>

        <Row className="mb-3">
          <Column lg={8}>
            <AutocompleteProvider fetchOptions={fetchValues} skipConditions={skipConditions}>
              <AutocompleteClientLocation 
                setValue={(value: string | null) => handleFilterChange({ clientLocationCode: value })} 
                ref={autoCompleteRef}
                />
            </AutocompleteProvider>
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
          <Column lg={8}>
            <FlexGrid className="p-0">
              <Row>
                <Column
                  sm={4}
                  lg={16}
                  xl={16}
                  max={5}
                  className="date-type-col mt-sm-2 mt-lg-0"
                >
                  <Dropdown
                    id="date-type-dropdown"
                    titleText="Date type"
                    items={dateTypeItems}
                    itemToString={(item: any) => (item ? item.text : "")}
                    onChange={(e: any) =>
                      handleFilterChange({ dateType: e.selectedItem.value })
                    }
                    selectedItem={
                      filters.dateType
                        ? dateTypeItems.find(
                          (item: any) => item.value === filters.dateType
                        )
                        : ""
                    }
                    label="Date type"
                  />
                </Column>
                <Column
                  sm={4}
                  lg={16}
                  xl={16}
                  max={11}
                  className="date-selectors-col"
                >
                  <div className="d-flex flex-auto">
                    <DatePicker
                      datePickerType="single"
                      className="me-1"
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
            </FlexGrid>
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
    </div>
  );
};

export default AdvancedSearchDropdown;
