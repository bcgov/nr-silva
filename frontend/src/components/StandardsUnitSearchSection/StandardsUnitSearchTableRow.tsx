import { Link } from "react-router-dom";
import { TableRow, TableCell, Button, DefinitionTooltip, Stack, Tooltip } from "@carbon/react";
import { Launch, Warning } from "@carbon/icons-react";
import { formatLocalDate } from "@/utils/DateUtils";
import { PLACE_HOLDER, UNIQUE_CHARACTERS_UNICODE } from "@/constants";
import { MAP_KINDS } from "@/constants/mapKindConstants";
import { StandardsUnitHeaderKeyType, StandardsUnitHeaderType } from "@/types/TableHeader";
import { StandardUnitSearchResponseDto } from "@/services/OpenApi";
import { OpeningDetailsRoute } from "@/routes/config";
import StackedTooltip from "@/components/StackedTooltip";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import { getClientLabel, getClientSimpleLabel } from "@/utils/ForestClientUtils";
import usePolygonAvailability from "@/hooks/usePolygonAvailability";
import SpatialCheckbox from "@/components/SpatialCheckbox";
import { PREFERRED_SPECIES_LIMIT } from "./constants";

import "./styles.scss";



type props = {
  headers: StandardsUnitHeaderType[];
  rowData: StandardUnitSearchResponseDto;
  showMap: boolean;
  selectedRows: number[];
  handleRowSelection: (openingId: number, compoundId: string) => void;
}

const StandardsUnitSearchTableRow = ({
  headers,
  rowData,
  showMap,
  selectedRows,
  handleRowSelection,
}: props) => {

  const compoundId = `${rowData.stockingStandardUnitId}`;
  const { isAvailable, isLoading: isAvailabilityLoading } = usePolygonAvailability(
    rowData.openingId!,
    MAP_KINDS.standardsUnit,
    compoundId,
  );
  const openingUrl = OpeningDetailsRoute.path!.replace(
    ":openingId",
    `${rowData.openingId!.toString()}?tab=standards-units`
  );

  const openInNewTab = () => {
    window.open(openingUrl, '_blank', 'noopener,noreferrer');
  };

  const renderCellContent = (header: StandardsUnitHeaderKeyType) => {
    switch (header) {
      case "actions":
        return (
          <div className="action-container">
            {
              showMap ? (
                <SpatialCheckbox
                  spatialType="standards unit"
                  rowId={rowData.stockingStandardUnitId!}
                  selectedRows={selectedRows}
                  isAvailable={isAvailable}
                  isLoading={isAvailabilityLoading}
                  handleRowSelection={(standardsUnitId) => {
                    handleRowSelection(rowData.openingId!, `${standardsUnitId}`);
                  }}
                />
              ) : null
            }
            <Button
              hasIconOnly
              className="new-tab-button"
              renderIcon={Launch}
              iconDescription={`View standards unit ${rowData.stockingStandardUnitId} in a new tab`}
              tooltipPosition="right"
              size="sm"
              kind="ghost"
              onClick={openInNewTab}
            />
          </div>
        );

      case "standardsRegimeId":
        return (
          <Stack gap={1} orientation="horizontal">
            <span>{rowData.standardsRegimeId ?? PLACE_HOLDER}</span>
            {
              rowData.isStandardsRegimeExpired
                ? (
                  <Tooltip definition="Expired stocking standard">
                    <Warning size={16} className="expired-standard-warning-icon" />
                  </Tooltip>
                )
                : null
            }

          </Stack>
        );

      case "dueDates":
        return (
          <Stack gap={1} className="due-dates-cell">
            <span>Regeneration: {formatLocalDate(rowData.regenDueDate, true)}</span>
            <span>Free growing: {formatLocalDate(rowData.freeGrowingDueDate, true)}</span>
          </Stack>
        );

      case "preferredSpecies":
        if (!rowData.preferredSpecies || rowData.preferredSpecies.length === 0) {
          return PLACE_HOLDER;
        }

        return (
          <StackedTooltip
            items={rowData.preferredSpecies}
            unit="species"
            displayLimit={PREFERRED_SPECIES_LIMIT}
            align="left"
          />
        );

      case "bgc":
        return [
          rowData.bgcZone,
          rowData.bgcSubZone,
          rowData.bgcVariant,
          rowData.bgcPhase,
          rowData.becSiteSeries,
          rowData.becSiteType,
          rowData.becSeral,
        ]
          .map(val => val ?? '-')
          .join('.');

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

      case "openingClient":
        if (rowData.openingClient) {
          return (
            <DefinitionTooltip
              openOnHover
              definition={getClientLabel(rowData.openingClient)}
              align="left"
            >
              <span>{getClientSimpleLabel(rowData.openingClient)}</span>
            </DefinitionTooltip>
          );
        }
        return PLACE_HOLDER;

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
    <TableRow className="standards-unit-search-table-row" data-testid={`standards-unit-search-table-row-${rowData.stockingStandardUnitId}`}>
      {headers
        .filter((header) => header.selected)
        .map((header) => (
          <TableCell key={header.key} data-testid={`standards-unit-search-table-cell-${header.key}-${rowData.stockingStandardUnitId}`}>
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

export default StandardsUnitSearchTableRow;
