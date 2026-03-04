import { useNavigate, Link } from "react-router-dom";
import { TableRow, TableCell, Button, DefinitionTooltip } from "@carbon/react";
import { Launch } from "@carbon/icons-react";
import { formatLocalDate } from "@/utils/DateUtils";
import { PLACE_HOLDER } from "@/constants";
import { MAP_KINDS } from "@/constants/mapKindConstants";
import { ActivityHeaderKeyType, ActivityHeaderType } from "@/types/TableHeader";
import { ActivitySearchResponseDto } from "@/services/OpenApi";
import { OpeningDetailsRoute } from "@/routes/config";
import { getClientLabel, getClientSimpleLabel } from "@/utils/ForestClientUtils";
import usePolygonAvailability from "@/hooks/usePolygonAvailability";
import SpatialCheckbox from "../SpatialCheckbox";
import { ActivityStatusTag } from "../Tags";


import "./styles.scss";
type props = {
  headers: ActivityHeaderType[];
  rowData: ActivitySearchResponseDto;
  showMap: boolean;
  selectedRows: number[];
  handleRowSelection: (openingId: number, compoundId: string) => void;
}

const ActivitySearchTableRow = ({
  headers,
  rowData,
  showMap,
  selectedRows,
  handleRowSelection,
}: props) => {
  const navigate = useNavigate();

  const compoundId = `${rowData.activityId}-${rowData.base?.code ?? ''}`;
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

  const renderCellContent = (header: ActivityHeaderKeyType) => {
    switch (header) {
      case "actions":
        return (
          <div className="action-container">
            {
              showMap ? (
                <SpatialCheckbox
                  spatialType="activity"
                  rowId={rowData.activityId!}
                  selectedRows={selectedRows}
                  isAvailable={isAvailable}
                  isLoading={isAvailabilityLoading}
                  handleRowSelection={(activityId) => {
                    handleRowSelection(rowData.openingId!, `${activityId}-${rowData.base?.code ?? ''}`);
                  }}
                />
              ) : null
            }
            <Button
              hasIconOnly
              className="new-tab-button"
              renderIcon={Launch}
              iconDescription={`View activity ${rowData.activityId} in a new tab`}
              tooltipPosition="right"
              size="sm"
              kind="ghost"
              onClick={openInNewTab}
            />
          </div>
        );
      case "base":
        if (rowData.base) {
          return (
            <DefinitionTooltip
              openOnHover
              definition={rowData.base.description}
              align="right"
            >
              <span>{rowData.base.code}</span>
            </DefinitionTooltip>
          );
        }
        return PLACE_HOLDER;
      case "technique":
        if (rowData.technique) {
          return (
            <DefinitionTooltip
              openOnHover
              definition={rowData.technique.description}
              align="right"
            >
              <span>{rowData.technique.code}</span>
            </DefinitionTooltip>
          );
        }
        return PLACE_HOLDER;
      case "method":
        if (rowData.method) {
          return (
            <DefinitionTooltip
              openOnHover
              definition={rowData.method.description}
              align="right"
            >
              <span>{rowData.method.code}</span>
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
      case "fundingSource":
        if (rowData.fundingSource) {
          return (
            <DefinitionTooltip
              openOnHover
              definition={rowData.fundingSource.description}
              align="left"
            >
              <span>{rowData.fundingSource.code}</span>
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
      case "isComplete":
        if (rowData.isComplete !== undefined) {
          return <ActivityStatusTag isComplete={rowData.isComplete} />
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
    <TableRow className="activity-table-row" data-testid={`activity-table-row-${rowData.activityId}`}>
      {headers
        .filter((header) => header.selected)
        .map((header) => (
          <TableCell key={header.key} data-testid={`activity-table-cell-${header.key}-${rowData.activityId}`}>
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

export default ActivitySearchTableRow;
