import React, { useEffect, useState } from "react";
import "./OpeningsSearchBar.scss";
import { Search, Button, FlexGrid, Row, Column, DismissibleTag } from "@carbon/react";
import * as Icons from "@carbon/icons-react";
import AdvancedSearchDropdown from "../AdvancedSearchDropdown";
import SearchFilterBar from "../SearchFilterBar";
import { useOpeningsSearch } from "../../../../contexts/search/OpeningsSearch";
import { countActiveFilters } from "../../../../utils/searchUtils";

interface IOpeningsSearchBar {
  onSearchClick: Function;
}

const OpeningsSearchBar: React.FC<IOpeningsSearchBar> = ({
  onSearchClick
}) => {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [showFilters, setShowFilters] = useState<boolean>(false);
  const [searchInput, setSearchInput] = useState<string>("");
  const [filtersCount, setFiltersCount] = useState<number>(0);
  const [filtersList, setFiltersList] = useState(null);
  const { filters, clearFilters, searchTerm, setSearchTerm } = useOpeningsSearch();

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const toggleShowFilters = () => {
    setShowFilters(!showFilters);
  };

  const handleSearchClick = () => {
    onSearchClick();
    setIsOpen(false);
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setSearchTerm(value);
  };

  const handleFiltersChanged = () => {
    const activeFiltersCount = countActiveFilters(filters);
    setFiltersCount(activeFiltersCount); // Update the state with the active filters count
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
                  value={searchTerm}
                />
            </Column>

            <Column lg={4} max={4} className="p-0">
              <div className="advanced-search-field ms-lg-1" onClick={toggleDropdown}>
                {filtersCount > 0 ? (
                  <DismissibleTag
                    className="mx-1"
                    type="high-contrast"
                    text={"+" + filtersCount}
                    onClose={() => clearFilters()}
                  >
                  </DismissibleTag>
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
