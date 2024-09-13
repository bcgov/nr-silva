import React, { useEffect, useState } from "react";
import "./OpeningsSearchBar.scss";
import { Search, Button, Tag, FlexGrid, Row, Column } from "@carbon/react";
import * as Icons from "@carbon/icons-react";
import AdvancedSearchDropdown from "../AdvancedSearchDropdown";
import SearchFilterBar from "../SearchFilterBar";
import { useOpeningsSearch } from "../../../../contexts/search/OpeningsSearch";

interface IOpeningsSearchBar {
  onSearchInputChange: (searchInput: string) => void; // New prop
  onSearchClick: Function;
}

const OpeningsSearchBar: React.FC<IOpeningsSearchBar> = ({
  onSearchInputChange,
  onSearchClick,
}) => {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [showFilters, setShowFilters] = useState<boolean>(false);
  const [searchInput, setSearchInput] = useState<string>("");
  const [filtersCount, setFiltersCount] = useState<number>(0);
  const [filtersList, setFiltersList] = useState(null);
  const { filters, clearFilters } = useOpeningsSearch();

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
  useEffect(() => {
    handleFiltersChanged();
  }, [filters]);

  return (
    <div>
      <div>
        <FlexGrid className="openings-searchbar-container">
          <Row>
            <Column lg={8} xl={6} max={10} className="p-0 mb-2 mb-lg-0">
                <Search
                  size="md"
                  placeholder="Search by opening ID, opening number, timber mark or file ID"
                  labelText="Search"
                  closeButtonLabelText="Clear search input"
                  id={`search-1`}
                  onChange={handleInputChange} // Handle input change
                  onKeyDown={() => {}}
                />
            </Column>

            <Column lg={4} max={4} className="p-0">
              <div className="advanced-search-field ms-lg-1" onClick={toggleDropdown}>
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
            </Column>
            
            <Column sm={0} lg={1} max={1} className="p-0">
              <Button
                className="search-button ms-xl-2"
                renderIcon={Icons.Search}
                type="button"
                size="md"
                onClick={handleSearchClick}
              >
                Search
              </Button>
            </Column>

          </Row>

          <Row>
            <Column lg={14} className="p-0">
              <div className={isOpen ? "d-block" : "d-none"}>
                <AdvancedSearchDropdown toggleShowFilters={toggleShowFilters} />
              </div>
              {filtersCount > 0 && <SearchFilterBar filters={filtersList} />}
            </Column>

            <Column lg={0} className="p-0 mt-2">
              <Button
                className="search-button ms-xl-2"
                renderIcon={Icons.Search}
                type="button"
                size="md"
                onClick={handleSearchClick}
              >
                Search
              </Button>
            </Column>
          </Row>

        </FlexGrid>
      </div>
    </div>
  );
};

export default OpeningsSearchBar;
