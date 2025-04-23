import React from "react";
import { MockedDisturbanceType } from "./definitions";
import { Accordion, AccordionItem } from "@carbon/react";
import { TreeFallRisk } from "@carbon/icons-react";

type DisturbanceAccordionProps = {
  data: MockedDisturbanceType[]
}

const AccordionTitle = ({ total }: { total: number }) => (
  <div className="default-accordion-title-container">
    <div className="accordion-title-top">
      <TreeFallRisk size={20} />
      <h4>
        Disturbance events
      </h4>
    </div>
    <div className="accordion-title-bottom">
      {`Total disturbance: ${total}`}
    </div>
  </div>
)

const DisturbanceAccordion = ({ data }: DisturbanceAccordionProps) => {

  return (
    <Accordion
      className="default-tab-accordion"
      align="end"
      size="lg"
    >
      <AccordionItem
        className="default-tab-accordion-item"
        title={<AccordionTitle total={data.length} />}
      >
        sss
      </AccordionItem>
    </Accordion>
  )
}

export default DisturbanceAccordion;
