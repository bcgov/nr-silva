import React, { useEffect, useState } from "react";
import "./OpeningsSearchBar.scss";
import { Search, Button, Tag } from "@carbon/react";
import * as Icons from "@carbon/icons-react";
import AdvancedSearchDropdown from "../AdvancedSearchDropdown";
import SearchFilterBar from "../SearchFilterBar";
import { useOpeningsSearch } from "../../contexts/search/OpeningsSearch";

interface IOpeningsSearchBar {
  onSearchInputChange: (searchInput: string) => void; // New prop
  onSearchClick: Function
}

const OpeningsSearchBar: React.FC<IOpeningsSearchBar> = ({ onSearchInputChange, onSearchClick }) => {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [showFilters, setShowFilters] = useState<boolean>(false);
  const [searchInput, setSearchInput] = useState<string>("");
  const [filtersCount, setFiltersCount] = useState<number>(0);
  const [filtersList, setFiltersList] = useState(null);
  const {filters, clearFilters} = useOpeningsSearch();

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const toggleShowFilters = () => {
    setShowFilters(!showFilters);
  };

  const handleSearchClick = () => {
    onSearchClick();
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setSearchInput(value);
    onSearchInputChange(value); // Call the function to update the search input in the parent component
  };

  const countActiveFilters = (filters: any): number => {
    let count = 0;
  
    Object.keys(filters).forEach((key) => {
      const value = filters[key];
  
      // Check if the value is an array (e.g., for checkboxes)
      if (Array.isArray(value)) {
        count += value.filter((item) => item !== "").length; // Count non-empty values in the array
      } else if (value !== null && value !== "" && value !== "Option 1") {
        // Increment the count for non-default, non-null, and non-empty values
        count += 1;
      }
    });
  
    return count;
  };
  
  const handleFiltersChanged = () => {
    const activeFiltersCount = countActiveFilters(filters);
    setFiltersCount(activeFiltersCount); // Update the state with the active filters count
    console.log("Number of active filters:", activeFiltersCount);
    setFiltersList(filters);
  };
  useEffect(()=>{
    handleFiltersChanged();
  },[filters])

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

            <div className="advanced-search-field" onClick={toggleDropdown}>
              {filtersCount > 0 ? (
                <Tag
                  filter
                  className="mx-1"
                  type="high-contrast"
                  title="Clear Filter"
                  onClose={() => clearFilters()}
                >
                  {"+" + filtersCount}
                </Tag>
              ) : null}
              <p className={filtersCount > 0 ? "text-active" : ""}>
                Advanced Search
              </p>
              {isOpen ? <Icons.ChevronSortUp /> : <Icons.ChevronSortDown />}
            </div>
          </div>

          <div className={isOpen ? "d-block" : "d-none"}>
            <AdvancedSearchDropdown
              toggleShowFilters={toggleShowFilters}
            />
          </div>
          {filtersCount > 0  && <SearchFilterBar filters={filtersList} />}
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
