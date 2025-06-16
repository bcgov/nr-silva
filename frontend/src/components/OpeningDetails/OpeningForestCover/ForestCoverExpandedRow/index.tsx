import React from "react";
import { useQuery } from "@tanstack/react-query";
import { delayMock } from "@/utils/MockUtils";
import EmptySection from "@/components/EmptySection";
import { Column, Grid } from "@carbon/react";
import PolygonDetail from "./PolygonDetail";
import API from "@/services/API";

import './styles.scss';
import ForestManagement from "./ForestManagement";




type ForestCoverDetailProps = {
  forestCoverId: number;
  openingId: number;
};

const ForestCoverExpandedRow = ({ forestCoverId, openingId }: ForestCoverDetailProps) => {
  const { data, isLoading } = useQuery({
    queryKey: ["forestCoverDetails", forestCoverId],
    queryFn: () => API.OpeningEndpointService.getCoverDetails(openingId, forestCoverId),
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
      <PolygonDetail polygon={data?.polygon} isLoading={isLoading} />

      <Column sm={4} md={8} lg={16}>
        <hr className="expanded-row-hr" />
      </Column>

      <ForestManagement forestManagementData={data?.layers} isLoading={isLoading} />
    </Grid >
  );
};

export default ForestCoverExpandedRow;
