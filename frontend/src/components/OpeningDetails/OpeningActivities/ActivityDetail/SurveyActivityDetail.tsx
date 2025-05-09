import { Column, Grid, SkeletonText } from "@carbon/react";
import { ActivityDetailProps, MockedActivityDetailType } from "../definitions";
import { CardItem } from "../../../Card";

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
                    {activityDetail?.surveySpecification?.numberOfPlots}
                </CardItem>
            </Column>

            <Column sm={4} md={4} lg={4} xlg={3} max={2}>
                <CardItem label="Minimum plots per stratum" showSkeleton={isLoading}>
                    {activityDetail?.surveySpecification?.minPlotsPerStratum}
                </CardItem>
            </Column>
        </Grid>
    );
};

export default SurveyActivityDetail;
