import React from "react";
import { CardContainer, CardTitle } from "@/components/Card";
import { Accordion, AccordionItem, Column } from "@carbon/react";
import { ForestManagementLayerDto } from "../../definitions";
import LayerAccordionTitle from "./LayerAccordionTitle";
import { TEXT_CONFIG } from "./constants";
import LayerTable from "./LayerTable";

type SingleMultiLayerProps = {
  layers: ForestManagementLayerDto[]
}

const SingleMultiLayer = ({ layers }: SingleMultiLayerProps) => {
  if (!layers || !layers.length) {
    return null;
  }

  const isMultiLayer = layers.length > 1;

  return (
    <CardContainer className="single-multi-layer-card-container">
      <Column sm={4} md={8} lg={16}>
        <CardTitle
          title={`${isMultiLayer ? 'Multi' : 'Single'} layer`}
          subtitle={
            isMultiLayer
              ? TEXT_CONFIG.multiLayerDesc
              : TEXT_CONFIG.singleLayerDesc
          }
        />
      </Column>

      {
        isMultiLayer
          ? (
            <div>ss</div>
          )
          : (
            // SINGLE LAYER
            <Column sm={4} md={8} lg={16} className="accordion-col">
              <Accordion className="layer-accordion" align="end">
                <AccordionItem
                  className="layer-item"
                  title={
                    <LayerAccordionTitle
                      title={TEXT_CONFIG.inventoryLayer.title}
                      subtitle={TEXT_CONFIG.inventoryLayer.subtitle}
                    />
                  }
                >
                  <LayerTable layer={layers[0]!.inventoryLayer} />
                </AccordionItem>

                <AccordionItem
                  className="layer-item"
                  title={
                    <LayerAccordionTitle
                      title={TEXT_CONFIG.silvicultureLayer.title}
                      subtitle={TEXT_CONFIG.silvicultureLayer.subtitle}
                    />
                  }
                >
                  <LayerTable layer={layers[0]!.silvicultureLayer} />
                </AccordionItem>
              </Accordion>
            </Column>
          )
      }
    </CardContainer>
  )
}

export default SingleMultiLayer;
