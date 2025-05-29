import React from "react";

import { Column, Grid } from "@carbon/react";

import { OpeningDetailsActivitiesDisturbanceDto } from "@/types/OpenApiTypes";

import { CardItem } from "../../Card";
import Comments from "../../Comments";

const DisturbanceDetail = ({ detail }: { detail: OpeningDetailsActivitiesDisturbanceDto }) => {

  return (
    <Grid className="expanded-row-content-grid">
      <Column sm={4} md={4} lg={16}>
        <h6 className="expanded-row-title">Disturbance overview</h6>
      </Column>

      <Column sm={4} md={4} lg={4} max={3}>
        <CardItem label="Licensee activity ID">
          {detail.licenseeActivityId}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} max={3}>
        <CardItem label="Disturbance location">
          {
            detail.forestClientLocation ? (
              detail.forestClientLocation.locationName ?? null
            ) : null
          }
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} max={3}>
        <CardItem label="Licence number">
          {detail.licenceNumber}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} max={3}>
        <CardItem label="Cutting permit">
          {detail.cuttingPermitId}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} max={3}>
        <CardItem label="Cut block">
          {detail.cutBlock}
        </CardItem>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <CardItem label="Comment">
          <Comments comments={detail.comments ?? []} />
        </CardItem>
      </Column>
    </Grid>
  )
}

export default DisturbanceDetail;
