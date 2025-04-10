import React from "react";

import {
  CropGrowth as CropGrowthIcon,
  Security as SecurityIcon,
  Layers as LayersIcon
} from "@carbon/icons-react";

import { AcceptableSpeciesHeaders, DummyStandardUnits, PreferredSpeciesHeaders } from "./constants";


import { Accordion, AccordionItem, Column, Grid, Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@carbon/react";
import AcoordionTitle from "./AccordionTitle";
import CardItem from "../../Card/CardItem";
import { CardTitle } from "../../Card";

import './styles.scss';
import { codeDescriptionToDisplayText } from "../../../utils/multiSelectUtils";

const OpeningStandardUnits = () => {
  return (
    <Grid className="opening-standard-units-grid default-grid">
      <Column sm={4} md={8} lg={16}>
        <h3 className="standard-units-title">
          {
            `${DummyStandardUnits.length
              ? DummyStandardUnits.length
              : 'No'
            }
            standard unit${DummyStandardUnits.length > 1 ? 's' : ''}
            in the opening area`
          }
        </h3>
      </Column>

      {
        DummyStandardUnits.map((standardUnit) => (
          <Column sm={4} md={8} lg={16} className="accordion-col" key={standardUnit.standardUnitId}>
            <Accordion
              className="standard-unit-accordion"
              align="end"
              size="lg"
            >
              <AccordionItem
                className="standard-unit-accordion-item"
                title={<AcoordionTitle standardUnit={standardUnit} />}
              >
                <Grid className="standard-unit-content-grid">

                  <Column sm={4} md={8} lg={16}>
                    <Grid className="standard-unit-content-subgrid">
                      <Column sm={4} md={8} lg={16}>
                        <CardItem label="Net area to be reforested (ha)"></CardItem>
                      </Column>

                      <Column sm={4} md={8} lg={16}>
                        <CardItem label="Max soil allowable disturbance (%)"></CardItem>
                      </Column>

                      <Column sm={4} md={8} lg={16}>
                        <CardItem label="BEC information"></CardItem>
                      </Column>

                      <Column sm={4} md={8} lg={16}>
                        <CardItem label="Comment"></CardItem>
                      </Column>
                    </Grid>
                  </Column>

                  <Column sm={4} md={8} lg={16}>
                    <hr />
                  </Column>

                  <Column sm={4} md={8} lg={16}>
                    <CardTitle title="Stocking standard" subtitle="Linked to existing stocking standard ID" />
                  </Column>

                  <Column sm={4} md={8} lg={16}>
                    <section className="section-title-with-icon">
                      <CropGrowthIcon size={20} />
                      <h4>
                        {`${standardUnit.preferredSpecies.length + standardUnit.acceptableSpecies.length} species`}
                      </h4>
                    </section>
                  </Column>

                  <Column sm={4} md={8} lg={10} xlg={8}>
                    <div className="species-table-container">
                      {/* Preferred Species */}
                      <Table
                        className="default-zebra-table-with-border"
                        aria-label="Recent openings table"
                        useZebraStyles
                      >
                        <TableHead>
                          <TableRow>
                            {
                              PreferredSpeciesHeaders.map((header) => (
                                <TableHeader key={header.key}>{header.header}</TableHeader>
                              ))
                            }
                          </TableRow>
                        </TableHead>
                        <TableBody>
                          {
                            standardUnit.preferredSpecies.map((row) => (
                              <TableRow key={row.code}>
                                <TableCell>
                                  {codeDescriptionToDisplayText(row)}
                                </TableCell>
                                <TableCell>
                                  {row.minHeight}
                                </TableCell>
                              </TableRow>
                            ))
                          }
                        </TableBody>
                      </Table>
                      {/* Acceptable Species */}
                      <Table
                        className="default-zebra-table-with-border"
                        aria-label="Recent openings table"
                        useZebraStyles
                      >
                        <TableHead>
                          <TableRow>
                            {
                              AcceptableSpeciesHeaders.map((header) => (
                                <TableHeader key={header.key}>{header.header}</TableHeader>
                              ))
                            }
                          </TableRow>
                        </TableHead>
                        <TableBody>
                          {
                            standardUnit.acceptableSpecies.map((row) => (
                              <TableRow key={row.code}>
                                <TableCell>
                                  {codeDescriptionToDisplayText(row)}
                                </TableCell>
                                <TableCell>
                                  {row.minHeight}
                                </TableCell>
                              </TableRow>
                            ))
                          }
                        </TableBody>
                      </Table>
                    </div>
                  </Column>

                  <Column sm={4} md={8} lg={16}>
                    <section className="section-title-with-icon">
                      <SecurityIcon size={20} />
                      <h4>Regen obligation</h4>
                    </section>
                  </Column>

                  <Column sm={4} md={8} lg={16}>
                    <Grid className="standard-unit-content-subgrid">
                      <Column sm={4} md={8} lg={16}>
                        <CardItem label="Regen delay"></CardItem>
                      </Column>
                      <Column sm={4} md={8} lg={16}>
                        <CardItem label="Free growing late"></CardItem>
                      </Column>
                    </Grid>
                  </Column>

                  <Column sm={4} md={8} lg={16}>
                    <section className="section-title-with-icon">
                      <LayersIcon size={20} />
                      <h4>Single layer</h4>
                    </section>
                  </Column>

                  <Column sm={4} md={8} lg={16}>
                    <Grid className="standard-unit-content-subgrid">
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Minimum well-spaced trees"></CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Minimum preferred well-spaced trees"></CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Minimum horizontal distance well-spaced trees (m)"></CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Target well-spaced trees (ha)"></CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Minimum residual basal area (m²/ha)"></CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Minimum post-spacing density (st/ha)"></CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Maximum post-spacing density (st/ha)"></CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Maximum Coniferous (st/ha)"></CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Height relative to comp (cm/%)"></CardItem>
                      </Column>
                    </Grid>
                  </Column>
                </Grid>
              </AccordionItem>
            </Accordion>
          </Column>
        ))
      }
    </Grid >
  );
};


export default OpeningStandardUnits;
