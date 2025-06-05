// TableRowComponent.tsx

import React from "react";
import { useNavigate, Link } from "react-router-dom";
import { TableRow, TableCell, Button, DefinitionTooltip } from "@carbon/react";
import { Launch } from "@carbon/icons-react";

import OpeningStatusTag from "../OpeningStatusTag";
import SpatialCheckbox from "../SpatialCheckbox";
import ActionButtons from "../ActionButtons";
import { formatLocalDate } from "@/utils/DateUtils";
import { PLACE_HOLDER } from "@/constants";
import { OpendingHeaderKeyType, TableHeaderType } from "@/types/TableHeader";
import { OpeningSearchResponseDto } from "@/types/OpenApiTypes";

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
            <ActionButtons
              favorited={rowData.favourite}
              rowId={rowData.openingId.toString()}
              showToast
            />
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
