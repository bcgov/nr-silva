import React, { useState } from "react";
import { AccordionSkeleton, Button, Column, Grid, Stack } from "@carbon/react";
import { Add } from "@carbon/icons-react";
import { generatePath, useNavigate, useSearchParams } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import useBreakpoint from "@/hooks/UseBreakpoint";
import { CREATE_ACTIVITY_PATH } from "@/routes/paths";
import { DEEP_LINK_PARAMS } from "@/constants/deepLinkConstants";

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
  const breakPoint = useBreakpoint();
  const targetDisturbanceId = searchParams.get(DEEP_LINK_PARAMS.disturbanceId);
  const targetActivityId = searchParams.get(DEEP_LINK_PARAMS.activityId);
  const [isActivityAccordionOpen, setIsActivityAccordionOpen] = useState(!!targetActivityId);
  const showAddActivityButton = !isActivityAccordionOpen && breakPoint !== "sm" && breakPoint !== "md";

  const disturbanceQuery = useQuery({
    queryKey: ['opening', openingId, 'disturbance'],
    queryFn: () => API.OpeningEndpointService.getOpeningDisturbances(openingId),
  });

  const activityQuery = useQuery({
    queryKey: ['opening', openingId, 'activities', DefaultFilter],
    queryFn: () => {
      const { page, size } = DefaultFilter;

      return API.OpeningEndpointService.getOpeningActivities(
        openingId,
        undefined,
        page,
        size,
        undefined
      );
    },
  });

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
              ${(activityQuery.data?.page?.totalElements ?? 0) + (disturbanceQuery.data?.content?.length ?? 0)}
              activities in the opening area
            `
          }
        </h3>
      </Column>
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
                  onOpenChange={setIsActivityAccordionOpen}
                />

                {showAddActivityButton && (
                  <div className="add-activity-button-wrapper">
                    <Button
                      kind="tertiary"
                      hasIconOnly
                      renderIcon={() => <Add size={24} />}
                      iconDescription="Add activity"
                      size="2xl"
                      className="add-activity-button"
                      onClick={() => navigate(generatePath(CREATE_ACTIVITY_PATH, { openingId: String(openingId) }))}
                    />
                  </div>
                )}
              </Stack>
            </Column>
          )
          : null
      }
    </Grid>
  )
}

export default OpeningActivities;
