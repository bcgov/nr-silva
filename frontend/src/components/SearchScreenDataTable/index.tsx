import React, { useContext, useEffect, useState } from "react";
import {
  TableToolbar,
  TableToolbarAction,
  TableToolbarContent,
  TableToolbarMenu,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableHeader,
  TableRow,
  Button,
  Pagination
} from "@carbon/react";
import * as Icons from "@carbon/icons-react";
import StatusTag from "../StatusTag";
import "./styles.scss";
import EmptySection from "../EmptySection";
import PaginationContext from "../../contexts/PaginationContext";
import { OpeningsSearch } from "../../types/OpeningsSearch";
import { ITableHeader } from "../../types/TableHeader";

interface ISearchScreenDataTable {
  rows: OpeningsSearch[];
  headers: ITableHeader[];
  setOpeningId: Function;
  toggleSpatial: Function;
  showSpatial: boolean;
}

const SearchScreenDataTable: React.FC<ISearchScreenDataTable> = ({
  rows,
  headers,
  setOpeningId,
  toggleSpatial,
  showSpatial,
}) => {
  const [filteredRows, setFilteredRows] = useState<OpeningsSearch[]>(rows);
  const {
    getCurrentData,
    currentPage,
    totalPages,
    handlePageChange,
    handleItemsPerPageChange,
    itemsPerPage,
    setPageData,
    setInitialItemsPerPage,
  } = useContext(PaginationContext);
  // Create a variable for current data
  const currentData = Array.isArray(getCurrentData?.()) ? getCurrentData() : [];

  useEffect(() => {
    setPageData(filteredRows);
    setInitialItemsPerPage(5);
  }, [filteredRows]);

  return (
    <>
      <TableContainer className="search-data-table">
        <TableToolbar aria-label="data table toolbar">
          <TableToolbarContent className="table-toolbar align-items-center justify-content-between">
            <div className="total-results-container">
              <p className="total-search-results">
                Total Search Results: {filteredRows.length}
              </p>
            </div>
            <TableToolbarMenu
              iconDescription="More"
              tooltipposition="bottom"
              renderIcon={Icons.OverflowMenuVertical}
              className="d-block d-sm-none"
            >
              <TableToolbarAction
                onClick={() => console.log("Download Click")}
              >
                Print
              </TableToolbarAction>
              <TableToolbarAction
                onClick={() => {
                  console.log("Clicked print");
                }}
              >
                Download
              </TableToolbarAction>
            </TableToolbarMenu>
            <div className="d-none d-sm-flex align-items-center">
              <div className="divider"></div>
              <Button
                iconDescription="Show Map"
                tooltipposition="bottom"
                kind="ghost"
                onClick={() => toggleSpatial()}
                renderIcon={Icons.Location}
                size="md"
              >
                {showSpatial === true ? "Hide map" : "Show map"}
              </Button>
              <div className="divider"></div>
              <Button
                iconDescription="Edit Columns"
                tooltipposition="bottom"
                kind="ghost"
                onClick={() => {
                  console.log("Edit Columns");
                }}
                renderIcon={Icons.Column}
                size="md"
              >
                Edit columns
              </Button>
              <div className="divider"></div>
              <Button
                iconDescription="Download"
                tooltipposition="bottom"
                kind="ghost"
                onClick={() => console.log("Download Click")}
                renderIcon={Icons.Download}
                size="md"
              >
                Download
              </Button>
            </div>
          </TableToolbarContent>
        </TableToolbar>
        <Table aria-label="sample table">
          <TableHead>
            <TableRow>
              {headers.map((header) => (
                <TableHeader key={header.key}>{header.header}</TableHeader>
              ))}
            </TableRow>
          </TableHead>
          <TableBody>
            {currentData && currentData.map((row : any, i: number) => (
              <TableRow key={row.openingId+i.toString()}>
                {headers.map((header) => (
                  <TableCell key={header.key}>
                    {header.key === "statusCode" ? (
                      <StatusTag code={row[header.key]} />
                    ) : header.key === "actions" ? (
                      <TableToolbarMenu
                        iconDescription="More"
                        autoAlign
                        renderIcon={Icons.OverflowMenuVertical}
                        className="float-start"
                      >
                        <TableToolbarAction
                          onClick={() => console.log("Download Click")}
                        >
                          Print
                        </TableToolbarAction>
                        <TableToolbarAction
                          onClick={() => {
                            console.log("Clicked print");
                          }}
                        >
                          Download
                        </TableToolbarAction>
                      </TableToolbarMenu>
                    ) : (
                      row[header.key]
                    )}
                  </TableCell>
                ))}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {filteredRows.length <= 0 ? (
        <EmptySection
          pictogram="Magnify"
          title={"There are no openings to show yet"}
          description={
            "Your recent openings will appear here once you generate one"
          }
          fill="#0073E6"
        />
      ) : null}

      {filteredRows.length > 0 && (
        <Pagination
          totalItems={filteredRows.length}
          backwardText="Previous page"
          forwardText="Next page"
          pageSize={itemsPerPage}
          pageSizes={[5, 20, 50]}
          itemsPerPageText="Items per page"
          onChange={({ page, pageSize } : { page: number, pageSize: number}) => {
            handlePageChange(page);
            handleItemsPerPageChange(page, pageSize);
          }}
        />
      )}
    </>
  );
};

export default SearchScreenDataTable;
