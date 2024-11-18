import React, { useContext, useEffect, useState, useRef } from "react";
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
  Row,
  Column,
  MenuItemDivider,
} from "@carbon/react";
import * as Icons from "@carbon/icons-react";
import StatusTag from "../../../StatusTag";
import "./styles.scss";
import EmptySection from "../../../EmptySection";
import PaginationContext from "../../../../contexts/PaginationContext";
import { OpeningsSearch } from "../../../../types/OpeningsSearch";
import { ITableHeader } from "../../../../types/TableHeader";
import { FlexGrid } from "@carbon/react";
import { MenuItem } from "@carbon/react";
import {
  convertToCSV,
  downloadCSV,
  downloadPDF,
  downloadXLSX,
} from "../../../../utils/fileConversions";
import { Tooltip } from "@carbon/react";
import { useNavigate } from "react-router-dom";
import { usePostViewedOpening } from "../../../../services/queries/dashboard/dashboardQueries";
import { useNotification } from '../../../../contexts/NotificationProvider';
import TruncatedText from "../../../TruncatedText";
import FriendlyDate from "../../../FriendlyDate";
import ComingSoonModal from "../../../ComingSoonModal";


interface ISearchScreenDataTable {
  rows: OpeningsSearch[];
  headers: ITableHeader[];
  defaultColumns: ITableHeader[];
  handleCheckboxChange: (columnKey: string) => void;
  toggleSpatial: () => void;
  showSpatial: boolean;
  totalItems: number;
}

interface ICellRefs {
  offsetWidth: number;
}

