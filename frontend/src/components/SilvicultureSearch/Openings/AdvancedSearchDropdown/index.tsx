import React, { useEffect } from "react";
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
import { color } from "@carbon/charts";

interface AdvancedSearchDropdownProps {
  toggleShowFilters: () => void; // Function to be passed as a prop
}

const AdvancedSearchDropdown: React.FC<AdvancedSearchDropdownProps> = ({
}) => {
  const { filters, setFilters, clearFilters } = useOpeningsSearch();
  const { data, isLoading, isError } = useOpeningFiltersQuery();

  const handleFilterChange = (updatedFilters: Partial<typeof filters>) => {
    const newFilters = { ...filters, ...updatedFilters };
    setFilters(newFilters);
  };

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
      text: item.code,
      value: item.code,
    })) || [];

  const orgUnitItems =
    data.orgUnits?.map((item: any) => ({
      text: item.orgUnitCode,
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
          </Column>
        </Row>

        <Row className="mb-3">
          <Column lg={8}>
            <Dropdown
              id="orguni-dropdown"
              titleText="Org unit"
              items={orgUnitItems}
              itemToString={(item: any) => (item ? item.text : "")}
              onChange={(e: any) =>
                handleFilterChange({ orgUnit: e.selectedItem.value })
              }
              label="Enter or choose an org unit"
              selectedItem={
                filters.orgUnit
                  ? orgUnitItems.find(
                      (item: any) => item.value === filters.orgUnit
                    )
                  : ""
              }
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
                    <Icons.Information/>
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
              <>
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
              </>
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
                          filters.startDate !== null
                            ? filters.startDate // Display the date in YYYY-MM-DD format
                            : "yyyy/MM/dd"
                        }
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
