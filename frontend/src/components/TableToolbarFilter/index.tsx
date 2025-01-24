import React, { useState, SyntheticEvent } from 'react';
import { 
  FlexGrid,
  Row,
  Column,

  CheckboxGroup,
  Checkbox,
  FilterableMultiSelect,
  TextInput,
  DatePicker,
  DatePickerInput,
  Dropdown,
  ComboBox
} from "@carbon/react";

import { TextValueData, sortItems } from '@/utils/multiSelectSortUtils';
import { OpeningSearchFilters } from "@/services/search/openings";
import { statusTypes, dateTypes } from '@/constants/searchConstants';
// AutoComplete section
import { AutocompleteProvider } from '@/contexts/AutocompleteProvider';
import AutocompleteClientLocation, { skipConditions, fetchValues, AutocompleteComponentRefProps} from '@/components/AutocompleteClientLocation';

import './index.scss';

interface FilterProps {
  filters: OpeningSearchFilters;
  categories: TextValueData[];
  orgUnits: TextValueData[];
  onFilterChange: (updatedFilter: OpeningSearchFilters) => void;
}

interface TextInputEvent extends SyntheticEvent {
  target: HTMLInputElement;
}

interface SelectEvent {
  selectedItems: TextValueData[];
}

interface AutocompleteProps {
  id: string,
  label: string,
}

interface AutocompleteComboboxProps{
  selectedItem: AutocompleteProps
}

