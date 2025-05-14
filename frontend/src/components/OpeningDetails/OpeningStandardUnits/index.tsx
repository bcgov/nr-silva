import React from "react";
import {
  Accordion, AccordionItem, Column,
  Grid, Table, TableBody, TableCell,
  TableHead, TableHeader, TableRow,
  TextAreaSkeleton
} from "@carbon/react";
import {
  CropGrowth as CropGrowthIcon,
  Security as SecurityIcon,
  Layers as LayersIcon,
  Launch as LaunchIcon
} from "@carbon/icons-react";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import { PLACE_HOLDER } from "@/constants";
import { useQuery } from "@tanstack/react-query";
import { fetchOpeningSsu } from "@/services/OpeningDetailsService";
import { pluralize } from "@/utils/StringUtils";

import AcoordionTitle from "./AccordionTitle";
import CardItem from "../../Card/CardItem";
import { CardTitle } from "../../Card";
import VerticalDivider from "../../VerticalDivider";

import { AcceptableSpeciesHeaders, PreferredSpeciesHeaders } from "./constants";
import './styles.scss';


type OpeningStandardUnitsProps = {
  openingId: number
}

const OpeningStandardUnits = ({ openingId }: OpeningStandardUnitsProps) => {

  const openingDetailSsuQuery = useQuery({
    queryKey: ['openings', openingId, 'ssu'],
    queryFn: () => fetchOpeningSsu(Number(openingId)),
    enabled: !!openingId,
    refetchOnMount: 'always'
  });

  if (openingDetailSsuQuery.isLoading) {
    return (
      <TextAreaSkeleton />
    )
  }

  return (
    <Grid className="opening-standard-units-grid default-grid">
      <Column sm={4} md={8} lg={16}>
        <h3 className="default-tab-content-title">
          {
            `${openingDetailSsuQuery.data?.length
              ? openingDetailSsuQuery.data.length
              : 'No'
            }
            ${pluralize('standard unit', openingDetailSsuQuery.data?.length)}
            in the opening area`
          }
        </h3>
      </Column>

      {
        openingDetailSsuQuery.data?.map((standardUnit, index) => (
          <Column sm={4} md={8} lg={16} className="accordion-col" key={`standard-unit-col-${index}`}>
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
                        <CardItem label="Net area to be reforested (ha)" isNumber showSkeleton={openingDetailSsuQuery.isLoading}>
                          {standardUnit.stocking.netArea}
                        </CardItem>
                      </Column>

                      <Column sm={4} md={8} lg={16}>
                        <CardItem label="Max soil allowable disturbance (%)" isNumber>
                          {standardUnit.stocking.soilDisturbancePercent}
                        </CardItem>
                      </Column>

                      <Column sm={4} md={8} lg={16}>
                        <Grid className="standard-unit-bec-content-grid">

                          <Column sm={4} md={8} lg={16}>
                            <section className="section-title-without-icon">
                              <h4>BEC Information</h4>
                            </section>
                          </Column>

                          <Column sm={4} md={8} lg={16}>
                            <Grid>
                              <Column sm={2} md={2} lg={2}>
                                <CardItem label="BGC Zone">
                                  {standardUnit.stocking.bec.becZoneCode}
                                </CardItem>
                              </Column>

                              <Column sm={2} md={2} lg={2}>
                                <CardItem label="BGC Subzone">
                                  {standardUnit.stocking.bec.becSubzoneCode}
                                </CardItem>
                              </Column>

                              <Column sm={2} md={2} lg={2}>
                                <CardItem label="BGC Variant">
                                  {standardUnit.stocking.bec.becVariant}
                                </CardItem>
                              </Column>

                              <Column sm={2} md={2} lg={2}>
                                <CardItem label="Phase">
                                  {standardUnit.stocking.bec.becPhase}
                                </CardItem>
                              </Column>

                              <Column sm={2} md={2} lg={2}>
                                <CardItem label="Site series">
                                  {standardUnit.stocking.bec.becSiteSeries}
                                </CardItem>
                              </Column>

                              <Column sm={2} md={2} lg={2}>
                                <CardItem label="Site phase">
                                  {standardUnit.stocking.bec.becSiteType}
                                </CardItem>
                              </Column>

                              <Column sm={2} md={2} lg={2}>
                                <CardItem label="Site seral">
                                  {standardUnit.stocking.bec.becSeral}
                                </CardItem>
                              </Column>

                            </Grid>
                          </Column>
                        </Grid>
                      </Column>

                      <Column sm={4} md={8} lg={16}>
                        <CardItem label="Comment">
                          {
                            (standardUnit.comments ?? []).length > 0
                              ? (
                                <ul className="comment-list">
                                  {standardUnit.comments.map((comment, index) =>
                                    comment.commentText ? (
                                      <li key={index}>
                                        {comment.commentText}
                                      </li>
                                    ) : null
                                  )}
                                </ul>
                              )
                              : null
                          }
                        </CardItem>
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
                        (!standardUnit.stocking.ssid && !standardUnit.stocking.fspId)
                          ? 'Manual stocking requirement'
                          : null
                      }
                      {/* Has standard unit id but no FSP id */}
                      {
                        (standardUnit.stocking.ssid && !standardUnit.stocking.fspId)
                          ? (
                            <>
                              {`SSID ${standardUnit.stocking.ssid}, Stocking objective`}
                              <VerticalDivider />
                              <span>Ministry default</span>
                            </>
                          )
                          : null
                      }
                      {/* Has standard unit id AND FSP id */}
                      {
                        (standardUnit.stocking.ssid && standardUnit.stocking.fspId)
                          ? (
                            <>
                              {`SSID ${standardUnit.stocking.ssid}, Stocking objective`}
                              <VerticalDivider />
                              {
                                <a
                                  className="fsp-link"
                                  href={`https://apps.nrs.gov.bc.ca/ext/fsp/indexAction.do?fsp_id=${standardUnit.stocking.fspId}`}
                                  target="_blank"
                                  rel="noopener noreferrer"
                                >
                                  {`FSP ID ${standardUnit.stocking.fspId}`} <LaunchIcon />
                                </a>
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
                              <TableRow key={row.species.code}>
                                <TableCell>
                                  {codeDescriptionToDisplayText(row.species)}
                                </TableCell>
                                <TableCell>
                                  {row.minHeight ?? PLACE_HOLDER}
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
                              <TableRow key={row.species.code}>
                                <TableCell>
                                  {codeDescriptionToDisplayText(row.species)}
                                </TableCell>
                                <TableCell>
                                  {row.minHeight ?? PLACE_HOLDER}
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
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Regen delay (Years)" isNumber>
                          {standardUnit.stocking.regenDelay}
                        </CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Free growing early (Years)" isNumber>
                          {standardUnit.stocking.freeGrowingEarly}
                        </CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Free growing late (Years)" isNumber>
                          {standardUnit.stocking.freeGrowingLate}
                        </CardItem>
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
                        <CardItem label="Minimum well-spaced trees" isNumber>
                          {standardUnit.layer?.minWellspacedTrees}
                        </CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Minimum preferred well-spaced trees" isNumber>
                          {standardUnit.layer?.minPreferredWellspacedTrees}
                        </CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Minimum horizontal distance well-spaced trees (m)" isNumber>
                          {standardUnit.layer?.minHorizontalDistanceWellspacedTrees}
                        </CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Target well-spaced trees (ha)" isNumber>
                          {standardUnit.layer?.targetWellspacedTrees}
                        </CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Minimum residual basal area (m²/ha)" isNumber>
                          {standardUnit.layer?.minResidualBasalArea}
                        </CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Minimum post-spacing density (st/ha)" isNumber>
                          {standardUnit.layer?.minPostspacingDensity}
                        </CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Maximum post-spacing density (st/ha)" isNumber>
                          {standardUnit.layer?.maxPostspacingDensity}
                        </CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Maximum Coniferous (st/ha)" isNumber>
                          {standardUnit.layer?.maxConiferous}
                        </CardItem>
                      </Column>
                      <Column sm={4} md={4} lg={4}>
                        <CardItem label="Height relative to comp (cm/%)" isNumber>
                          {standardUnit.layer?.heightRelativeToComp}
                        </CardItem>
                      </Column>
                    </Grid>
                  </Column>

                  <Column sm={4} md={8} lg={16}>
                    <CardItem label="Additional standards">
                      {standardUnit.stocking.additionalStandards}
                    </CardItem>
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
