// TableRowComponent.tsx

import React from "react";
import { Link } from "react-router-dom";
import { TableRow, TableCell, Tooltip } from "@carbon/react";
import { OpeningSearchResponseDto } from "@/types/OpeningTypes";
import StatusTag from "../StatusTag";
import SpatialCheckbox from "../SpatialCheckbox";
import ActionButtons from "../ActionButtons";
import { formatLocalDate } from "@/utils/DateUtils";
import { PLACE_HOLDER } from "@/constants";
import { OpendingHeaderKeyType, TableHeaderType } from "@/types/TableHeader";

import { OpeningDetailsRoute } from "@/routes/config";
import "./styles.scss";

interface TableRowComponentProps {
  headers: TableHeaderType<OpendingHeaderKeyType>[];
  rowData: OpeningSearchResponseDto;
  showMap: boolean;
  selectedRows: number[];
  handleRowSelection: (rowId: number) => void;
}

const OpeningTableRow: React.FC<TableRowComponentProps> = ({
  headers,
  rowData,
  showMap,
  selectedRows,
  handleRowSelection,
}) => {
  const renderCellContent = (header: OpendingHeaderKeyType) => {
    switch (header) {
      case "status":
        return (
          <StatusTag description={rowData.status?.description ?? "Unknown"} />
        );
      case "actions":
        return (
          <div className="action-container">
            {showMap ? (
              <SpatialCheckbox
                rowId={rowData.openingId}
                selectedRows={selectedRows}
                handleRowSelection={handleRowSelection}
              />
            ) : null}
            <ActionButtons
              favorited={rowData.favourite}
              rowId={rowData.openingId.toString()}
              showToast
            />
          </div>
        );
      case "category":
        if (rowData.category) {
          return (
            <Tooltip label={rowData.category.description}>
              <span>{rowData.category.code}</span>
            </Tooltip>
          );
        }
        return PLACE_HOLDER;

      case "regenDelayDate":
      case "updateTimestamp":
      case "earlyFreeGrowingDate":
      case "disturbanceStartDate":
        return formatLocalDate(rowData.disturbanceStartDate, true);
      default:
        return rowData[header];
    }
  };

  return (
    <TableRow className="opening-table-row">
      {headers
        .filter((header) => header.selected)
        .map((header) => (
          <TableCell key={header.key}>
            {header.key !== "actions" ? (
              <Link
                to={OpeningDetailsRoute.path!.replace(
                  ":openingId",
                  rowData.openingId.toString()
                )}
                className="table-cell-link-wrapper"
                rel="noopener noreferrer"
              >
                {renderCellContent(header.key) ?? PLACE_HOLDER}
              </Link>
            ) : (
              renderCellContent(header.key) ?? PLACE_HOLDER
            )}
          </TableCell>
        ))}
    </TableRow>
  );
};

export default OpeningTableRow;
