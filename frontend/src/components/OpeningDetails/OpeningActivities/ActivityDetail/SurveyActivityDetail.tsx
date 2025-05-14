import { Column, Grid, SkeletonText } from "@carbon/react";
import { CardItem } from "../../../Card";
import { ActivityDetailProps } from "./definitions";

const SurveyActivityDetail = ({ activityDetail, isLoading }: ActivityDetailProps) => {
  return (
    <Grid className="activity-detail-content-grid">
      <Column sm={4} md={4} lg={16}>
        {isLoading ? (
          <SkeletonText className="activity-detail-content-title" />
        ) : (
          <h6 className="activity-detail-content-title">
            Survery Specifications
          </h6>
        )}
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem label="No. of plots" showSkeleton={isLoading}>
          {activityDetail?.plotsCount}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem label="Minimum plots per stratum" showSkeleton={isLoading}>
          {activityDetail?.surveyMinPlotsPerStratum}
        </CardItem>
      </Column>
    </Grid>
  );
};

export default SurveyActivityDetail;
