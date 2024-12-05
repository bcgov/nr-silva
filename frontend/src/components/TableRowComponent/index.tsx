// TableRowComponent.tsx

import React from "react";
import { TableRow, TableCell } from "@carbon/react";
import TableCellContent from "../TableCellContent";
import { OpeningsSearch } from "../..//types/OpeningsSearch";
import { ITableHeader } from "../..//types/TableHeader";

interface TableRowComponentProps {
  row: OpeningsSearch;
  headers: ITableHeader[];
  showSpatial: boolean;
  selectedRows: string[];
  handleRowSelectionChanged: (rowId: string) => void;
  setOpeningDetails: (openingId: string) => void;
}

const TableRowComponent: React.FC<TableRowComponentProps> = ({
  row,
  headers,
  showSpatial,
  selectedRows,
  handleRowSelectionChanged,
  setOpeningDetails
}) => (
  <TableRow>
    {headers.map((header) =>
      header.selected ? (
        <TableCell 
          key={header.key}
          onClick={() => {
            if (header.key !== "actions")
            setOpeningDetails(row.openingId.toString())
          }}
        >
          <TableCellContent
            headerKey={header.key}
            row={row}
            showSpatial={showSpatial}
            selectedRows={selectedRows}
            handleRowSelectionChanged={handleRowSelectionChanged}
          />
        </TableCell>
      ) : null
    )}
  </TableRow>
);

export default TableRowComponent;
