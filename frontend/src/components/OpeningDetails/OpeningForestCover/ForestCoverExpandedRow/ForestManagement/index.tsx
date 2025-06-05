import React from "react";
import { Column, TextAreaSkeleton } from "@carbon/react";
import UnmappedArea from "./UnmappedArea";
import { ForestManagementDto } from "../../definitions";

type ForestManagementProps = {
  forestManagementData?: ForestManagementDto;
  isLoading?: boolean;
}

const ForestManagement = ({ isLoading, forestManagementData }: ForestManagementProps) => {
  if (isLoading) {
    return (
      <>
        <Column sm={4} md={8} lg={16}>
          <h2 className="details-title">Forest management</h2>
        </Column >
        <Column sm={4} md={8} lg={16}>
          <TextAreaSkeleton />
        </Column>
      </>
    )
  }

  if (forestManagementData) {
    return (
      <>
        <Column sm={4} md={8} lg={16}>
          <h2 className="details-title">Forest management</h2>
        </Column >

        {
          isLoading
            ? (
              <Column sm={4} md={8} lg={16}>
                <TextAreaSkeleton />
              </Column>
            )
            : (
              <Column sm={4} md={8} lg={16}>
                <UnmappedArea data={forestManagementData.unmappedArea} />
              </Column>
            )
        }
      </>
    )
  }
};

export default ForestManagement;
