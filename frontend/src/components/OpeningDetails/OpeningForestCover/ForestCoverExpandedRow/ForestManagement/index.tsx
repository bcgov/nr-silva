import React from "react";
import { Column, TextAreaSkeleton } from "@carbon/react";
import UnmappedArea from "./UnmappedArea";
import SingleMultiLayer from "./SingleMultiLayer";
import { OpeningForestCoverLayerDto, OpeningForestCoverUnmappedDto } from "@/services/OpenApi";


type ForestManagementProps = {
  isSingleLayer?: boolean;
  layersData?: OpeningForestCoverLayerDto[];
  unmappedAreaData?: OpeningForestCoverUnmappedDto[];
  isLoading?: boolean;
}

const Title = () => (
  <Column sm={4} md={8} lg={16}>
    <h2 className="details-title">Forest management</h2>
  </Column >
);

const ForestManagement = ({
  isLoading,
  layersData,
  unmappedAreaData,
  isSingleLayer = true
}: ForestManagementProps) => {
  if (isLoading) {
    return (
      <>
        <Title />
        <Column sm={4} md={8} lg={16}>
          <TextAreaSkeleton />
        </Column>
      </>
    )
  }

  return (
    <>
      <Title />
      {
        isLoading
          ? (
            <Column sm={4} md={8} lg={16}>
              <TextAreaSkeleton />
            </Column>
          )
          : (
            <>
              {
                unmappedAreaData?.length
                  ? (
                    <Column sm={4} md={8} lg={16}>
                      <UnmappedArea data={unmappedAreaData} />
                    </Column>
                  )
                  : null
              }
              {
                layersData
                  ? (
                    <Column sm={4} md={8} lg={16}>
                      <SingleMultiLayer isSingleLayer={isSingleLayer} layersData={layersData} />
                    </Column>
                  )
                  : null
              }
            </>
          )
      }
    </>
  )
};

export default ForestManagement;
