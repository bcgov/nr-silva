import React from "react";

import { CropGrowth as CropGrowthIcon } from "@carbon/icons-react";

import { DummyStandardUnits } from "./constants";


import { Accordion, AccordionItem, Column, Grid } from "@carbon/react";
import AcoordionTitle from "./AccordionTitle";
import CardItem from "../../Card/CardItem";
import { CardTitle } from "../../Card";

import './styles.scss';

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
                      <h4>{`${standardUnit.species.length} species`}</h4>
                    </section>
                  </Column>


                </Grid>
              </AccordionItem>
            </Accordion>
          </Column>
        ))
      }
    </Grid>
  );
};


export default OpeningStandardUnits;
