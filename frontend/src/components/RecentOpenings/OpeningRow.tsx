// TableRowComponent.tsx

import React from "react";
import { TableRow, TableCell, Tooltip } from "@carbon/react";
import { OpeningSearchResponseDto } from "../../types/OpeningTypes";
import { recentOpeningsHeaders as headers } from "./constants";
import { OpendingHeaderKeyType } from "./definitions";
import StatusTag from "../StatusTag";
import SpatialCheckbox from "../SpatialCheckbox";
import ActionButtons from "../ActionButtons";
import { formatLocalDate } from "../../utils/DateUtils";
import { PLACE_HOLDER } from "../../constants";

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
          <Tooltip label={rowData.category.description}>
            <span>{rowData.category.code}</span>
          </Tooltip>
        );
      case "disturbanceStartDate":
        return formatLocalDate(rowData.disturbanceStartDate, true)
      default:
        return rowData[header];
    }
  }

  return (
    <TableRow>
      {
        headers.map((header) => (
          <TableCell key={header.key}>
            {
              renderCellContent(header.key) ?? PLACE_HOLDER
            }
          </TableCell>
        ))
      }
    </TableRow>
  )
};

export default OpeningRow;
