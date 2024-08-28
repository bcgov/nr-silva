import React, { useState } from "react";
import "./OpeningsSearchBar.scss";
import { Search, Button } from "@carbon/react";
import * as Icons from "@carbon/icons-react";
import AdvancedSearchDropdown from "../AdvancedSearchDropdown";
import SearchFilterBar from "../SearchFilterBar";

interface IOpeningsSearchBar {
  onSearch: (searchData: any) => void;
  onSearchInputChange: (searchInput: string) => void; // New prop
  onSearchClick: Function
}

const OpeningsSearchBar: React.FC<IOpeningsSearchBar> = ({ onSearch, onSearchInputChange, onSearchClick }) => {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [showFilters, setShowFilters] = useState<boolean>(false);
  const [searchInput, setSearchInput] = useState<string>("");

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const toggleShowFilters = () => {
    setShowFilters(!showFilters);
  };

  const handleSearchClick = () => {
    onSearch({ searchInput });
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setSearchInput(value);
    onSearchInputChange(value); // Call the function to update the search input in the parent component
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
              onChange={handleInputChange} // Handle input change
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
            <div className={isOpen ? 'd-block' : 'd-none'}>
            <AdvancedSearchDropdown
              toggleShowFilters={toggleShowFilters}
              onSearch={onSearch}
            />
            </div>
          {showFilters && <SearchFilterBar />}
        </div>
        <div className="col-2 p-0">
          <Button
            className="search-button ms-2"
            renderIcon={Icons.Search}
            type="button"
            size="md"
            onClick={handleSearchClick}
          >
            Search
          </Button>
        </div>
      </div>
    </div>
  );
};

export default OpeningsSearchBar;
