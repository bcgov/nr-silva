import React, { useEffect, useState } from "react";
import { Button, Checkbox, CheckboxGroup, Column, ComposedModal, DatePicker, DatePickerInput, Dropdown, Grid, Modal, ModalBody, ModalFooter, ModalHeader, Search, TableToolbar, TableToolbarAction, TableToolbarContent, TableToolbarMenu, TextInput } from "@carbon/react";
import {
  FilterEdit as FilterIcon,
  Search as SearchIcon,
  Column as ColumnIcon
} from "@carbon/icons-react";

import { API_DATE_FORMAT, DATE_PICKER_FORMAT, DATE_TYPE_LIST, OPENING_STATUS_LIST } from "../../../../constants";
import CustomMultiSelect from "../../../CustomMultiSelect";
import { codeDescriptionToDisplayText, MultiSelectEvent } from "../../../../utils/multiSelectUtils";
import { OpeningSearchFilterType } from "./definitions";
import CodeDescriptionDto from "../../../../types/CodeDescriptionType";
import { CheckBoxEvent, TextInputEvent } from "../../../../types/GeneralTypes";
import { OpendingHeaderKeyType, OpeningHeaderType } from "../../../../types/TableHeader";
import useBreakpoint from "../../../../hooks/UseBreakpoint";
import ForestClientInput from "../../../ForestClientInput";
import { DateTime } from "luxon";
import { MAX_TEXT_INPUT_LEN } from "./constants";

type OpeningSearchBarProps = {
  headers: OpeningHeaderType[],
  setHeaders: React.Dispatch<React.SetStateAction<OpeningHeaderType[]>>,
  filters: OpeningSearchFilterType,
  setFilters: React.Dispatch<React.SetStateAction<OpeningSearchFilterType>>,
  categories: CodeDescriptionDto[],
  orgUnits: CodeDescriptionDto[],
  handleSearch: () => void,
  totalResults: number | undefined,
  showMap: boolean,
  setShowMap: React.Dispatch<React.SetStateAction<boolean>>
}

type CustomInputProp = {
  id: string,
  label?: string
}


