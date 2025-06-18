import React from "react";
import {
  Accordion,
  AccordionItem,
  Column,
  Grid,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableHeader,
  TableRow,
  TextAreaSkeleton,
} from "@carbon/react";
import {
  CropGrowth as CropGrowthIcon,
  Security as SecurityIcon,
  Launch as LaunchIcon,
} from "@carbon/icons-react";

import { useQuery } from "@tanstack/react-query";
import { fetchOpeningSsu } from "@/services/OpeningDetailsService";
import { pluralize, renderLabelValueWithUnit } from "@/utils/StringUtils";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import { PLACE_HOLDER } from "@/constants";
import { OpeningDetailsStockingDto, OpeningDetailsStockingLayerDto } from "@/types/OpenApiTypes";
import { formatLocalDate } from "@/utils/DateUtils";

import AcoordionTitle from "./AccordionTitle";
import CardItem from "../../Card/CardItem";
import { CardTitle } from "../../Card";
import VerticalDivider from "../../VerticalDivider";
import Comments from "../../Comments";
import StockingStandardMilestoneStatusTag from "../../Tags/StockingStandardMilestoneStatusTga";

import SpeciesTooltipList from "./SpeciesTooltipList";
import { LayerHeaderConfig } from "./constants";
import { countUniqueSpeciesByCode, isSingleLayer } from "./utils";
import "./styles.scss";


type OpeningStandardUnitsProps = {
  openingId: number;
};

