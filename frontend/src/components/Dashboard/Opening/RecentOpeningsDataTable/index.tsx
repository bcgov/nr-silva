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
  Pagination,
  OverflowMenu,
  OverflowMenuItem,
  Popover,
  PopoverContent,
  Checkbox,
  CheckboxGroup,
  Modal,
  ActionableNotification
} from "@carbon/react";
import * as Icons from "@carbon/icons-react";
import StatusTag from "../../../StatusTag";
import "./styles.scss";
import EmptySection from "../../../EmptySection";
import PaginationContext from "../../../../contexts/PaginationContext";
import { OpeningsSearch } from "../../../../types/OpeningsSearch";
import { ITableHeader } from "../../../../types/TableHeader";
import { MenuItem } from "@carbon/react";
import {
  convertToCSV,
  downloadCSV,
  downloadPDF,
  downloadXLSX,
} from "../../../../utils/fileConversions";
import { Tooltip } from "@carbon/react";
import { useNavigate } from "react-router-dom";

interface IRecentOpeningsDataTable {
  rows: OpeningsSearch[];
  headers: ITableHeader[];
  defaultColumns: ITableHeader[];
  handleCheckboxChange: Function;
  setOpeningId: Function;
  toggleSpatial: Function;
  showSpatial: boolean;
  totalItems: number;
}

