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

interface TableRowComponentProps {
  headers: TableHeaderType<OpendingHeaderKeyType>[];
  rowData: OpeningSearchResponseDto;
  showMap: boolean;
  selectedRows: number[];
  handleRowSelection: (rowId: number) => void;
  enableClick?: boolean;
  handleComingSoon?: (rowData: OpeningSearchResponseDto) => void;
}

const OpeningTableRow: React.FC<TableRowComponentProps> = ({
  headers,
  rowData,
  showMap,
  selectedRows,
  handleRowSelection,
  enableClick,
  handleComingSoon
}) => {

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

  const postRecentOpeningMutation = useMutation({
    mutationFn: (openingId: number) => putUserRecentOpening(openingId)
  });

  const handleRowClick = () => {
    if (enableClick) {
      postRecentOpeningMutation.mutate(rowData.openingId);
    }
    if (handleComingSoon) {
      handleComingSoon(rowData);
    }
  }

  return (
    <TableRow
      className={`opening-table-row${enableClick ? ' clickable-opening-row' : ''}`}
      onClick={handleRowClick}
    >
      {
        headers
          .filter((header) => header.selected)
          .map((header) => (
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

export default OpeningTableRow;
