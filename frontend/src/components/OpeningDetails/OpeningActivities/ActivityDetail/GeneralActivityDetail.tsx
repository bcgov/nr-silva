import { Column, Grid, SkeletonText } from "@carbon/react";
import { UNIQUE_CHARACTERS_UNICODE } from "@/constants";
import { CardItem } from "../../../Card";
import { ActivityDetailProps } from "./definitions";

const GeneralAcitivityDetail = ({ activityDetail, base, isPlanning, isComplex, isLoading }: ActivityDetailProps) => {
  return (
    <Grid className="activity-detail-content-grid">
      <Column sm={4} md={4} lg={16}>
        {isLoading ? (
          <SkeletonText className="activity-detail-content-title" />
        ) : (
          <h6 className="activity-detail-content-title">
            {base?.description}
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
        <CardItem label={`Intra agency n${UNIQUE_CHARACTERS_UNICODE.ORDINAL_INDICATOR}`}
          showSkeleton={isLoading}>
          {activityDetail?.intraAgencyNumber}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem label="Activity Location" showSkeleton={isLoading}>
          {activityDetail?.activityLocation?.locationName}
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
