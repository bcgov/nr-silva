import React from "react";
import { LayerDto } from "../../definitions";
import { Column, Grid, Table, TableBody, TableContainer, TableHead, TableHeader, TableRow } from "@carbon/react";
import { LayerTableHeaders } from "./constants";

type LayerTableProps = {
  layer: LayerDto;
}

const LayerTable = ({ layer }: LayerTableProps) => {

  return (
    <Grid>
      <Column sm={4} md={8} lg={16}>
        <TableContainer className="default-table-container">
          <div>
            fs
          </div>
          <Table className="default-zebra-table-with-border child-table" aria-label="Unmapped area table">
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
              {
                data.map((row) => {
                  return (
                    <TableRow key={row.unmappedAreaId}>
                      {
                        UnmappedAreaHeaders.map((header) => {
                          return (
                            <TableCell key={header.key}>
                              {renderCellContent(row, header.key)}
                            </TableCell>
                          )
                        })
                      }
                    </TableRow>
                  )
                })
              }
            </TableBody>
          </Table>
        </TableContainer>
      </Column>

    </Grid>
  );
};

export default LayerTable;
