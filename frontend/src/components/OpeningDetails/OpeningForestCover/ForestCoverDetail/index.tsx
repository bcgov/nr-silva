import React from "react";
import { useQuery } from "@tanstack/react-query";
import { delayMock } from "@/utils/MockUtils";
import EmptySection from "@/components/EmptySection";
import { Column, Grid } from "@carbon/react";
import { CardItem } from "@/components/Card";
import Comments from "@/components/Comments";

import { OpeningForestCoverDetails, mockPolygonDetails } from "../definitions";

import './styles.scss';

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
      <Column sm={4} md={8} lg={16}>
        <h2 className="details-title">Polygon details</h2>
      </Column>
      <Column sm={4} md={8} lg={16}>
        <Grid className="forest-cover-details-subgrid">
          <Column sm={4} md={4} lg={4} max={2}>
            <CardItem label="Forest cover ID" showSkeleton={isLoading} isNumber>
              {data?.forestCoverId}
            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={4} max={2}>
            <CardItem label="Reserve type" showSkeleton={isLoading}>
              {data?.reserveType}
            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={4} max={2}>
            <CardItem label="Reserve objective" showSkeleton={isLoading}>
              {data?.reserveObjective}
            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={4} max={2}>
            <CardItem label="Site class" showSkeleton={isLoading}>
              {data?.siteClass}
            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={4} max={2}>
            <CardItem label="Site index" showSkeleton={isLoading}>
              {data?.siteIndex}
            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={4} max={2}>
            <CardItem label="Site index source" showSkeleton={isLoading}>
              {data?.siteIndexSource}
            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={4} max={2}>
            <CardItem label="Tree cover pattern" showSkeleton={isLoading}>
              {data?.treeCoverPattern}
            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={4} max={2}>
            <CardItem label="Re-entry year" showSkeleton={isLoading}>
              {data?.reEntryYear}
            </CardItem>
          </Column>

          <Column sm={4} md={8} lg={16}>
            <CardItem label="Comment" showSkeleton={isLoading}>
              <Comments
                comments={[
                  {
                    commentSource: { code: '', description: '' },
                    commentType: { code: '', description: '' },
                    commentText: data?.comment ?? null
                  }
                ]}
              />
            </CardItem>
          </Column>
        </Grid>
      </Column>
    </Grid >
  );
};

export default ForestCoverDetail;
