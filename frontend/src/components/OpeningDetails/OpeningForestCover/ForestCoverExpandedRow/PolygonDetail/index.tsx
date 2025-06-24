import React from "react";
import { Column, Grid } from "@carbon/react";
import { CardItem } from "@/components/Card";
import { OpeningForestCoverPolygonDto } from "@/services/OpenApi";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import { formatLocalDate } from "@/utils/DateUtils";

type PolygonDetailProps = {
  polygon?: OpeningForestCoverPolygonDto,
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
            {codeDescriptionToDisplayText(polygon?.reserve)}
          </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} max={2}>
          <CardItem label="Reserve objective" showSkeleton={isLoading}>
            {codeDescriptionToDisplayText(polygon?.objective)}
          </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} max={2}>
          <CardItem label="Site class" showSkeleton={isLoading}>
            {codeDescriptionToDisplayText(polygon?.siteClass)}
          </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} max={2}>
          <CardItem label="Site index" showSkeleton={isLoading}>
            {polygon?.siteIndex}
          </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} max={2}>
          <CardItem label="Site index source" showSkeleton={isLoading}>
            {codeDescriptionToDisplayText(polygon?.siteIndexSource)}
          </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} max={2}>
          <CardItem label="Tree cover pattern" showSkeleton={isLoading}>
            {codeDescriptionToDisplayText(polygon?.treeCoverPattern)}
          </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} max={2}>
          <CardItem label="Re-entry year" showSkeleton={isLoading}>
            {formatLocalDate(polygon?.reentryYear, true)}
          </CardItem>
        </Column>
      </Grid>
    </Column>
  </>
);

export default PolygonDetail;
