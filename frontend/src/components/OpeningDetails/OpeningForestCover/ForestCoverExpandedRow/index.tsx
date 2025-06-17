import React from "react";
import { useQuery } from "@tanstack/react-query";
import EmptySection from "@/components/EmptySection";
import { Column, Grid } from "@carbon/react";
import API from "@/services/API";
import ForestManagement from "./ForestManagement";
import PolygonDetail from "./PolygonDetail";
import './styles.scss';

type ForestCoverDetailProps = {
  forestCoverId: number;
  openingId: number;
};

const ForestCoverExpandedRow = ({ forestCoverId, openingId }: ForestCoverDetailProps) => {
  const { data, isLoading } = useQuery({
    queryKey: ["openings", openingId, "cover", forestCoverId],
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

      <ForestManagement layersData={data?.layers} unmappedAreaData={data?.unmapped} isLoading={isLoading} />
    </Grid >
  );
};

export default ForestCoverExpandedRow;
