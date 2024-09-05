import React, { useState, useEffect } from "react";
import "./styles.scss";
import EmptySection from "../EmptySection";
import OpeningsSearchBar from "../OpeningsSearchBar";
import TableSkeleton from "../TableSkeleton";
import SearchScreenDataTable from "../SearchScreenDataTable";
import { headers } from "../SearchScreenDataTable/testData";
import OpeningsMap from "../OpeningsMap";
import { useOpeningsQuery } from "../../services/queries/search/openingQueries";

const OpeningsSearchTab: React.FC = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [showSpatial, setShowSpatial] = useState<boolean>(false);
  const [loadId, setLoadId] = useState<number | null>(null);
  const [openingPolygonNotFound, setOpeningPolygonNotFound] =
    useState<boolean>(false);
  const [filtersApplied, setFiltersApplied] = useState<boolean>(false);
  const [searchParams, setSearchParams] = useState<Record<string, any>>({});

  const { data, isFetching } = useOpeningsQuery(searchParams);

  const toggleSpatial = () => {
    setShowSpatial(!showSpatial);
  };
  
  const toggleFiltersApplied = () => {
    setFiltersApplied(!filtersApplied);
  };

  const handleSearch = () => {
    toggleFiltersApplied();
    // No need to log data here as it won't be available immediately
    setFiltersApplied(true); // Set filters as applied to show results
  };

  const handleFiltersChanged = (searchData: any) => {
    setSearchParams((prevParams) => ({
      ...prevParams,
      ...searchData,
    }));
    // console.log("Search Data:", searchData);
  };

  const handleSearchInputChange = (searchInput: string) => {
    setSearchParams((prevParams) => ({
      ...prevParams,
      searchInput,
    }));
    // console.log("Search Input Changed:", searchInput);
  };

  return (
    <>
      <div className="container-fluid p-0 pb-5 align-content-center">
        <OpeningsSearchBar 
          onSearch={handleFiltersChanged} 
          onSearchInputChange={handleSearchInputChange} 
          onSearchClick={handleSearch}
        />
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
          {filtersApplied ? (
            <>
              {isFetching ? (
                <TableSkeleton headers={headers} />
              ) : (
                <SearchScreenDataTable
                  headers={headers}
                  rows={data.data || []}
                  setOpeningId={setLoadId}
                  toggleSpatial={toggleSpatial}
                  showSpatial={showSpatial}
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
