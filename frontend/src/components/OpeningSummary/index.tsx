import { Column, Grid } from "@carbon/react";
import React from "react";

import './styles.scss';
import { OpeningSearchResponseDto } from "../../types/OpeningTypes";
import CardItem from "../CardItem";
import { PLACE_HOLDER } from "../../constants";
import { getClientLabel } from "../../utils/ForestClientUtils";
import { formatLocalDate } from "../../utils/DateUtils";
import StatusTag from "../StatusTag";

type OpeningSummaryProps = {
  openingObj?: OpeningSearchResponseDto;
  isLoading?: boolean;
}

const OpeningSummary = ({ openingObj, isLoading }: OpeningSummaryProps) => {
  return (
    <Grid className="default-grid opening-summary-grid">

      <Column className="card-title-col" sm={4} md={8} lg={16}>
        <h3>Opening summary</h3>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="opening-number-card-item" label="Opening number" showSkeleton={isLoading}>
          {openingObj?.openingNumber}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="opening-status-card-item" label="Opening status" showSkeleton={isLoading}>
          {
            openingObj?.status?.description
              ? <StatusTag description={openingObj.status.description} />
              : null
          }
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="opening-type-card-item" label="Opening type" showSkeleton={isLoading}>
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="org-unit-card-item" label="Org unit" showSkeleton={isLoading}>
          {openingObj?.orgUnitCode}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="opening-category-card-item" label="Opening category" showSkeleton={isLoading}>
          {openingObj?.category?.code}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="client-card-item" label="Client" showSkeleton={isLoading}>
          {
            getClientLabel({
              id: openingObj?.clientNumber ?? '',
              name: openingObj?.clientName ?? '',
              acronym: openingObj?.clientAcronym ?? ''
            }, true)
          }
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="file-id-card-item" label="File ID" showSkeleton={isLoading}>
          {openingObj?.forestFileId}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="cut-block-card-item" label="Cut block" showSkeleton={isLoading}>
          {openingObj?.cutBlockId}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="cutting-permit-card-item" label="Cutting permit" showSkeleton={isLoading}>
          {openingObj?.cuttingPermitId}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="timber-mark-card-item" label="Timber mark" showSkeleton={isLoading}>
          {openingObj?.timberMark}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="max-allowed-access-card-item" label="Max allowed access" showSkeleton={isLoading}>
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="gross-area-card-item" label="Opening gross area" showSkeleton={isLoading}>
          {openingObj?.openingGrossAreaHa ? `${openingObj?.openingGrossAreaHa} ha` : undefined}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="created-by-card-item" label="Created by" showSkeleton={isLoading}>
          {openingObj?.entryUserId}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="created-by-card-item" label="Created on" showSkeleton={isLoading}>
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="last-updated-card-item" label="Last updated" showSkeleton={isLoading}>
          {formatLocalDate(openingObj?.updateTimestamp)}
        </CardItem>
      </Column>

      <Column sm={4} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="disturbance-start-card-item" label="Disturbance start" showSkeleton={isLoading}>
          {formatLocalDate(openingObj?.disturbanceStartDate)}
        </CardItem>
      </Column>

    </Grid>
  )
}

export default OpeningSummary;