const SearchScreenDataTable: React.FC<ISearchScreenDataTable> = ({
  rows,
  headers,
  defaultColumns,
  handleCheckboxChange,
  toggleSpatial,
  showSpatial,
  totalItems
}) => {
  const {
    handlePageChange,
    handleItemsPerPageChange,
    itemsPerPage,
    setInitialItemsPerPage,
    currentPage,
  } = useContext(PaginationContext);
  const alignTwo = document?.dir === "rtl" ? "bottom-left" : "bottom-right";
  const [openEdit, setOpenEdit] = useState(false);
  const [openDownload, setOpenDownload] = useState(false);
  const [selectedRows, setSelectedRows] = useState<string[]>([]); // State to store selected rows
  const [toastText, setToastText] = useState<string | null>(null);
  const [openingDetails, setOpeningDetails] = useState('');
  const { mutate: markAsViewedOpening, isError, error } = usePostViewedOpening();
  const navigate = useNavigate();

  // This ref is used to calculate the width of the container for each cell
  const cellRefs = useRef([]);
  // Holds the with of each cell in the table
  const [cellWidths, setCellWidths] = useState<number[]>([]);
  const { displayNotification } = useNotification();

  useEffect(() => {
    const widths = cellRefs.current.map((cell: ICellRefs) => cell.offsetWidth || 0);
    setCellWidths(widths);

    const handleResize = () => {
      const newWidths = cellRefs.current.map((cell: ICellRefs) => cell.offsetWidth || 0);
      setCellWidths(newWidths);
    };
    
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

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

  const handleRowClick = (openingId: string) => {
    // Call the mutation to mark as viewed
    markAsViewedOpening(openingId, {
      onSuccess: () => {
        setOpeningDetails(openingId.toString());
      },
      onError: (err: any) => {
        // Display error notification (UI needs to be designed for this)
      }
    });
  };

  //Function to handle the favourite feature of the opening for a user
  const handleFavouriteOpening = (rowId: string) => {
    displayNotification({
      title: `Following OpeningID ${rowId}`,          
      type: 'success',
      dismissIn: 8000,
      onClose: () => {}
    });
  }

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
              <Popover
                open={openEdit}
                isTabTip
                align={alignTwo}
                onRequestClose={() => setOpenEdit(false)}
              >
                <Button
                  iconDescription="Edit Columns"
                  tooltipposition="bottom"
                  kind="ghost"
                  onClick={() => {
                    setOpenEdit(!openEdit);
                  }}
                  renderIcon={Icons.Column}
                  size="md"
                >
                  Edit columns
                </Button>

                <PopoverContent className="edit-column-content">
                  <div className="dropdown-label">
                    <p>Select Columns you want to see:</p>
                  </div>
                  <FlexGrid className="dropdown-container">
                    {headers.map((header, index) =>
                      index > 0 && index % 2 === 1 ? ( // Start from index 1 and handle even-indexed pairs to skip the actions
                        <Row key={`row-${index}`}>
                          <Column sm={2} md={4} lg={8}>
                            <Checkbox
                              className="checkbox-item"
                              key={header.key}
                              labelText={header.header}
                              id={`checkbox-label-${header.key}`}
                              checked={header.selected === true}
                              onChange={() => handleCheckboxChange(header.key)}
                            />
                          </Column>
                          {headers[index + 1] && (
                            <Column sm={2} md={4} lg={8}>
                              <Checkbox
                                className="checkbox-item"
                                key={headers[index + 1].key}
                                labelText={headers[index + 1].header}
                                id={`checkbox-label-${headers[index + 1].key}`}
                                checked={headers[index + 1].selected === true}
                                onChange={() =>
                                  handleCheckboxChange(headers[index + 1].key)
                                }
                              />
                            </Column>
                          )}
                        </Row>
                      ) : null
                    )}
                  </FlexGrid>

                  <MenuItemDivider />
                  <MenuItem
                    className="menu-item"
                    label="Show all columns"
                    onClick={() => handleCheckboxChange("select-all")}
                  />
                  <MenuItem
                    className="menu-item"
                    label="Reset columns to default "
                    onClick={() => handleCheckboxChange("select-default")}
                  />
                </PopoverContent>
              </Popover>
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
                  Download
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
        <Table aria-label="opening search result table">
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
                  onClick={() => {
                    //add the api call to send the viewed opening
                    handleRowClick(row.openingId);
                  }}
                >
                  {headers.map((header) =>
                    header.selected ? (
                      <TableCell
                        ref={(el: never) => (cellRefs.current[i] = el)}
                        key={header.key}
                        className={
                          header.key === "actions" && showSpatial
                            ? "p-0"                            
                            : null
                        }
                      >
                        {header.key === "statusDescription" ? (
                          <StatusTag code={row[header.key]} />
                        ) : header.key === "actions" ? (
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
                                <div
                                  className="mb-2 mx-2"
                                  onClick={(e) => e.stopPropagation()}
                                  role="button"
                                  onKeyDown={(e) => {
                                    if (e.key === 'Enter' || e.key === ' ') {
                                      e.stopPropagation();
                                    }
                                  }}
                                  tabIndex={0}
                                >
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
                            <OverflowMenu
                              size={"md"}
                              ariaLabel="More actions"
                              onClick={(e: any) => e.stopPropagation()} // Stop row onClick from triggering
                            >
                              <OverflowMenuItem
                                itemText="Favourite opening"
                                onClick={(e: any) => {
                                  e.stopPropagation(); // Stop row onClick from triggering
                                  handleFavouriteOpening(row.openingId);
                                }}
                              />
                              <OverflowMenuItem
                                itemText="Download opening as PDF file"
                                onClick={(e: any) => {
                                  e.stopPropagation(); // Stop row onClick from triggering
                                  downloadPDF(defaultColumns, [row]);
                                }}
                              />
                              <OverflowMenuItem
                                itemText="Download opening as CSV file"
                                onClick={(e: any) => {
                                  e.stopPropagation(); // Stop row onClick from triggering
                                  const csvData = convertToCSV(defaultColumns, [
                                    row,
                                  ]);
                                  downloadCSV(csvData, "openings-data.csv");
                                }}
                              />
                              <OverflowMenuItem itemText="Delete opening" />
                            </OverflowMenu>
                          </CheckboxGroup>
                        ) : header.header === "Category" ? (
                          <TruncatedText
                            text={
                              row["categoryCode"] +
                              " - " +
                              row["categoryDescription"]
                            }
                            parentWidth={cellWidths[i]}
                          />
                        ) : header.key === "disturbanceStartDate" ? (
                          <FriendlyDate date={row[header.key]} />
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
          pageSizes={[20, 40, 60, 80, 100]}
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

      <ComingSoonModal openingDetails={openingDetails} setOpeningDetails={setOpeningDetails} />
    </>
  );
};

export default SearchScreenDataTable;
