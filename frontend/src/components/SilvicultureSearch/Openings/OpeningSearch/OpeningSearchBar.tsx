import React, { useEffect, useState } from "react";
import { Button, Checkbox, Column, Search, TableToolbar, TableToolbarAction, TableToolbarContent, TableToolbarMenu } from "@carbon/react";
import {
  FilterEdit as FilterIcon,
  Search as SearchIcon,
  Column as ColumnIcon
} from "@carbon/icons-react";

import { OPENING_STATUS_LIST } from "../../../../constants";
import CustomMultiSelect from "../../../CustomMultiSelect";
import { codeDescriptionToDisplayText, filterCodeDescriptionItems, MultiSelectEvent } from "../../../../utils/multiSelectUtils";
import { OpeningSearchFilterType } from "./definitions";
import CodeDescriptionDto from "../../../../types/CodeDescriptionType";
import { TextInputEvent } from "../../../../types/GeneralTypes";
import { OpendingHeaderKeyType, OpeningHeaderType } from "../../../../types/TableHeader";
import useBreakpoint from "../../../../hooks/UseBreakpoint";

type OpeningSearchBarProps = {
  headers: OpeningHeaderType[],
  setHeaders: React.Dispatch<React.SetStateAction<OpeningHeaderType[]>>,
  filters: OpeningSearchFilterType,
  setFilters: React.Dispatch<React.SetStateAction<OpeningSearchFilterType>>,
  categories: CodeDescriptionDto[],
  orgUnits: CodeDescriptionDto[],
  handleSearch: () => void,
  totalResults: number,
  showMap: boolean,
  setShowMap: React.Dispatch<React.SetStateAction<boolean>>
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


  // Generic handler for string-based filters
  const handleStringChange = (key: keyof OpeningSearchFilterType) => (event: TextInputEvent) => {
    setFilters((prev) => ({
      ...prev,
      [key]: event.target.value
    }));
  };

  // Generic handler for boolean-based filters
  const handleBooleanChange = (key: keyof OpeningSearchFilterType) => (value: boolean) => {
    setFilters((prev) => ({
      ...prev,
      [key]: value
    }));
  };

  // Generic handler for CodeDescriptionDto array filters
  const handleMultiSelectChange = (key: keyof OpeningSearchFilterType) => (event: MultiSelectEvent) => {
    setFilters((prev) => ({
      ...prev,
      [key]: event.selectedItems
    }));
  };


  const SearchButton = () => (
    <Button
      className="search-button"
      renderIcon={SearchIcon}
      iconDescription="Search"
      type="button"
      size="md"
      onClick={handleSearch}
    >
      Search
    </Button>
  )

  const AdvancedSearchButton = ({ hasIconOnly }: { hasIconOnly?: boolean }) => (
    <Button
      className="advanced-search-button"
      renderIcon={FilterIcon}
      iconDescription="Advanced Search"
      type="button"
      size="md"
      kind="tertiary"
      hasIconOnly={hasIconOnly}
    >
      Advanced Search
    </Button>
  )

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

  const toggleColumn = (key: OpendingHeaderKeyType) => {
    if (key !== 'openingId' && key !== 'actions') {
      setHeaders((prevHeaders) =>
        prevHeaders.map((header) =>
          header.key === key ? { ...header, selected: !header.selected } : header
        )
      );
    }
  };

  return (
    <>
      {/* Main Search */}
      <Column className="main-search-term-col" sm={4} md={8} lg={16} max={5}>
        <Search
          size="md"
          placeholder="Search by opening ID, opening number or file ID"
          labelText="Search"
          closeButtonLabelText="Clear search input"
          id="main-search-term-input"
          onBlur={handleStringChange('mainSearchTerm')}
          defaultValue={filters.mainSearchTerm}
        />
      </Column>

      {/* Category multiselect */}
      <Column sm={4} md={2} lg={4} max={3}>
        <CustomMultiSelect
          placeholder="Category"
          id="category-multiselect"
          items={categories}
          itemToString={codeDescriptionToDisplayText}
          selectionFeedback="top-after-reopen"
          filterItems={filterCodeDescriptionItems}
          onChange={handleMultiSelectChange('category')}
        />
      </Column>

      {/* Org unit multiselect */}
      <Column sm={4} md={2} lg={4} max={3}>
        <CustomMultiSelect
          placeholder="Org unit"
          id="orgunit-multiselect"
          items={orgUnits}
          itemToString={codeDescriptionToDisplayText}
          selectionFeedback="top-after-reopen"
          filterItems={filterCodeDescriptionItems}
          onChange={handleMultiSelectChange('orgUnit')}
          initialSelectedItems={filters.orgUnit}
        />
      </Column>

      {/* Status multiselect */}
      <Column sm={4} md={2} lg={4} max={3}>
        <CustomMultiSelect
          id="status-multiselect"
          placeholder="Status"
          items={OPENING_STATUS_LIST}
          itemToString={codeDescriptionToDisplayText}
          selectionFeedback="top-after-reopen"
          filterItems={filterCodeDescriptionItems}
          onChange={handleMultiSelectChange('statusList')}
        />
      </Column>

      {/* Advanced Search and Search buttons, hidden on small */}
      <Column sm={0} md={2} lg={4} max={2}>
        <div className="search-buttons-container">
          <AdvancedSearchButton hasIconOnly />
          <SearchButton />
        </div>
      </Column>

      {/* Small Screen's Advanced Search button */}
      <Column className="search-col-sm" sm={4} md={0}>
        <AdvancedSearchButton />
      </Column>

      {/* Small Screen's Search button */}
      <Column className="search-col-sm" sm={4} md={0}>
        <SearchButton />
      </Column>

      {/* Action button row, hidden when there's no data */}
      {
        totalResults > 0
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

    </>
  )

}

export default OpeningSearchBar;
