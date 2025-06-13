import React from "react";
import { Column, Grid } from "@carbon/react";
import { CardItem } from "@/components/Card";
import Comments from "@/components/Comments";
import { PolygonDetailDto } from "../../definitions";

type PolygonDetailProps = {
  polygon?: PolygonDetailDto,
  isLoading?: boolean
}

const PolygonDetail = ({ polygon, isLoading }: PolygonDetailProps) => (
  <>
    <Column sm={4} md={8} lg={16}>
      <h2 className="details-title">Polygon details</h2>
    </Column>
    <Column sm={4} md={8} lg={16}>
      <Grid className="forest-cover-details-subgrid">
        <Column sm={4} md={4} lg={4} max={2}>
          <CardItem label="Forest cover ID" showSkeleton={isLoading} isNumber>
            {polygon?.forestCoverId}
          </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} max={2}>
          <CardItem label="Reserve type" showSkeleton={isLoading}>
            {polygon?.reserveType}
          </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} max={2}>
          <CardItem label="Reserve objective" showSkeleton={isLoading}>
            {polygon?.reserveObjective}
          </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} max={2}>
          <CardItem label="Site class" showSkeleton={isLoading}>
            {polygon?.siteClass}
          </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} max={2}>
          <CardItem label="Site index" showSkeleton={isLoading}>
            {polygon?.siteIndex}
          </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} max={2}>
          <CardItem label="Site index source" showSkeleton={isLoading}>
            {polygon?.siteIndexSource}
          </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} max={2}>
          <CardItem label="Tree cover pattern" showSkeleton={isLoading}>
            {polygon?.treeCoverPattern}
          </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} max={2}>
          <CardItem label="Re-entry year" showSkeleton={isLoading}>
            {polygon?.reEntryYear}
          </CardItem>
        </Column>

        <Column sm={4} md={8} lg={16}>
          <CardItem label="Comment" showSkeleton={isLoading}>
            <Comments
              comments={[
                {
                  commentSource: { code: '', description: '' },
                  commentType: { code: '', description: '' },
                  commentText: polygon?.comment ?? null
                }
              ]}
            />
          </CardItem>
        </Column>
      </Grid>
    </Column>
  </>
);

export default PolygonDetail;
