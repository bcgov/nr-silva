import { Column, Grid, SkeletonText } from "@carbon/react";
import { CardItem } from "../../../Card";
import { ActivityDetailProps } from "./definitions";

const PruningActivityDetail = ({ activityDetail, isLoading }: ActivityDetailProps) => {
  return (
    <Grid className="activity-detail-content-grid">
      <Column sm={4} md={4} lg={16}>
        {isLoading ? (
          <SkeletonText className="activity-detail-content-title" />
        ) : (
          <h6 className="activity-detail-content-title">
            Pruning specifications
          </h6>
        )}
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem label="Total stems per ha" showSkeleton={isLoading}>
          {activityDetail?.totalStemsPerHa}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem label={'Stems per ha to prune'} showSkeleton={isLoading}>
          {activityDetail?.stemsPerHaToPrune}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={3}>
        <CardItem label="Target inter-tree distance(m)" showSkeleton={isLoading}>
          {activityDetail?.targetIntertreeDistance}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={3}>
        <CardItem label="Minimum inter-tree distance(m)" showSkeleton={isLoading}>
          {activityDetail?.minimumIntertreeDistance}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={3}>
        <CardItem label="Height above ground(m)" showSkeleton={isLoading}>
          {activityDetail?.heightAboveGround}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem label="Minimum live crown (%)" showSkeleton={isLoading}>
          {activityDetail?.minimumLiveCrown}
        </CardItem>
      </Column>

    </Grid>
  );
};

export default PruningActivityDetail;
