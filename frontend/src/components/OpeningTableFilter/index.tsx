import React, { useState, useEffect } from "react";
import {
  Grid,
  Column,
  CheckboxGroup,
  Checkbox,
  FilterableMultiSelect,
  TextInput,
  DatePicker,
  DatePickerInput,
  ComboBox,
  Search,
} from "@carbon/react";

// Types
import {
  IdTextValueData,
  AutocompleteProps,
  SelectEvent,
  SelectEvents,
  AutocompleteComboboxProps,
  TextInputEvent,
} from "@/types/GeneralTypes";
import { sortItems } from "@/utils/multiSelectSortUtils";
import {
  OpeningSearchFilters,
  CodeDescription,
  OrgUnit,
} from "@/services/search/openings";
import { statusTypes, dateTypes } from "@/constants/searchConstants";

// Function libs
import { format } from "date-fns";

// AutoComplete section
import {
  fetchClientsByNameAcronymNumber,
  fetchClientLocations,
  ForestClientAutocomplete,
  ForestClientLocation,
} from "@/services/OpeningClientLocationService";

import "./index.scss";

interface FilterProps {
  filters: OpeningSearchFilters;
  categories: IdTextValueData[];
  orgUnits: IdTextValueData[];
  onFilterChange: (updatedFilter: OpeningSearchFilters) => void;
}

const dateFormat = "yyyy-MM-dd";

