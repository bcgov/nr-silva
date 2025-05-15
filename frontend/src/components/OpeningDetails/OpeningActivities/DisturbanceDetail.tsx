import React from "react";
import { MockedDisturbanceDetailType } from "./definitions";
import { Column, Grid } from "@carbon/react";
import { CardItem } from "../../Card";

const DisturbanceDetail = ({ detail }: { detail: MockedDisturbanceDetailType }) => {

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
          {detail.disturbanceLocation}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} max={3}>
        <CardItem label="Licence number">
          {detail.licenceNumber}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} max={3}>
        <CardItem label="Cutting permit">
          {detail.cuttingPermit}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} max={3}>
        <CardItem label="Cut block">
          {detail.cutBlock}
        </CardItem>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <CardItem label="Comment">
          {detail.comment}
        </CardItem>
      </Column>
    </Grid>
  )
}

export default DisturbanceDetail;
