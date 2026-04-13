import { Link } from "react-router-dom";
import { TableRow, TableCell, Button, DefinitionTooltip, Stack } from "@carbon/react";
import { Launch } from "@carbon/icons-react";
import { formatLocalDate } from "@/utils/DateUtils";
import { PLACE_HOLDER } from "@/constants";
import { MAP_KINDS } from "@/constants/mapKindConstants";
import { ForestCoverHeaderKeyType, ForestCoverHeaderType } from "@/types/TableHeader";
import { ForestCoverSearchResponseDto } from "@/services/OpenApi";
import { OpeningDetailsRoute } from "@/routes/config";
import usePolygonAvailability from "@/hooks/usePolygonAvailability";
import SpatialCheckbox from "../SpatialCheckbox";
import { StockingStatusTag } from "../Tags";
import StackedTooltip from "../StackedTooltip";
import { DAMAGE_AGENT_DISPLAY_LIMIT } from "./constants";

import "./styles.scss";

type props = {
  headers: ForestCoverHeaderType[];
  rowData: ForestCoverSearchResponseDto;
  showMap: boolean;
  selectedRows: number[];
  handleRowSelection: (openingId: number, compoundId: string) => void;
}

const ForestCoverSearchTableRow = ({
  headers,
  rowData,
  showMap,
  selectedRows,
  handleRowSelection,
}: props) => {

  const compoundId = `${rowData.forestCoverId}-${rowData.polygonId}`;
  const { isAvailable, isLoading: isAvailabilityLoading } = usePolygonAvailability(
    rowData.openingId!,
    MAP_KINDS.forestCoverInventory,
    compoundId,
  );
  const openingUrl = OpeningDetailsRoute.path!.replace(
    ":openingId",
    `${rowData.openingId!.toString()}?tab=forest-cover`
  );

  const openInNewTab = () => {
    window.open(openingUrl, '_blank', 'noopener,noreferrer');
  };

  const renderCellContent = (header: ForestCoverHeaderKeyType) => {
    switch (header) {
      case "actions":
        return (
          <div className="action-container">
            {
              showMap ? (
                <SpatialCheckbox
                  spatialType="forest cover"
                  rowId={rowData.forestCoverId!}
                  selectedRows={selectedRows}
                  isAvailable={isAvailable}
                  isLoading={isAvailabilityLoading}
                  handleRowSelection={(forestCoverId) => {
                    handleRowSelection(rowData.openingId!, `${forestCoverId}-${rowData.polygonId ?? ''}`);
                  }}
                />
              ) : null
            }
            <Button
              hasIconOnly
              className="new-tab-button"
              renderIcon={Launch}
              iconDescription={`View forest cover ${rowData.forestCoverId} in a new tab`}
              tooltipPosition="right"
              size="sm"
              kind="ghost"
              onClick={openInNewTab}
            />
          </div>
        );
      case "forestCoverId":
        return (
          <Stack gap={1} className="forest-cover-id-cell">
            <span>Polygon ID: {rowData.polygonId ?? PLACE_HOLDER}</span>
            <span>Standards unit: {rowData.standardUnitId ?? PLACE_HOLDER}</span>
          </Stack>
        );
      case "damageAgents":
        if (!rowData.damageAgents || rowData.damageAgents.length === 0) {
          return PLACE_HOLDER;
        }

        return (
          <StackedTooltip
            items={rowData.damageAgents}
            unit="agents"
            displayLimit={DAMAGE_AGENT_DISPLAY_LIMIT}
          />
        );
      case "stockingType":
        if (rowData.stockingType) {
          return (
            <DefinitionTooltip
              openOnHover
              definition={rowData.stockingType.description}
              align="right"
            >
              <span>{rowData.stockingType.code}</span>
            </DefinitionTooltip>
          );
        }
        return PLACE_HOLDER;
      case "stockingStatus":
        if (rowData.stockingStatus) {
          return (
            <StockingStatusTag status={rowData.stockingStatus} />
          );
        }
        return PLACE_HOLDER;
      case "orgUnit":
        if (rowData.orgUnit) {
          return (
            <DefinitionTooltip
              openOnHover
              definition={rowData.orgUnit.description}
              align="left"
            >
              <span>{rowData.orgUnit.code}</span>
            </DefinitionTooltip>
          );
        }
        return PLACE_HOLDER;
      case "openingCategory":
        if (rowData.openingCategory) {
          return (
            <DefinitionTooltip
              openOnHover
              definition={rowData.openingCategory.description}
              align="left"
            >
              <span>{rowData.openingCategory.code}</span>
            </DefinitionTooltip>
          );
        }
        return PLACE_HOLDER;
      case "regenDueDate":
        return formatLocalDate(rowData.regenDueDate, true);
      case "freeGrowingDueDate":
        return formatLocalDate(rowData.freeGrowingDueDate, true);
      case "updateTimestamp":
        return formatLocalDate(rowData.updateTimestamp, true);
      default:
        if (rowData[header]) {
          return String(rowData[header]);
        }
        return PLACE_HOLDER;
    }
  };

  return (
    <TableRow className="forest-cover-search-table-row" data-testid={`forest-cover-search-table-row-${rowData.forestCoverId}`}>
      {headers
        .filter((header) => header.selected)
        .map((header) => (
          <TableCell key={header.key} data-testid={`forest-cover-search-table-cell-${header.key}-${rowData.forestCoverId}`}>
            {
              header.key !== "actions" ? (
                <Link className="default-table-cell-link-wrapper" to={openingUrl}>
                  {renderCellContent(header.key) ?? PLACE_HOLDER}
                </Link>
              ) : (
                renderCellContent(header.key) ?? PLACE_HOLDER
              )
            }
          </TableCell>
        ))}
    </TableRow>
  );
};

export default ForestCoverSearchTableRow;