const OpeningStandardUnits = ({ openingId }: OpeningStandardUnitsProps) => {
  const openingDetailSsuQuery = useQuery({
    queryKey: ["openings", openingId, "ssu"],
    queryFn: () => fetchOpeningSsu(Number(openingId)),
    enabled: !!openingId,
    refetchOnMount: "always",
  });


  const renderCellContent = (
    rowKey: keyof OpeningDetailsStockingLayerDto | keyof OpeningDetailsStockingDto,
    stockingStandard: OpeningDetailsStockingDto,
    layer: OpeningDetailsStockingLayerDto
  ) => {
    switch (rowKey) {
      case 'layers':
        return `Layer ${codeDescriptionToDisplayText(layer.layer)}`;
      case 'preferredSpecies':
        return (
          stockingStandard.preferredSpecies.filter((species) => species.layer === layer.layer.code).length
            ? (
              <div className="verticle-cell-items">
                <SpeciesTooltipList speciesList={stockingStandard.preferredSpecies} layerCode={layer.layer.code!} />
              </div>
            )
            : PLACE_HOLDER
        );

      case 'acceptableSpecies':
        return (
          stockingStandard.acceptableSpecies.filter((species) => species.layer === layer.layer.code).length
            ? (
              <div className="verticle-cell-items">
                <SpeciesTooltipList speciesList={stockingStandard.acceptableSpecies} layerCode={layer.layer.code!} />
              </div>
            )
            : PLACE_HOLDER
        );
      case 'targetWellspacedTrees':
        const wellSpacedValues = [
          { label: 'Target', value: layer.targetWellspacedTrees },
          { label: 'Min', value: layer.minWellspacedTrees },
          { label: 'Min preferred', value: layer.minPreferredWellspacedTrees },
          { label: 'Min horizontal', value: layer.minHorizontalDistanceWellspacedTrees },
        ];

        return (
          <div className="verticle-cell-items">
            {
              wellSpacedValues.map(({ label, value }) => (
                <span key={label}>{renderLabelValueWithUnit(label, value, '(st/ha)')}</span>
              ))
            }
          </div>
        );
      case 'minResidualBasalArea':
        return (
          layer.minResidualBasalArea ? `${layer.minResidualBasalArea} (m²/ha)` : PLACE_HOLDER
        );
      case 'minPostspacingDensity':
        const postSpacingValues = [
          { label: 'Min', value: layer.minPostspacingDensity },
          { label: 'Max', value: layer.maxPostspacingDensity }
        ];

        return (
          <div className="verticle-cell-items">
            {
              postSpacingValues.map(({ label, value }) => (
                <span key={label}>{renderLabelValueWithUnit(label, value, '(st/ha)')}</span>
              ))
            }
          </div>
        );
      case 'maxConiferous':
        return (
          layer.maxConiferous ? `${layer.maxConiferous} (st/ha)` : PLACE_HOLDER
        )
      case 'heightRelativeToComp':
        return (
          layer.heightRelativeToComp ? `${layer.heightRelativeToComp} (cm/%)` : PLACE_HOLDER
        )
      default:
        return PLACE_HOLDER;
    }
  }

  if (openingDetailSsuQuery.isLoading) {
    return <TextAreaSkeleton />;
  }

  return (
    <Grid className="opening-standard-units-grid default-grid">
      <Column sm={4} md={8} lg={16}>
        <h3 className="default-tab-content-title">
          {`${openingDetailSsuQuery.data?.length
            ? openingDetailSsuQuery.data.length
            : "No"
            }
            ${pluralize("standard unit", openingDetailSsuQuery.data?.length)}
            in the opening area`}
        </h3>
      </Column>

      {
        openingDetailSsuQuery.data?.map((standardUnit, index) => (
          <Column
            sm={4}
            md={8}
            lg={16}
            className="accordion-col"
            key={`standard-unit-col-${index}`}
          >
            <Accordion className="default-tab-accordion" align="end" size="lg">
              <AccordionItem
                className="default-tab-accordion-item"
                title={<AcoordionTitle standardUnit={standardUnit} />}
              >
                <Grid className="standard-unit-content-grid">
                  <Column sm={4} md={8} lg={16}>
                    <Grid className="standard-unit-content-subgrid">
                      <Column sm={4} md={8} lg={16}>
                        <CardItem
                          label="Net area to be reforested (ha)"
                          isNumber
                          showSkeleton={openingDetailSsuQuery.isLoading}
                        >
                          {standardUnit.stocking.netArea}
                        </CardItem>
                      </Column>

                      <Column sm={4} md={8} lg={16}>
                        <CardItem
                          label="Max soil allowable disturbance (%)"
                          isNumber
                        >
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
                          <Comments comments={standardUnit.comments} />
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
                      {!standardUnit.stocking.ssid && !standardUnit.stocking.fspId
                        ? "Manual stocking requirement"
                        : null}
                      {/* Has standard unit id but no FSP id */}
                      {standardUnit.stocking.ssid &&
                        !standardUnit.stocking.fspId ? (
                        <>
                          {`SSID ${standardUnit.stocking.ssid}, Stocking objective`}
                          <VerticalDivider />
                          <span>Ministry default</span>
                        </>
                      ) : null}
                      {/* Has standard unit id AND FSP id */}
                      {standardUnit.stocking.ssid &&
                        standardUnit.stocking.fspId ? (
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
                              {`FSP ID ${standardUnit.stocking.fspId}`}{" "}
                              <LaunchIcon />
                            </a>
                          }
                        </>
                      ) : null}
                    </div>
                  </Column>

                  {/* Regen obligation */}
                  <Column sm={4} md={8} lg={16}>
                    <section className="section-title-with-icon">
                      <SecurityIcon size={20} />
                      <h4>Milestones</h4>
                      {
                        standardUnit.stocking.milestones.extentDeclared ?
                          <StockingStandardMilestoneStatusTag
                            status="EX"
                          />
                          : null
                      }
                    </section>
                  </Column>

                  <Column sm={4} md={8} lg={16} className="subsection-col">
                    <Grid className="standard-unit-content-subgrid">
                      <Column sm={4} md={8} lg={16}>
                        <CardItem label="Post harvested declared date">
                          {standardUnit.stocking.milestones?.postHarvestDeclaredDate
                            ? formatLocalDate(standardUnit.stocking.milestones.postHarvestDeclaredDate, true)
                            : PLACE_HOLDER}
                        </CardItem>
                      </Column>

                      {!standardUnit.stocking.milestones?.noRegenIndicated ? (
                        <>
                          <Column sm={4} md={4} lg={5}>
                            <CardItem label="Regeneration offset (Years)">
                              {standardUnit.stocking.milestones?.regenOffsetYears ?? PLACE_HOLDER}
                            </CardItem>
                          </Column>

                          <Column sm={4} md={4} lg={5}>
                            <CardItem label="Regeneration declared date">
                              {standardUnit.stocking.milestones?.regenDeclaredDate
                                ? formatLocalDate(standardUnit.stocking.milestones.regenDeclaredDate, true)
                                : PLACE_HOLDER}
                            </CardItem>
                          </Column>

                          <Column sm={4} md={4} lg={5}>
                            <CardItem label="Regeneration due date">
                              {standardUnit.stocking.milestones?.regenDueDate
                                ? formatLocalDate(standardUnit.stocking.milestones.regenDueDate, true)
                                : PLACE_HOLDER}
                            </CardItem>
                          </Column>

                          <Column sm={4} md={4} lg={5}>
                            <CardItem label="Free growing offset (Years)">
                              {standardUnit.stocking.milestones?.freeGrowingOffsetYears ?? PLACE_HOLDER}
                            </CardItem>
                          </Column>

                          <Column sm={4} md={4} lg={5}>
                            <CardItem label="Free growing declared date">
                              {standardUnit.stocking.milestones?.freeGrowingDeclaredDate
                                ? formatLocalDate(standardUnit.stocking.milestones.freeGrowingDeclaredDate, true)
                                : PLACE_HOLDER}
                            </CardItem>
                          </Column>

                          <Column sm={4} md={4} lg={5}>
                            <CardItem label="Free growing due date">
                              {standardUnit.stocking.milestones?.freeGrowingDueDate
                                ? formatLocalDate(standardUnit.stocking.milestones.freeGrowingDueDate, true)
                                : PLACE_HOLDER}
                            </CardItem>
                          </Column>
                        </>
                      ) : (
                        <>
                          <Column sm={4} md={4} lg={5}>
                            <CardItem label="No regeneration offset (Years)">
                              {standardUnit.stocking.milestones?.noRegenOffsetYears ?? PLACE_HOLDER}
                            </CardItem>
                          </Column>

                          <Column sm={4} md={4} lg={5}>
                            <CardItem label="No regeneration declared date">
                              {standardUnit.stocking.milestones?.noRegenDeclaredDate
                                ? formatLocalDate(standardUnit.stocking.milestones.noRegenDeclaredDate, true)
                                : PLACE_HOLDER}
                            </CardItem>
                          </Column>

                          <Column sm={4} md={4} lg={5}>
                            <CardItem label="No regeneration due date">
                              {standardUnit.stocking.milestones?.noRegenDueDate
                                ? formatLocalDate(standardUnit.stocking.milestones.noRegenDueDate, true)
                                : PLACE_HOLDER}
                            </CardItem>
                          </Column>
                        </>
                      )}

                    </Grid>
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

                  {/* Species and Layers */}
                  <Column sm={4} md={8} lg={16}>
                    <section className="section-title-with-icon">
                      <CropGrowthIcon size={20} />
                      <h4>
                        {
                          `
                          ${countUniqueSpeciesByCode(standardUnit.preferredSpecies, standardUnit.acceptableSpecies)} species
                          in a ${isSingleLayer(standardUnit.layers) ? 'single' : 'multi'} layer
                          `
                        }
                      </h4>
                    </section>
                  </Column>

                  <Column sm={4} md={8} lg={16} className="subsection-col">
                    <TableContainer className="default-table-container">
                      <Table
                        className="species-table-container default-zebra-table"
                        aria-label="Species and layer table"
                        useZebraStyles
                      >
                        <TableHead>
                          <TableRow>
                            {
                              LayerHeaderConfig
                                .filter((header) => {
                                  // Omit the layer column if it's single layer
                                  if (isSingleLayer(standardUnit.layers)) {
                                    return header.key !== 'layers'
                                  }
                                  return true;
                                }).map((header) => (
                                  <TableHeader key={header.key}>
                                    {header.header}
                                  </TableHeader>
                                ))
                            }
                          </TableRow>
                        </TableHead>
                        <TableBody>
                          {standardUnit.layers.map((row) => (
                            <TableRow key={row.layer.code}>
                              {
                                LayerHeaderConfig
                                  .filter((header) => {
                                    // Omit the layer column if it's single layer
                                    if (isSingleLayer(standardUnit.layers)) {
                                      return header.key !== 'layers'
                                    }
                                    return true;
                                  }).map((header) => (
                                    <TableCell key={header.key} className="species-table-cell">
                                      {renderCellContent(header.key, standardUnit, row)}
                                    </TableCell>
                                  ))
                              }
                            </TableRow>
                          ))}
                        </TableBody>
                      </Table>
                    </TableContainer>
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
        ))}
    </Grid>
  );
};

export default OpeningStandardUnits;
