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
  Popover,
  PopoverContent,
  Checkbox,
  Row,
  Column,
  MenuItemDivider,
  MenuItem,
  FlexGrid,
} from "@carbon/react";
import * as Icons from "@carbon/icons-react";
import StatusTag from "../../../StatusTag";
import "./styles.scss";
import EmptySection from "../../../EmptySection";
import PaginationContext from "../../../../contexts/PaginationContext";
import { OpeningSearchResponseDto } from "../../../../types/OpeningTypes";
import { ITableHeader } from "../../../../types/TableHeader";
import { usePostViewedOpening } from "../../../../services/queries/dashboard/dashboardQueries";
import { useNotification } from "../../../../contexts/NotificationProvider";
import TruncatedText from "../../../TruncatedText";
import FriendlyDate from "../../../FriendlyDate";
import ComingSoonModal from "../../../ComingSoonModal";
import ActionButtons from "../../../ActionButtons";
import SpatialCheckbox from "../../../SpatialCheckbox";

interface ISearchScreenDataTable {
  rows: OpeningSearchResponseDto[];
  headers: ITableHeader[];
  handleCheckboxChange: (columnKey: string) => void;
  toggleSpatial: () => void;
  showSpatial: boolean;
  totalItems: number;
  setOpeningIds: (openingIds: number[]) => void;
}

interface ICellRefs {
  offsetWidth: number;
}