const RecentOpeningsDataTable: React.FC<IRecentOpeningsDataTable> = ({
  rows,
  headers,
  defaultColumns,
  showSpatial,
  totalItems,
}) => {
  const {
    handlePageChange,
    handleItemsPerPageChange,
    itemsPerPage,
    setInitialItemsPerPage,
    currentPage,
  } = useContext(PaginationContext);
  const alignTwo = document?.dir === "rtl" ? "bottom-left" : "bottom-right";
  const [openDownload, setOpenDownload] = useState(false);
  const [selectedRows, setSelectedRows] = useState<string[]>([]); // State to store selected rows
  const [toastText, setToastText] = useState<string | null>(null);
  const [openingDetails, setOpeningDetails] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    setInitialItemsPerPage(itemsPerPage);
  }, [rows, totalItems]);
  
  // Function to handle row selection changes
  const handleRowSelectionChanged = (rowId: string) => {
    setSelectedRows((prevSelectedRows) => {
      if (prevSelectedRows.includes(rowId)) {
        // If the row is already selected, remove it from the selected rows
        return prevSelectedRows.filter((id) => id !== rowId);
      } else {
        // If the row is not selected, add it to the selected rows
        return [...prevSelectedRows, rowId];
      }
    });
  };

  return (
    <>
      <TableContainer className="search-data-table">
        <TableToolbar aria-label="data table toolbar">
          <TableToolbarContent className="table-toolbar align-items-center justify-content-between">
            <div className="total-results-container">
              <p className="total-search-results">
                Total Search Results: {totalItems}
              </p>
            </div>
            <TableToolbarMenu
              iconDescription="More"
              tooltipposition="bottom"
              renderIcon={Icons.OverflowMenuVertical}
              className="d-block d-sm-none"
            >
              <TableToolbarAction onClick={() => console.log("Download Click")}>
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
              <div className="divider"></div>
              <Popover
                open={openDownload}
                isTabTip
                align={alignTwo}
                onRequestClose={() => setOpenDownload(false)}
              >
                <Button
                  iconDescription="Download"
                  tooltipposition="bottom"
                  kind="ghost"
                  onClick={() => {
                    setOpenDownload(!openDownload);
                  }}
                  renderIcon={Icons.Download}
                  size="lg"
                >
                  Download Table
                </Button>
                <PopoverContent className="download-column-content">
                  <MenuItem
                    className="menu-item"
                    size={"lg"}
                    label="Download table as PDF file"
                    onClick={() => {
                      downloadPDF(headers, rows);
                    }}
                  />
                  <MenuItem
                    className="menu-item"
                    size="lg"
                    label="Download table as CSV file"
                    onClick={() => {
                      const csvData = convertToCSV(headers, rows);
                      downloadCSV(csvData, "openings-data.csv");
                    }}
                  />
                  <MenuItem
                    className="menu-item"
                    size={"lg"}
                    label="Download table as XLS file"
                    onClick={() => downloadXLSX(headers, rows)}
                  />
                </PopoverContent>
              </Popover>
            </div>
          </TableToolbarContent>
        </TableToolbar>
        <Table aria-label="sample table">
          <TableHead>
            <TableRow>
              {headers.map((header) =>
                header.selected ? (
                  <TableHeader key={header.key}>{header.header}</TableHeader>
                ) : null
              )}
            </TableRow>
          </TableHead>
          <TableBody>
            {rows &&
              rows.map((row: any, i: number) => (
                <TableRow
                  key={row.openingId + i.toString()}
                  onClick={async () => {
                    //add the api call to send the viewed opening
                    // await handleRowClick(row.openingId);
                    setOpeningDetails(true);
                  }}
                >
                  {headers.map((header) =>
                    header.selected ? (
                      <TableCell
                        key={header.key}
                        className={
                          header.key === "actions" && showSpatial ? "p-0" : null
                        }
                      >
                        {header.key === "statusDescription" ? (
                          <StatusTag code={row[header.key]} />
                        ) : header.key === "actions" ? (
                          <>
                            <>
                            <Button
                              hasIconOnly
                              iconDescription="View"
                              tooltipPosition="auto"
                              kind="ghost"
                              onClick={() => console.log(row.openingid)}
                              renderIcon={Icons.View}
                              size="md"
                            />
                            <Button
                              hasIconOnly
                              iconDescription="Document Download"
                              tooltipPosition="auto"
                              kind="ghost"
                              onClick={() => null}
                              renderIcon={Icons.DocumentDownload}
                              size="md"
                            />
                          </>
                            <CheckboxGroup
                              orientation="horizontal"
                              className="align-items-center justify-content-start"
                            >
                              {/* Checkbox for selecting rows */}
                              {showSpatial && (
                                <Tooltip
                                  className="checkbox-tip"
                                  label="Click to view this opening's map activity."
                                  align="bottom-left"
                                  autoAlign
                                >
                                  <div className="mb-2 mx-2">
                                    <Checkbox
                                      id={`checkbox-label-${row.openingId}`}
                                      checked={selectedRows.includes(
                                        row.openingId
                                      )}
                                      onChange={() =>
                                        handleRowSelectionChanged(row.openingId)
                                      }
                                    />
                                  </div>
                                </Tooltip>
                              )}
                            </CheckboxGroup>
                          </>
                        ) : header.header === "Category" ? (
                          row["categoryCode"] +
                          " - " +
                          row["categoryDescription"]
                        ) : (
                          row[header.key]
                        )}
                      </TableCell>
                    ) : null
                  )}
                </TableRow>
              ))}
          </TableBody>
        </Table>
      </TableContainer>

      {rows.length <= 0 ? (
        <EmptySection
          pictogram="Magnify"
          title={"There are no openings to show yet"}
          description={
            "Your recent openings will appear here once you generate one"
          }
          fill="#0073E6"
        />
      ) : null}

      {rows.length > 0 && (
        <Pagination
          totalItems={totalItems}
          backwardText="Previous page"
          forwardText="Next page"
          pageSize={itemsPerPage}
          pageSizes={[5, 20, 50, 200, 400]}
          itemsPerPageText="Items per page"
          page={currentPage}
          onChange={({
            page,
            pageSize,
          }: {
            page: number;
            pageSize: number;
          }) => {
            handlePageChange(page);
            handleItemsPerPageChange(page, pageSize);
          }}
        />
      )}
      {toastText != null ? (
        <ActionableNotification
          className="fav-toast"
          title="Success"
          subtitle={toastText}
          lowContrast={true}
          kind="success"
          role="status"
          closeOnEscape
          onClose={() => setToastText(null)}
          actionButtonLabel="Go to track openings"
          onActionButtonClick={() =>
            navigate("/opening?tab=metrics&scrollTo=trackOpenings")
          }
        />
      ) : null}

      <Modal
        open={openingDetails}
        onRequestClose={() => setOpeningDetails(false)}
        passiveModal
        modalHeading="We are working hard to get this feature asap, unfortunately you cannot view the opening details from SILVA atm."
        modalLabel="Opening Details"
      />
    </>
  );
};

export default RecentOpeningsDataTable;
