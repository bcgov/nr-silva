import { useNavigate, Link } from "react-router-dom";
import { TableRow, TableCell, Button, DefinitionTooltip } from "@carbon/react";
import { Launch } from "@carbon/icons-react";
import { formatLocalDate } from "@/utils/DateUtils";
import { PLACE_HOLDER } from "@/constants";
import { MAP_KINDS } from "@/constants/mapKindConstants";
import { DisturbanceHeaderKeyType, DisturbanceHeaderType } from "@/types/TableHeader";
import { DisturbanceSearchResponseDto } from "@/services/OpenApi";
import { OpeningDetailsRoute } from "@/routes/config";
import { getClientLabel, getClientSimpleLabel } from "@/utils/ForestClientUtils";
import usePolygonAvailability from "@/hooks/usePolygonAvailability";
import SpatialCheckbox from "../SpatialCheckbox";


type props = {
  headers: DisturbanceHeaderType[];
  rowData: DisturbanceSearchResponseDto;
  showMap: boolean;
  selectedRows: number[];
  handleRowSelection: (openingId: number, compoundId: string) => void;
}

const DisturbanceSearchTableRow = ({
  headers,
  rowData,
  showMap,
  selectedRows,
  handleRowSelection,
}: props) => {
  const navigate = useNavigate();

  const compoundId = `${rowData.activityId}-DN`;
  const { isAvailable, isLoading: isAvailabilityLoading } = usePolygonAvailability(
    rowData.openingId!,
    MAP_KINDS.activityTreatment,
    compoundId,
  );
  const openingUrl = OpeningDetailsRoute.path!.replace(
    ":openingId",
    `${rowData.openingId!.toString()}?tab=activities`
  );

  const navToOpening = () => {
    navigate(openingUrl)
  }

  const openInNewTab = () => {
    window.open(openingUrl, '_blank', 'noopener,noreferrer');
  };

  const renderCellContent = (header: DisturbanceHeaderKeyType) => {
    switch (header) {
      case "actions":
        return (
          <div className="action-container">
            {
              showMap ? (
                <SpatialCheckbox
                  spatialType="disturbance"
                  rowId={rowData.activityId!}
                  selectedRows={selectedRows}
                  isAvailable={isAvailable}
                  isLoading={isAvailabilityLoading}
                  handleRowSelection={(activityId) => {
                    handleRowSelection(rowData.openingId!, `${activityId}-DN`);
                  }}
                />
              ) : null
            }
            <Button
              hasIconOnly
              className="new-tab-button"
              renderIcon={Launch}
              iconDescription={`View disturbance ${rowData.activityId} in a new tab`}
              tooltipPosition="right"
              size="sm"
              kind="ghost"
              onClick={openInNewTab}
            />
          </div>
        );
      case "disturbance":
        if (rowData.disturbance) {
          return `${rowData.disturbance.code} - ${rowData.disturbance.description}`;
        }
        return PLACE_HOLDER;
      case "silvSystem":
        if (rowData.silvSystem) {
          return (
            <DefinitionTooltip
              openOnHover
              definition={rowData.silvSystem.description}
              align="right"
            >
              <span>{rowData.silvSystem.code}</span>
            </DefinitionTooltip>
          );
        }
        return PLACE_HOLDER;
      case "variant":
        if (rowData.variant) {
          return (
            <DefinitionTooltip
              openOnHover
              definition={rowData.variant.description}
              align="right"
            >
              <span>{rowData.variant.code}</span>
            </DefinitionTooltip>
          );
        }
        return PLACE_HOLDER;
      case "cutPhase":
        if (rowData.cutPhase) {
          return (
            <DefinitionTooltip
              openOnHover
              definition={rowData.cutPhase.description}
              align="right"
            >
              <span>{rowData.cutPhase.code}</span>
            </DefinitionTooltip>
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
    <TableRow className="disturbance-table-row" data-testid={`disturbance-table-row-${rowData.activityId}`}>
      {headers
        .filter((header) => header.selected)
        .map((header) => (
          <TableCell key={header.key} data-testid={`disturbance-table-cell-${header.key}-${rowData.activityId}`}>
            {
              header.key !== "actions" ? (
                <Link className="default-table-cell-link-wrapper" onClick={navToOpening} to={openingUrl}>
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

export default DisturbanceSearchTableRow;
