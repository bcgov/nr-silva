import React from "react";
import { Column, Grid } from "@carbon/react";

import EmptySection from "../../EmptySection";

import './styles.scss';
import { MOCKED_ACTIVITY_RES, MOCKED_DISTURBANCE_EVENTS } from "./constants";


const OpeningActivities = () => {

  if (MOCKED_ACTIVITY_RES.page.totalElements + MOCKED_DISTURBANCE_EVENTS.length < 100) {
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
    </Grid>
  )
}

export default OpeningActivities;
