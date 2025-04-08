import React from "react";

import { DummyStandardUnits } from "./constants";

import './styles.scss';
import { Accordion, AccordionItem, Column, Grid } from "@carbon/react";
import AcoordionTitle from "./AccordionTitle";

const OpeningStandardUnits = () => {
  return (
    <Grid className="opening-standard-units-grid default-grid">
      <Column sm={4} md={8} lg={16}>
        <h3 className="standard-units-title">
          {
            `${DummyStandardUnits.length
              ? String(DummyStandardUnits.length).padStart(2, '0')
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
                {standardUnit.name}
              </AccordionItem>
            </Accordion>
          </Column>
        ))
      }
    </Grid>
  );
};


export default OpeningStandardUnits;
