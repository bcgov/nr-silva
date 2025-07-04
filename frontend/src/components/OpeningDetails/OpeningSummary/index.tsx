import React, { useState } from "react";
import { Button, ButtonSkeleton, Column, Grid } from "@carbon/react";
import { Location } from "@carbon/icons-react";

import { OpeningDetailsTombstoneDto } from "@/services/OpenApi";
import { mapKinds, MapKindType } from "@/types/MapLayer";
import { CardItem } from "@/components/Card";
import { getClientLabel } from "@/utils/ForestClientUtils";
import { formatLocalDate } from "@/utils/DateUtils";
import { OpeningStatusTag } from "@/components/Tags";
import OpeningsMap from "@/components/OpeningsMap";

import "./styles.scss";

type OpeningSummaryProps = {
  openingId?: number;
  tombstoneObj?: OpeningDetailsTombstoneDto;
  isLoading?: boolean;
  currentTab: number;
};

const OpeningSummary = ({
  openingId,
  tombstoneObj,
  isLoading,
  currentTab,
}: OpeningSummaryProps) => {
  const [showMap, setShowMap] = useState<boolean>(true);

  const mapKind = (tabIndex: number): MapKindType => {
    switch (tabIndex) {
      case 0:
        return mapKinds[0]!.code as MapKindType;
      case 1:
        return mapKinds[1]!.code as MapKindType;
      case 2:
        return mapKinds[2]!.code as MapKindType;
      case 3:
        return mapKinds[3]!.code as MapKindType;
      default:
        return mapKinds[0]!.code as MapKindType;
    }
  };

  return (
    <Grid className="default-grid opening-summary-grid">
      <Column className="card-title-col" sm={4} md={8} lg={16}>
        <div className="card-title-container">
          <h3>Opening summary</h3>
          {isLoading ? (
            <ButtonSkeleton size="sm" />
          ) : (
            <Button
              type="button"
              kind="tertiary"
              size="sm"
              renderIcon={Location}
              onClick={() => setShowMap((prev) => !prev)}
              data-testid="toggle-map-button"
            >
              {`${showMap ? "Hide" : "Show"} Map`}
            </Button>
          )}
        </div>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem
          id="opening-number-card-item"
          label="Opening number"
          showSkeleton={isLoading}
        >
          {tombstoneObj?.openingNumber?.trim() === ""
            ? null
            : tombstoneObj?.openingNumber}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem
          id="opening-status-card-item"
          label="Opening status"
          showSkeleton={isLoading}
        >
          {tombstoneObj?.openingStatus?.description ? (
            <OpeningStatusTag status={tombstoneObj.openingStatus} />
          ) : null}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem
          id="opening-type-card-item"
          label="Opening type"
          showSkeleton={isLoading}
        >
          {/* Will be implemented in the future */}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem
          id="org-unit-card-item"
          label="Org unit"
          showSkeleton={isLoading}
          tooltipText={tombstoneObj?.orgUnitName ?? ""}
        >
          {tombstoneObj?.orgUnitCode}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem
          id="opening-category-card-item"
          label="Opening category"
          showSkeleton={isLoading}
          tooltipText={tombstoneObj?.openCategory.description!}
        >
          {tombstoneObj?.openCategory.code}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem
          id="client-card-item"
          label="Client"
          showSkeleton={isLoading}
          tooltipText={tombstoneObj?.client?.clientName}
        >
          {
            getClientLabel(
              {
                id: tombstoneObj?.client.clientNumber ?? "",
                name: "",
                acronym: tombstoneObj?.client.acronym ?? "",
              },
              true
            )
          }
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem
          id="file-id-card-item"
          label="File ID"
          showSkeleton={isLoading}
        >
          {tombstoneObj?.fileId}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem
          id="cut-block-card-item"
          label="Cut block"
          showSkeleton={isLoading}
        >
          {tombstoneObj?.cutBlockID}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem
          id="cutting-permit-card-item"
          label="Cutting permit"
          showSkeleton={isLoading}
        >
          {tombstoneObj?.cuttingPermitId}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem
          id="timber-mark-card-item"
          label="Timber mark"
          showSkeleton={isLoading}
        >
          {tombstoneObj?.timberMark}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem
          id="max-allowed-access-card-item"
          label="Max allowed access"
          showSkeleton={isLoading}
        >
          {tombstoneObj?.maxAllowedAccess}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem
          id="gross-area-card-item"
          label="Opening gross area"
          showSkeleton={isLoading}
        >
          {tombstoneObj?.openingGrossArea
            ? `${tombstoneObj?.openingGrossArea} ha`
            : undefined}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem
          id="created-by-card-item"
          label="Created by"
          showSkeleton={isLoading}
        >
          {tombstoneObj?.createdBy}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem
          id="created-by-card-item"
          label="Created on"
          showSkeleton={isLoading}
        >
          {formatLocalDate(tombstoneObj?.createdOn, true)}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem
          id="last-updated-card-item"
          label="Last updated"
          showSkeleton={isLoading}
        >
          {formatLocalDate(tombstoneObj?.lastUpdatedOn, true)}
        </CardItem>
      </Column>

      <Column sm={2} md={4} lg={4} xlg={3} max={2}>
        <CardItem
          id="disturbance-start-card-item"
          label="Disturbance start"
          showSkeleton={isLoading}
        >
          {formatLocalDate(tombstoneObj?.disturbanceStartDate, true)}
        </CardItem>
      </Column>

      {tombstoneObj?.openingNumber && showMap ? (
        <Column className="map-col" sm={4} md={8} lg={16}>
          <OpeningsMap
            openingIds={openingId ? [openingId] : null}
            setOpeningPolygonNotFound={() => { }}
            mapHeight={280}
            layerFilter={true}
            kind={mapKind(currentTab)}
          />
        </Column>
      ) : null}
    </Grid>
  );
};

export default OpeningSummary;
