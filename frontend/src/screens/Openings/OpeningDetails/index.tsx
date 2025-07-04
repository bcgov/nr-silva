import React, { useEffect, useState, lazy, Suspense } from "react";
import {
  Column,
  Grid,
  Tab,
  TabList,
  TabPanel,
  TabPanels,
  Tabs,
  TextAreaSkeleton,
  AccordionSkeleton,
} from "@carbon/react";
import { AxiosError } from "axios";
import {
  MapBoundaryVegetation,
  Development,
  CropHealth,
  Certificate,
  VegetationAsset,
  DocumentAttachment
} from "@carbon/icons-react";
import { useParams, useSearchParams } from "react-router-dom";
import { useMutation, useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import ActionableFavouriteButton from "@/components/FavoriteButton/ActionableFavouriteButton";
import PageTitle from "@/components/PageTitle";
import EmptySection from "@/components/EmptySection";
import { OpeningSummary } from "@/components/OpeningDetails";
import OpeningNotifications from "@/components/OpeningDetails/OpeningNotifications";

import { OpeningDetailBreadCrumbs, OpeningDetailsTabs } from "./constants";

// Lazy load each tab content so data is only fetched when the tab is selected.
const OpeningOverview = lazy(
  () => import("@/components/OpeningDetails/OpeningOverview")
);
const TenureIdentification = lazy(
  () => import("@/components/OpeningDetails/TenureIdentification")
);
const OpeningStandardUnits = lazy(
  () => import("@/components/OpeningDetails/OpeningStandardUnits")
);
const OpeningActivities = lazy(
  () => import("@/components/OpeningDetails/OpeningActivities")
);
const OpeningForestCover = lazy(
  () => import("@/components/OpeningDetails/OpeningForestCover")
);
const OpeningAttachment = lazy(
  () => import("@/components/OpeningDetails/OpeningAttachment")
);

import "./styles.scss";

const OpeningDetails = () => {
  const param = useParams();
  const [searchParams, setSearchParams] = useSearchParams();
  const openingId = param.openingId;

  useEffect(() => {
    document.title = `Opening ${openingId} - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, [openingId]);

  const [activeTab, setActiveTab] = useState<number>(() => {
    const tabName = searchParams.get("tab");
    const index = tabName ? OpeningDetailsTabs.indexOf(tabName as any) : 0;
    return index >= 0 ? index : 0;
  });

  const isActive = (index: number) => activeTab === index;

  const handleTabChange = (selectedTabIndex: number) => {
    setActiveTab(selectedTabIndex);
    const tabName = OpeningDetailsTabs[selectedTabIndex];
    const newSearchParams = new URLSearchParams(searchParams.toString());
    newSearchParams.set("tab", tabName!);
    setSearchParams(newSearchParams, { replace: true });
  };

  const openingDetailsTombstoneQuery = useQuery({
    queryKey: ["openings", openingId, "tombstone"],
    queryFn: () => API.OpeningEndpointService.getOpeningTombstone(Number(openingId)),
    enabled: !!openingId,
    refetchOnMount: "always",
  });

  const postRecentOpeningMutation = useMutation({
    mutationFn: (openingId: number) => API.UserRecentOpeningEndpointService.recordUserViewedOpening(openingId),
  });

  useEffect(() => {
    if (openingId && openingDetailsTombstoneQuery.isSuccess) {
      postRecentOpeningMutation.mutate(Number(openingId));
    }
  }, [openingId, openingDetailsTombstoneQuery.status]);

  if (openingDetailsTombstoneQuery.isError) {
    const openingDetailsError =
      openingDetailsTombstoneQuery.error as AxiosError;
    const errorCode = openingDetailsError?.response?.status;
    const isNotFound = errorCode === 404;

    return (
      <EmptySection
        icon={isNotFound ? undefined : "BreakingChange"}
        pictogram={isNotFound ? "Summit" : undefined}
        title={
          isNotFound
            ? `Opening ${openingId} not found`
            : `Error fetching data for Opening ${openingId}`
        }
        description={
          isNotFound ? "" : openingDetailsTombstoneQuery.error.message
        }
      />
    );
  }

  return (
    <Grid className="default-grid opening-detail-grid">
      <PageTitle
        title={`Opening ID ${openingId}`}
        subtitle="Check and manage this opening"
        breadCrumbs={OpeningDetailBreadCrumbs}
      >
        <ActionableFavouriteButton openingId={Number(openingId)} />
      </PageTitle>

      {
        openingDetailsTombstoneQuery.data?.notifications.length! > 0 ? <Column sm={4} md={8} lg={16}>
          <OpeningNotifications
            notifications={openingDetailsTombstoneQuery.data?.notifications ?? []}
          />
        </Column>
          : null
      }

      <Column sm={4} md={8} lg={16}>
        <OpeningSummary
          openingId={Number(openingId)}
          tombstoneObj={openingDetailsTombstoneQuery.data?.tombstone}
          isLoading={openingDetailsTombstoneQuery.isLoading}
          currentTab={activeTab}
        />
      </Column>

      <Column className="opening-detail-tabs-col" sm={4} md={8} lg={16}>
        <Tabs
          selectedIndex={activeTab}
          onChange={(state) => handleTabChange(state.selectedIndex)}
        >
          <TabList
            className="default-tab-list"
            aria-label="List of Tabs"
            contained
          >
            <Tab renderIcon={() => <MapBoundaryVegetation size={16} />}>
              Overview
            </Tab>
            <Tab renderIcon={() => <Certificate size={16} />}>
              Tenure identification
            </Tab>
            <Tab renderIcon={() => <Development size={16} />}>
              Standard units
            </Tab>
            <Tab renderIcon={() => <CropHealth size={16} />}>Activities</Tab>
            <Tab renderIcon={() => <VegetationAsset size={16} />}>Forest cover</Tab>
            <Tab renderIcon={() => <DocumentAttachment size={16} />}>Attachment</Tab>
          </TabList>
          <TabPanels>
            <TabPanel className="tab-content full-width-col">
              {isActive(0) ? (
                <Suspense fallback={<TextAreaSkeleton />}>
                  <OpeningOverview
                    overviewObj={openingDetailsTombstoneQuery.data?.overview}
                    isLoading={openingDetailsTombstoneQuery.isLoading}
                  />
                </Suspense>
              ) : null}
            </TabPanel>

            <TabPanel className="tab-content full-width-col">
              {isActive(1) ? (
                <Suspense fallback={<TextAreaSkeleton />}>
                  <TenureIdentification openingId={Number(openingId)} />
                </Suspense>
              ) : null}
            </TabPanel>

            <TabPanel className="tab-content full-width-col">
              {isActive(2) ? (
                <Suspense fallback={<TextAreaSkeleton />}>
                  <OpeningStandardUnits openingId={Number(openingId)} />
                </Suspense>
              ) : null}
            </TabPanel>

            <TabPanel className="tab-content full-width-col">
              {isActive(3) ? (
                <Suspense fallback={<AccordionSkeleton />}>
                  <OpeningActivities openingId={Number(openingId)} />
                </Suspense>
              ) : null}
            </TabPanel>

            <TabPanel className="tab-content full-width-col">
              {isActive(4) ? (
                <Suspense fallback={<TextAreaSkeleton />}>
                  <OpeningForestCover openingId={Number(openingId)} />
                </Suspense>
              ) : null}
            </TabPanel>

            <TabPanel className="tab-content full-width-col">
              {isActive(5) ? (
                <Suspense fallback={<TextAreaSkeleton />}>
                  <OpeningAttachment openingId={Number(openingId)} />
                </Suspense>
              ) : null}
            </TabPanel>
          </TabPanels>
        </Tabs>
      </Column>
    </Grid>
  );
};

export default OpeningDetails;
