// TableRowComponent.tsx

import React from "react";
import { TableRow, TableCell } from "@carbon/react";
import { OpeningSearchResponseDto } from "../../types/OpeningTypes";
import { recentOpeningsHeaders as headers } from "./constants";
import { OpendingHeaderKeyType } from "./definitions";
import StatusTag from "../StatusTag";
import SpatialCheckbox from "../SpatialCheckbox";
import ActionButtons from "../ActionButtons";
import TruncatedText from "../TruncatedText";

interface TableRowComponentProps {
  rowData: OpeningSearchResponseDto;
  showMap: boolean;
  selectedRows: number[];
  handleRowSelection: (rowId: number) => void;
}

const OpeningRow: React.FC<TableRowComponentProps> = ({
  rowData,
  showMap,
  selectedRows,
  handleRowSelection
}) => {

  const renderCellContent = (header: OpendingHeaderKeyType) => {
    switch (header) {
      case "status":
        return <StatusTag code={rowData.status.description} />;
      case "actions":
        return (
          <div className="action-container">
            {
              showMap ? (
                <SpatialCheckbox
                  rowId={rowData.openingId}
                  selectedRows={selectedRows}
                  handleRowSelection={handleRowSelection}
                />
              )
                : null
            }
            <ActionButtons
              favorited={rowData.favourite}
              rowId={rowData.openingId.toString()}
            />
          </div>
        );
      case "category":
        return (
          <TruncatedText text={`${rowData.category.code} - ${rowData.category.description}`} />
        );
      default:
        return rowData[header];
    }
  }

  return (
    <TableRow>
      {
        headers.map((header) => (
          <TableCell
            key={header.key}
            onClick={() => {
              if (header.key !== "actions")
                handleRowSelection(rowData.openingId)
            }}
          >
            {
              renderCellContent(header.key)
            }
          </TableCell>
        ))
      }
    </TableRow>
  )
};

export default OpeningRow;
