import React from "react";
import { useQuery } from "@tanstack/react-query";
import { delayMock } from "@/utils/MockUtils";
import EmptySection from "@/components/EmptySection";
import { Column, Grid } from "@carbon/react";
import PolygonDetail from "./PolygonDetail";

import { OpeningForestCoverDetails, mockPolygonDetails } from "../definitions";

import './styles.scss';
import ForestManagement from "./ForestManagement";


const fetchPolygonDetails = async (forestCoverId: string) => {
  const details = mockPolygonDetails.find((d) => d.polygonDetail.forestCoverId === forestCoverId);
  return delayMock(details ?? null, 500);
};

type ForestCoverDetailProps = {
  forestCoverId: number;
};

const ForestCoverExpandedRow = ({ forestCoverId }: ForestCoverDetailProps) => {
  const { data, isLoading } = useQuery<OpeningForestCoverDetails | null>({
    queryKey: ["forestCoverDetails", forestCoverId],
    queryFn: () => fetchPolygonDetails(forestCoverId),
  });

  if (!data && !isLoading) {
    return (
      <EmptySection
        pictogram="UserSearch"
        title=""
        description={`Forest cover ${forestCoverId} not found.`}
      />
    )
  }

  return (
    <Grid className="opening-forest-cover-details-grid">
      <PolygonDetail polygon={data?.polygonDetail} isLoading={isLoading} />

      <Column sm={4} md={8} lg={16}>
        <hr className="expanded-row-hr" />
      </Column>

      <ForestManagement forestManagementData={data?.forestManagement} isLoading={isLoading} />
    </Grid >
  );
};

export default ForestCoverExpandedRow;