const OpeningSearchBar = ({
  showMap,
  setShowMap,
  headers,
  setHeaders,
  filters,
  setFilters,
  categories,
  orgUnits,
  handleSearch,
  totalResults
}: OpeningSearchBarProps
) => {
  const breakpoint = useBreakpoint();
  const [isAdvancedSearchOpen, setIsAdvancedSearchOpen] = useState<boolean>(false);
  const [selectedDateType, setSelectedDateType] = useState<CodeDescriptionDto | null>();

  /**
   * A workaround to add text to Carbon's `TableToolbarMenu` button since it does not natively support text alongside the icon.
   * This approach modifies the DOM to insert the button text before the icon while preserving the usability of `TableToolbarMenu`,
   * which simplifies handling popover content.
   *
   * @effect Runs whenever the `breakpoint` or `totalResults` value changes.
   */
  useEffect(() => {
    const menuButton = document.querySelector(".column-menu-button");

    if (menuButton) {
      // Remove existing text if already added (prevents duplication)
      const existingText = menuButton.querySelector(".column-menu-text");
      if (existingText) {
        existingText.remove();
      }

      // If the breakpoint is not 'sm', insert the text before the SVG icon
      if (breakpoint !== "sm") {
        const textSpan = document.createElement("span");
        textSpan.className = "column-menu-text";
        textSpan.textContent = "Edit columns";

        const icon = menuButton.querySelector("svg");
        if (icon) {
          menuButton.insertBefore(textSpan, icon);
        } else {
          menuButton.appendChild(textSpan);
        }
      }
    }
  }, [breakpoint, totalResults]);

  // Generic handler for string-based filters
  const handleStringChange = (key: keyof OpeningSearchFilterType) => (event: TextInputEvent) => {
    setFilters((prev) => ({
      ...prev,
      [key]: event.target.value
    }));
  };

  // Generic handler for boolean-based filters
  const handleBooleanChange = (key: keyof OpeningSearchFilterType): CheckBoxEvent => (_evt, data) => {
    setFilters((prev) => ({
      ...prev,
      [key]: data.checked,
    }));
  };

  // Generic handler for CodeDescriptionDto array filters
  const handleMultiSelectChange = (key: keyof OpeningSearchFilterType) => (event: MultiSelectEvent) => {
    setFilters((prev) => ({
      ...prev,
      [key]: event.selectedItems,
    }));
  }

  const handleDateTypeChange = (dateType: CodeDescriptionDto | null) => {
    setSelectedDateType(dateType)
    setFilters((prev) => ({
      ...prev,
      disturbanceDateStart: undefined,
      disturbanceDateEnd: undefined,

      regenDelayDateStart: undefined,
      regenDelayDateEnd: undefined,

      freeGrowingDateStart: undefined,
      freeGrowingDateEnd: undefined,

      updateDateStart: undefined,
      updateDateEnd: undefined,
    }))
  }

  const handleDateChange = (isStartDate: boolean) => (dates?: (Date)[]) => {
    if (!selectedDateType || !dates) return;

    const formattedDate = dates.length ? DateTime.fromJSDate(dates[0]).toFormat(API_DATE_FORMAT) : "";

    const key = `${selectedDateType.code}${isStartDate ? "DateStart" : "DateEnd"}` as keyof OpeningSearchFilterType;

    setFilters((prev) => {
      // Prevent unnecessary updates
      if (prev[key] === formattedDate) {
        return prev;
      }

      return {
        ...prev,
        [key]: formattedDate,
      };
    });
  };


  const toggleColumn = (key: OpendingHeaderKeyType) => {
    if (key !== 'openingId' && key !== 'actions') {
      setHeaders((prevHeaders) =>
        prevHeaders.map((header) =>
          header.key === key ? { ...header, selected: !header.selected } : header
        )
      );
    }
  };

  const getStartMaxDate = () => {
    if (!selectedDateType) {
      return undefined;
    }
    const type = selectedDateType.code;
    const endDateKey = `${type}DateEnd` as keyof OpeningSearchFilterType;

    const maxDate = filters[endDateKey]
      ? DateTime.fromFormat(filters[endDateKey] as string, API_DATE_FORMAT).toFormat(DATE_PICKER_FORMAT)
      : DateTime.now().toFormat(DATE_PICKER_FORMAT);

    return maxDate;
  }

  const getEndMinDate = () => {
    if (!selectedDateType) {
      return undefined;
    }
    const type = selectedDateType.code;
    const startDateKey = `${type}DateStart` as keyof OpeningSearchFilterType;

    const minDate = filters[startDateKey]
      ? DateTime.fromFormat(filters[startDateKey] as string, API_DATE_FORMAT).toFormat(DATE_PICKER_FORMAT)
      : undefined;

    return minDate;
  }

  const getStartDateValue = () => {
    if (!selectedDateType) {
      return undefined;
    }
    const type = selectedDateType.code;
    const startDateKey = `${type}DateStart` as keyof OpeningSearchFilterType;
    if (filters[startDateKey]) {
      return DateTime.fromFormat(filters[startDateKey] as string, API_DATE_FORMAT).toFormat(DATE_PICKER_FORMAT)
    }
    return undefined;
  }

  const getEndDateValue = () => {
    if (!selectedDateType) {
      return undefined;
    }
    const type = selectedDateType.code;
    const endDateKey = `${type}EndStart` as keyof OpeningSearchFilterType;
    if (filters[endDateKey]) {
      return DateTime.fromFormat(filters[endDateKey] as string, API_DATE_FORMAT).toFormat(DATE_PICKER_FORMAT)
    }
    return undefined;
  }

  // Child components, shared between search bar and advanced modal
  const SearchButton = ({ size }: { size: 'md' | 'lg' }) => (
    <Button
      className="search-button"
      renderIcon={SearchIcon}
      iconDescription="Search"
      type="button"
      size={size}
      onClick={handleSearch}
    >
      Search
    </Button>
  )

  const SearchInput = ({ id }: CustomInputProp) => (
    <Search
      size="md"
      placeholder="Search by opening ID, opening number or file ID"
      labelText="Search"
      closeButtonLabelText="Clear search input"
      id={id}
      onBlur={handleStringChange('mainSearchTerm')}
      defaultValue={filters.mainSearchTerm}
    />
  );

  const openAdvancedSearch = () => {
    setIsAdvancedSearchOpen(true);
  }

  const AdvancedSearchButton = ({ hasIconOnly }: { hasIconOnly?: boolean }) => (
    <Button
      className="advanced-search-button"
      renderIcon={FilterIcon}
      iconDescription="Advanced Search"
      type="button"
      size="md"
      kind="tertiary"
      hasIconOnly={hasIconOnly}
      onClick={openAdvancedSearch}
    >
      Advanced Search
    </Button>
  );

  return (
    <>
      {/* Main Search */}
      <Column className="main-search-term-col" sm={4} md={8} lg={16} max={5}>
        <SearchInput id="main-search-term-input" />
      </Column>

      {/* Category multiselect */}
      <Column sm={4} md={2} lg={4} max={3}>
        <CustomMultiSelect
          placeholder="Category"
          id="category-multi-select"
          items={categories}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('category')}
          selectedItems={filters.category}
        />
      </Column>

      {/* Org unit multiselect */}
      <Column sm={4} md={2} lg={4} max={3}>
        <CustomMultiSelect
          placeholder="Org unit"
          id="orgunit-multiselect"
          items={orgUnits}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('orgUnit')}
          selectedItems={filters.orgUnit}
        />
      </Column>

      {/* Status multiselect */}
      <Column sm={4} md={2} lg={4} max={3}>
        <CustomMultiSelect
          id="status-multiselect"
          placeholder="Status"
          items={OPENING_STATUS_LIST}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('statusList')}
          selectedItems={filters.statusList}
        />
      </Column>

      {/* Advanced Search and Search buttons, hidden on small */}
      <Column sm={0} md={2} lg={4} max={2}>
        <div className="search-buttons-container">
          <AdvancedSearchButton hasIconOnly />
          <SearchButton size="md" />
        </div>
      </Column>

      {/* Small Screen's Advanced Search button */}
      <Column className="search-col-sm" sm={4} md={0}>
        <AdvancedSearchButton />
      </Column>

      {/* Small Screen's Search button */}
      <Column className="search-col-sm" sm={4} md={0}>
        <SearchButton size="md" />
      </Column>

      {/* Action button row, hidden until a search is completed */}
      {
        totalResults !== undefined
          ? (
            <Column className="table-toolbar-col  subgrid-full-width-no-row-gap-col" sm={4} md={8} lg={16}>
              <TableToolbar>
                <TableToolbarContent>
                  <div className="total-rows-display">
                    Total search results: {totalResults}
                  </div>
                  <div className="action-button-group">
                    <Button
                      className="map-button"
                      renderIcon={SearchIcon}
                      iconDescription="Toggle map"
                      type="button"
                      size="lg"
                      kind="ghost"
                      hasIconOnly={breakpoint === 'sm'}
                      onClick={() => setShowMap(!showMap)}
                    >
                      {showMap ? 'Hide' : 'Show'} map
                    </Button>
                    <TableToolbarMenu
                      className="action-menu-button column-menu-button"
                      renderIcon={ColumnIcon}
                      iconDescription="Edit columns"
                      menuOptionsClass="opening-search-action-menu-option"
                    >
                      <div className="opening-search-action-menu-option-item">
                        <div className="helper-text">
                          Select columns you want to see
                        </div>
                        {
                          headers.map((header) => (
                            header.key !== 'actions'
                              ? (
                                <Checkbox
                                  key={header.key}
                                  className="column-checkbox"
                                  id={`${header.key}-checkbox`}
                                  labelText={header.header}
                                  checked={header.selected}
                                  onChange={() => toggleColumn(header.key)}
                                  readOnly={header.key === 'openingId'}
                                />
                              )
                              : null
                          ))
                        }
                      </div>
                    </TableToolbarMenu>
                  </div>
                </TableToolbarContent>
              </TableToolbar>
            </Column>
          )
          : null
      }

      {/* Modal Section, conatins some duplicated inputs. These input components can't be made into their own due to behaviour issues. */}
      <ComposedModal
        className="advanced-search-modal"
        open={isAdvancedSearchOpen}
        onClose={() => setIsAdvancedSearchOpen(false)}
        size="lg"
        selectorPrimaryFocus=".advanced-search-body"
        preventCloseOnClickOutside={false}
      >
        <ModalHeader title="Advanced search" />
        <ModalBody className="advanced-search-body">
          <Grid className="advanced-search-grid">
            {/* Main Search Term */}
            <Column sm={4} md={8} lg={16}>
              <SearchInput id="advanced-search-input" />
            </Column>

            {/* Opening filters */}
            <Column sm={4} md={8} lg={16}>
              <CheckboxGroup
                legendText="Opening filters"
                orientation="horizontal"
              >
                <Checkbox
                  id="created-by-me-checkbox"
                  labelText="Openings created by me"
                  checked={filters.myOpenings}
                  onChange={handleBooleanChange('myOpenings')}
                />
                <Checkbox
                  id="frpa-checkbox"
                  labelText="FRPA section 108"
                  checked={filters.submittedToFrpa}
                  onChange={handleBooleanChange('submittedToFrpa')}
                />
              </CheckboxGroup>
            </Column>

            {/* Category */}
            <Column sm={4} md={4} lg={8}>
              <CustomMultiSelect
                placeholder="Category"
                id={'advanced-category-multiselect'}
                titleText="Category"
                items={categories}
                itemToString={codeDescriptionToDisplayText}
                onChange={handleMultiSelectChange('category')}
                selectedItems={filters.category}
              />
            </Column>

            {/* Org unit */}
            <Column sm={4} md={4} lg={8}>
              <CustomMultiSelect
                placeholder="Org unit"
                id="advanced-orgunit-multiselect"
                titleText="Org unit"
                items={orgUnits}
                itemToString={codeDescriptionToDisplayText}
                onChange={handleMultiSelectChange('orgUnit')}
                selectedItems={filters.orgUnit}
              />
            </Column>

            {/* Status */}
            <Column sm={4} md={4} lg={8}>
              <CustomMultiSelect
                id="status-multiselect"
                placeholder="Status"
                titleText="Status"
                items={OPENING_STATUS_LIST}
                itemToString={codeDescriptionToDisplayText}
                onChange={handleMultiSelectChange('statusList')}
                selectedItems={filters.statusList}
              />
            </Column>

            {/* Client and location code */}
            <Column sm={4} md={4} lg={8}>
              <ForestClientInput
                clientInputId="opening-advanced-search-client-input"
                locationInputId="opening-advanced-location-code-input"
                setClientNumber={handleStringChange('clientNumber')}
                setClientLocationCode={handleStringChange('clientLocationCode')}
              />
            </Column>
            <Column sm={4} md={4} lg={8}>
              <div className="date-filter-container">
                <Dropdown
                  id="date-type-dropdown"
                  titleText="Date type"
                  label=""
                  items={DATE_TYPE_LIST}
                  initialSelectedItem={selectedDateType}
                  itemToString={(item) => item?.description ?? "Unknown"}
                  onChange={(data) => handleDateTypeChange(data.selectedItem)}
                />
                {/* Start date */}
                <DatePicker
                  className="advanced-date-picker"
                  datePickerType="single"
                  dateFormat="Y/m/d"
                  allowInput
                  maxDate={getStartMaxDate()}
                  onChange={handleDateChange(true)}
                  readOnly={!selectedDateType}
                  value={getStartDateValue()}
                >
                  <DatePickerInput
                    id="start-date-picker-input-id"
                    size="md"
                    labelText="Start Date"
                    placeholder="yyyy/mm/dd"
                  />
                </DatePicker>
                {/* End date */}
                <DatePicker
                  className="advanced-date-picker"
                  datePickerType="single"
                  dateFormat="Y/m/d"
                  allowInput
                  minDate={getEndMinDate()}
                  maxDate={DateTime.now().toFormat(DATE_PICKER_FORMAT)}
                  onChange={handleDateChange(false)}
                  readOnly={!selectedDateType}
                  value={getEndDateValue()}
                >
                  <DatePickerInput
                    id="end-date-picker-input-id"
                    size="md"
                    labelText="End Date"
                    placeholder="yyyy/mm/dd"
                  />
                </DatePicker>
              </div>
            </Column>

            {/* Cut block */}
            <Column sm={4} md={4} lg={8}>
              <TextInput
                className="advanced-text-input"
                id="cut-block-text-input"
                type="text"
                labelText="Cut block"
                defaultValue={filters.cutBlockId}
                onBlur={handleStringChange('cutBlockId')}
                maxLength={MAX_TEXT_INPUT_LEN}
              />
            </Column>

            {/* Cutting permit */}
            <Column sm={4} md={4} lg={8}>
              <TextInput
                className="advanced-text-input"
                id="cutting-permit-text-input"
                type="text"
                labelText="Cutting permit"
                defaultValue={filters.cuttingPermitId}
                onBlur={handleStringChange('cuttingPermitId')}
                maxLength={MAX_TEXT_INPUT_LEN}
              />
            </Column>

            {/* Timber mark */}
            <Column sm={4} md={4} lg={8}>
              <TextInput
                className="advanced-text-input"
                id="timber-mark-text-input"
                type="text"
                labelText="Timber mark"
                defaultValue={filters.timberMark}
                onBlur={handleStringChange('timberMark')}
                maxLength={MAX_TEXT_INPUT_LEN}
              />
            </Column>
          </Grid>
        </ModalBody>
        <ModalFooter>
          <Button kind="secondary" onClick={() => setIsAdvancedSearchOpen(false)}>
            Cancel
          </Button>
          <SearchButton size="lg" />
        </ModalFooter>
      </ComposedModal>
    </>
  )
}

export default OpeningSearchBar;
