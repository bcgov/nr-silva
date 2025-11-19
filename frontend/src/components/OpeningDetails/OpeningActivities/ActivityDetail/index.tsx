import { JSX } from "react";

import { Column, Grid } from "@carbon/react";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { OpeningDetailsActivitiesActivitiesDto } from "@/services/OpenApi";
import { isAuthRefreshInProgress } from "@/constants/tanstackConfig";

import DirectSeedingActivityDetail from "./DirectSeedingActivityDetail";
import JuvenileSpacingActivityDetail from "./JuvenileSpacingActivityDetail";
import PlantingActivityDetail from "./PlantingActivityDetail";
import PruningActivityDetail from "./PruningActivityDetail";
import SitePreparationActivityDetail from "./SitePreprationActivityDetail";
import SurveyActivityDetail from "./SurveyActivityDetail";
import GeneralAcitivityDetail from "./GeneralActivityDetail";
import { CardItem } from "../../../Card";
import Comments from "../../../Comments";
import { COMPLEX_ACTIVITY_CODE } from "../constants";

import "./styles.scss";

type ActivityDetailOutlineProps = {
  activity: OpeningDetailsActivitiesActivitiesDto;
  openingId: number;
};

const ActivityDetail = ({ activity, openingId, }: ActivityDetailOutlineProps) => {
  const activityDetailQuery = useQuery({
    queryKey: ["opening", openingId, "activities", activity.atuId],
    queryFn: () => API.OpeningEndpointService.getOpeningActivity(openingId, activity.atuId),
    enabled: !!activity && !!openingId,
  });

  const isComplexActivity = () => {
    const code = activity.base?.code;
    return code ? COMPLEX_ACTIVITY_CODE.includes(code as typeof COMPLEX_ACTIVITY_CODE[number]) : false;
  };

  const renderAdditionalDetail = () => {
    const code = activity.base?.code;

    if (!code) return null;

    const isLoading = activityDetailQuery.isLoading || isAuthRefreshInProgress();

    const detailComponents: Record<string, JSX.Element> = {
      DS: <DirectSeedingActivityDetail activityDetail={activityDetailQuery.data} isLoading={isLoading} />,
      JS: <JuvenileSpacingActivityDetail activityDetail={activityDetailQuery.data} isLoading={isLoading} />,
      PL: <PlantingActivityDetail activityDetail={activityDetailQuery.data} isLoading={isLoading} />,
      PR: <PruningActivityDetail activityDetail={activityDetailQuery.data} isLoading={isLoading} />,
      SP: <SitePreparationActivityDetail activityDetail={activityDetailQuery.data} isLoading={isLoading} />,
      SU: <SurveyActivityDetail activityDetail={activityDetailQuery.data} isLoading={isLoading} />,
    };

    return detailComponents[code];
  };

  return (
    <Grid className="activity-detail-grid">

      <Column sm={4} md={4} lg={16}>
        <GeneralAcitivityDetail
          activityDetail={activityDetailQuery.data}
          base={activity.base}
          isPlanning={activity.status.code === "P" ? true : false}
          isComplex={isComplexActivity()}
          isLoading={activityDetailQuery.isLoading || isAuthRefreshInProgress()} />
      </Column>

      {isComplexActivity() ? (
        <Column sm={4} md={4} lg={16}>
          {renderAdditionalDetail()}
        </Column>
      ) : null}

      <Column sm={4} md={4} lg={16}>
        <CardItem label="Comment" showSkeleton={activityDetailQuery.isLoading || isAuthRefreshInProgress()}>
          <Comments comments={activityDetailQuery.data?.comments ?? []} />
        </CardItem>
      </Column>

    </Grid>
  )
};

export default ActivityDetail;
