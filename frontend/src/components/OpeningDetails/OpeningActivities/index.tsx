import React from "react";
import { AccordionSkeleton, Column, Grid } from "@carbon/react";

import EmptySection from "../../EmptySection";

import './styles.scss';
import { MOCKED_ACTIVITY_RES, MOCKED_DISTURBANCE_EVENTS } from "./constants";
import DisturbanceAccordion from "./DisturbanceAccordion";

type OpeningActivitiesProps = {
  openingId: number;
}

const OpeningActivities = ({ openingId }: OpeningActivitiesProps) => {

  const isLoading = false;


  // Loading case
  if (isLoading) {
    return (
      <AccordionSkeleton />
    )
  }

  // Empty case
  if (MOCKED_ACTIVITY_RES.page.totalElements + MOCKED_DISTURBANCE_EVENTS.length < 1) {
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
            ${MOCKED_ACTIVITY_RES.page.totalElements + MOCKED_DISTURBANCE_EVENTS.length}
            activities in the opening area
            `
          }
        </h3>
      </Column>
      {
        MOCKED_DISTURBANCE_EVENTS.length > 0
          ? (
            <Column sm={4} md={8} lg={16}>
              <DisturbanceAccordion data={MOCKED_DISTURBANCE_EVENTS} />
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
