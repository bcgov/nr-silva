import React from "react";
import { CardContainer, CardTitle } from "@/components/Card";
import { Accordion, AccordionItem, Column, Grid } from "@carbon/react";
import { Firewall } from "@carbon/icons-react";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
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
            // MULTI LAYER
            <Column sm={4} md={8} lg={16} className="accordion-col">
              {
                layers.map((layer) => (
                  <Accordion className="layer-accordion" align="end" key={layer.layer.code}>
                    <AccordionItem
                      className="layer-item"
                      title={
                        <LayerAccordionTitle
                          title={`Layer ${codeDescriptionToDisplayText(layer.layer)}`}
                        />
                      }
                    >
                      <Grid className="multi-layer-accordion-item-grid">
                        <Column sm={4} md={8} lg={16}>
                          <div className="card-title-container">
                            <div className="icon-and-title">
                              <Firewall size={20} />
                              <h4>
                                {TEXT_CONFIG.inventoryLayer.title}
                              </h4>
                            </div>
                            <p className="card-subtitle">{TEXT_CONFIG.inventoryLayer.subtitle}</p>
                          </div>
                          <div className="multi-layer-table-container">
                            <LayerTable layer={layer.inventoryLayer} />
                          </div>
                        </Column>

                        <Column sm={4} md={8} lg={16}>
                          <hr className="expanded-row-hr" />
                        </Column>

                        <Column sm={4} md={8} lg={16}>
                          <div className="card-title-container">
                            <div className="icon-and-title">
                              <Firewall size={20} />
                              <h4>
                                {TEXT_CONFIG.silvicultureLayer.title}
                              </h4>
                            </div>
                            <p className="card-subtitle">{TEXT_CONFIG.silvicultureLayer.subtitle}</p>
                          </div>
                          <div className="multi-layer-table-container">
                            <LayerTable layer={layer.silvicultureLayer} />
                          </div>
                        </Column>
                      </Grid>
                    </AccordionItem>
                  </Accordion>
                ))
              }
            </Column>
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
