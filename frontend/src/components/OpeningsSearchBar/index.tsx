import React, { useState } from "react";
import "./OpeningsSearchBar.scss";
import { Search, Button } from "@carbon/react";
import * as Icons from "@carbon/icons-react";
import AdvancedSearchDropdown from "../AdvancedSearchDropdown";
import OpeningsMap from "../OpeningsMap";
import SearchFilterBar from "../SearchFilterBar";
import TableSkeleton from "../TableSkeleton";
import SearchScreenDataTable from "../SearchScreenDataTable";
import { headers, rows } from "../SearchScreenDataTable/testData";
import { RecentOpening } from "../../types/RecentOpening";

const OpeningsSearchBar: React.FC = () => {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [showFilters, setShowFilters] = useState<boolean>(false);
  const [showSpatial, setShowSpatial] = useState<boolean>(false);
  const [openingRows, setOpeningRows] = useState<RecentOpening[]>([]);
  const [loadId, setLoadId] = useState<number | null>(null);
  const [openingPolygonNotFound, setOpeningPolygonNotFound] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(false);

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
            onClick={() => console.log("search clicked")}
          >
            Search
          </Button>
        </div>
      </div>
      {showSpatial ? (
        <div className="search-spatial-container row p-0">
          <div className="leaflet-container">
            <OpeningsMap
              openingId={null}
              setOpeningPolygonNotFound={setOpeningPolygonNotFound}
            />
          </div>
        </div>
      ) : null}
      <div className="row">
        {loading ? (
            <TableSkeleton headers={headers} />
          ) : (
            <SearchScreenDataTable
              headers={headers}
              rows={rows}
              setOpeningId={setLoadId}
              showSpatial={showSpatial}
            />
          )}
      </div>
    </div>
  );
};

export default OpeningsSearchBar;
