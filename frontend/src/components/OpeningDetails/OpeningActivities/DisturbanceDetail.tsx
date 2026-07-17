import React from "react";

import { Column, Grid } from "@carbon/react";

import { OpeningDetailsActivitiesDisturbanceDto } from "@/services/OpenApi";
import Comments from "@/components/Comments";
import { CardItem } from "@/components/Card";
import useDeepLinkScroll from "@/hooks/useDeepLinkScroll";
import { DEEP_LINK_ELEMENT_ID } from "@/constants/deepLinkConstants";

type DisturbanceDetailProps = {
  detail: OpeningDetailsActivitiesDisturbanceDto;
  targetComment?: boolean;
};

const DisturbanceDetail = ({ detail, targetComment }: DisturbanceDetailProps) => {
  useDeepLinkScroll(
    targetComment ? DEEP_LINK_ELEMENT_ID.disturbanceComment(detail.atuId) : null,
    true
  );

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

      <Column sm={4} md={8} lg={16} id={DEEP_LINK_ELEMENT_ID.disturbanceComment(detail.atuId)}>
        <CardItem label="Comment">
          <Comments comments={detail.comments ?? []} />
        </CardItem>
      </Column>
    </Grid>
  )
}

export default DisturbanceDetail;
