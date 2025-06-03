import React from "react";
import { useQuery } from "@tanstack/react-query";
import { OpeningForestCoverDetails, mockPolygonDetails } from "../definitions";
import { delayMock } from "@/utils/MockUtils";
import EmptySection from "../../../EmptySection";
import { Grid } from "@carbon/react";

const fetchPolygonDetails = async (forestCoverId: string) => {
  const details = mockPolygonDetails.find((d) => d.forestCoverId === forestCoverId);
  return delayMock(details ?? null, 500);
};

type ForestCoverDetailProps = {
  forestCoverId: string;
};

const ForestCoverDetail = ({ forestCoverId }: ForestCoverDetailProps) => {
  const { data, isLoading } = useQuery<OpeningForestCoverDetails | null>({
    queryKey: ["forestCoverDetails", forestCoverId],
    queryFn: () => fetchPolygonDetails(forestCoverId),
    staleTime: 10000,
  });

  if (isLoading) return <div className="opening-forest-cover-details">Loading details...</div>;

  if (!data) {
    return (
      <EmptySection
        pictogram="UserSearch"
        title=""
        description={`Forest cover ${forestCoverId} not found.`}
      />
    )
  }

  return (
    <Grid className=" opening-forest-cover-details-grid">
      <div className="opening-forest-cover-details-title">Polygon details</div>
      <div className="opening-forest-cover-details-fields">
        <div className="opening-forest-cover-details-field"><strong>Forest cover ID:</strong> {data.forestCoverId}</div>
        <div className="opening-forest-cover-details-field"><strong>Reserve type:</strong> {data.reserveType ?? "—"}</div>
        <div className="opening-forest-cover-details-field"><strong>Reserve objective:</strong> {data.reserveObjective ?? "—"}</div>
        <div className="opening-forest-cover-details-field"><strong>Site class:</strong> {data.siteClass ?? "—"}</div>
        <div className="opening-forest-cover-details-field"><strong>Site index:</strong> {data.siteIndex ?? "—"}</div>
        <div className="opening-forest-cover-details-field"><strong>Site index source:</strong> {data.siteIndexSource ?? "—"}</div>
        <div className="opening-forest-cover-details-field"><strong>Tree cover pattern:</strong> {data.treeCoverPattern ?? "—"}</div>
        <div className="opening-forest-cover-details-field"><strong>Re-entry year:</strong> {data.reEntryYear ?? "—"}</div>
      </div>
      {data.comment && (
        <div className="opening-forest-cover-details-comment">
          <strong>Comment:</strong>
          <div>{data.comment}</div>
        </div>
      )}
    </Grid>
  );
};

export default ForestCoverDetail;
