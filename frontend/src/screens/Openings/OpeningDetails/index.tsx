import React, { useEffect, useState } from "react";
import { Column, Grid, Tab, TabList, TabPanel, TabPanels, Tabs } from "@carbon/react";
import { MapBoundaryVegetation, Development, CropHealth } from "@carbon/icons-react";
import { useParams, useSearchParams } from "react-router-dom";
import { useMutation, useQuery } from "@tanstack/react-query";

import { fetchOpeningSsu, fetchOpeningTombstone } from "@/services/OpeningDetailsService";
import { putUserRecentOpening } from "@/services/OpeningService";
import { OpeningStandardUnits, OpeningSummary, OpeningOverview, OpeningActivities } from "@/components/OpeningDetails";
import ActionableFavouriteButton from "@/components/FavoriteButton/ActionableFavouriteButton";
import PageTitle from "@/components/PageTitle";

import { OpeningDetailBreadCrumbs, OpeningDetailsTabs } from "./constants";
import './styles.scss';
import { AxiosError } from "axios";
import EmptySection from "../../../components/EmptySection";

const OpeningDetails = () => {

  const param = useParams();
  const [searchParams, setSearchParams] = useSearchParams();
  const openingId = param.openingId;

  const [activeTab, setActiveTab] = useState<number>(() => {
    const tabName = searchParams.get('tab');
    const index = tabName ? OpeningDetailsTabs.indexOf(tabName as any) : 0;
    return index >= 0 ? index : 0;
  });

  const handleTabChange = (selectedTabIndex: number) => {
    setActiveTab(selectedTabIndex);

    const tabName = OpeningDetailsTabs[selectedTabIndex];
    const newSearchParams = new URLSearchParams(searchParams.toString());
    newSearchParams.set('tab', tabName);

    setSearchParams(newSearchParams, { replace: true });
  };

  const openingDetailsTombstoneQuery = useQuery({
    queryKey: ['openings', openingId, 'tombstone'],
    queryFn: () => fetchOpeningTombstone(Number(openingId)),
    enabled: !!openingId,
    refetchOnMount: 'always'
  })

  const openingDetailsSsuQuery = useQuery({
    queryKey: ['openings', openingId, 'ssu'],
    queryFn: () => fetchOpeningSsu(Number(openingId)),
    enabled: !!openingId,
    refetchOnMount: 'always'
  });

  const postRecentOpeningMutation = useMutation({
    mutationFn: (openingId: number) => putUserRecentOpening(openingId)
  });
  
  const openingDetailsError = openingDetailsTombstoneQuery.error as AxiosError;
  const openingNotFound = openingDetailsTombstoneQuery.isError && openingDetailsError?.response?.status === 404;

  /**
   * Update most recent openings when this page loads
   */
  useEffect(() => {
    if (openingId && Number.isInteger(Number(openingId)) && openingNotFound) {
      postRecentOpeningMutation.mutate(Number(openingId));
    }
  }, [openingId, openingNotFound]);

  if (openingNotFound) {
    return (
      <EmptySection pictogram="Summit" title={`Opening ${openingId} not found`} description="" />
    )
  }

  if (!openingNotFound && openingDetailsTombstoneQuery.isError) {
    return (
      <EmptySection
        icon="BreakingChange"
        title={`Error fetching data for Opening ${openingId}`}
        description={openingDetailsTombstoneQuery.error.message}
      />
    )
  }

  return (
    <Grid className="default-grid opening-detail-grid">
      <PageTitle
        title={`Opening ID ${openingId}`}
        subtitle="Check and manage this opening"
        breadCrumbs={OpeningDetailBreadCrumbs}
        experimental
      >
        <ActionableFavouriteButton openingId={Number(openingId)} />
      </PageTitle>

      <Column sm={4} md={8} lg={16}>
        <OpeningSummary
          openingId={Number(openingId)}
          tombstoneObj={openingDetailsTombstoneQuery.data?.tombstone}
          isLoading={openingDetailsTombstoneQuery.isLoading}
        />
      </Column>

      <Column className="opening-detail-tabs-col" sm={4} md={8} lg={16}>
        <Tabs selectedIndex={activeTab} onChange={(state) => handleTabChange(state.selectedIndex)}>
          <TabList className="default-tab-list" aria-label="List of Tab" contained>
            <Tab renderIcon={() => <MapBoundaryVegetation size={16} />}>Overview</Tab>
            <Tab renderIcon={() => <Development size={16} />}>Standard units</Tab>
            <Tab renderIcon={() => <CropHealth size={16} />}>Activities</Tab>
          </TabList>
          <TabPanels>
            <TabPanel className="tab-content full-width-col">
              <OpeningOverview
                overviewObj={openingDetailsTombstoneQuery.data?.overview}
                isLoading={openingDetailsTombstoneQuery.isLoading}>
              </OpeningOverview>
            </TabPanel>

            <TabPanel className="tab-content full-width-col">
              <OpeningStandardUnits
                standardUnitObjs={openingDetailsSsuQuery.data}
                isLoading={openingDetailsSsuQuery.isLoading}>
              </OpeningStandardUnits>
            </TabPanel>

            <TabPanel className="tab-content full-width-col">
              <OpeningActivities openingId={Number(openingId)} />
            </TabPanel>
          </TabPanels>
        </Tabs>
      </Column>
    </Grid >
  )
}

export default OpeningDetails;
