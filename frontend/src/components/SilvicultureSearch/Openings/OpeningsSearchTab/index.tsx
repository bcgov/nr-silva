import React, { useState, useEffect, useContext } from "react";

// Styles
import "./styles.scss";

// Utility functions
import { searchScreenColumns } from "../../../../constants/tableConstants";
import { useOpeningsQuery } from "../../../../services/queries/search/openingQueries";
import { useOpeningsSearch } from "../../../../contexts/search/OpeningsSearch";
import { countActiveFilters } from "../../../../utils/searchUtils";

// Types
import { ITableHeader } from "../../../../types/TableHeader";
import { OpeningFilters } from "../../../../services/search/openings";

// Local components
import EmptySection from "../../../EmptySection";
import OpeningsSearchBar from "../OpeningsSearchBar";
import TableSkeleton from "../../../TableSkeleton";
import SearchScreenDataTable from "../SearchScreenDataTable";
import OpeningsMap from "../../../OpeningsMap";
import PaginationContext from "../../../../contexts/PaginationContext";

const OpeningsSearchTab: React.FC = () => {
  const [showSpatial, setShowSpatial] = useState<boolean>(false);
  const [openingPolygonNotFound, setOpeningPolygonNotFound] =
    useState<boolean>(false);
  const [filtersApplied, setFiltersApplied] = useState<boolean>(false);
  const [searchParams, setSearchParams] = useState<Record<string, any>>({});
  const [finalParams, setFinalParams] = useState<Record<string, any>>({}); // Store params for query after search
  const [isSearchTriggered, setIsSearchTriggered] = useState<boolean>(false); // Trigger state for search
  const [isNoFilterSearch, setIsNoFilterSearch] = useState<boolean>(false); // Handles the notification for no filters applied
  const { currentPage, itemsPerPage } = useContext(PaginationContext);
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [hasExternalParams, setHasExternalParams] = useState<boolean>(false);

  const [headers, setHeaders] = useState<ITableHeader[]>(searchScreenColumns);

  // Only fetch when search is triggered and with finalParams
  const { data, isFetching } = useOpeningsQuery(finalParams, isSearchTriggered);
  const { filters, searchTerm, setFilters } = useOpeningsSearch();

  const toggleSpatial = () => {
    setShowSpatial(!showSpatial);
  };

  const toggleFiltersApplied = () => {
    setFiltersApplied(!filtersApplied);
  };

  const hasFilters = countActiveFilters(filters) > 0 || searchTerm.length > 0;

  const handleSearch = () => {
    setIsNoFilterSearch(!hasFilters);

    if (hasFilters) {
      toggleFiltersApplied();
      setFiltersApplied(true); // Set filters as applied to show results
      setFinalParams({
        ...searchParams,
        ...filters,
        page: currentPage,
        perPage: itemsPerPage,
      }); // Only update finalParams on search
      setIsSearchTriggered(true); // Trigger the search
    }
  };

  const handleFiltersChanged = () => {
    setSearchParams((prevParams) => ({
      ...prevParams,
      ...filters,
      page: currentPage,
      perPage: itemsPerPage,
    }));
  };

  const handlePaginationChanged = () => {
    setFinalParams((prevParams) => ({
      ...prevParams,
      page: currentPage,
      perPage: itemsPerPage,
    }));
  };

  const handleSearchInputChange = (searchInput: string) => {
    setSearchParams((prevParams) => ({
      ...prevParams,
      searchInput,
      page: 1, //going back to page 1 when the user clicks to make another search
      perPage: itemsPerPage,
    }));
  };

  const handleCheckboxChange = (columnKey: string) => {
    if (columnKey === "select-default") {
      //set to the deafult
      setHeaders(searchScreenColumns);
    } else if (columnKey === "select-all") {
      setHeaders((prevHeaders) =>
        prevHeaders.map((header) => ({
          ...header,
          selected: true, // Select all headers
        }))
      );
    } else {
      setHeaders((prevHeaders) =>
        prevHeaders.map((header) =>
          header.key === columnKey
            ? { ...header, selected: !header.selected }
            : header
        )
      );
    }
  };

  useEffect(() => {
    handleFiltersChanged();
  }, [filters]);

  useEffect(() => {
    handlePaginationChanged();
  }, [currentPage, itemsPerPage]);

  useEffect(() => {
    handleSearchInputChange(searchTerm);
  }, [searchTerm]);

  useEffect(() => {
    if (hasExternalParams) {
      handleSearch();
    }
  }, [hasExternalParams]);

  // Check if we have query parms and if the params align with the filter fields
  useEffect(() => {
    // Get the query params
    const urlParams = new URLSearchParams(window.location.search);
    let hasParams = false;

    // Here we do a match between the query params and the filter fields
    Object.keys(filters).forEach((key) => {
      // This is to avoid setting the filter fields with the query params if they don't exist on the filter
      if (urlParams.has(key)) {
        hasParams = true;
        setFilters((prevFilters: OpeningFilters) => ({
          ...prevFilters,
          [key]: urlParams.get(key),
        }));
      }
    });

    //initally when the screen loads check if there was a earch term present
    if (searchTerm.length > 0 || countActiveFilters(filters) > 0) {
      handleSearch();
    }

    setHasExternalParams(hasParams);
  }, []);

  return (
    <>
      <div className="container-fluid p-0 pb-5 align-content-center">
        <OpeningsSearchBar
          showNoFilterNotification={isNoFilterSearch}
          onSearchClick={handleSearch}
        />
        {showSpatial ? (
          <div
            className="search-spatial-container row p-0"
            data-testid="openings-map"
          >
            <div className="leaflet-container">
              <OpeningsMap
                openingId={null}
                openingIds={selectedOpeningIds}
                setOpeningPolygonNotFound={setOpeningPolygonNotFound}
              />
            </div>
          </div>
        ) : null}
        <div className="row">
          {filtersApplied ? (
            <>
              {isFetching ? (
                <TableSkeleton headers={headers} />
              ) : (
                <SearchScreenDataTable
                  rows={data?.content || []}
                  headers={headers}
                  handleCheckboxChange={handleCheckboxChange}
                  toggleSpatial={toggleSpatial}
                  showSpatial={showSpatial}
                  totalItems={data?.page.totalElements || 0}
                  setOpeningIds={setSelectedOpeningIds}
                />
              )}
            </>
          ) : (
            <EmptySection
              pictogram="Summit"
              title={"Nothing to show yet"}
              description={
                "At least one criteria must be entered to start the search: opening ID, opening number, timber mark, file ID or apply advanced search criteria. The matching results will be shown here."
              }
              fill="#0073E6"
            />
          )}
        </div>
      </div>
    </>
  );
};

export default OpeningsSearchTab;
