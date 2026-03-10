// TableRowComponent.tsx

import React from "react";
import { Link } from "react-router-dom";
import { TableRow, TableCell, Button, DefinitionTooltip } from "@carbon/react";
import { Launch } from "@carbon/icons-react";
import { formatLocalDate } from "@/utils/DateUtils";
import { PLACE_HOLDER } from "@/constants";
import { MAP_KINDS } from "@/constants/mapKindConstants";
import { OpendingHeaderKeyType, TableHeaderType } from "@/types/TableHeader";
import { OpeningSearchResponseDto } from "@/services/OpenApi";
import { OpeningDetailsRoute } from "@/routes/config";

import usePolygonAvailability from "@/hooks/usePolygonAvailability";
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
  const openingUrl = OpeningDetailsRoute.path!.replace(
    ":openingId",
    rowData.openingId.toString()
  );

  const { isAvailable, isLoading: isAvailabilityLoading } = usePolygonAvailability(
    rowData.openingId,
    MAP_KINDS.opening,
    showMap ? `${rowData.openingId}` : null,
  );

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
                  spatialType="opening"
                  rowId={rowData.openingId}
                  selectedRows={selectedRows}
                  handleRowSelection={handleRowSelection}
                  isAvailable={isAvailable}
                  isLoading={isAvailabilityLoading}
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
              <Link className="default-table-cell-link-wrapper" to={openingUrl}>
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
