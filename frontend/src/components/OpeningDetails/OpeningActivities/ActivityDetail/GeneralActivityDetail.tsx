import { Column, Grid, SkeletonText } from "@carbon/react";
import { ActivityDetailProps } from "../definitions";
import { CardItem } from "../../../Card";

const GeneralAcitivityDetail = ({activityDetail, isPlanning, isComplex, isLoading}: ActivityDetailProps) => {
    return (
        <Grid className="activity-detail-content-grid">
            <Column sm={4} md={4} lg={16}>
                {isLoading ? (
                    <SkeletonText className="activity-detail-content-title" />
                ) : (
                    <h6 className="activity-detail-content-title">
                    {activityDetail?.base.description}
                    {isComplex ? " overview" : " details"}
                    </h6>
                )}
            </Column>

            <Column sm={4} md={4} lg={4} xlg={3} max={2}>
                <CardItem label="Licensee activity ID" showSkeleton={isLoading}>
                    {activityDetail?.licenseeActivityId}
                </CardItem>
            </Column>
            
            <Column sm={4} md={4} lg={4} xlg={3} max={2}>
                <CardItem label={`Intra agency n\u00BA`} showSkeleton={isLoading}>
                    {activityDetail?.intraAgencyNumber}
                </CardItem>
            </Column>

            <Column sm={4} md={4} lg={4} xlg={3} max={2}>
                <CardItem label="Activity Location" showSkeleton={isLoading}>
                    {activityDetail?.activityLocation}
                </CardItem>
            </Column>

            <Column sm={4} md={4} lg={4} xlg={3} max={2}>
                <CardItem label="Planned amount (ha)" showSkeleton={isLoading}>
                    {activityDetail?.plannedAmount}
                </CardItem>
            </Column>

            <Column sm={4} md={4} lg={4} xlg={3} max={2}>
                <CardItem label="Treated amount (ha)" showSkeleton={isLoading}>
                    {!isPlanning ? activityDetail?.treatedAmount : null}
                </CardItem>
            </Column>

            <Column sm={4} md={4} lg={4} xlg={3} max={2}>
                <CardItem label="Planned cost ($)" showSkeleton={isLoading}>
                    {activityDetail?.plannedCost}
                </CardItem>
            </Column>

            <Column sm={4} md={4} lg={4} xlg={3} max={2}>
                <CardItem label="Actual cost ($)" showSkeleton={isLoading}>
                    {!isPlanning ? activityDetail?.actualCost : null}
                </CardItem>
            </Column>
        </Grid>
    )
}

export default GeneralAcitivityDetail;
