import React from "react";
import { Column, TextAreaSkeleton } from "@carbon/react";
import UnmappedArea from "./UnmappedArea";
import { ForestManagementDto } from "../../definitions";
import SingleMultiLayer from "./SingleMultiLayer";

type ForestManagementProps = {
  forestManagementData?: ForestManagementDto;
  isLoading?: boolean;
}

const Title = () => (
  <Column sm={4} md={8} lg={16}>
    <h2 className="details-title">Forest management</h2>
  </Column >
);

const ForestManagement = ({ isLoading, forestManagementData }: ForestManagementProps) => {
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

  if (forestManagementData) {
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
                <Column sm={4} md={8} lg={16}>
                  <UnmappedArea data={forestManagementData.unmappedArea} />
                </Column>
                <Column sm={4} md={8} lg={16}>
                  <SingleMultiLayer layers={forestManagementData.layers} />
                </Column>
              </>
            )
        }
      </>
    )
  }
};

export default ForestManagement;
