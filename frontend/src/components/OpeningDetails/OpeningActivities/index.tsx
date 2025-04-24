import React from "react";
import { AccordionSkeleton, Column, Grid } from "@carbon/react";

import EmptySection from "../../EmptySection";

import './styles.scss';
import { MOCKED_ACTIVITY_RES, MOCKED_DISTURBANCE_EVENTS } from "./constants";
import DisturbanceAccordion from "./DisturbanceAccordion";
import { useQuery } from "@tanstack/react-query";
import { delayMock } from "../../../utils/MockUtils";

type OpeningActivitiesProps = {
  openingId: number;
}

const OpeningActivities = ({ openingId }: OpeningActivitiesProps) => {

  const disturbanceQuery = useQuery({
    queryKey: ['opening', openingId, 'disturbance'],
    queryFn: () => delayMock(MOCKED_DISTURBANCE_EVENTS)
  });

  const activityQuery = useQuery({
    queryKey: ['opening', openingId, 'activities'],
    queryFn: () => delayMock(MOCKED_ACTIVITY_RES)
  });


  // Loading case
  if (disturbanceQuery.isLoading || activityQuery.isLoading) {
    return (
      <AccordionSkeleton />
    )
  }

  // Empty case
  if (
    disturbanceQuery.isSuccess && activityQuery.isSuccess &&
    (disturbanceQuery.data.length + activityQuery.data.page.totalElements < 1)
  ) {
    return (
      <EmptySection
        pictogram="Summit"
        title="Nothing to show yet!"
        description="No Activities have been added to this opening yet"
      />
    )
  }

  return (
    <Grid className="opening-activities-grid default-grid">
      <Column sm={4} md={8} lg={16}>
        <h3 className="default-tab-content-title">
          {
            `
              ${(activityQuery.data?.page.totalElements ?? 0) + (disturbanceQuery.data?.length ?? 0)}
              activities in the opening area
            `
          }
        </h3>
      </Column>
      {
        (disturbanceQuery.data?.length ?? 0) > 0
          ? (
            <Column sm={4} md={8} lg={16}>
              <DisturbanceAccordion data={disturbanceQuery.data!} />
            </Column>
          )
          : null
      }

      <Column sm={4} md={8} lg={16}>
        act
      </Column>
    </Grid>
  )
}

export default OpeningActivities;
