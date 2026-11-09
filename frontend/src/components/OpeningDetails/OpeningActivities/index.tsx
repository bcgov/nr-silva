import React from "react";
import { AccordionSkeleton, Button, Column, Grid, Stack } from "@carbon/react";
import { Add } from "@carbon/icons-react";
import { generatePath, useNavigate, useSearchParams } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { CREATE_ACTIVITY_PATH } from "@/routes/paths";
import { DEEP_LINK_PARAMS } from "@/constants/deepLinkConstants";
import { gatePostgresFeature } from "@/utils/featureFlags";

import EmptySection from "../../EmptySection";
import DisturbanceAccordion from "./DisturbanceAccordion";
import ActivityAccordion from "./ActivityAccordion";
import { DefaultFilter } from "./constants";

import "./styles.scss";

type OpeningActivitiesProps = {
  openingId: number;
  selectedSilvicultureActivityIds: string[];
  setSelectedSilvicultureActivityIds: React.Dispatch<React.SetStateAction<string[]>>;
  selectedDisturbanceIds: string[];
  setSelectedDisturbanceIds: React.Dispatch<React.SetStateAction<string[]>>;
}

const OpeningActivities = ({
  openingId,
  selectedSilvicultureActivityIds,
  setSelectedSilvicultureActivityIds,
  selectedDisturbanceIds,
  setSelectedDisturbanceIds
}: OpeningActivitiesProps) => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const targetDisturbanceId = searchParams.get(DEEP_LINK_PARAMS.disturbanceId);
  const targetActivityId = searchParams.get(DEEP_LINK_PARAMS.activityId);

  const disturbanceQuery = useQuery({
    queryKey: ['opening', openingId, 'disturbance'],
    queryFn: () => API.OpeningEndpointService.getOpeningDisturbances(openingId),
  });

  const activityQuery = useQuery({
    queryKey: ['opening', openingId, 'activities', DefaultFilter],
    queryFn: () => {
      const { page, size, sortField, sortDirection } = DefaultFilter;

      return API.OpeningEndpointService.getOpeningActivities(
        openingId,
        undefined,
        page,
        size,
        [`${sortField},${sortDirection}`]
      );
    },
  });

  const navToCreateActivity = () => (
    navigate(generatePath(CREATE_ACTIVITY_PATH, { openingId: String(openingId) }))
  );

  // Loading case
  if (disturbanceQuery.isLoading || activityQuery.isLoading) {
    return (
      <AccordionSkeleton />
    )
  }

  // Empty case
  if (
    disturbanceQuery.isSuccess && activityQuery.isSuccess &&
    ((disturbanceQuery.data.content?.length ?? 0) + (activityQuery.data.page?.totalElements ?? 0) < 1)
  ) {
    return (
      <EmptySection
        pictogram="Summit"
        title="Nothing to show yet!"
        description={
          <Stack orientation="vertical" gap={8}>
            <div>
              <p>No Activities have been added to this opening yet</p>
            </div>
            <Stack orientation="horizontal" gap={8}>
              <Button disabled kind="tertiary" renderIcon={Add}>
                Add disturbance
              </Button>

              <Button kind="tertiary" renderIcon={Add} onClick={navToCreateActivity}>
                Add activity
              </Button>
            </Stack>
          </Stack>
        }
      />
    )
  }

  return (
    <Grid className="opening-activities-grid default-grid">
      <Column sm={4} md={8} lg={8} max={12}>
        <h3 className="default-tab-content-title">
          {
            `
              ${(activityQuery.data?.page?.totalElements ?? 0) + (disturbanceQuery.data?.content?.length ?? 0)}
              activities in the opening area
            `
          }
        </h3>
      </Column>
      {
        gatePostgresFeature()
          ? null
          : (
            <Column sm={4} md={4} lg={4} max={2}>
              <Stack orientation="horizontal" gap={4}>
                <Button
                  kind="primary"
                  disabled
                  size="sm"
                  renderIcon={Add}
                >
                  Add disturbance
                </Button>
                <Button
                  kind="primary"
                  size="sm"
                  renderIcon={Add}
                  onClick={navToCreateActivity}
                >
                  Add activity
                </Button>
              </Stack>
            </Column>
          )
      }

      {
        (disturbanceQuery.data?.content?.length ?? 0) > 0
          ? (
            <Column sm={4} md={8} lg={16}>
              <DisturbanceAccordion
                openingId={openingId}
                data={disturbanceQuery.data!.content!}
                selectedDisturbanceIds={selectedDisturbanceIds}
                setSelectedDisturbanceIds={setSelectedDisturbanceIds}
                targetDisturbanceId={targetDisturbanceId}
              />
            </Column>
          )
          : null
      }

      {
        (activityQuery.data?.page?.totalElements ?? 0) > 0
          ? (
            <Column sm={4} md={8} lg={16}>
              <Stack gap={4} orientation="horizontal" className="opening-activities-stack">
                <ActivityAccordion
                  openingId={openingId}
                  totalUnfiltered={activityQuery.data?.page?.totalElements ?? 0}
                  selectedSilvicultureActivityIds={selectedSilvicultureActivityIds}
                  setSelectedSilvicultureActivityIds={setSelectedSilvicultureActivityIds}
                  targetActivityId={targetActivityId}
                />
              </Stack>
            </Column>
          )
          : null
      }
    </Grid>
  )
}

export default OpeningActivities;
