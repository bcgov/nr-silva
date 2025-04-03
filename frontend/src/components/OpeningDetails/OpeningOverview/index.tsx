import React from "react";
import { CardContainer, CardItem, CardTitle } from "@/components/Card";
import { Column, Grid } from "@carbon/react";

type OpeningOverviewProps = {
  isLoading?: boolean
}

const OpeningOverview = ({ isLoading }: OpeningOverviewProps) => {

  return (
    <CardContainer>
      <Column sm={4} md={8} lg={16}>
        <CardTitle title="Opening" />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Grid className="default-card-section-grid">
          <Column sm={2} md={4} lg={3}>
            <CardItem label="Licensee opening ID" showSkeleton={isLoading}>

            </CardItem>
          </Column>
          <Column sm={2} md={4} lg={3}>
            <CardItem label="Tenure type" showSkeleton={isLoading}>

            </CardItem>
          </Column>
          <Column sm={2} md={4} lg={3}>
            <CardItem label="Management unit type" showSkeleton={isLoading}>

            </CardItem>
          </Column>
          <Column sm={2} md={4} lg={3}>
            <CardItem label="Management unit ID" showSkeleton={isLoading}>

            </CardItem>
          </Column>
          <Column sm={2} md={4} lg={4}>
            <CardItem label="Timber sales office" showSkeleton={isLoading}>

            </CardItem>
          </Column>
          <Column sm={4} md={8} lg={16}>
            <CardItem label="Comment" showSkeleton={isLoading}>

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
          <Column sm={4} md={8} lg={16}>
            <CardItem label="Post harvested declared date" showSkeleton={isLoading}>

            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={4}>
            <CardItem label="Regeneration declared date" showSkeleton={isLoading}>

            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={4}>
            <CardItem label="Regeneration offset" showSkeleton={isLoading}>

            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={4}>
            <CardItem label="Regeneration due date" showSkeleton={isLoading}>

            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={4}>
            <CardItem label="Free growing declared date" showSkeleton={isLoading}>

            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={4}>
            <CardItem label="Free growing offset" showSkeleton={isLoading}>

            </CardItem>
          </Column>

          <Column sm={4} md={4} lg={4}>
            <CardItem label="Free growing due date" showSkeleton={isLoading}>

            </CardItem>
          </Column>
        </Grid>
      </Column>

    </CardContainer>
  )
}

export default OpeningOverview;
