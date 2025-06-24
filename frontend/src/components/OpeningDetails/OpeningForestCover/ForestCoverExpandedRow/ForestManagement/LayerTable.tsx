import React from "react";
import {
  Column, DefinitionTooltip, Grid,
  Table, TableBody, TableCell, TableContainer,
  TableHead, TableHeader, TableRow
} from "@carbon/react";
import { TreeFallRisk } from "@carbon/icons-react";
import { NOT_APPLICABLE, PLACE_HOLDER, UNIQUE_CHARACTERS_UNICODE } from "@/constants";
import { OpeningForestCoverDamageDto, OpeningForestCoverLayerDto } from "@/services/OpenApi";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import { DamageAgentTableHeader, LayerTableHeaders } from "./constants";

type LayerTableProps = {
  layer?: OpeningForestCoverLayerDto;
}

const LayerTable = ({ layer }: LayerTableProps) => {
  if (!layer) {
    return null;
  }

  const renderDamageAgentCell = (row: OpeningForestCoverDamageDto, key: keyof OpeningForestCoverDamageDto) => {
    switch (key) {
      case 'damageAgent':
        return codeDescriptionToDisplayText(row.damageAgent);
      case 'healthIncidencePercentage':
        return row.healthIncidencePercentage ? `${row.healthIncidencePercentage}%` : PLACE_HOLDER;
      case 'incidenceArea':
        return row.incidenceArea ? `${row.incidenceArea} ha` : PLACE_HOLDER;
      default:
        return String(row[key]);
    }
  }

  return (
    <Grid className="layer-table-grid">
      <Column sm={4} md={8} lg={16}>
        <TableContainer className="default-table-container">
          <div className="layer-table-title">
            {`${layer.species.length} species`}
          </div>
          <Table className="default-zebra-table child-table" aria-label="Layer table">
            <TableHead>
              <TableRow>
                {
                  LayerTableHeaders.map((header) => (
                    <TableHeader key={header.key}>{header.header}</TableHeader>
                  ))
                }
              </TableRow>
            </TableHead>

            <TableBody>
              <TableRow>
                {/* Species • Distribution */}
                <TableCell className="default-table-cell">
                  <ul className="cell-content-list">
                    {layer.species.map((species) => (
                      <li key={species.species.code}>
                        <DefinitionTooltip
                          openOnHover
                          definition={species.species.description}
                          align="right"
                        >
                          {`${species.species.code}`}
                        </DefinitionTooltip>
                        {' '}
                        {`${UNIQUE_CHARACTERS_UNICODE.BULLET} ${species.percentage}%`}
                      </li>
                    ))}
                  </ul>
                </TableCell>

                {/* Average age  • Average height */}
                <TableCell className="default-table-cell">
                  <ul className="cell-content-list">
                    {layer.species.map((species) => (
                      <li key={species.species.code}>
                        {
                          `
                          ${species.averageAge ? species.averageAge : PLACE_HOLDER}
                          ${UNIQUE_CHARACTERS_UNICODE.BULLET}
                          ${species.averageAge ? `${species.averageHeight} m` : PLACE_HOLDER}
                          `
                        }
                      </li>
                    ))}
                  </ul>
                </TableCell>

                <TableCell className="default-table-cell">
                  {layer.crownClosure ? `${layer.crownClosure}%` : PLACE_HOLDER}
                </TableCell>

                <TableCell className="default-table-cell">
                  {layer.basalAreaSt ? `${layer.basalAreaSt} (m²/ha)` : PLACE_HOLDER}
                </TableCell>

                {/* Stems */}
                <TableCell className="default-table-cell">
                  <ul className="cell-content-list">
                    <li>Total stems: {layer.totalStems ? `${layer.totalStems} (st/ha)` : NOT_APPLICABLE}</li>
                    <li>Total well spaced: {layer.totalWellSpaced ? `${layer.totalWellSpaced} (st/ha)` : NOT_APPLICABLE}</li>
                    <li>Well spaced: {layer.wellSpaced ? `${layer.wellSpaced} (st/ha)` : NOT_APPLICABLE}</li>
                    <li>Free growing: {layer.freeGrowing ? `${layer.freeGrowing} (st/ha)` : NOT_APPLICABLE}</li>
                  </ul>
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </Column>

      {
        layer.damage?.length
          ? (
            <>
              <Column sm={4} md={8} lg={16}>
                <div className="card-title-container">
                  <div className="icon-and-title">
                    <TreeFallRisk size={20} />
                    <h4>
                      Damage agent
                    </h4>
                  </div>
                  <p className="card-subtitle">Biotic or abiotic factor causing harm to forest ecosystems</p>
                </div>
              </Column>
              <Column sm={4} md={8} lg={16}>
                <div className="damage-agent-table-container">
                  <Table className="default-zebra-table-with-border child-table" aria-label="Damage agent table">
                    <TableHead>
                      <TableRow>
                        {
                          DamageAgentTableHeader.map((header) => (
                            <TableHeader key={header.key}>{header.header}</TableHeader>
                          ))
                        }
                      </TableRow>
                    </TableHead>

                    <TableBody>
                      {
                        layer.damage.map((row) => {
                          return (
                            <TableRow key={row.damageAgent.code}>
                              {
                                DamageAgentTableHeader.map((header) => (
                                  <TableCell key={header.key}>
                                    {renderDamageAgentCell(row, header.key)}
                                  </TableCell>
                                ))
                              }
                            </TableRow>
                          )
                        })
                      }
                    </TableBody>
                  </Table>
                </div>
              </Column>
            </>
          )
          : null
      }

    </Grid>
  );
};

export default LayerTable;
