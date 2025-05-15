import { Column, Grid, SkeletonText } from "@carbon/react";
import { CardItem } from "../../../Card";
import { ActivityDetailProps } from "./definitions";

const SitePreparationActivityDetail = ({ activityDetail, isLoading }: ActivityDetailProps) => {
  return (
    <Grid className="activity-detail-content-grid">
      <Column sm={4} md={4} lg={16}>
        {isLoading ? (
          <SkeletonText className="activity-detail-content-title" />
        ) : (
          <h6 className="activity-detail-content-title">
            Site prepration specifications
          </h6>
        )}
      </Column>

      <Column sm={16} md={16} lg={16}>
        <CardItem label="Target prepared spot per ha" showSkeleton={isLoading}>
          {activityDetail?.targetSpot}
        </CardItem>
      </Column>
    </Grid>
  );
};

export default SitePreparationActivityDetail;
