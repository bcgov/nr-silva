import React from "react";
import { AccordionSkeleton, Column, Grid } from "@carbon/react";

import EmptySection from "../../EmptySection";

import './styles.scss';
import DisturbanceAccordion from "./DisturbanceAccordion";
import { useQuery } from "@tanstack/react-query";
import ActivityAccordion from "./ActivityAccordion";
import { fetchOpeningActivities, fetchOpeningDisturbances } from "@/services/OpeningDetailsService";

type OpeningActivitiesProps = {
  openingId: number;
}

const OpeningActivities = ({ openingId }: OpeningActivitiesProps) => {

  const disturbanceQuery = useQuery({
    queryKey: ['opening', openingId, 'disturbance'],
    queryFn: () => fetchOpeningDisturbances(openingId),
  });

  const activityQuery = useQuery({
    queryKey: ['opening', openingId, 'activities'],
    queryFn: () => fetchOpeningActivities(openingId),
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
    (disturbanceQuery.data.content.length + activityQuery.data.page.totalElements < 1)
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
              ${(activityQuery.data?.page.totalElements ?? 0) + (disturbanceQuery.data?.content.length ?? 0)}
              activities in the opening area
            `
          }
        </h3>
      </Column>
      {
        (disturbanceQuery.data?.content.length ?? 0) > 0
          ? (
            <Column sm={4} md={8} lg={16}>
              <DisturbanceAccordion data={disturbanceQuery.data!.content} />
            </Column>
          )
          : null
      }

      {
        (activityQuery.data?.page.totalElements ?? 0) > 0
          ? (
            <Column sm={4} md={8} lg={16}>
              <ActivityAccordion data={activityQuery.data?.content!} openingId={openingId}/>
            </Column>
          )
          : null
      }
    </Grid>
  )
}

export default OpeningActivities;
