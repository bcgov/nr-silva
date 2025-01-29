import React, { useState, useEffect, SyntheticEvent } from 'react';
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
  ComboBox
} from "@carbon/react";

import { TextValueData, sortItems } from '@/utils/multiSelectSortUtils';
import { OpeningSearchFilters } from "@/services/search/openings";
import { statusTypes, dateTypes } from '@/constants/searchConstants';

// Function libs
import { format } from "date-fns";

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

interface SelectEvents {
  selectedItems: TextValueData[];
}

interface SelectEvent {
  selectedItem: IdTextValueData;
}

interface AutocompleteProps {
  id: string,
  label: string,
}

interface AutocompleteComboboxProps{
  selectedItem: AutocompleteProps
}

interface IdTextValueData {
  id: string;
  text: string;
}

const dateFormat = 'yyyy-MM-dd';

const TableToolbarFilter: React.FC<FilterProps> = ({ filters, onFilterChange }) => {

  const [isClientLocationActive,setIsClientLocationActive] = useState(false);
  const [dateKind, setDateKind] = useState<IdTextValueData | null>(null);
  const [dates, setDates] = useState<string[]>([]);
  
  const locations : AutocompleteProps[] = [];
  const clients: AutocompleteProps[] = [];

  // The setTimeout is to avoid carbon complaining about updates on components
  const setValue = (value: Partial<OpeningSearchFilters>) => {
    setTimeout(() => { onFilterChange({...filters, ...value}) },0);
  }

  // This is the autocomplete selection function that is used to select the value based on the text entered
  // This is to cover the problem ComboBoxes have with the selected item when deployed
  const selectBy = (entries: AutocompleteProps[], value: string, field: string) => {
    // We use the useEffect to do a lookup on the entries options
    const selectedItem = entries.find((item: AutocompleteProps) => item.label === value);
    // If there's text typed and we found an entry based on the value selected, we select it
    if(value && selectedItem){
      // This triggers the useEffect to set the value
      onFilterChange({...filters, [field]: selectedItem.id});
    }
  }

  // This is to reset the date fields including dateKind once the date is cleared on the parent
  useEffect(() => {
    const isClearedStart = !Object.keys(filters).some((key) =>
      key.endsWith("DateStart")
    );

    const isClearedEnd = !Object.keys(filters).some((key) =>
      key.endsWith("DateEnd")
    );
  
    if ((dateKind !== null && dates.length > 1)) {
      if(isClearedStart && isClearedEnd){
        setDateKind(null);
      }
    }

  }, [filters]);

  // This is just to reset the dates in case of a dateKind change to empty/null
  useEffect(() =>{
    if(!(dates && dateKind && dateKind.id)) { 
      setValue({
      disturbanceDateEnd: undefined,            
      regenDelayDateEnd: undefined,            
      freeGrowingDateEnd: undefined,            
      updateDateEnd: undefined,            
      disturbanceDateStart: undefined,            
      regenDelayDateStart: undefined,            
      freeGrowingDateStart: undefined,            
      updateDateStart: undefined
      });
      setDates([]);
    }
  },[dateKind]);

  // This use effect updates the filter when date changes. It also updates the date when dateKind changes
  // This is to accomodate the dateKind change when the user selects a new date type, while we keep the dates
  useEffect(() =>{
    if(dates && dateKind && dateKind.id && dates.length > 1) {
      setValue({
        disturbanceDateStart: undefined,            
        regenDelayDateStart: undefined,            
        freeGrowingDateStart: undefined,            
        updateDateStart: undefined,            
        [`${dateKind.id}DateStart`]: format(dates[0],dateFormat),        
        disturbanceDateEnd: undefined,            
        regenDelayDateEnd: undefined,            
        freeGrowingDateEnd: undefined,            
        updateDateEnd: undefined,            
        [`${dateKind.id}DateEnd`]: format(dates[1],dateFormat)
      });
    }    
  },[dateKind,dates]);

  return (
    <div className="advanced-search-dropdown">
      <FlexGrid className="container-fluid advanced-search-container">
        <Row>

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

          <Column sm={4} md={8} lg={16} className="separator-top">
            <FilterableMultiSelect
              titleText="Status"
              label="Enter or choose"
              id="status-multiselect"
              className="multi-select status-multi-select"              
              items={statusTypes}
              itemToString={(item: TextValueData) => (item ? `${item.value} - ${item.text}` : "")}
              selectionFeedback="top-after-reopen"
              onChange={(e: SelectEvents) => { setValue({ statusList: e.selectedItems.map((item: TextValueData) => item.value) }); }}
              selectedItems={ filters.statusList ? statusTypes.filter(item => filters.statusList?.includes(item.value)) : []}
              sortItems={sortItems}
            />
          </Column>

          <Column sm={4} md={8} lg={16} className="separator-top">
            <ComboBox
              id="client-name"
              className="flex-fill"
              allowCustomValue={false}              
              selectedItem={ filters.clientNumber ? clients.filter(item => item.id === filters.clientNumber ) : {}}
              onInputChange={(value: string) => selectBy(locations, value, "clientNumber")}           
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
              onInputChange={(value: string) => selectBy(locations, value, "clientLocationCode")}
              onChange={(item: AutocompleteComboboxProps) => onFilterChange({...filters, clientLocationCode: item.selectedItem.id})}
              itemToElement={(item: AutocompleteProps) => item.label}
              items={locations || [{ id: "", label: "No results found" }]}
              titleText="Location code" />
          </Column>

          <Column sm={4} md={8} lg={16} className="separator-top">
            <TextInput
              id="text-input-2"
              type="text"
              labelText="Cut block"
              value={filters.cutBlockId}
              onChange={(e: TextInputEvent) => { setValue({cutBlockId: e.target.value}) }}
            />
          </Column>

          <Column sm={4} md={8} lg={16} className="separator-top">
            <TextInput
              id="text-input-3"
              labelText="Cutting permit"
              type="text"
              value={filters.cuttingPermitId}
              onChange={(e: TextInputEvent) => { setValue({cuttingPermitId: e.target.value}) }}
            />            
          </Column>

          <Column sm={4} md={8} lg={16} className="separator-top">
            <TextInput
              id="timber-mark-input"
              labelText="Timber mark"
              value={filters.timberMark || ''}
              onChange={(e: TextInputEvent) => { setValue({timberMark: e.target.value}) }}
            />
          </Column>

          <Column sm={4} md={8} lg={16} className="separator-top">
            <ComboBox 
              id="date-type-combobox"
              items={dateTypes.map(item => ({id: item.value, text: item.text}))} 
              itemToString={(item: IdTextValueData) => item ? item.text : ''} 
              titleText="Date type"
              onChange={(item: SelectEvent) => setDateKind(item.selectedItem || null)}
              selectedItem={dateKind}
              />
          </Column>
          
            <Column sm={4} md={8} lg={16}>
              <DatePicker
                datePickerType="range"
                dateFormat="Y-m-d"
                allowInput={true}
                onChange={setDates}
                value={dates}
                maxDate={format(new Date(),dateFormat)}
              >
                <DatePickerInput
                  autoComplete="off"
                  id="start-date-picker-input-id"
                  placeholder={dateFormat}
                  size="md"
                  labelText="Start Date"
                  disabled={!dateKind?.id}
                  value={dateKind?.id ? filters[`${dateKind.id}DateStart` as keyof typeof filters] || '' : ''}
                  
                />
                <DatePickerInput
                  autoComplete="off"
                  id="end-date-picker-input-id"
                  placeholder={dateFormat}
                  size="md"
                  labelText="End Date"
                  disabled={!dateKind?.id}
                  value={dateKind?.id ? filters[`${dateKind.id}DateEnd` as keyof typeof filters] || '' : ''}
                  
                />
              </DatePicker>
            </Column>
          
        </Row>

      </FlexGrid>
    </div>);
}

export default TableToolbarFilter;