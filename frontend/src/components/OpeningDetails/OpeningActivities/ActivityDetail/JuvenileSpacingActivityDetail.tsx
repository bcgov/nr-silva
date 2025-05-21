import { Column, Grid, SkeletonText } from "@carbon/react";
import { CardItem } from "../../../Card";
import { ActivityDetailProps } from "./definitions";

const JuvenileSpacingActivityDetail = ({ activityDetail, isLoading }: ActivityDetailProps) => {
  return (
    <Grid className="activity-detail-content-grid">
      <Column sm={4} md={4} lg={16}>
        {isLoading ? (
          <SkeletonText className="activity-detail-content-title" />
        ) : (
          <h6 className="activity-detail-content-title">
            Spacing Specifications
          </h6>
        )}
      </Column>

      <Column sm={4} md={4} lg={4}>
        <CardItem label="Target Inter-tree distance(m)" showSkeleton={isLoading}>
          {activityDetail?.targetIntertreeDistance}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4}>
        <CardItem label="Allowable variation in inter-tree distance(m)" showSkeleton={isLoading}>
          {activityDetail?.allowableVariationDistance}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4}>
        <CardItem label="Allowable trees per plot" showSkeleton={isLoading}>
          {activityDetail?.allowableTreePerLot}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4}>
        <CardItem label="Spacing per ha" showSkeleton={isLoading}>
          {activityDetail?.spacingPerHa}
        </CardItem>
      </Column>
    </Grid>
  );
};

export default JuvenileSpacingActivityDetail;