const OpeningTableFilter: React.FC<FilterProps> = ({
  categories,
  orgUnits,
  filters,
  onFilterChange,
}) => {
  const [isClientLocationActive, setIsClientLocationActive] = useState(false);
  const [dateKind, setDateKind] = useState<IdTextValueData | null>(null);
  const [dates, setDates] = useState<string[]>([]);
  const [clients, setClients] = useState<AutocompleteProps[]>([]);
  const [locations, setLocations] = useState<AutocompleteProps[]>([]);

  // The setTimeout is to avoid carbon complaining about updates on components
  const setValue = (value: Partial<OpeningSearchFilters>) => {
    setTimeout(() => {
      onFilterChange({ ...filters, ...value });
    }, 0);
  };

  // This is the autocomplete selection function that is used to select the value based on the text entered
  // This is to cover the problem ComboBoxes have with the selected item when deployed
  const selectBy = (
    entries: AutocompleteProps[],
    value: string,
    field: string
  ) => {
    // We use the useEffect to do a lookup on the entries options
    const selectedItem = entries.find(
      (item: AutocompleteProps) => item.label === value
    );
    // If there's text typed and we found an entry based on the value selected, we select it
    if (value && selectedItem) {
      // This triggers the useEffect to set the value
      onFilterChange({ ...filters, [field]: selectedItem.id });
    }
  };

  const onCheckboxChange = (field: string) => {
    onFilterChange({
      ...filters,
      [field]: filters[field as keyof typeof filters] ? undefined : true,
    });
  };

  const clientAutocompleteBy = async (
    entries: AutocompleteProps[],
    value: string,
    field: string
  ) => {
    // We use a regex to validate the format of the client name, number, acronym
    const regex = /^[A-Z\s.,]+,\s\d{8},\s([A-Z]+)?$/;

    // We only fetch if the value is more than 2 characters and it doesn't match
    // If it matches, it means that it is a selected value from the dropdown, so avoid a new fetch
    if (value && value.length > 2 && !regex.test(value)) {
      // Fetch if the value is more than 2 characters
      const response = await fetchClientsByNameAcronymNumber(value);
      // Once we have the response, we map it to the autocomplete format
      const results = response.map((item: ForestClientAutocomplete) => ({
        id: item.id,
        label: `${item.name}, ${item.id}, ${item.acronym ? item.acronym : ""}`,
      }));
      // We set the clients to the results for the autocomplete dropdown
      setClients(results);
    }

    // We use the selectBy function to select the value based on the text entered
    if (regex.test(value)) {
      setIsClientLocationActive(true);
      selectBy(clients, value, field);
    }

    // When receiving an empty value, it means we are cleaning the selected value
    if (!value) {
      setValue({ [field]: undefined });
    }
  };

  const locationAutocompleteBy = (value: string) => {
    // We use the selectBy function to select the value based on the text entered
    if (value) {
      selectBy(locations, value, "clientLocationCode");
    } else {
      // When receiving an empty value, it means we are cleaning the selected value
      setValue({ clientLocationCode: undefined });
    }
  };

  const onSearchTermChange = (e: never) => {};
  const onKeyDown = (e: never) => {};

  // This is to reset the date fields including dateKind once the date is cleared on the parent
  useEffect(() => {
    const isClearedStart = !Object.keys(filters).some((key) =>
      key.endsWith("DateStart")
    );

    const isClearedEnd = !Object.keys(filters).some((key) =>
      key.endsWith("DateEnd")
    );

    if (dateKind !== null && dates.length > 1) {
      if (isClearedStart && isClearedEnd) {
        setDateKind(null);
      }
    }
  }, [filters]);

  // This is just to reset the dates in case of a dateKind change to empty/null
  useEffect(() => {
    if (!(dates && dateKind && dateKind.id)) {
      setValue({
        disturbanceDateEnd: undefined,
        regenDelayDateEnd: undefined,
        freeGrowingDateEnd: undefined,
        updateDateEnd: undefined,
        disturbanceDateStart: undefined,
        regenDelayDateStart: undefined,
        freeGrowingDateStart: undefined,
        updateDateStart: undefined,
      });
      setDates([]);
    }
  }, [dateKind]);

  // This use effect updates the filter when date changes. It also updates the date when dateKind changes
  // This is to accomodate the dateKind change when the user selects a new date type, while we keep the dates
  useEffect(() => {
    if (dates && dateKind && dateKind.id && dates.length > 1) {
      setValue({
        disturbanceDateStart: undefined,
        regenDelayDateStart: undefined,
        freeGrowingDateStart: undefined,
        updateDateStart: undefined,
        [`${dateKind.id}DateStart`]: format(dates[0], dateFormat),
        disturbanceDateEnd: undefined,
        regenDelayDateEnd: undefined,
        freeGrowingDateEnd: undefined,
        updateDateEnd: undefined,
        [`${dateKind.id}DateEnd`]: format(dates[1], dateFormat),
      });
    }
  }, [dateKind, dates]);

  useEffect(() => {
    if (isClientLocationActive && filters.clientNumber) {
      fetchClientLocations(filters.clientNumber || "")
        .then((response) => {
          return response.map((item: ForestClientLocation) => ({
            id: item.id,
            label: `${item.id} - ${item.name}`,
          }));
        })
        .then(setLocations);
    }
  }, [isClientLocationActive]);

  return (
    <Grid fullWidth className="advanced-search-container">
      <Column sm={4} md={8} lg={16} xlg={16} max={16}>
        <Search
          size="md"
          placeholder="Search by opening ID, opening number or file ID"
          labelText="Search"
          closeButtonLabelText="Clear search input"
          id="search-1"
          className="search-input"
          onChange={onSearchTermChange}
          onKeyDown={onKeyDown}
          value={filters.mainSearchTerm}
        />
      </Column>

      <Column sm={4} md={8} lg={16} xlg={16} max={16}>
        <CheckboxGroup orientation="horizontal" legendText="Opening Filters">
          <Checkbox
            labelText={`Openings created by me`}
            id="checkbox-label-1"
            checked={!!filters.myOpenings}
            onChange={() => onCheckboxChange("myOpenings")}
          />
          <Checkbox
            labelText={`FRPA section 108`}
            id="checkbox-label-2"
            checked={!!filters.submittedToFrpa}
            onChange={() => onCheckboxChange("submittedToFrpa")}
          />
        </CheckboxGroup>
      </Column>

      <Column sm={4} md={8} lg={8} xlg={8} max={8}>
        <FilterableMultiSelect
          titleText="Category"
          label="Enter or choose a category"
          id="category-multiselect"
          className="multi-select category-multi-select ms-1"
          items={categories}
          itemToString={(item: IdTextValueData) =>
            item ? `${item.id} - ${item.text}` : ""
          }
          selectionFeedback="top-after-reopen"
          onChange={(e: SelectEvents) => {
            setValue({
              category: e.selectedItems.map((item: IdTextValueData) => item.id),
            });
          }}
          selectedItems={
            filters.category
              ? categories.filter((item) => filters.category?.includes(item.id))
              : []
          }
          sortItems={sortItems}
        />
      </Column>

      <Column sm={4} md={8} lg={8} xlg={8} max={8}>
        <FilterableMultiSelect
          titleText="Org Unit"
          label="Enter or choose an org unit"
          id="orgunit-multiselect"
          className="multi-select orgunit-multi-select ms-1"
          items={orgUnits}
          itemToString={(item: IdTextValueData) =>
            item ? `${item.id} - ${item.text}` : ""
          }
          selectionFeedback="top-after-reopen"
          onChange={(e: SelectEvents) => {
            setValue({
              orgUnit: e.selectedItems.map((item: IdTextValueData) => item.id),
            });
          }}
          selectedItems={
            filters.orgUnit
              ? orgUnits.filter((item) => filters.orgUnit?.includes(item.id))
              : []
          }
          sortItems={sortItems}
        />
      </Column>

      <Column sm={4} md={8} lg={8} xlg={8} max={8}>
        <FilterableMultiSelect
          titleText="Status"
          label="Enter or choose a status"
          id="status-multiselect"
          className="multi-select status-multi-select ms-1"
          items={statusTypes}
          itemToString={(item: IdTextValueData) =>
            item ? `${item.id} - ${item.text}` : ""
          }
          selectionFeedback="top-after-reopen"
          onChange={(e: SelectEvents) => {
            setValue({
              statusList: e.selectedItems.map(
                (item: IdTextValueData) => item.id
              ),
            });
          }}
          selectedItems={
            filters.statusList
              ? statusTypes.filter((item) =>
                  filters.statusList?.includes(item.id)
                )
              : []
          }
          sortItems={sortItems}
        />
      </Column>

      <Column sm={4} md={8} lg={8} xlg={8} max={8}>
        <ComboBox
          id="client-name"
          selectedItem={
            filters.clientNumber
              ? clients.find((item) => item.id === filters.clientNumber)
              : null
          }
          onInputChange={(value: string) =>
            clientAutocompleteBy(locations, value, "clientNumber")
          }
          onChange={(item: AutocompleteComboboxProps) =>
            selectBy(clients, item?.selectedItem?.label, "clientNumber")
          }
          itemToString={(item: AutocompleteProps) => (item ? item.label : "")}
          helperText="Search by client name, number or acronym"
          items={clients}
          titleText="Client"
        />
        <ComboBox
          disabled={!filters.clientNumber}
          id="client-location"
          selectedItem={
            filters.clientLocationCode
              ? locations.find((item) => item.id === filters.clientLocationCode)
              : null
          }
          onInputChange={(value: string) => locationAutocompleteBy(value)}
          onChange={(item: AutocompleteComboboxProps) =>
            selectBy(clients, item?.selectedItem?.label, "clientLocationCode")
          }
          itemToString={(item: AutocompleteProps) => (item ? item.label : "")}
          items={locations || [{ id: "", label: "No results found" }]}
          titleText="Location code"
        />
      </Column>

      <Column sm={4} md={8} lg={8} xlg={8} max={8}>
        <ComboBox
          id="date-type-combobox"
          items={dateTypes}
          itemToString={(item: IdTextValueData) => (item ? item.text : "")}
          titleText="Date type"
          onChange={(item: SelectEvent) =>
            setDateKind(item.selectedItem || null)
          }
          selectedItem={dateKind}
        />
        <DatePicker
          datePickerType="range"
          dateFormat="Y-m-d"
          allowInput={true}
          onChange={setDates}
          value={dates}
          maxDate={format(new Date(), dateFormat)}
        >
          <DatePickerInput
            autoComplete="off"
            id="start-date-picker-input-id"
            placeholder={dateFormat}
            size="md"
            labelText="Start Date"
            disabled={!dateKind?.id}
            value={
              dateKind?.id
                ? filters[`${dateKind.id}DateStart` as keyof typeof filters] ||
                  ""
                : ""
            }
          />
          <DatePickerInput
            autoComplete="off"
            id="end-date-picker-input-id"
            placeholder={dateFormat}
            size="md"
            labelText="End Date"
            disabled={!dateKind?.id}
            value={
              dateKind?.id
                ? filters[`${dateKind.id}DateEnd` as keyof typeof filters] || ""
                : ""
            }
          />
        </DatePicker>
      </Column>

      <Column sm={4} md={8} lg={8} xlg={8} max={8}>
        <TextInput
          id="text-input-2"
          type="text"
          labelText="Cut block"
          value={filters.cutBlockId}
          onChange={(e: TextInputEvent) => {
            setValue({ cutBlockId: e.target.value });
          }}
        />
      </Column>

      <Column sm={4} md={8} lg={8} xlg={8} max={8}>
        <TextInput
          id="text-input-3"
          labelText="Cutting permit"
          type="text"
          value={filters.cuttingPermitId}
          onChange={(e: TextInputEvent) => {
            setValue({ cuttingPermitId: e.target.value });
          }}
        />
      </Column>

      <Column sm={4} md={8} lg={8} xlg={8} max={8}>
        <TextInput
          id="timber-mark-input"
          labelText="Timber mark"
          value={filters.timberMark || ""}
          onChange={(e: TextInputEvent) => {
            setValue({ timberMark: e.target.value });
          }}
        />
      </Column>
    </Grid>
  );
};

export default OpeningTableFilter;
