import { useQuery } from "@tanstack/react-query";
import { MockedActivityType } from "../definitions";
import { delayMock } from "@/utils/MockUtils";
import { COMPLEX_ACTIVITY_CODE, MOCKED_ACTIVITY_DETAIL } from "../constants";
import { Column, Grid } from "@carbon/react";
import DirectSeedingActivityDetail from "./DirectSeedingActivityDetail";
import JuvenileSpacingActivityDetail from "./JuvenileSpacingActivityDetail";
import PlantingActivityDetail from "./PlantingActivityDetail";
import PruningActivityDetail from "./PruningActivityDetail";
import SitePreparationActivityDetail from "./SitePreprationActivityDetail";
import SurveyActivityDetail from "./SurveyActivityDetail";
import { JSX } from "react";
import GeneralAcitivityDetail from "./GeneralActivityDetail";
import { CardItem } from "../../../Card";

type ActivityDetailOutlineProps = {
  activity: MockedActivityType;
  openingId: number;
  isExpanded: boolean;
};

const ActivityDetail = ({ activity, openingId, isExpanded }: ActivityDetailOutlineProps) => {
  const activityDetailQuery = useQuery({
    queryKey: ["opening", openingId, "activities", activity.activityId],
    queryFn: () => {
      const matchingActivity = MOCKED_ACTIVITY_DETAIL.find((item) => item.activityId === activity.activityId);
      return delayMock(matchingActivity);
    },
    enabled: !!activity && !!openingId && isExpanded,
  });

  const isComplexActivity = () => {
    const code = activityDetailQuery.data?.base.code;
    return code ? COMPLEX_ACTIVITY_CODE.includes(code as typeof COMPLEX_ACTIVITY_CODE[number]) : false;
  };

  const renderAdditionalDetail = () => {
    const code = activityDetailQuery.data?.base.code;
    
    if (!code) return null;

    const detailComponents: Record<string, JSX.Element> = {
      DS: <DirectSeedingActivityDetail activityDetail={activityDetailQuery.data} isLoading={activityDetailQuery.isLoading} />,
      JS: <JuvenileSpacingActivityDetail activityDetail={activityDetailQuery.data} isLoading={activityDetailQuery.isLoading} />,
      PL: <PlantingActivityDetail activityDetail={activityDetailQuery.data} isLoading={activityDetailQuery.isLoading}/>,
      PR: <PruningActivityDetail activityDetail={activityDetailQuery.data} isLoading={activityDetailQuery.isLoading}/>,
      SP: <SitePreparationActivityDetail activityDetail={activityDetailQuery.data} isLoading={activityDetailQuery.isLoading}/>,
      SU: <SurveyActivityDetail activityDetail={activityDetailQuery.data} isLoading={activityDetailQuery.isLoading}/>,
    };

    return detailComponents[code];
  };

  return (
    <Grid className="activity-detail-grid">
      <Column sm={4} md={4} lg={16}>
        <GeneralAcitivityDetail
          activityDetail={activityDetailQuery.data}
          isPlanning={activity.status.code === "P" ? true : false}
          isComplex={isComplexActivity()}
          isLoading={activityDetailQuery.isLoading}/>
      </Column>
      {isComplexActivity() && (
        <Column sm={4} md={4} lg={16}>
          {renderAdditionalDetail()}
        </Column>
      )}
      <Column sm={4} md={4} lg={16}>
        <CardItem label="Comment" showSkeleton={activityDetailQuery.isLoading}>
          {activityDetailQuery.data?.comment}
        </CardItem>
      </Column>
    </Grid>
  )
};

export default ActivityDetail;
