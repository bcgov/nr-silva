// TableRowComponent.tsx

import React from "react";
import { TableRow, TableCell, Tooltip } from "@carbon/react";
import { OpeningSearchResponseDto } from "../../types/OpeningTypes";
import StatusTag from "../StatusTag";
import SpatialCheckbox from "../SpatialCheckbox";
import ActionButtons from "../ActionButtons";
import { formatLocalDate } from "../../utils/DateUtils";
import { PLACE_HOLDER } from "../../constants";
import { OpendingHeaderKeyType, TableHeaderType } from "../../types/TableHeader";

import './styles.scss';
import { useMutation } from "@tanstack/react-query";
import { putUserRecentOpening } from "../../services/OpeningService";
import { useNavigate } from "react-router-dom";
import { OpeningDetailsRoute } from "../../routes/config";

interface TableRowComponentProps {
  headers: TableHeaderType<OpendingHeaderKeyType>[];
  rowData: OpeningSearchResponseDto;
  showMap: boolean;
  selectedRows: number[];
  handleRowSelection: (rowId: number) => void;
  navigateOnClick?: boolean;
}

const OpeningTableRow: React.FC<TableRowComponentProps> = ({
  headers,
  rowData,
  showMap,
  selectedRows,
  handleRowSelection,
  navigateOnClick
}) => {
  const navigate = useNavigate();

  const renderCellContent = (header: OpendingHeaderKeyType) => {
    switch (header) {
      case "status":
        return <StatusTag code={rowData.status?.description ?? 'Unknown'} />;
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
        return formatLocalDate(rowData.disturbanceStartDate, true)
      default:
        return rowData[header];
    }
  }

  const handleRowClick = () => {
    if (navigateOnClick) {
      navigate(OpeningDetailsRoute.path!.replace(":openingId", rowData.openingId.toString()))
    }
  }

  return (
    <TableRow
      className={`opening-table-row${navigateOnClick ? ' clickable-opening-row' : ''}`}
    >
      {
        headers
          .filter((header) => header.selected)
          .map((header) => (
            <TableCell
              key={header.key}
              onClick={() => {
                if (header.key !== 'actions') {
                  handleRowClick();
                }
              }}
            >
              {
                renderCellContent(header.key) ?? PLACE_HOLDER
              }
            </TableCell>
          ))
      }
    </TableRow>
  )
};

export default OpeningTableRow;
