import React, { useState } from "react";
import { Button, ButtonSkeleton, Column, Grid } from "@carbon/react";
import { Location } from "@carbon/icons-react";

import { OpeningSearchResponseDto } from "@/types/OpeningTypes";
import { CardItem } from "@/components/Card";
import { getClientLabel } from "@/utils/ForestClientUtils";
import { formatLocalDate } from "@/utils/DateUtils";
import StatusTag from "@/components/StatusTag";
import OpeningsMap from "@/components/OpeningsMap";

import './styles.scss';

type OpeningSummaryProps = {
  openingObj?: OpeningSearchResponseDto;
  isLoading?: boolean;
}

const OpeningSummary = ({ openingObj, isLoading }: OpeningSummaryProps) => {
  const [showMap, setShowMap] = useState<boolean>(true);

  return (
    <Grid className="default-grid opening-summary-grid">

      <Column className="card-title-col" sm={4} md={8} lg={16}>
        <div className="card-title-container">
          <h3>Opening summary</h3>
          {
            isLoading ?
              (
                <ButtonSkeleton size="sm" />
              )
              : (
                <Button
                  type="button"
                  kind="tertiary"
                  size="sm"
                  renderIcon={Location}
                  onClick={() => setShowMap((prev) => !prev)}
                >
                  {`${showMap ? 'Hide' : 'Show'} Map`}
                </Button>
              )
          }
        </div>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="opening-number-card-item" label="Opening number" showSkeleton={isLoading}>
          {openingObj?.openingNumber}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="opening-status-card-item" label="Opening status" showSkeleton={isLoading}>
          {
            openingObj?.status?.description
              ? <StatusTag description={openingObj.status.description} />
              : null
          }
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="opening-type-card-item" label="Opening type" showSkeleton={isLoading}>
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="org-unit-card-item" label="Org unit" showSkeleton={isLoading} tooltipText={openingObj?.orgUnitName}>
          {openingObj?.orgUnitCode}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="opening-category-card-item" label="Opening category" showSkeleton={isLoading} tooltipText={openingObj?.category?.description}>
          {openingObj?.category?.code}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
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

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="file-id-card-item" label="File ID" showSkeleton={isLoading}>
          {openingObj?.forestFileId}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="cut-block-card-item" label="Cut block" showSkeleton={isLoading}>
          {openingObj?.cutBlockId}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="cutting-permit-card-item" label="Cutting permit" showSkeleton={isLoading}>
          {openingObj?.cuttingPermitId}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="timber-mark-card-item" label="Timber mark" showSkeleton={isLoading}>
          {openingObj?.timberMark}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="max-allowed-access-card-item" label="Max allowed access" showSkeleton={isLoading}>
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="gross-area-card-item" label="Opening gross area" showSkeleton={isLoading}>
          {openingObj?.openingGrossAreaHa ? `${openingObj?.openingGrossAreaHa} ha` : undefined}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="created-by-card-item" label="Created by" showSkeleton={isLoading}>
          {openingObj?.entryUserId}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="created-by-card-item" label="Created on" showSkeleton={isLoading}>
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="last-updated-card-item" label="Last updated" showSkeleton={isLoading}>
          {formatLocalDate(openingObj?.updateTimestamp)}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem id="disturbance-start-card-item" label="Disturbance start" showSkeleton={isLoading}>
          {formatLocalDate(openingObj?.disturbanceStartDate)}
        </CardItem>
      </Column>

      {
        openingObj?.openingId && showMap
          ? (
            <Column className="map-col" sm={4} md={8} lg={16}>
              <OpeningsMap
                openingIds={[openingObj.openingId]}
                setOpeningPolygonNotFound={() => { }}
                mapHeight={280}
              />
            </Column>
          )
          : null
      }
    </Grid >
  )
}

export default OpeningSummary;
