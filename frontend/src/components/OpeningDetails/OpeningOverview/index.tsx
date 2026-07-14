import React from "react";
import { Column, Grid } from "@carbon/react";
import { useSearchParams } from "react-router-dom";

import { CardContainer, CardItem, CardTitle } from "@/components/Card";
import { OpeningDetailsOverviewDto } from "@/services/OpenApi";
import useDeepLinkScroll from "@/hooks/useDeepLinkScroll";
import { DEEP_LINK_PARAMS, DEEP_LINK_SECTIONS, DEEP_LINK_ELEMENT_ID } from "@/constants/deepLinkConstants";

import { formatLocalDate } from "@/utils/DateUtils";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import { PLACE_HOLDER } from "@/constants";
import Comments from "@/components/Comments";

import "./styles.scss";

type OpeningOverviewProps = {
  isLoading?: boolean
  overviewObj?: OpeningDetailsOverviewDto
}

const OpeningOverview = ({ overviewObj, isLoading }: OpeningOverviewProps) => {
  const [searchParams] = useSearchParams();
  const section = searchParams.get(DEEP_LINK_PARAMS.section);

  useDeepLinkScroll(
    section === DEEP_LINK_SECTIONS.openingComment ? DEEP_LINK_ELEMENT_ID.openingComment : null,
    !isLoading
  );

  return (
    <CardContainer className="opening-overview-card-container">
      <Column sm={4} md={8} lg={16}>
        <CardTitle title="Opening" />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Grid className="default-card-section-grid">
          <Column sm={4} md={4} lg={3}>
            <CardItem label="Licensee opening ID" showSkeleton={isLoading}>
              {overviewObj?.opening.licenseeId}
            </CardItem>
          </Column>
          <Column sm={4} md={4} lg={3}>
            <CardItem label="Tenure type" showSkeleton={isLoading}>
              {codeDescriptionToDisplayText(overviewObj?.opening.tenureType)}
            </CardItem>
          </Column>
          <Column sm={4} md={4} lg={3}>
            <CardItem label="Management unit type" showSkeleton={isLoading}>
              {codeDescriptionToDisplayText(overviewObj?.opening.managementUnitType)}
            </CardItem>
          </Column>
          <Column sm={4} md={4} lg={3}>
            <CardItem label="Management unit ID" showSkeleton={isLoading}>
              {overviewObj?.opening.managementUnitId}
            </CardItem>
          </Column>
          <Column sm={4} md={4} lg={4}>
            <CardItem label="Timber sales office" showSkeleton={isLoading}>
              {codeDescriptionToDisplayText(overviewObj?.opening.timberSaleOffice)}
            </CardItem>
          </Column>
          <Column sm={4} md={8} lg={16} id={DEEP_LINK_ELEMENT_ID.openingComment}>
            <CardItem label="Comment" showSkeleton={isLoading}>
              <Comments comments={overviewObj?.opening.comments.filter((comment) => comment.commentType.code !== "FORCOVER") ?? []} />
            </CardItem>
          </Column>
        </Grid>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <hr />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <CardTitle title="Milestone" />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Grid className="default-card-section-grid">
          <Column className="standard-unit-card-title-col" sm={4} md={8} lg={16}>
            <h5>{`Standards unit: ${overviewObj?.milestones.standardsUnitId ?? PLACE_HOLDER}`}</h5>
          </Column>

          <Column sm={4} md={8} lg={16}>
            <CardItem label="Post harvested declared date" showSkeleton={isLoading}>
              {formatLocalDate(overviewObj?.milestones.postHarvestDeclaredDate, true)}
            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={5}>
            <CardItem label="Regeneration offset (Years)" showSkeleton={isLoading}>
              {overviewObj?.milestones.regenOffsetYears}
            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={5}>
            <CardItem label="Regeneration declared date" showSkeleton={isLoading}>
              {formatLocalDate(overviewObj?.milestones.regenDeclaredDate, true)}
            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={5}>
            <CardItem label="Regeneration due date" showSkeleton={isLoading}>
              {formatLocalDate(overviewObj?.milestones.regenDueDate, true)}
            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={5}>
            <CardItem label="Free growing offset (Years)" showSkeleton={isLoading}>
              {overviewObj?.milestones.freeGrowingOffsetYears}
            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={5}>
            <CardItem label="Free growing declared date" showSkeleton={isLoading}>
              {formatLocalDate(overviewObj?.milestones.freeGrowingDeclaredDate, true)}
            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={5}>
            <CardItem label="Free growing due date" showSkeleton={isLoading}>
              {formatLocalDate(overviewObj?.milestones.freeGrowingDueDate, true)}
            </CardItem>
          </Column>
        </Grid>
      </Column>

    </CardContainer>
  )
}

export default OpeningOverview;
