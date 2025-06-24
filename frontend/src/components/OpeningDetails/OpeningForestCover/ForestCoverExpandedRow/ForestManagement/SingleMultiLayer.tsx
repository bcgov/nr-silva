import React from "react";
import { CardContainer, CardTitle } from "@/components/Card";
import { Accordion, AccordionItem, Column, Grid } from "@carbon/react";
import { Firewall } from "@carbon/icons-react";
import LayerAccordionTitle from "./LayerAccordionTitle";
import { TEXT_CONFIG } from "./constants";
import LayerTable from "./LayerTable";
import { OpeningForestCoverLayerDto } from "@/services/OpenApi";
import { groupMultiLayerDisplay } from "./utils";
import { MultiLayerDisplayType } from "./definitions";

type SingleMultiLayerProps = {
  isSingleLayer: boolean;
  layersData?: OpeningForestCoverLayerDto[];
}

const SingleMultiLayer = ({ isSingleLayer, layersData }: SingleMultiLayerProps) => {
  if (!layersData || !layersData.length) {
    return null;
  }

  // Single Layer
  const inventoryLayer = isSingleLayer ? layersData.find((layer) => layer.layer.code === 'I') : undefined;
  const silvicultureLayer = isSingleLayer ? layersData.find((layer) => layer.layer.code === 'S') : undefined;

  // Multi Layer
  const groupedLayers = groupMultiLayerDisplay(layersData);

  const getLayerTitle = (
    key: string,
    layers: MultiLayerDisplayType[keyof MultiLayerDisplayType] | undefined
  ): string => {
    if (key === "other") {
      return "Other Layers";
    }

    if (layers && "inventoryLayer" in layers && layers.inventoryLayer) {
      return `Layer ${key} - ${layers.inventoryLayer.layer.description}`;
    }

    return `Layer ${key}`;
  };


  return (
    <CardContainer className="single-multi-layer-card-container">
      <Column sm={4} md={8} lg={16}>
        <CardTitle
          title={`${isSingleLayer ? 'Single' : 'Multi'} layer`}
          subtitle={
            isSingleLayer
              ? TEXT_CONFIG.singleLayerDesc
              : TEXT_CONFIG.multiLayerDesc
          }
        />
      </Column>

      {
        isSingleLayer
          ? (
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
                  <LayerTable layer={inventoryLayer} />
                </AccordionItem>

                {
                  silvicultureLayer
                    ? (
                      <AccordionItem
                        className="layer-item"
                        title={
                          <LayerAccordionTitle
                            title={TEXT_CONFIG.silvicultureLayer.title}
                            subtitle={TEXT_CONFIG.silvicultureLayer.subtitle}
                          />
                        }
                      >
                        <LayerTable layer={silvicultureLayer} />
                      </AccordionItem>
                    )
                    : null
                }
              </Accordion>
            </Column>
          )
          : (
            // MULTI LAYER
            <Column sm={4} md={8} lg={16} className="accordion-col">
              {
                Object.entries(groupedLayers).map(([key, layers]) => {
                  if (key !== 'other') {
                    const group = layers as Exclude<typeof layers, { layers: OpeningForestCoverLayerDto[] }>;
                    return (
                      <Accordion className="layer-accordion" align="end" key={key}>
                        <AccordionItem
                          className="layer-item"
                          title={
                            <LayerAccordionTitle title={getLayerTitle(key, layers)} />
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
                                <LayerTable layer={group.inventoryLayer} />
                              </div>
                            </Column>
                            {
                              group.silvicultureLayer
                                ? (
                                  <>

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
                                        <LayerTable layer={group.silvicultureLayer} />
                                      </div>
                                    </Column>
                                  </>
                                )
                                : null
                            }
                          </Grid>
                        </AccordionItem>
                      </Accordion>
                    )
                  }
                })
              }
            </Column>
          )
      }
    </CardContainer>
  )
}

export default SingleMultiLayer;
