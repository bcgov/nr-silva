import { Column, Grid, SkeletonText } from "@carbon/react";
import { ActivityDetailProps, MockedActivityDetailType } from "../definitions";
import { CardItem } from "../../../Card";

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
                {activityDetail?.pruningSpecification?.totalStemsPerHa}
            </CardItem>
        </Column>
        
        <Column sm={4} md={4} lg={4} xlg={3} max={2}>
            <CardItem label={'Stems per ha to prune'} showSkeleton={isLoading}>
                {activityDetail?.pruningSpecification?.stemsperHaToPrune}
            </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} xlg={3} max={3}>
            <CardItem label="Target inter-tree distance(m)" showSkeleton={isLoading}>
                {activityDetail?.pruningSpecification?.targetInterTreeDistance}
            </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} xlg={3} max={3}>
            <CardItem label="Minimum inter-tree distance(m)" showSkeleton={isLoading}>
                {activityDetail?.pruningSpecification?.minInterTreeDistance}
            </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} xlg={3} max={3}>
            <CardItem label="Height above ground(m)" showSkeleton={isLoading}>
                {activityDetail?.pruningSpecification?.heightAboveGround}
            </CardItem>
        </Column>

        <Column sm={4} md={4} lg={4} xlg={3} max={2}>
            <CardItem label="Planned cost ($)" showSkeleton={isLoading}>
                {activityDetail?.plannedCost}
            </CardItem>
        </Column>

    </Grid>
    );
};

export default PruningActivityDetail;
