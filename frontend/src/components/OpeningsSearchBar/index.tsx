import React, { useState } from "react";
import "./OpeningsSearchBar.scss";
import { Search, Button } from "@carbon/react";
import * as Icons from "@carbon/icons-react";
import AdvancedSearchDropdown from "../AdvancedSearchDropdown";
import SearchFilterBar from "../SearchFilterBar";

interface IOpeningsSearchBar {
  toggleFiltersApplied : Function
}
const OpeningsSearchBar: React.FC<IOpeningsSearchBar> = ({
  toggleFiltersApplied
}) => {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [showFilters, setShowFilters] = useState<boolean>(false);
  const [showSpatial, setShowSpatial] = useState<boolean>(false);

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const toggleShowFilters = () => {
    console.log("toggle filter called");
    setShowFilters(!showFilters);
    console.log("the new value is:" + showFilters);
  };

  return (
    <div>
      <div className="openings-searchbar-container row align-content-center">
        <div className="col-10 p-0">
          <div className="d-flex flex-row">
            <Search
              size="md"
              placeholder="Search by opening ID, opening number, timber mark or file ID"
              labelText="Search"
              closeButtonLabelText="Clear search input"
              id={`search-1`}
              onChange={() => {}}
              onKeyDown={() => {}}
            />
            <Button
              className="toggle-dropdown-button ms-2"
              renderIcon={isOpen ? Icons.ChevronSortUp : Icons.ChevronSortDown}
              type="button"
              size="md"
              onClick={toggleDropdown}
            >
              Advanced Search
            </Button>
          </div>
          {isOpen && (
            <AdvancedSearchDropdown toggleShowFilters={toggleShowFilters} />
          )}
          {showFilters && <SearchFilterBar />}
        </div>
        <div className="col-2 p-0">
          <Button
            className="search-button ms-2"
            renderIcon={Icons.Search}
            type="button"
            size="md"
            onClick={() => toggleFiltersApplied()}
          >
            Search
          </Button>
        </div>
      </div>
    </div>
  );
};

export default OpeningsSearchBar;
