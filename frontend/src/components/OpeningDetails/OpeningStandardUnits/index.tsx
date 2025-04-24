import React from "react";
import {
  Accordion, AccordionItem, Column,
  Grid, Table, TableBody, TableCell,
  TableHead, TableHeader, TableRow
} from "@carbon/react";
import { Link } from "react-router-dom";
import {
  CropGrowth as CropGrowthIcon,
  Security as SecurityIcon,
  Layers as LayersIcon,
  Launch as LaunchIcon
} from "@carbon/icons-react";

import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import AcoordionTitle from "./AccordionTitle";
import CardItem from "../../Card/CardItem";
import { CardTitle } from "../../Card";
import VerticalDivider from "../../VerticalDivider";

import { AcceptableSpeciesHeaders, DummyStandardUnits, PreferredSpeciesHeaders } from "./constants";
import './styles.scss';


const OpeningStandardUnits = () => {
  return (
    <Grid className="opening-standard-units-grid default-grid">
      <Column sm={4} md={8} lg={16}>
        <h3 className="default-tab-content-title">
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
              className="default-tab-accordion"
              align="end"
              size="lg"
            >
              <AccordionItem
                className="default-tab-accordion-item"
                title={<AcoordionTitle standardUnit={standardUnit} />}
              >
                <Grid className="standard-unit-content-grid">

                  <Column sm={4} md={8} lg={16}>
                    <Grid className="standard-unit-content-subgrid">
                      <Column sm={4} md={8} lg={16}>
                        <CardItem label="Net area to be reforested (ha)" isNumber></CardItem>
                      </Column>

                      <Column sm={4} md={8} lg={16}>
                        <CardItem label="Max soil allowable disturbance (%)" isNumber></CardItem>
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
                  {/* Stocking standard */}
                  <Column sm={4} md={8} lg={16}>
                    <CardTitle title="Stocking standard" />
                    <div className="stocking-standard-links">
                      {/* No standard unit id or FSP id */}
                      {
                        (!standardUnit.ssid && !standardUnit.fspId)
                          ? 'Manual stocking requirement'
                          : null
                      }
                      {/* Has standard unit id but no FSP id */}
                      {
                        (standardUnit.ssid && !standardUnit.fspId)
                          ? (
                            <>
                              {`SSID ${standardUnit.ssid}, Stocking objective`}
                              <VerticalDivider />
                              <span>Ministry default</span>
                            </>
                          )
                          : null
                      }
                      {/* Has standard unit id AND FSP id */}
                      {
                        (standardUnit.ssid && standardUnit.fspId)
                          ? (
                            <>
                              {`SSID ${standardUnit.ssid}, Stocking objective`}
                              <VerticalDivider />
                              {
                                <Link
                                  className="fsp-link"
                                  to={`https://apps.nrs.gov.bc.ca/ext/fsp/indexAction.do?fsp_id=${standardUnit.fspId}`}
                                  target="_blank"
                                >
                                  {`FSP ID ${standardUnit.fspId}`} <LaunchIcon />
                                </Link>
                              }
                            </>
                          )
                          : null
                      }
                    </div>
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
                        className="default-expandable-table"
                        aria-label="Preferred species table"

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
                  {/* Regen obligation */}
                  <Column sm={4} md={8} lg={16}>
                    <section className="section-title-with-icon">
                      <SecurityIcon size={20} />
                      <h4>Regen obligation</h4>
                    </section>
                  </Column>

                  <Column sm={4} md={8} lg={16} className="subsection-col">
                    <Grid className="standard-unit-content-subgrid">
                      <Column sm={4} md={8} lg={16}>
                        <CardItem label="Regen delay" isNumber></CardItem>
                      </Column>
                      <Column sm={4} md={8} lg={16}>
                        <CardItem label="Free growing late" isNumber></CardItem>
                      </Column>
                    </Grid>
                  </Column>
                  {/* Single layer */}
                  <Column sm={4} md={8} lg={16}>
                    <section className="section-title-with-icon">
                      <LayersIcon size={20} />
                      <h4>Single layer</h4>
                    </section>
                  </Column>

                  <Column sm={4} md={8} lg={16} className="subsection-col">
                    <Grid className="standard-unit-content-subgrid">
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Minimum well-spaced trees" isNumber></CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Minimum preferred well-spaced trees" isNumber></CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Minimum horizontal distance well-spaced trees (m)" isNumber></CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Target well-spaced trees (ha)" isNumber></CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Minimum residual basal area (m²/ha)" isNumber></CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Minimum post-spacing density (st/ha)" isNumber></CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Maximum post-spacing density (st/ha)" isNumber></CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Maximum Coniferous (st/ha)" isNumber></CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Height relative to comp (cm/%)" isNumber></CardItem>
                      </Column>
                    </Grid>
                  </Column>

                  <Column sm={4} md={8} lg={16}>
                    <CardItem label="Additional standards"></CardItem>
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
