// TableRowComponent.tsx

import React from "react";
import { useNavigate, Link } from "react-router-dom";
import { TableRow, TableCell, Button, DefinitionTooltip } from "@carbon/react";
import { Launch } from "@carbon/icons-react";
import { formatLocalDate } from "@/utils/DateUtils";
import { PLACE_HOLDER } from "@/constants";
import { OpendingHeaderKeyType, TableHeaderType } from "@/types/TableHeader";
import { OpeningSearchResponseDto } from "@/services/OpenApi";
import { OpeningDetailsRoute } from "@/routes/config";

import { OpeningStatusTag } from "../Tags";
import SpatialCheckbox from "../SpatialCheckbox";
import OpeningBookmarkBtn from "../OpeningBookmarkBtn";

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
  const navigate = useNavigate();
  const openingUrl = OpeningDetailsRoute.path!.replace(
    ":openingId",
    rowData.openingId.toString()
  );

  const navToOpening = () => {
    navigate(openingUrl)
  }

  const openInNewTab = () => {
    window.open(openingUrl, '_blank', 'noopener,noreferrer');
  };

  const renderCellContent = (header: OpendingHeaderKeyType) => {
    switch (header) {
      case "status":
        return (
          <OpeningStatusTag status={rowData.status}></OpeningStatusTag>
        );
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
              ) : null
            }
            <OpeningBookmarkBtn openingId={rowData.openingId} tooltipPosition="right" />
            <Button
              hasIconOnly
              className="new-tab-button"
              renderIcon={Launch}
              iconDescription={`Open ${rowData.openingId} in a new tab`}
              tooltipPosition="right"
              size="sm"
              kind="ghost"
              onClick={openInNewTab}
            />
          </div>
        );
      case "category":
        if (rowData.category) {
          return (
            <DefinitionTooltip
              openOnHover
              definition={rowData.category.description}
              align="right"
            >
              <span>{rowData.category.code}</span>
            </DefinitionTooltip>
          );
        }
        return PLACE_HOLDER;

      case "regenDelayDate":
        return formatLocalDate(rowData.regenDelayDate, true);
      case "updateTimestamp":
        return formatLocalDate(rowData.updateTimestamp, true);
      case "earlyFreeGrowingDate":
        return formatLocalDate(rowData.earlyFreeGrowingDate, true);
      case "entryTimestamp":
        return formatLocalDate(rowData.entryTimestamp, true);
      case "disturbanceStartDate":
        return formatLocalDate(rowData.disturbanceStartDate, true);
      default:
        return rowData[header];
    }
  };

  return (
    <TableRow className="opening-table-row" data-testid={`opening-table-row-${rowData.openingId}`}>
      {headers
        .filter((header) => header.selected)
        .map((header) => (
          <TableCell key={header.key} data-testid={`opening-table-cell-${header.key}-${rowData.openingId}`}>
            {header.key !== "actions" ? (
              <Link className="table-cell-link-wrapper" onClick={navToOpening} to={openingUrl}>
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
