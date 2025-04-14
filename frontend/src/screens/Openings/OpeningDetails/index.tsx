import React, { useEffect, useState } from "react";
import { Column, Grid, Tab, TabList, TabPanel, TabPanels, Tabs } from "@carbon/react";
import { MapBoundaryVegetation, Development } from "@carbon/icons-react";
import { useParams, useSearchParams } from "react-router-dom";
import { useMutation, useQuery } from "@tanstack/react-query";

import { fetchOpeningTombstone, searchOpenings } from "@/services/OpeningSearchService";
import { putUserRecentOpening } from "@/services/OpeningService";
import { OpeningStandardUnits, OpeningSummary, OpeningOverview } from "@/components/OpeningDetails";
import ActionableFavouriteButton from "@/components/FavoriteButton/ActionableFavouriteButton";
import PageTitle from "@/components/PageTitle";

import { OpeningDetailBreadCrumbs, OpeningDetailsTabs } from "./constants";
import './styles.scss';

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

  /**
   * TODO:
   * Temporarily using opening search to get data, will need to update this once API is done
   */
  const openingOverviewQuery = useQuery({
    queryKey: ['openings', openingId, 'tombstone'],
    queryFn: () => fetchOpeningTombstone(Number(openingId)),
    enabled: !!openingId,
    refetchOnMount: 'always'
  })

  const postRecentOpeningMutation = useMutation({
    mutationFn: (openingId: number) => putUserRecentOpening(openingId)
  });

  /**
   * Update most recent openings when this page loads
   */
  useEffect(() => {
    if (openingId && Number.isInteger(Number(openingId))) {
      postRecentOpeningMutation.mutate(Number(openingId));
    }
  }, [openingId]);


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
          tombstoneObj={openingOverviewQuery.data?.tombstone}
          isLoading={openingOverviewQuery.isLoading}
        />
      </Column>

      {/* <Column className="opening-detail-tabs-col" sm={4} md={8} lg={16}>
        <Tabs selectedIndex={activeTab} onChange={(state) => handleTabChange(state.selectedIndex)}>
          <TabList className="default-tab-list" aria-label="List of Tab" contained>
            <Tab renderIcon={() => <MapBoundaryVegetation size={16} />}>Overview</Tab>
            <Tab renderIcon={() => <Development size={16} />}>Standard units</Tab>
          </TabList>
          <TabPanels>
            <TabPanel className="tab-content full-width-col">
              <OpeningOverview isLoading={openingQuery.isLoading} />
            </TabPanel>
            <TabPanel className="tab-content full-width-col">
              <OpeningStandardUnits />
            </TabPanel>
          </TabPanels>
        </Tabs>
      </Column> */}
    </Grid >
  )
}

export default OpeningDetails;