const SearchScreenDataTable: React.FC<ISearchScreenDataTable> = ({
  rows,
  headers,
  handleCheckboxChange,
  toggleSpatial,
  showSpatial,
  totalItems,
  setOpeningIds,
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
  const [selectedRows, setSelectedRows] = useState<number[]>([]); // State to store selected rows
  const [openingDetails, setOpeningDetails] = useState("");
  const [columnsSelected, setColumnsSelected] =
    useState<string>("select-default");
  const { mutate: markAsViewedOpening } = usePostViewedOpening();

  // This ref is used to calculate the width of the container for each cell
  const cellRefs = useRef([]);
  // Holds the with of each cell in the table
  const [cellWidths, setCellWidths] = useState<number[]>([]);
  const { displayNotification } = useNotification();

  useEffect(() => {
    const widths = cellRefs.current.map(
      (cell: ICellRefs) => cell.offsetWidth || 0
    );
    setCellWidths(widths);

    const handleResize = () => {
      const newWidths = cellRefs.current.map(
        (cell: ICellRefs) => cell.offsetWidth || 0
      );
      setCellWidths(newWidths);
    };

    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  useEffect(() => {
    setInitialItemsPerPage(itemsPerPage);
  }, [rows, totalItems]);

  // Function to handle row selection changes
  const handleRowSelection = (openingId: number) => {
    setSelectedRows((prevSelectedRows) => {
      if (prevSelectedRows.includes(openingId)) {
        // If the row is already selected, remove it from the selected rows
        const selectedValues = prevSelectedRows.filter(
          (id) => id !== openingId
        );
        setOpeningIds(selectedValues);
        return selectedValues;
      } else {
        // If the row is not selected, add it to the selected rows
        const selectedValues = [...prevSelectedRows, openingId];
        setOpeningIds(selectedValues);
        return selectedValues;
      }
    });
  };

  const handleRowClick = (openingId: string) => {
    // Call the mutation to mark as viewed
    markAsViewedOpening(openingId, {
      onSuccess: () => {
        setOpeningDetails(openingId);
      },
      onError: (err: any) => {
        displayNotification({
          title: "Unable to process your request",
          subTitle: "Please try again in a few minutes",
          type: "error",
          onClose: () => {},
        });
      },
    });
  };

  return (
    <div className="search-screen-data-table-container">
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
                data-testid="toggle-spatial"
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
                  data-testid="edit-columns"
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
                  <FlexGrid
                    className="dropdown-container scrollable"
                    data-test-id="edit-columns-container"
                  >
                    {headers.map(
                      (header, index) =>
                        header &&
                        header.key !== "actions" && (
                          <Row key={`row-${index}`} className="my-3">
                            <Column sm={2} md={4} lg={8} key={header.key}>
                              {header.key === "openingId" ? (
                                <div className="d-flex flex-row align-items-center checkbox-item cursor-pointer">
                                  <Icons.CheckboxChecked size={21} />
                                  <p className="bx--checkbox-label-text">
                                    {header.header}
                                  </p>
                                </div>
                              ) : (
                                <Checkbox
                                  className="checkbox-item"
                                  labelText={header.header}
                                  id={`checkbox-label-${header.key}`}
                                  checked={header.selected === true}
                                  onChange={() => {
                                    handleCheckboxChange(header.key);
                                    setColumnsSelected("select-custom");
                                  }}
                                />
                              )}
                            </Column>
                          </Row>
                        )
                    )}
                  </FlexGrid>

                  <MenuItemDivider />
                  <div
                    className="w-100 d-flex flex-row justify-content-between menu-item-container"
                    id="select-all-column"
                    data-testid="select-all-column"
                    onClick={() => {
                      handleCheckboxChange("select-all");
                      setColumnsSelected("select-all");
                    }}
                    onKeyDown={(event) => {
                      if (event.key === "Enter" || event.key === " ") {
                        event.preventDefault();
                        handleCheckboxChange("select-all");
                        setColumnsSelected("select-all");
                      }
                    }}
                  >
                    <p className="menu-item">Select all columns</p>
                    {columnsSelected === "select-all" && (
                      <Icons.Checkmark size={13} />
                    )}
                  </div>
                  <div
                    className="w-100 d-flex flex-row justify-content-between menu-item-container"
                    id="select-default-column"
                    data-testid="select-default-column"
                    onClick={() => {
                      handleCheckboxChange("select-default");
                      setColumnsSelected("select-default");
                    }}
                    onKeyDown={(event) => {
                      if (event.key === "Enter" || event.key === " ") {
                        event.preventDefault();
                        handleCheckboxChange("select-default");
                        setColumnsSelected("select-default");
                      }
                    }}
                  >
                    <p className="menu-item">Reset columns to default</p>
                    {columnsSelected === "select-default" && (
                      <Icons.Checkmark size={13} />
                    )}
                  </div>
                </PopoverContent>
              </Popover>
              <div className="divider"></div>
            </div>
          </TableToolbarContent>
        </TableToolbar>
        <Table aria-label="opening search result table">
          <TableHead>
            <TableRow>
              {headers.map((header) =>
                header.selected ? (
                  <TableHeader key={header.key} data-testid={header.header}>
                    {header.header}
                  </TableHeader>
                ) : null
              )}
            </TableRow>
          </TableHead>
          <TableBody>
            {rows &&
              rows.map((row: any, i: number) => (
                <TableRow
                  key={row.openingId + i.toString()}
                  data-testid={`row-${row.openingId}`}
                >
                  {headers.map((header) =>
                    header.selected ? (
                      <TableCell
                        data-testid={`cell-${header.key}-${row.openingId}`}
                        ref={(el: never) => (cellRefs.current[i] = el)}
                        key={header.key}
                        className={
                          header.key === "actions" && showSpatial
                            ? "p-0"
                            : undefined
                        }
                        onClick={() => {
                          if (header.key !== "actions") {
                            handleRowClick(row.openingId);
                          }
                        }}
                      >
                        {header.key === "statusDescription" ? (
                          <StatusTag code={row[header.key]} />
                        ) : header.key === "actions" ? (
                          <div
                            className={
                              showSpatial ? "d-flex space-left-1" : "d-flex"
                            }
                          >
                            {showSpatial && (
                              <div className="pt-3">
                                <SpatialCheckbox
                                  rowId={row.openingId}
                                  selectedRows={selectedRows}
                                  handleRowSelection={handleRowSelection}
                                />
                              </div>
                            )}
                            <ActionButtons
                              favorited={row.favourite}
                              rowId={row.openingId}
                              showToast
                            />
                          </div>
                        ) : header.header === "Category" ? (
                          <TruncatedText
                            text={
                              row["categoryCode"] +
                              " - " +
                              row["categoryDescription"]
                            }
                            parentWidth={cellWidths[i]}
                          />
                        ) : header.key.includes("Date") ||
                          header.key.includes("Timestamp") ? (
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
          pictogram="UserSearch"
          title={"Results not found"}
          description={"Check spelling or try different parameters"}
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

      <ComingSoonModal
        openingDetails={openingDetails}
        setOpeningDetails={setOpeningDetails}
      />
    </div>
  );
};

export default SearchScreenDataTable;
