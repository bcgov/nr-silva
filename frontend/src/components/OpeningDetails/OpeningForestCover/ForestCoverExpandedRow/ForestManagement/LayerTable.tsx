import React from "react";
import { LayerDto } from "../../definitions";
import { Column, DefinitionTooltip, Grid, Table, TableBody, TableCell, TableContainer, TableHead, TableHeader, TableRow } from "@carbon/react";
import { LayerTableHeaders } from "./constants";
import { NOT_APPLICABLE, UNIQUE_CHARACTERS_UNICODE } from "../../../../../constants";

type LayerTableProps = {
  layer: LayerDto;
}

const LayerTable = ({ layer }: LayerTableProps) => {

  return (
    <Grid>
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
                      <li>
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
                      <li>
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

    </Grid>
  );
};

export default LayerTable;