const TableToolbarFilter: React.FC<FilterProps> = ({ filters, categories, orgUnits, onFilterChange }) => {

  const [isClientLocationActive,setIsClientLocationActive] = useState(false);

  const locations : AutocompleteProps[] = [];
  const clients: AutocompleteProps[] = [];

  const setMuliSelectValue = (field: string, value: TextValueData[]) => {
    setTimeout(() => {
      if (field === 'orgUnit') {
        onFilterChange({...filters, orgUnit: value.map(item => item.value)})
      } else if (field === 'category') {
        onFilterChange({...filters, category: value.map(item => item.value)})
      } else if (field === 'status') {
        onFilterChange({...filters, statusList: value.map(item => item.value)})
      }
    },0);
  }

  const setValue = (value: Partial<OpeningSearchFilters>) => {
    setTimeout(() => { onFilterChange({...filters, ...value}) },0);
  }

  const setLocationByName = (value: string) =>{
    // We use the useEffect to do a lookup on the locations options
    const selectedItem = locations.find((item: AutocompleteProps) => item.label === value);
    // If there's text typed and we found a location based on the label selected, we select it
    if(value && selectedItem){
      // This triggers the location useEffect to set the value
      onFilterChange({...filters, clientLocationCode: selectedItem.id});
    }
  }

  const setClientByName = (value: string) =>{
    // We use the useEffect to do a lookup on the clients options
    // It will fill with the autocomplete triggering the fetchOptions
    const selectedItem = clients.find((item: AutocompleteProps) => item.label === value);

    // If there's text typed and we found a client based on the label selected, we select it
    if(value && selectedItem){
      onFilterChange({...filters, clientNumber: selectedItem.id});
    }
    
    // This also triggers the autocomplete to fetch the options
    //if(valueTyped)
    //  fetchOptions(valueTyped, "clients")
  }

  return (
    <div className="advanced-search-dropdown">
      <FlexGrid className="container-fluid advanced-search-container p-32">
        <Row className="pb-32">
          <Column sm={4} md={8} lg={16}>
            <CheckboxGroup
              orientation="horizontal"
              legendText="Opening Filters"
            >
              <Checkbox
                labelText={`Openings created by me`}
                id="checkbox-label-1"
                checked={!!filters.myOpenings}
                onChange={() =>{ onFilterChange({...filters, myOpenings: !filters.myOpenings}) }}
              />
              <Checkbox
                labelText={`FRPA section 108`}
                id="checkbox-label-2"
                checked={!!filters.submittedToFrpa}
                onChange={() =>{ onFilterChange({...filters, submittedToFrpa: !filters.submittedToFrpa}) }}
              />
            </CheckboxGroup>
          </Column>
          <Column sm={4} md={8} lg={16}>
            <FilterableMultiSelect
              label="Enter or choose an org unit"
              id="orgunit-multiselect"
              className="multi-select"
              titleText="Org Unit"
              items={orgUnits}
              itemToString={(item: TextValueData) => (item ? `${item.value} - ${item.text}` : "")}
              selectionFeedback="top-after-reopen"
              onChange={(e: SelectEvent) => { setMuliSelectValue('orgUnit',e.selectedItems) }}
              selectedItems={ filters.orgUnit ? orgUnits.filter(item => filters.orgUnit?.includes(item.value)) : []}
              sortItems={sortItems}
            />
          </Column>
          <Column sm={4} md={8} lg={16}>
            <FilterableMultiSelect
              label="Enter or choose a category"
              id="category-multiselect"
              className="multi-select"
              titleText="Category"
              items={categories}
              itemToString={(item: TextValueData) => (item ? `${item.value} - ${item.text}` : "")}
              selectionFeedback="top-after-reopen"
              onChange={(e: SelectEvent) => { setMuliSelectValue('category',e.selectedItems) }}
              selectedItems={ filters.category ? categories.filter(item => filters.category?.includes(item.value)) : []}
              sortItems={sortItems}
            />
          </Column>
          <Column sm={4} md={8} lg={16}>
            <FilterableMultiSelect
              label="Enter or choose a status"
              id="status-multiselect"
              className="multi-select"
              titleText="Status"
              items={statusTypes}
              itemToString={(item: TextValueData) => (item ? `${item.value} - ${item.text}` : "")}
              selectionFeedback="top-after-reopen"
              onChange={(e: SelectEvent) => { setMuliSelectValue('status',e.selectedItems) }}
              selectedItems={ filters.statusList ? statusTypes.filter(item => filters.statusList?.includes(item.value)) : []}
              sortItems={sortItems}
            />
          </Column>
          <Column sm={4} md={8} lg={16}>
            <TextInput
              id="text-input-2"
              type="text"
              labelText="Cut block"
              value={filters.cutBlockId}
              onChange={(e: TextInputEvent) => { setValue({cutBlockId: e.target.value}) }}
            />
          </Column>
          <Column sm={4} md={8} lg={16}>
            <TextInput
              id="text-input-3"
              labelText="Cutting permit"
              type="text"
              value={filters.cuttingPermitId}
              onChange={(e: TextInputEvent) => { setValue({cuttingPermitId: e.target.value}) }}
            />            
          </Column>
          <Column sm={4} md={8} lg={16}>
            <TextInput
              id="timber-mark-input"
              labelText="Timber mark"
              value={filters.timberMark || ''}
              onChange={(e: TextInputEvent) => { setValue({timberMark: e.target.value}) }}
            />
          </Column>
          <Column sm={4} md={8} lg={16}>
            <Dropdown
              id="date-type-dropdown"
              titleText="Date type"
              items={dateTypes}
              itemToString={(item: TextValueData) => (item ? item.text : "")}
              onChange={(e: any) => {}}
              
              label="Date type"
            />
          </Column>
          <Column sm={4} md={8} lg={16}>
            <DatePicker
              datePickerType="range"
              dateFormat="Y/m/d"
              allowInput={true}
              
            >
              <DatePickerInput
                autoComplete="off"
                id="start-date-picker-input-id"
                placeholder="yyyy/MM/dd"
                size="md"
                labelText="Start Date"
                
              />
              <DatePickerInput
                autoComplete="off"
                id="end-date-picker-input-id"
                placeholder="yyyy/MM/dd"
                size="md"
                labelText="End Date"
                
              />
            </DatePicker>
          </Column>              
          <Column sm={4} md={8} lg={16}>
            <ComboBox
              id="client-name"
              className="flex-fill"
              allowCustomValue={false}              
              selectedItem={ filters.clientNumber ? clients.filter(item => item.id === filters.clientNumber ) : {}}
              onInputChange={setClientByName}              
              onChange={(item: AutocompleteComboboxProps) => onFilterChange({...filters, clientNumber: item.selectedItem.id})}
              itemToElement={(item: AutocompleteProps) => item.label}
              helperText="Search by client name, number or acronym"
              items={clients || []}
              titleText="Client" 
            />
          </Column>
          <Column sm={4} md={8} lg={16}>
            <ComboBox
              disabled={!isClientLocationActive}
              id="client-location"
              className="flex-fill"
              selectedItem={ filters.clientLocationCode ? locations.filter(item => item.id === filters.clientLocationCode ) : {}}
              onInputChange={setLocationByName}
              onChange={(item: AutocompleteComboboxProps) => onFilterChange({...filters, clientLocationCode: item.selectedItem.id})}
              itemToElement={(item: AutocompleteProps) => item.label}
              items={locations || [{ id: "", label: "No results found" }]}
              titleText="Location code" />
          </Column>
        </Row>

      </FlexGrid>
    </div>);
}

export default TableToolbarFilter;