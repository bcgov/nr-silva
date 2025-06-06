import React from "react";
import { CardContainer, CardTitle } from "@/components/Card";
import { Column } from "@carbon/react";
import { ForestManagementLayerDto } from "../../definitions";

type SingleMultiLayerProps = {
  layers: ForestManagementLayerDto[]
}

const SingleMultiLayer = ({ layers }: SingleMultiLayerProps) => {
  const isMultiLayer = layers.length > 1;

  return (
    <CardContainer>
      <Column sm={4} md={8} lg={16}>
        <CardTitle
          title={`${isMultiLayer ? 'Multi' : 'Single'} layer`}
          subtitle={
            isMultiLayer
              ? 'A forest stand with multiple vertical layers of vegetation, including different tree ages, sizes, and often species, creating a complex canopy.'
              : 'A forest stand with trees predominantly in the same age or height class, forming a single canopy layer'
          }
        />
      </Column>
    </CardContainer>
  )
}

export default SingleMultiLayer;
