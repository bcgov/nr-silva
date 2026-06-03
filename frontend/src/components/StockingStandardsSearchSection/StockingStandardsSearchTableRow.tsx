import { useState } from 'react';
import { TableRow, TableCell, Stack, Tooltip, Button, DefinitionTooltip, Tag } from "@carbon/react";
import { Warning } from "@carbon/icons-react";
import { PLACE_HOLDER, PREFERRED_SPECIES_LIMIT } from "@/constants";
import { StockingStandardsHeaderKeyType, StockingStandardsHeaderType } from "@/types/TableHeader";
import { CodeDescriptionDto, ForestClientDto, StockingStandardsSearchResponseDto } from "@/services/OpenApi";
import { formatLocalDate } from "@/utils/DateUtils";
import { getClientLabel, getClientSimpleLabel } from '@/utils/ForestClientUtils';
import StackedTooltip from "../StackedTooltip";

import "./styles.scss";

type props = {
  headers: StockingStandardsHeaderType[];
  rowData: StockingStandardsSearchResponseDto;
}

const StockingStandardsSearchTableRow = ({ headers, rowData }: props) => {
  const [isBgcExpanded, setIsBgcExpanded] = useState(false);
  const [isClientsExpanded, setIsClientsExpanded] = useState(false);
  const [isFspExpanded, setIsFspExpanded] = useState(false);
  const [isOrgUnitsExpanded, setIsOrgUnitsExpanded] = useState(false);

  const renderCellContent = (header: StockingStandardsHeaderKeyType) => {
    switch (header) {
      case "standardsRegimeId":
        return (
          <div className="ssid-cell">
            <span>{rowData.standardsRegimeId ?? PLACE_HOLDER}</span>
            {
              rowData.isDefaultStandard
                ? (
                  <Tag size="sm" type="blue" className="ministry-default-tag">
                    Default
                  </Tag>
                )
                : null
            }
            {
              rowData.isExpired
                ? (
                  <Tooltip label="Expired stocking standard" align="right">
                    <Warning size={16} className="default-warning-icon" />
                  </Tooltip>
                )
                : null
            }
          </div>
        );

      case "bgcList": {
        if (!rowData.bgcList?.length) return PLACE_HOLDER;
        if (rowData.bgcList.length === 1) return rowData.bgcList[0];

        if (isBgcExpanded) {
          return (
            <div className="expandable-items--expanded">
              <div className="bgc-list">
                {rowData.bgcList.map((bgc, index) => (
                  <span key={`${bgc}-${index}`} className="bgc-list__item">{bgc}</span>
                ))}
              </div>
              <Button
                type="button"
                kind="ghost"
                size="sm"
                className="expand-button"
                onClick={() => setIsBgcExpanded(false)}
              >
                show fewer
              </Button>
            </div>
          );
        }

        return (
          <span className="expandable-items--collapsed">
            <span>{rowData.bgcList[0]}</span>
            <span>and</span>
            <Tooltip label="show more BGC entries">
              <Button
                type="button"
                kind="ghost"
                size="sm"
                className="expand-button"
                onClick={() => setIsBgcExpanded(true)}
              >
                {rowData.bgcList.length - 1} more
              </Button>
            </Tooltip>
          </span>
        );
      }

      case "clients": {
        if (!rowData.clients?.length) return PLACE_HOLDER;

        const clientTooltip = (c: ForestClientDto) => (
          <DefinitionTooltip openOnHover definition={getClientLabel(c)} align="left">
            <span>{getClientSimpleLabel(c)}</span>
          </DefinitionTooltip>
        );

        if (rowData.clients.length === 1) {
          return rowData.clients[0] ? clientTooltip(rowData.clients[0]) : PLACE_HOLDER;
        }

        if (isClientsExpanded) {
          return (
            <div className="expandable-items--expanded">
              <div className="expandable-items__list">
                {rowData.clients.map((c, index) => (
                  <span key={c.clientNumber ?? `client-${index}`} className="expandable-items__item">
                    {clientTooltip(c)}
                  </span>
                ))}
              </div>
              <Button
                type="button"
                kind="ghost"
                size="sm"
                className="expand-button"
                onClick={() => setIsClientsExpanded(false)}
              >
                show fewer
              </Button>
            </div>
          );
        }

        return (
          <span className="expandable-items--collapsed">
            {rowData.clients[0] ? clientTooltip(rowData.clients[0]) : null}
            <span>and</span>
            <Tooltip label="show more clients">
              <Button
                type="button"
                kind="ghost"
                size="sm"
                className="expand-button"
                onClick={() => setIsClientsExpanded(true)}
              >
                {rowData.clients.length - 1} more
              </Button>
            </Tooltip>
          </span>
        );
      }

      case "fspIds": {
        if (!rowData.fspIds?.length) return PLACE_HOLDER;
        if (rowData.fspIds.length === 1) return rowData.fspIds[0];

        if (isFspExpanded) {
          return (
            <div className="expandable-items--expanded">
              <div className="expandable-items__list">
                {rowData.fspIds.map((id) => (
                  <span key={id} className="expandable-items__item">{id}</span>
                ))}
              </div>
              <Button
                type="button"
                kind="ghost"
                size="sm"
                className="expand-button"
                onClick={() => setIsFspExpanded(false)}
              >
                show fewer
              </Button>
            </div>
          );
        }

        return (
          <span className="expandable-items--collapsed">
            <span>{rowData.fspIds[0]}</span>
            <span>and</span>
            <Tooltip label="show more FSP IDs">
              <Button
                type="button"
                kind="ghost"
                size="sm"
                className="expand-button"
                onClick={() => setIsFspExpanded(true)}
              >
                {rowData.fspIds.length - 1} more
              </Button>
            </Tooltip>
          </span>
        );
      }

      case "orgUnits": {
        if (!rowData.orgUnits?.length) return PLACE_HOLDER;

        const orgUnitTooltip = (ou: CodeDescriptionDto) => (
          <DefinitionTooltip openOnHover definition={ou.description ?? ''} align="left">
            <span>{ou.code ?? PLACE_HOLDER}</span>
          </DefinitionTooltip>
        );

        if (rowData.orgUnits.length === 1) {
          return rowData.orgUnits[0] ? orgUnitTooltip(rowData.orgUnits[0]) : PLACE_HOLDER;
        }

        if (isOrgUnitsExpanded) {
          return (
            <div className="expandable-items--expanded">
              <div className="expandable-items__list">
                {rowData.orgUnits.map((ou, index) => (
                  <span key={ou.code ?? `org-unit-${index}`} className="expandable-items__item">
                    {orgUnitTooltip(ou)}
                  </span>
                ))}
              </div>
              <Button
                type="button"
                kind="ghost"
                size="sm"
                className="expand-button"
                onClick={() => setIsOrgUnitsExpanded(false)}
              >
                show fewer
              </Button>
            </div>
          );
        }

        return (
          <span className="expandable-items--collapsed">
            {rowData.orgUnits[0] ? orgUnitTooltip(rowData.orgUnits[0]) : null}
            <span>and</span>
            <Tooltip label="show more org units">
              <Button
                type="button"
                kind="ghost"
                size="sm"
                className="expand-button"
                onClick={() => setIsOrgUnitsExpanded(true)}
              >
                {rowData.orgUnits.length - 1} more
              </Button>
            </Tooltip>
          </span>
        );
      }

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


      case "approvedDate":
        return formatLocalDate(rowData.approvedDate, true);

      default:
        const value = rowData[header];
        if (value !== undefined && value !== null) {
          return String(value);
        }
        return PLACE_HOLDER;
    }
  };

  return (
    <TableRow data-testid={`stocking-standards-search-table-row-${rowData.standardsRegimeId}`}>
      {headers
        .filter((header) => header.selected)
        .map((header) => (
          <TableCell
            key={header.key}
            data-testid={`stocking-standards-search-table-cell-${header.key}-${rowData.standardsRegimeId}`}
          >
            {renderCellContent(header.key) ?? PLACE_HOLDER}
          </TableCell>
        ))}
    </TableRow>
  );
};

export default StockingStandardsSearchTableRow;
