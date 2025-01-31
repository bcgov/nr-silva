import React, { useEffect, useState } from "react";
import {
  Table,
  TableBody,
  TableContainer,
  TableHead,
  TableHeader,
  TableRow,
} from "@carbon/react";
import EmptySection from "../../../EmptySection";
import { OpeningsSearch } from "../../../../types/OpeningsSearch";
import { ITableHeader } from "../../../../types/TableHeader";
import TableRowComponent from "../../../TableRowComponent";
import ComingSoonModal from "../../../ComingSoonModal";

interface IRecentOpeningsDataTable {
  rows: OpeningsSearch[];
  headers: ITableHeader[];
  setOpeningIds: (openingIds: number[]) => void;
  showSpatial: boolean;
  totalItems: number;
}

const RecentOpeningsDataTable: React.FC<IRecentOpeningsDataTable> = ({
  rows,
  headers,
  showSpatial,
  setOpeningIds,
  totalItems,
}) => {
  const [selectedRows, setSelectedRows] = useState<string[]>([]);
  const [openingDetails, setOpeningDetails] = useState("");

  useEffect(() => {}, [rows, totalItems]);

  useEffect(() => {
    setOpeningIds(selectedRows.map((id) => parseFloat(id)));
  }, [selectedRows]);

  // Function to handle row selection changes
  const handleRowSelectionChanged = (rowId: string) => {
    setSelectedRows((prevSelectedRows) =>
      prevSelectedRows.includes(rowId)
        ? prevSelectedRows.filter((id) => id !== rowId)
        : [...prevSelectedRows, rowId]
    );
  };

  return (
    <TableContainer className="search-data-table">
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
  );
};
export default RecentOpeningsDataTable;
