import React, { useContext, useEffect, useState } from "react";
import {
  TableToolbar,
  Table,
  TableBody,
  TableContainer,
  TableHead,
  TableHeader,
  TableRow
} from "@carbon/react";
import EmptySection from "../../../EmptySection";
import PaginationContext from "../../../../contexts/PaginationContext";
import { OpeningsSearch } from "../../../../types/OpeningsSearch";
import { ITableHeader } from "../../../../types/TableHeader";
import { useNavigate } from "react-router-dom";
import TableRowComponent from "../../../TableRowComponent";
import ComingSoonModal from "../../../ComingSoonModal";

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
  showSpatial,
  totalItems,
}) => {
  const {
    itemsPerPage,
    setInitialItemsPerPage,
  } = useContext(PaginationContext);
  const [selectedRows, setSelectedRows] = useState<string[]>([]);
  const [openingDetails, setOpeningDetails] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    setInitialItemsPerPage(itemsPerPage);
  }, [rows, totalItems]);

  // Function to handle row selection changes
  const handleRowSelectionChanged = (rowId: string) => {
    setSelectedRows((prevSelectedRows) =>
      prevSelectedRows.includes(rowId)
        ? prevSelectedRows.filter((id) => id !== rowId)
        : [...prevSelectedRows, rowId]
    );
  };

  return (
    <>
      <TableContainer className="search-data-table">
        <TableToolbar aria-label="data table toolbar">
          {/* Toolbar content... */}
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
            {rows.map((row, index) => (
              <TableRowComponent
                key={row.openingId + index.toString()}
                row={row}
                headers={headers}
                showSpatial={showSpatial}
                selectedRows={selectedRows}
                handleRowSelectionChanged={handleRowSelectionChanged}
                setOpeningDetails={setOpeningDetails}
              />
            ))}
          </TableBody>
        </Table>

        {rows.length <= 0 && (
          <EmptySection
            pictogram="Magnify"
            title="There are no openings to show yet"
            description="Your recent openings will appear here once you generate one"
            fill="#0073E6"
          />
        )}
        <ComingSoonModal
          openingDetails={openingDetails}
          setOpeningDetails={setOpeningDetails}
        />
      </TableContainer>
    </>
  );
};
export default RecentOpeningsDataTable;
