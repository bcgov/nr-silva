import React, { useState } from "react";
import "./styles.scss";
import EmptySection from "../EmptySection";
import OpeningsSearchBar from "../OpeningsSearchBar";
import TableSkeleton from "../TableSkeleton";
import SearchScreenDataTable from "../SearchScreenDataTable";
import { headers, rows } from "../SearchScreenDataTable/testData";
import OpeningsMap from "../OpeningsMap";

const OpeningsSearchTab: React.FC = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [showSpatial, setShowSpatial] = useState<boolean>(false);
  const [loadId, setLoadId] = useState<number | null>(null);
  const [openingPolygonNotFound, setOpeningPolygonNotFound] =
    useState<boolean>(false);
  const [filtersApplied, setFiltersApplied] = useState<boolean>(false);

  const toggleSpatial = () => {
    setShowSpatial(!showSpatial);
  };
  const toggleFiltersApplied = () => {
    setFiltersApplied(!filtersApplied);
  };

  return (
    <>
      <div className="container-fluid p-0 pb-5 align-content-center">
        <OpeningsSearchBar toggleFiltersApplied={toggleFiltersApplied} />
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
              {loading ? (
                <TableSkeleton headers={headers} />
              ) : (
                <SearchScreenDataTable
                  headers={headers}
                  rows={rows}
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
