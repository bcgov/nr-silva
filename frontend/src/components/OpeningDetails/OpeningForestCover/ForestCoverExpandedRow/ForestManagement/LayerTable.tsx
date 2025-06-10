import React from "react";
import { DamageAgentDto, LayerDto } from "../../definitions";
import {
  Column, DefinitionTooltip, Grid,
  Table, TableBody, TableCell, TableContainer,
  TableHead, TableHeader, TableRow
} from "@carbon/react";
import { TreeFallRisk } from "@carbon/icons-react";
import { NOT_APPLICABLE, UNIQUE_CHARACTERS_UNICODE } from "@/constants";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import { DamageAgentTableHeader, LayerTableHeaders } from "./constants";

type LayerTableProps = {
  layer: LayerDto;
}

const LayerTable = ({ layer }: LayerTableProps) => {

  const renderDamageAgentCell = (row: DamageAgentDto, key: keyof DamageAgentDto) => {
    switch (key) {
      case 'species':
        return codeDescriptionToDisplayText(row.species);
      case 'forestHealthIncidence':
        return `${row.forestHealthIncidence}%`;
      case 'incidenceArea':
        return `${row.incidenceArea} ha`;
      default:
        return String(row[key]);
    }
  }

  return (
    <Grid className="layer-table-grid">
      <Column sm={4} md={8} lg={16}>
        <TableContainer className="default-table-container">
          <div className="layer-table-title">
            {`${layer.speciesDistribution.length} species`}
          </div>
          <Table className="default-zebra-table-with-border child-table" aria-label="Layer table">
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
                    {layer.speciesDistribution.map((species) => (
                      <li key={species.species.code}>
                        <DefinitionTooltip
                          openOnHover
                          definition={species.species.description}
                          align="right"
                        >
                          {`${species.species.code}`}
                        </DefinitionTooltip>
                        {' '}
                        {`${UNIQUE_CHARACTERS_UNICODE.BULLET} ${species.distribution}%`}
                      </li>
                    ))}
                  </ul>
                </TableCell>

                {/* Average age  • Average height */}
                <TableCell className="default-table-cell">
                  <ul className="cell-content-list">
                    {layer.speciesDistribution.map((species) => (
                      <li key={species.species.code}>
                        {`${species.averageAge} ${UNIQUE_CHARACTERS_UNICODE.BULLET} ${species.averageHeight} m`}
                      </li>
                    ))}
                  </ul>
                </TableCell>

                <TableCell className="default-table-cell">
                  {`${layer.crownClosure}%`}
                </TableCell>

                <TableCell className="default-table-cell">
                  {`${layer.basalAreaPerTotalStems} (m²/ha)`}
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
        layer.damageAgent?.length
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
                        layer.damageAgent.map((row) => {
                          return (
                            <TableRow key={row.species.code}>
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
